/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.csa.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DBException;
import org.compiere.apps.ADialog;
import org.compiere.model.MBPartner;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProductPrice;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
//import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;


/**
 *
 *
 * @author Rodrigo Olivares Hurtado
 * @version $Id: ProcessInventory.java $
 */
public class ProcessCSACreateOCFromReq extends SvrProcess {
	// private String p_DocStatus = null;
	private int p_Requisition_ID = 0;


	/**
	 * Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare() {
		/*ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();

			if (name.equals("Action"))
				p_Action = para[i].getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}*/
		p_Requisition_ID = getRecord_ID();
	} // prepare

	/**
	 * Perrform process.
	 *
	 * @return Message (clear text)
	 * @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception {
		String msg = "";
		if (p_Requisition_ID > 0) {

			int OrderID = DB.getSQLValue(get_TrxName(), "SELECT  coalesce(count(c_order_id),0)"
					+ "FROM C_ORDER WHERE M_REQUISITION_ID = "+p_Requisition_ID);
			if(OrderID > 0) {
				MRequisition req = new MRequisition(getCtx(), p_Requisition_ID, get_TrxName());
				msg = CreateOC(req);
			}else {
				msg = "No existen productos pendientes";
			}


		}
		return msg;
	} // doIt

	// Crear orden en relacion a la solicitud
		public String CreateOC (MRequisition req) throws Exception {
			//Rodrigo Olivares Hurtado 2023-08-14
			//Se realiza ajuste para que se creen OC separadas por BPartner
			if(req.getC_DocType().getDocBaseType().equals("POR")){
				String sqlbp = "SELECT coalesce(mrl.C_BPartner_ID, mr.C_BPartner_ID,0) as bp_id "
						+ "FROM M_Requisition mr join M_RequisitionLine mrl on mrl.M_Requisition_ID="
						+ "mr.M_Requisition_ID where mr.M_Requisition_ID="+req.get_ID()+" group by bp_id order by bp_id";

				PreparedStatement pstmtbp = null;
				ResultSet rsbp = null;
				ADialog.info(1, null, "Prueba de mensaje.");
				
				try {
					pstmtbp = DB.prepareStatement(sqlbp, get_TrxName());
					rsbp = pstmtbp.executeQuery();
					while(rsbp.next()) {
						if(rsbp.getInt("bp_id")!=0) {
							MOrder oc = new MOrder(getCtx(),0,get_TrxName());
							MBPartner bp = new MBPartner(getCtx(),rsbp.getInt("bp_id"),get_TrxName());
							oc.setC_BPartner_ID(bp.get_ID());
							//se asigna primera direccion valida que encuentre
							int bpLocationID = bp.getPrimaryC_BPartner_Location_ID();
							//si no encuentra una direccion envia mensaje de error
							if(bpLocationID==0)
								throw new AdempiereException("El proveedor "+bp.getName()+" no tiene activa o creada una direccion");
							oc.setC_BPartner_Location_ID(bpLocationID);
							oc.setM_PriceList_ID(bp.getPO_PriceList_ID()==0 ? req.getM_PriceList_ID(): bp.getPO_PriceList_ID());
							oc.save();

							oc.setAD_Org_ID(req.getAD_Org_ID());
							oc.setC_DocTypeTarget_ID(1000016);//126
							Timestamp fechaHoy = new Timestamp(System.currentTimeMillis());
							oc.setDateOrdered(fechaHoy);
							oc.setDateAcct(fechaHoy);
							oc.setDatePromised(fechaHoy);
							oc.setM_Warehouse_ID(req.getM_Warehouse_ID());
							oc.setC_Currency_ID(req.getC_Currency_ID());
							oc.setIsSOTrx(false);

							oc.setSalesRep_ID(req.get_ValueAsInt("SalesRep_ID")); //102
							oc.setDescription("**Generado desde solicitud N° "+req.getDocumentNo()+"**");
							oc.set_ValueOfColumn("M_Requisition_ID",req.get_ID());
							oc.set_ValueOfColumn("AD_OrgRef_ID",req.get_Value("AD_OrgRef_ID"));
							oc.save();

							MRequisitionLine[] rLines = req.getLines();
							//Se busca productos que no esten en orden de compra o que falte cantidad con las ordenes de compras ya creadas
							String sql = "select (coalesce(rl.Qty,0)- coalesce(sum(ol.QtyEntered),0)) as diferencia, "
									+ "coalesce(rl.m_product_id,0) as m_product_id "
									+ "from m_requisition re "
									+ "join m_requisitionline rl on rl.m_requisition_id = re.m_requisition_id "
									+ "join c_order ord on ord.m_requisition_id = re.m_requisition_id "
									+ "join c_orderline ol on ol.c_order_id  = ord.c_order_id "
									+ "where rl.M_Product_ID = ol.M_Product_ID "
									+ "and re.m_requisition_id = "+req.get_ID() + " and rl.c_bpartner_id = "+ rsbp.getInt("bp_id")
									+ " group by rl.C_BPartner_ID, rl.m_product_id,rl.qty "
									+ "union all "
									+ "select rl.Qty as diferencia, "
									+ " coalesce(rl.m_product_id,0) as m_product_id "
									+ "from m_requisition re "
									+ "join m_requisitionline rl on rl.m_requisition_id = re.m_requisition_id "
									+ "join c_order ord on ord.m_requisition_id = re.m_requisition_id "
									+ "join c_orderline ol on ol.c_order_id  = ord.c_order_id "
									+ "where  rl.M_Product_ID not in(select ol.M_Product_ID from c_order ord "
									+ "						join c_orderline ol on ol.c_order_id  = ord.c_order_id "
									+ "						where ord.m_requisition_id ="+req.get_ID()+" and rl.c_bpartner_id ="+rsbp.getInt("bp_id")+") "
									+ "and re.m_requisition_id = "+req.get_ID() +" and rl.c_bpartner_id = "+rsbp.getInt("bp_id")
									+ "group by rl.C_BPartner_ID, rl.m_product_id,rl.qty";



							PreparedStatement pstmt = null;
							ResultSet rs = null;

							int diferencia = 0;
							try {
								pstmt = DB.prepareStatement(sql, get_TrxName());
								rs = pstmt.executeQuery();
								while(rs.next()){

									if(rs.getInt("diferencia") >= 0) {
										diferencia = diferencia + rs.getInt("diferencia");
									}

									if(rsbp.getInt("bp_id")>0) {

										//Se crean las lineas
										for (MRequisitionLine reql : rLines) {
											//se valida y actualiza lista de precios con los nuevos valores
											// solo si es producto
											if(reql.getM_Product_ID()>0)
												validPriceList(reql, oc);

											int bpVendor = (reql.getC_BPartner_ID()>0)?reql.getC_BPartner_ID():req.get_ValueAsInt("C_BPartner_ID");
											if(rsbp.getInt("bp_id")==bpVendor){

												if(rs.getInt("diferencia")>0 && reql.getM_Product_ID() == rs.getInt("m_product_id")) {

													MOrderLine ocl = new MOrderLine(oc);
													ocl.setAD_Org_ID(oc.getAD_Org_ID());
													ocl.setM_Product_ID(reql.getM_Product_ID());
													ocl.setC_Charge_ID(reql.getC_Charge_ID());



													if(reql.getM_Product_ID() == rs.getInt("m_product_id")) {
														ocl.setQtyEntered(rs.getBigDecimal("diferencia"));
														ocl.setQtyOrdered(rs.getBigDecimal("diferencia"));
													}

													if(reql.getM_Product_ID()>0)
														ocl.setC_UOM_ID(reql.getM_Product().getC_UOM_ID());
													else
														ocl.setC_UOM_ID(100);

													ocl.setPriceEntered(reql.getPriceActual());
													ocl.setPriceActual(reql.getPriceActual());
													ocl.setPriceList(reql.getPriceActual());
													ocl.setC_Tax_ID(reql.getC_Tax_ID());
													ocl.setLineNetAmt(reql.getLineNetAmt());
													ocl.setM_RequisitionLine_ID(reql.get_ID());
													ocl.setDescription(reql.getDescription());
													ocl.save();
													reql.setC_OrderLine_ID(ocl.get_ID());
													reql.save();

												}

											}
										}

									}

								}

								if (diferencia <= 0) {
									return "No existen productos pendientes";
								}


							}
							catch (SQLException e)
							{
								throw new DBException(e, sql);
							}
							finally
							{
								DB.close(rs, pstmt);
								rs = null; pstmt = null;
							}

						}

					}
				}
				catch (SQLException e)
				{
					throw new DBException(e, sqlbp);
				}
				finally
				{
					DB.close(rsbp, pstmtbp);
					rsbp = null; pstmtbp = null;
				}
			}







			return "Se creo OC para los productos pendientes ";

		}


	public void validPriceList(MRequisitionLine reql, MOrder oc)
	{
		int ppID = DB.getSQLValue(reql.get_TrxName(),"SELECT coalesce(max(m_productprice_id),0) "
				+ "from m_pricelist_version mplv join m_productprice mpp on "
				+ "mpp.m_pricelist_version_id=mplv.m_pricelist_version_id "
				+ "where mplv.isactive='Y' and mplv.ValidFrom<'"+oc.getDateOrdered().toString()+"' and "
				+ "mpp.m_product_id="+reql.getM_Product_ID()+" "
				+ "and mplv.m_pricelist_id="+oc.getM_PriceList_ID());
		MProductPrice pp = new MProductPrice(reql.getCtx(),ppID,reql.get_TrxName());
		if(ppID==0) {
			int plvID = DB.getSQLValue(reql.get_TrxName(),"SELECT coalesce(max(mplv.m_pricelist_version_id),0) "
					+ "from m_pricelist_version mplv join m_productprice mpp on "
					+ "mpp.m_pricelist_version_id=mplv.m_pricelist_version_id "
					+ "where mplv.isactive='Y' and mplv.ValidFrom<'"+oc.getDateOrdered().toString()+"' "
					+ "and mplv.m_pricelist_id="+oc.getM_PriceList_ID());
			if(plvID==0)
				throw new AdempiereException("No existe version de lista de precio valida para los productos");
			pp.setM_PriceList_Version_ID(plvID);
			pp.setM_Product_ID(reql.getM_Product_ID());
		}
		pp.setPriceStd(reql.getPriceActual());
		pp.setPriceLimit(reql.getPriceActual());
		pp.setPriceList(reql.getPriceActual());
		pp.save();
	}






	/**
	 * Actualiza el campo AD_User_ID con el ID proporcionado cuando este vacio
	 *
	 * @param req
	 * @param id
	 */
	/*private void ad_userValidate(MRequisition req, int id) {
		if (req.getAD_User_ID() == 0)
			req.setAD_User_ID(id);
		req.saveEx();
	}*/
}
