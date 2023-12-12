package org.csa.process;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MPriceListVersion;
import org.compiere.model.MSysConfig;
import org.compiere.model.X_C_Commune;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Integration with pharmacy sales
 *
 * @author JLeyton
 * @version 1 $
 */
public class ProcessCSAPharmacySale extends SvrProcess {
	private static final String Null = null;
	private Timestamp p_DateInvoiced;
	private Timestamp p_DateInvoiced_To;
	private String p_FilePath;

	/**
	 * Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();

			if (name.equals("DateInvoiced")) {
				p_DateInvoiced = element.getParameterAsTimestamp();
				p_DateInvoiced_To = element.getParameterToAsTimestamp();
			}
			else if(name.equals("FileName")){
				p_FilePath = element.getParameterAsString();
			} else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

	}

	/**
	 * Perrform process.
	 *
	 * @return Message (clear text)
	 * @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception {

		JsonObject document = new JsonObject();
		JsonArray contenidoList = new JsonArray();
		JsonObject contenido = new JsonObject();
		JsonArray ventaList = new JsonArray();
		JsonArray devolucionList = new JsonArray();


		PreparedStatement psVentas = null, psDevoluciones = null;
		ResultSet rsVentas = null, rsDevoluciones = null;
		BigDecimal ventaTotal = Env.ZERO;
		SimpleDateFormat f1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		try {
			
			String sqlVentas = "select ci.c_invoice_id,ao.value as cod_org, "
					+ "cbp.value||'-'||coalesce(cbp.ValueValidator,cbp.duns,'') as rut_vendedor "
					+ "from c_invoice ci join ad_org ao on ao.ad_org_id=ci.ad_org_id "
					+ "join c_order co on co.c_order_id=ci.c_order_id "
					+ "left join ad_user au on au.ad_user_id=ci.salesrep_id "
					+ "left join c_bpartner cbp on cbp.c_bpartner_id=au.c_bpartner_id "
					+ "where ci.ad_client_id=" + getAD_Client_ID() + " and co.c_doctype_id in (1000061,1000039,1000003) "
					+ "and ci.docstatus='CO' and ci.issotrx='Y' " + "and ci.dateinvoiced between '" + p_DateInvoiced
					+ "' and '" + p_DateInvoiced_To + "' "
					+ "and (upper(ci.description) not like 'MIGRACION%' or ci.description is null)";

			log.config("SQL VENTAS ==>" + sqlVentas);
			
			
			psVentas = DB.prepareStatement(sqlVentas, get_TrxName());
			rsVentas = psVentas.executeQuery();
			
			String sqlDevoluciones = "select ci.c_invoice_id,ao.value as cod_org, "
					+ "cbp.value||'-'||coalesce(cbp.ValueValidator,cbp.duns,'') as rut_vendedor "
					+ "from c_invoice ci join ad_org ao on ao.ad_org_id=ci.ad_org_id "
					+ "join c_order co on co.c_order_id=ci.c_order_id "
					+ "left join ad_user au on au.ad_user_id=ci.salesrep_id "
					+ "left join c_bpartner cbp on cbp.c_bpartner_id=au.c_bpartner_id "
					+ "where ci.ad_client_id=" + getAD_Client_ID() + " and co.c_doctype_id in (1000079) "
					+ "and ci.docstatus='CO' and ci.issotrx='Y' " + "and ci.dateinvoiced between '" + p_DateInvoiced
					+ "' and '" + p_DateInvoiced_To + "' "
					+ "and (upper(ci.description) not like 'MIGRACION%' or ci.description is null)";
			log.config("SQL DEVOLUCIONES ==>" + sqlDevoluciones);
			psDevoluciones = DB.prepareStatement(sqlDevoluciones, get_TrxName());
			rsDevoluciones = psDevoluciones.executeQuery();
				} catch (Exception e) {
				throw new AdempiereException("DB ERROR==>" + e);
			}
		    int cantDoc=0;
			if (rsVentas.next()) {
				int tipoVenta;

			
				do{
					// ********** VENTAS ***************
					MInvoice inv = new MInvoice(getCtx(), rsVentas.getInt("c_invoice_id"), get_TrxName());
					MBPartner bp = new MBPartner(getCtx(), inv.getC_BPartner_ID(), get_TrxName());
					JsonObject venta = new JsonObject();

					venta.addProperty("identificador_adempier", inv.get_ID());
					venta.addProperty("fecha_venta", f1.format(inv.getCreated()));
					venta.addProperty("codigo_bodega", inv.getC_Order().getM_Warehouse().getValue());
					venta.addProperty("codigo_servicio", "FT");
					venta.addProperty("usuario",rsVentas.getString("rut_vendedor"));
					venta.addProperty("fecha_usuario", f1.format(inv.getCreated()));
					// tipo de venta 1 si es venta paciente externo y 2 funcionario
					if (inv.getC_BPartner().isEmployee() && inv.getM_PriceList_ID()==1000000)
						tipoVenta = 2;
					else
						tipoVenta = 1;
					
					venta.addProperty("tipo_venta", tipoVenta);
					MOrder ov = new MOrder(getCtx(), inv.getC_Order_ID(), get_TrxName());
					MPriceListVersion plv = new MPriceListVersion(getCtx(), ov.get_ValueAsInt("M_PriceList_Version_ID"),
							get_TrxName());
					String cod_convenio=null;
					String rut_convenio=null;
					if(plv.get_ValueAsString("Value").length()>0) cod_convenio =plv.get_ValueAsString("Value");
					if(!(plv.getDescription()==null))
						if(plv.getDescription().length()>0) rut_convenio =plv.getDescription();	
					venta.addProperty("codigo_convenio",(tipoVenta == 1 ? cod_convenio : Null));
					venta.addProperty("rut_empresa_convenio",
							(tipoVenta == 1 ? rutSeparado(rut_convenio, 0) : Null));
					venta.addProperty("dv_empresa_convenio",
							(tipoVenta == 1 ? rutSeparado(rut_convenio, 1) : Null));
					venta.addProperty("rut_paciente_convenio",
							(tipoVenta == 1 ? inv.getC_BPartner().getValue() : Null));
					venta.addProperty("dv_paciente_convenio", (tipoVenta == 1 ? inv.getC_BPartner().getDUNS() : Null));
					String rutEmpFunc=DB.getSQLValueString(get_TrxName(), "select (select arl.description  "
							+ "from ad_ref_list arl where arl.value=max(cbpi.companies) and arl.ad_reference_id=1000101) "
							+ "from C_BPartner_Info_Oth cbpi where cbpi.c_bpartner_id="+inv.getC_BPartner_ID());
					if(!(rutEmpFunc==null)) {
						if(!(rutEmpFunc.length()>0))
							rutEmpFunc=null;
					}
					venta.addProperty("rut_empresa_funcionario", (tipoVenta == 2 ? rutSeparado(rutEmpFunc,0)  : Null));
					venta.addProperty("dv_empresa_funcionario", (tipoVenta == 2 ? rutSeparado(rutEmpFunc,1) : Null));
					venta.addProperty("rut_funcionario", (tipoVenta == 2 ? bp.getValue() : Null));
					venta.addProperty("dv_funcionario",
							(tipoVenta == 2 ? bp.get_ValueAsString("ValueValidator") : Null));
					venta.addProperty("nombre_funcionario", (tipoVenta == 2 ? bp.getName() : Null));
					venta.addProperty("observacion", inv.getDescription());

					// ***************** DETALLE VENTAS************************
					MInvoiceLine lineas[] = inv.getLines(false);
					if (lineas.length > 0) {
						JsonArray ventaDetalleList = new JsonArray();
						for (MInvoiceLine line : lineas) {
							JsonObject ventaDetalle = new JsonObject();
							ventaDetalle.addProperty("codigo_medicamento", line.getM_Product().getValue());
							String codBarra = DB.getSQLValueString(get_TrxName(), "select coalesce(max(VendorProductNo),'N') from M_Product_PO where m_product_id="+line.getM_Product_ID());
							if(codBarra.equals("N")) {
								codBarra = line.getM_Product().getSKU();
							}
							ventaDetalle.addProperty("codigo_barra", codBarra);
							ventaDetalle.addProperty("cantidad", line.getQtyEntered());
							ventaDetalle.addProperty("precio_unitario", line.getPriceEntered());
							ventaDetalle.addProperty("iva_total_producto", line.getTaxAmt());
							ventaDetalle.addProperty("precio_total_producto", line.getLineTotalAmt());
							int descuento = DB.getSQLValue(get_TrxName(), "select coalesce(max(discount),0) from c_orderline "
									+ "where m_product_id="+line.getM_Product_ID()+" and "
									+ "c_order_id="+inv.getC_Order_ID());
							ventaDetalle.addProperty("descuento_producto", descuento);
							ventaDetalleList.add(ventaDetalle);
							venta.add("VentaDetalle", ventaDetalleList);

						}
					}

					// ****************** FORMA DE PAGO VENTAS**********************
					JsonArray formaPagoList = new JsonArray();
					JsonObject formaPago = new JsonObject();
					formaPago.addProperty("forma_pago_codigo", inv.getPaymentRule());
					formaPago.addProperty("monto", inv.getGrandTotal());
					formaPagoList.add(formaPago);
					venta.add("FormaPago", formaPagoList);

					// ****************** DOCUMENTO ELECTRONICO VENTA**********************
					JsonArray DTEList = new JsonArray();
					JsonObject DTE = new JsonObject();
					DTE.addProperty("folio", inv.getDocumentNo());
					DTE.addProperty("tipodte", inv.getC_DocType().getDocumentNote());
					DTE.addProperty("fechaemision", f1.format(inv.getCreated()));
					DTE.addProperty("rutemisor", "88611600-4");
					String receptorDv;
					if(bp.get_ValueAsString("ValueValidator")==null && bp.getDUNS()==null)
						receptorDv="";
					else
						receptorDv=(bp.get_ValueAsString("ValueValidator")==null?bp.getDUNS():bp.get_ValueAsString("ValueValidator"));
					
					DTE.addProperty("rutreceptor",
							inv.getC_BPartner().getValue().concat("-").concat(receptorDv));
					DTE.addProperty("razonsocialreceptor", inv.getC_BPartner().getName());
					MBPartnerLocation bpl = new MBPartnerLocation(getCtx(), inv.getC_BPartner_Location_ID(),
							get_TrxName());
					String calle = bpl.get_ValueAsString("Address1");
					String nroCalle = bpl.get_ValueAsString("Address2");
					DTE.addProperty("direccionreceptor",calle.concat(" ").concat(nroCalle));
					X_C_Commune comuna = new X_C_Commune(getCtx(), bpl.get_ValueAsInt("C_Commune_ID"), get_TrxName());
					DTE.addProperty("codigo_comunareceptor",comuna.getValue());
					DTE.addProperty("totalafecto", inv.getTotalLines());
					DTE.addProperty("totalexento", 0);
					DTE.addProperty("totaliva", inv.getGrandTotal().subtract(inv.getTotalLines()));
					DTE.addProperty("totaldocumento", inv.getGrandTotal());
					DTE.addProperty("folio_referencia", Null);
					DTE.addProperty("tipodte_referencia", Null);
					DTE.addProperty("fecha_referencia", Null);
					DTE.addProperty("observacion1", inv.getDescription());
					DTE.addProperty("observacion2", Null);
					DTEList.add(DTE);
					venta.add("DocumentElectronico", DTEList);
					ventaList.add(venta);
					ventaTotal = ventaTotal.add(inv.getGrandTotal());
					cantDoc++;
					//System.out.print("\rVentas: [" + "=".repeat(rsVentas.getRow()) + " ".repeat(rowCount - rsVentas.getRow()) + "] " + (rsVentas.getRow() * 100 / rowCount) + "%");

				} while (rsVentas.next());
			}

			if (rsDevoluciones.next()) {
				do {
// ******************************************** DEVOLUCIONES *****************************************************************
					MInvoice inv = new MInvoice(getCtx(), rsDevoluciones.getInt("c_invoice_id"), get_TrxName());
					MBPartner bp = new MBPartner(getCtx(), inv.getC_BPartner_ID(), get_TrxName());
					MOrder ov = new MOrder(getCtx(), inv.getC_Order_ID(), get_TrxName());
					MInvoice invR = null;
					JsonObject devolucion = new JsonObject();
					devolucion.addProperty("identificador_adempier",inv.getC_Invoice_ID());
					if(ov.get_ValueAsInt("C_Invoice_ID")>0) {
						invR = new MInvoice(getCtx(), ov.get_ValueAsInt("C_Invoice_ID"), get_TrxName());
					}
					devolucion.addProperty("identificador_adempier_referencia",invR.getC_Invoice_ID());
					devolucion.addProperty("fecha_devolucion", f1.format(inv.getCreated()));
					devolucion.addProperty("codigo_bodega",ov.getM_Warehouse().getValue());
					devolucion.addProperty("codigo_servicio", "FT");
					devolucion.addProperty("usuario", rsDevoluciones.getString("rut_vendedor"));
					devolucion.addProperty("fecha_usuario", f1.format(inv.getCreated()));

					// ***************** DETALLE DEVOLUCIONES ************************
					MInvoiceLine lineas[] = inv.getLines(false);
					if (lineas.length > 0) {
						JsonArray devDetalleList = new JsonArray();
						for (MInvoiceLine line : lineas) {
							JsonObject devDetalle = new JsonObject();
							devDetalle.addProperty("codigo_medicamento",line.getM_Product().getValue());
							devDetalle.addProperty("codigo_barra", line.getM_Product().getSKU());
							devDetalle.addProperty("cantidad", line.getQtyEntered());
							devDetalleList.add(devDetalle);
							devolucion.add("DevolucionDetalle", devDetalleList);
						}
					}
					// ****************** DOCUMENTO ELECTRONICO DEVOLUCION**********************
					JsonArray devDTEList = new JsonArray();
					JsonObject devDTE = new JsonObject();
					devDTE.addProperty("folio", inv.getDocumentNo());
					devDTE.addProperty("tipodte", inv.getC_DocType().getDocumentNote());
					devDTE.addProperty("fechaemision", f1.format(inv.getCreated()));
					devDTE.addProperty("rutemisor", "88611600-4");
					String receptorDv;
					if(bp.get_ValueAsString("ValueValidator")==null && bp.getDUNS()==null)
						receptorDv="";
					else
						receptorDv=(bp.get_ValueAsString("ValueValidator")==null?bp.getDUNS():bp.get_ValueAsString("ValueValidator"));
					devDTE.addProperty("rutreceptor",
							inv.getC_BPartner().getValue().concat("-").concat(receptorDv));
					devDTE.addProperty("razonsocialreceptor", inv.getC_BPartner().getName());
					MBPartnerLocation bpl = new MBPartnerLocation(getCtx(), inv.getC_BPartner_Location_ID(),
							get_TrxName());
					String calle = bpl.get_ValueAsString("Address1");
					String nroCalle = bpl.get_ValueAsString("Address2");
					devDTE.addProperty("direccionreceptor",calle.concat(" ").concat(nroCalle));
					X_C_Commune comuna = new X_C_Commune(getCtx(), bpl.get_ValueAsInt("C_Commune_ID"), get_TrxName());
					devDTE.addProperty("codigo_comunareceptor",comuna.getValue());
					devDTE.addProperty("totalafecto", inv.getTotalLines());
					devDTE.addProperty("totalexento", 0);
					devDTE.addProperty("totaliva", inv.getGrandTotal().subtract(inv.getTotalLines()));
					devDTE.addProperty("totaldocumento", inv.getGrandTotal());
					devDTE.addProperty("folio_referencia", invR.getDocumentNo());
					devDTE.addProperty("tipodte_referencia", invR.getC_DocType().getDocumentNote());
					devDTE.addProperty("fecha_referencia", f1.format(invR.getCreated()));
					devDTE.addProperty("observacion1", inv.getDescription());
					devDTE.addProperty("observacion2", Null);
					devDTEList.add(devDTE);
					devolucion.add("DocumentElectronico", devDTEList);
					devolucionList.add(devolucion);
					ventaTotal = ventaTotal.subtract(inv.getGrandTotal());
					cantDoc++;
				} while (rsDevoluciones.next());
			}

			// ********** CUERPO ********************
			contenido.add("Ventas", ventaList);
			contenido.add("Devoluciones", devolucionList);
			contenidoList.add(contenido);
			// *********** CABECERA ******************
			// valida si hay datos de ventas o devoluciones
			if (cantDoc==0) {
				document.addProperty("codigo", "2");
				document.addProperty("mensaje", "Sin Ventas ni devoluciones dentro del rango especificado");
				document.addProperty("monto", 0);
				document.add("content", contenidoList);
			} else {
				document.addProperty("codigo", "1");
				document.addProperty("mensaje", "");
				document.addProperty("monto", ventaTotal);
				document.add("content", contenidoList);
			}

		//log.config("JSON===>" + document.toString());
		//return document.toString();
			p_FilePath = p_FilePath.replace(".json","");
			FileWriter jsonFile = new FileWriter(p_FilePath+".json");
			jsonFile.write(document.toString());
			jsonFile.flush();
			jsonFile.close();
			return "Documento json generado en: "+p_FilePath+".json";
	}

	/**
	 * Separa el rut del digito verificador (solo si estan separados por guion(-))
	 *
	 * @param rutConDV
	 * @param opt      0 para rut sin digito y 1 para digito
	 * @return
	 */
	private String rutSeparado(String rutConDV, int opt) {
		if(rutConDV==null)
			return null;
		if(!validarFormatoRut(rutConDV)) {
			return null;
		}
		if (rutConDV.contains("-")) {
			String[] rutSeparado = rutConDV.split("-");
			
			return rutSeparado[opt];
		} else
			log.warning("Rut no tiene formato correcto:("+rutConDV+")");
		return rutConDV;
	}
	private boolean validarFormatoRut(String rutConDV) {
		Pattern pattern = Pattern.compile("^[0-9]+-[0-9kK]{1}$");
		Matcher matcher = pattern.matcher(rutConDV);
		if(!matcher.matches())
			log.warning("Rut:"+rutConDV+" no cumple con el formato correcto");
		return matcher.matches();
	}

}
