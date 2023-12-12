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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.X_I_PreParqueo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;


/**
 *
 *
 *  @author Rodrigo Olivares Hurtado
 *  @version $Id: ImportPreParqueoInvoice.java $
 */
public class ImportPreParqueoInvoice extends SvrProcess
{

	/**
	 *  Prepare - e.g., get Parameters.
	 */

	private String 			p_FileName;


	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();

			if (name.equals("FileName"))
			{
				p_FileName = element.getParameterAsString();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		String pathFile = p_FileName;
		BufferedReader Lector;
		String linea;
		String partes[] = null;
		String[] a = null;
		int count = 0;
		try{
			Lector =  new BufferedReader(new FileReader(pathFile)) ;

			while((linea = Lector.readLine()) != null) {
				partes = linea.split(",");

				for(int i = 0; i< partes.length; i++) {
					if(!linea.contains("RUT Proveedor")) {
						 a = partes[i].split(";");
						 X_I_PreParqueo par = new X_I_PreParqueo(getCtx(), 0, get_TrxName());

						 //String Nro =a[0];
						 int DocTypeID = DB.getSQLValue(get_TrxName(), "SELECT C_DOCTYPE_ID FROM C_DOCTYPE WHERE DOCUMENTNO LIKE '"+a[1]+"'");
						 par.setC_DocType_ID(DocTypeID); //String TipoDoc =a[1];
						 par.setType(a[2]);// String TipoCompra =a[2];
						 int BPartnerID = DB.getSQLValue(get_TrxName(), "SELECT coalesce(max(C_BPARTNER_ID),0) FROM C_BPARTNER "
						 		+ "WHERE Value||'-'|| case when ValueValidator is null then '0' else ValueValidator end LIKE '%"+a[3]+"%'");
						 if(BPartnerID <= 0) {
							 MBPartner bp = new MBPartner(getCtx(), 0, get_TrxName());
							 bp.setName(a[4]);
							 bp.setValue(a[3].split("-")[0]);
							 bp.set_CustomColumn("ValueValidator", a[3].split("-")[1]);
							 bp.saveEx(get_TrxName());

						 }
						 BPartnerID = DB.getSQLValue(get_TrxName(), "SELECT coalesce(C_BPARTNER_ID,0) FROM C_BPARTNER "
							 		+ "WHERE Value||'-'|| case when ValueValidator is null then '0' else ValueValidator end LIKE '%"+a[3]+"%'");

						 par.setC_BPartner_ID(BPartnerID);// String RUTProveedor =a[3];
						 par.setName(a[4]);// String RazonSocial =a[4];
						 par.setDocumentNo(a[5]);// String Folio =a[5];

						 String FechaDocto =a[6]+" 00:00:00.0";
						 SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
						 Date parsedDate = dateFormat.parse(FechaDocto);
						 Timestamp tmpFechaDocto = new java.sql.Timestamp(parsedDate.getTime());

						 par.setDateDoc(tmpFechaDocto);

						 String FechaRecepcion =a[7] +":00.0";
						 Date parsedDateFR = dateFormat.parse(FechaRecepcion);
						 Timestamp tmpFechaRecepcion = new java.sql.Timestamp(parsedDateFR.getTime());
						 par.setDateReceived(tmpFechaRecepcion);

						 String FechaAcuse =a[8]+":00.0";

						 if(FechaAcuse.equals(":00.0")) {

						 }else {
							 Date parsedDateFA = dateFormat.parse(FechaAcuse);
							 Timestamp tmpFechaAcuse = new java.sql.Timestamp(parsedDateFA.getTime());
							 par.setDate1(tmpFechaAcuse);
						 }

						 par.setAmount(BigDecimal.valueOf(Integer.parseInt(a[9]))); //String MontoExento =a[9];
						 par.setTotalLines(BigDecimal.valueOf(Integer.parseInt(a[10])));//String MontoNeto =a[10];
						 par.setTaxAmt(BigDecimal.valueOf(Integer.parseInt(a[11]))); //String MontoIVARecuperable =a[11];
						 /*String MontoIvaNoRecuperable =a[12];
						 if(MontoIvaNoRecuperable.equals("")) {
							 MontoIvaNoRecuperable = "0";
						 }*/
						 //par.setmontoivanorecuperable(BigDecimal.valueOf(Integer.parseInt(MontoIvaNoRecuperable)));
						 //par.setcodigoivanorec(a[13]); //String CodigoIVANoRec =a[13];
						 String MontoTotal =a[14];
						 if(MontoTotal.equals("")) {
							 MontoTotal = "0";
						 }
						 par.setGrandTotal(BigDecimal.valueOf(Integer.parseInt(MontoTotal)));
						 /*String MontoNetoActivoFijo =a[15];
						 if(MontoNetoActivoFijo.equals("")) {
							 MontoNetoActivoFijo = "0";
						 }
						 par.setmontonetoactivofijo(BigDecimal.valueOf(Integer.parseInt(MontoNetoActivoFijo)));
						 String IVAActivoFijo =a[16];
						 if(IVAActivoFijo.equals("")) {
							 IVAActivoFijo = "0";
						 }
						 par.setivaactivofijo(BigDecimal.valueOf(Integer.parseInt(IVAActivoFijo)));
						 String IVAusoComun =a[17];
						 if(IVAusoComun.equals("")) {
							 IVAusoComun = "0";
						 }
						 par.setivausocomun(BigDecimal.valueOf(Integer.parseInt(IVAusoComun)));
						 String ImptoSinDerechoaCredito =a[18];
						 if(ImptoSinDerechoaCredito.equals("")) {
							 ImptoSinDerechoaCredito = "0";
						 }
						 par.setimptosinderechoacredito(BigDecimal.valueOf(Integer.parseInt(ImptoSinDerechoaCredito)));
						 String IVANoRetenido =a[19];
						 if(IVANoRetenido.equals("")) {
							 IVANoRetenido = "0";
						 }
						 par.setivanoretenido(BigDecimal.valueOf(Integer.parseInt(IVANoRetenido)));*/
						 //String TabacosPuros =a[20];
						 //String TabacosCigarrillos =a[21];
						 //String TabacosElaborados =a[22];
						 String NCEoNDEsobreFactdeCompra =a[23];
						 if(NCEoNDEsobreFactdeCompra.equals("")) {
							 NCEoNDEsobreFactdeCompra = "0";
						 }
						 par.setPOReference(BigDecimal.valueOf(Integer.parseInt(NCEoNDEsobreFactdeCompra)));
						 /*par.setcodigootroimpuesto(a[24]); //String CodigoOtroImpuesto =a[24];
						 String ValorOtroImpuesto =a[25];
						 if(ValorOtroImpuesto.equals("")) {
							 ValorOtroImpuesto = "0";
						 }
						 par.setvalorotroimpuesto(BigDecimal.valueOf(Integer.parseInt(ValorOtroImpuesto)));
						 String TasaOtroImpuesto =a[26];
						 if(TasaOtroImpuesto.equals("")) {
							 TasaOtroImpuesto = "0";
						 }
						 par.settasaotroimpuesto(BigDecimal.valueOf(Integer.parseInt(TasaOtroImpuesto)));*/
						 par.set_ValueOfColumn("PreParqueoStatus", "N");
						 par.saveEx(get_TrxName());
						log.log(Level.INFO, "linea_" +i +": "+partes[i]+ " -");
						count++;
					}
					//log.log(Level.INFO, "linea_" +i +": "+partes[i]);
				}

			}


		} catch(FileNotFoundException ex){
			System.err.println(ex.getMessage());
		}catch(IOException ex) {
			System.err.println(ex.getMessage());
		}

		return "Se ingresaron un total de: "+ count+ " registro de Pre-Parqueo.";
	}	//	doIt

	/*public static boolean validarFecha(String fecha) {
        try {
	            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
	            formatoFecha.setLenient(false);
	            formatoFecha.parse(fecha);
	        } catch (ParseException e) {
	            return false;
	        }
        return true;
    }*/



}	//	CopyOrder
