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
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MInvoice;
import org.compiere.model.X_I_PreParqueo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *
 *
 *  @author italo ni�oles ininoles
 *  @Updatedby Rodrigo Olivares Hurtado
 *  @version $Id: JoinPreParqueoInvoice.java $
 *  switch for Rodrigo Olivares Hurtado
 */
public class JoinPreParqueoInvoice extends SvrProcess
{

	/**
	 *  Prepare - e.g., get Parameters.
	 */

	private Timestamp 			p_DateFrom;
	private Timestamp 			p_DateTo;

	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();

			if (name.equals("DateInvoiced"))
			{
				p_DateFrom = element.getParameterAsTimestamp();
				p_DateTo = element.getParameterToAsTimestamp();
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
		//se buscan facturas
		String sqlIn = "SELECT C_Invoice_ID FROM C_Invoice WHERE IssoTrx = 'N' AND PreParqueoStatus IN ('N', 'C')";
		if(p_DateFrom != null && p_DateTo != null)
			sqlIn = sqlIn + " AND DateInvoiced BETWEEN ? AND ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int ID_PreParqueo;
		int ID_PreParAunx=0;
		X_I_PreParqueo parAux = null;
		try
		{
			pstmt = DB.prepareStatement(sqlIn, get_TrxName());
			if(p_DateFrom != null && p_DateTo != null)
			{
				pstmt.setTimestamp(1, p_DateFrom);
				pstmt.setTimestamp(2, p_DateTo);
			}
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				ID_PreParqueo = 0;
				ID_PreParAunx = 0;
				parAux = null;
				MInvoice inv = new MInvoice(getCtx(), rs.getInt("C_Invoice_ID"), get_TrxName());
				//mfrojas 20200602 obtener codigo sii en c_doctype
				String doctypesii = DB.getSQLValueString(get_TrxName(), "SELECT coalesce(c_doctype_id,0) FROM C_DocType "
						+ " WHERE c_doctype_id = ?", inv.getC_DocTypeTarget_ID());
				if(doctypesii.compareTo("0") == 0)
					continue;
				//Rodrigo Olivares Hurtado 2023-07-28
				//Se agrega como parametro de busqueda DateDoc
				ID_PreParqueo = DB.getSQLValue(get_TrxName(), "SELECT MAX(I_PreParqueo_ID) FROM I_PreParqueo "
	    				+" WHERE C_BPartner_ID = "+inv.getC_BPartner_ID()
	    				+" AND DocumentNO like '"+inv.getDocumentNo()+"' "
						+" AND GrandTotal = ? "
						+ " AND DateDoc = '"+ inv.getDateInvoiced() +"'"
						+ " AND PreParqueoStatus IN ('N','C')"
						+" AND c_doctype_id = "+doctypesii,inv.getGrandTotal());

	    		if(ID_PreParqueo > 0)//parqueo total
	    		{
	    			//DB.executeUpdate("UPDATE C_Invoice SET PreParqueoStatus='P' WHERE C_Invoice_ID = "+inv.get_ID(), get_TrxName());
	    			if(inv.getDocStatus().compareTo("DR")==0
	    					|| inv.getDocStatus().compareTo("IP")==0)
	    			{
	    				//Se completa el documento
	    				inv.processIt("CO");
	    				inv.set_CustomColumn("PreParqueoStatus", "P");
	    				inv.saveEx(get_TrxName());
	    				//DB.executeUpdate("UPDATE C_Invoice SET PreParqueoStatus='C' WHERE C_Invoice_ID = "+ID_Invoice, get_TrxName());
	    			}
	    			//se actualiza PreParqueo
	    			DB.executeUpdate("UPDATE I_PreParqueo SET PreParqueoStatus='P',C_Invoice_ID="+inv.get_ID()+" WHERE I_PreParqueo_ID = "+ID_PreParqueo, get_TrxName());
	    		}
	    		else
	    		{
	    			ID_PreParqueo = DB.getSQLValue(get_TrxName(), "SELECT MAX(I_PreParqueo_ID) FROM I_PreParqueo "
	        				+" WHERE C_BPartner_ID = "+inv.getC_BPartner_ID()
	    					+" AND GrandTotal = ? AND PreParqueoStatus IN ('N', 'C')",inv.getGrandTotal());
	    			if(ID_PreParqueo > 0)//parqueo parcial
	    			{
	    				//antes de parqueo parcial de factura se busca que no exista parqueo total para preparqueo
	    				parAux = new X_I_PreParqueo(getCtx(), ID_PreParqueo, get_TrxName());
	    				ID_PreParAunx = DB.getSQLValue(get_TrxName(), "SELECT MAX(inv.C_Invoice_ID) FROM C_Invoice inv "
	    						+ " JOIN c_bpartner bp on inv.c_bpartner_id = bp.c_bpartner_id"
	    						+" WHERE inv.IssoTrx = 'N' AND bp.Value||'-'|| case when bp.ValueValidator is null then '0' else bp.ValueValidator end LIKE '%"+parAux.getC_BPartner().getValue()+"%'"
	    						//+" AND inv.DocStatus IN ('CO','CL','IP','DR') "
	    						+" AND inv.GrandTotal = ? AND inv.PreParqueoStatus ='N'",parAux.getGrandTotal());
	    				if(ID_PreParAunx > 0)//no se parquea si existe un posible paequeo total
	    				{
		    				//DB.executeUpdate("UPDATE C_Invoice SET PreParqueoStatus='C' WHERE C_Invoice_ID = "+inv.get_ID(), get_TrxName());
	    					inv.processIt("PR");
		    				inv.set_CustomColumn("PreParqueoStatus", "C");
		    				inv.saveEx(get_TrxName());
		        			//se actualiza PreParqueo
		        			DB.executeUpdate("UPDATE I_PreParqueo SET PreParqueoStatus='C',C_Invoice_ID="+inv.get_ID()+" WHERE I_PreParqueo_ID = "+ID_PreParqueo, get_TrxName());
	    				}


	    			}else {
	    				//Rodrigo Olivares Hurtado 2023-07-27
		    			//Parqueo parcial solo con socio de negocio y documentno
	    				ID_PreParqueo = DB.getSQLValue(get_TrxName(),"SELECT MAX(I_PreParqueo_ID) FROM I_PreParqueo "
	    						+ "WHERE C_BPartner_ID = "+inv.getC_BPartner_ID()
	    						+ " AND PreParqueoStatus IN ('N', 'C') "
	    						+ " AND documentno like '"+ inv.getDocumentNo()+"'");
		    			if(ID_PreParqueo > 0){
		    				if(inv.getDocStatus().compareTo("DR")==0
			    					|| inv.getDocStatus().compareTo("IP")==0)
			    			{
			    				inv.processIt("PR");
			    				inv.set_CustomColumn("PreParqueoStatus", "C");
			    				inv.saveEx(get_TrxName());
			    			}
			    			//se actualiza PreParqueo
			    			DB.executeUpdate("UPDATE I_PreParqueo SET PreParqueoStatus='C',C_Invoice_ID="+inv.get_ID()+" WHERE I_PreParqueo_ID = "+ID_PreParqueo, get_TrxName());

		    			}else {
		    				//Rodrigo Olivares Hurtado
		    				//Parqueo parcial solo con el socio del negocio y fecha.
		    				ID_PreParqueo = DB.getSQLValue(get_TrxName(),"SELECT MAX(I_PreParqueo_ID) FROM I_PreParqueo "
		    						+ "WHERE C_BPartner_ID = "+inv.getC_BPartner_ID()
		    						+ " AND PreParqueoStatus IN ('N', 'C') "
		    						+ " AND DateDoc = '"+ inv.getDateInvoiced() +"'");
		    				if(ID_PreParqueo > 0) {
		    					if(inv.getDocStatus().compareTo("DR")==0
				    					|| inv.getDocStatus().compareTo("IP")==0)
				    			{
				    				inv.processIt("PR");
				    				inv.set_CustomColumn("PreParqueoStatus", "C");
				    				inv.saveEx(get_TrxName());
				    			}
				    			//se actualiza PreParqueo
				    			DB.executeUpdate("UPDATE I_PreParqueo SET PreParqueoStatus='C',C_Invoice_ID="+inv.get_ID()+" WHERE I_PreParqueo_ID = "+ID_PreParqueo, get_TrxName());
		    				}else {
		    					//Rodrigo Olivares 2023-07-31
		    					//Se agregan mas combinaciones de parqueos parciales
		    					ID_PreParqueo = DB.getSQLValue(get_TrxName(), "SELECT MAX(I_PreParqueo_ID) FROM I_PreParqueo "
		    		    				+" WHERE C_BPartner_ID = "+inv.getC_BPartner_ID()
		    		    				+" AND DocumentNO like '"+inv.getDocumentNo()+"' "
		    							+" AND GrandTotal = ? "
		    							+ " AND PreParqueoStatus IN ('N','C')",inv.getGrandTotal());
		    					int ID_PreParqueo2 = DB.getSQLValue(get_TrxName(), "SELECT MAX(I_PreParqueo_ID) FROM I_PreParqueo "
		    		    				+" WHERE C_BPartner_ID = "+inv.getC_BPartner_ID()
		    		    				+" AND DocumentNO like '"+inv.getDocumentNo()+"' "
		    							+ " AND DateDoc = '"+ inv.getDateInvoiced() +"'"
		    							+ " AND PreParqueoStatus IN ('N','C')");
		    					int ID_PreParqueo3 = DB.getSQLValue(get_TrxName(), "SELECT MAX(I_PreParqueo_ID) FROM I_PreParqueo "
		    		    				+" WHERE C_BPartner_ID = "+inv.getC_BPartner_ID()
		    							+" AND GrandTotal = ? "
		    							+ " AND DateDoc = '"+ inv.getDateInvoiced() +"'"
		    							+ " AND PreParqueoStatus IN ('N','C')",inv.getGrandTotal());
		    					int ID_PreParqueo4 = DB.getSQLValue(get_TrxName(), "SELECT MAX(I_PreParqueo_ID) FROM I_PreParqueo "
		    		    				+" WHERE C_BPartner_ID = "+inv.getC_BPartner_ID()
		    							+ " AND PreParqueoStatus IN ('N','C')"
		    							+" AND c_doctype_id = "+doctypesii);
		    					int ID_PreParqueo5 = DB.getSQLValue(get_TrxName(), "SELECT MAX(I_PreParqueo_ID) FROM I_PreParqueo "
		    		    				+" WHERE C_BPartner_ID = "+inv.getC_BPartner_ID()
		    							+" AND GrandTotal = ? "
		    							+ " AND PreParqueoStatus IN ('N','C')"
		    							+" AND c_doctype_id = "+doctypesii,inv.getGrandTotal());
		    					int ID_PreParqueo6 = DB.getSQLValue(get_TrxName(), "SELECT MAX(I_PreParqueo_ID) FROM I_PreParqueo "
		    		    				+" WHERE C_BPartner_ID = "+inv.getC_BPartner_ID()
		    		    				+" AND DocumentNO like '"+inv.getDocumentNo()+"' "
		    							+ " AND PreParqueoStatus IN ('N','C')"
		    							+" AND c_doctype_id = "+doctypesii);
		    					int ID_PreParqueo7 = DB.getSQLValue(get_TrxName(), "SELECT MAX(I_PreParqueo_ID) FROM I_PreParqueo "
		    		    				+" WHERE C_BPartner_ID = "+inv.getC_BPartner_ID()
		    		    				+ " AND DateDoc = '"+ inv.getDateInvoiced() +"'"
		    							+ " AND PreParqueoStatus IN ('N','C')"
		    							+" AND c_doctype_id = "+doctypesii);

		    					if(ID_PreParqueo > 0 || ID_PreParqueo2 > 0 || ID_PreParqueo3 > 0 || ID_PreParqueo4 > 0
		    							|| ID_PreParqueo5 > 0 ||ID_PreParqueo6 > 0 || ID_PreParqueo7 > 0) {
		    						if(inv.getDocStatus().compareTo("DR")==0
					    					|| inv.getDocStatus().compareTo("IP")==0)
					    			{
					    				inv.processIt("PR");
					    				inv.set_CustomColumn("PreParqueoStatus", "C");
					    				inv.saveEx(get_TrxName());
					    			}
					    			//se actualiza PreParqueo
					    			DB.executeUpdate("UPDATE I_PreParqueo SET PreParqueoStatus='C',C_Invoice_ID="+inv.get_ID()+" WHERE I_PreParqueo_ID = "+ID_PreParqueo, get_TrxName());

		    					}
		    				}


		    			}
	    			}



	    		}

	    		//mfrojas 20200602
	    		//Despues de obtener esta informacion, se debe ver si existe alguna NotaCredito en
	    		//la tabla de preparqueo, para actualizar c_invoice


	    		String sqlobtainnc = "SELECT coalesce(max(i_preparqueo_id),0) FROM i_preparqueo WHERE "
	    				+ " c_doctype_id = "+inv.getC_DocType_ID()+ "and c_bpartner_id = "+inv.getC_BPartner().getValue()
	    				+ " and POReference is not null and preparqueostatus like 'N' and POReference = 1";
	    		int idnc = DB.getSQLValue(get_TrxName(), sqlobtainnc);
	    		if(idnc > 0)
	    		{
    				DB.executeUpdate("UPDATE C_Invoice SET DocumentNoNC = (SELECT documentno from i_preparqueo where "
    						+ " i_preparqueo_id = "+idnc+"), amountnc = ( SELECT grandtotal from i_preparqueo where "
    						+ " i_preparqueo_id = "+idnc+") WHERE C_Invoice_ID = "+inv.get_ID(), get_TrxName());

        			DB.executeUpdate("UPDATE I_PreParqueo SET PreParqueoStatus='P',C_Invoice_ID="+inv.get_ID()+" WHERE I_PreParqueo_ID = "+idnc, get_TrxName());

	    		}


			}
		}
		catch (SQLException e)
		{
				throw new DBException(e, sqlIn);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		//se buscan preparqueos
		String sqlPP = "SELECT pre.I_PreParqueo_ID FROM I_PreParqueo pre "
				+ "JOIN C_DOCTYPE doc ON pre.c_doctype_id = doc.c_doctype_id "
				+ "WHERE pre.PreParqueoStatus IN ('N') and doc.documentno not in ('61','52')";
		if(p_DateFrom != null && p_DateTo != null)
			sqlPP = sqlPP + " AND DateDoc BETWEEN ? AND ?";
		PreparedStatement pstmtPP = null;
		ResultSet rsPP = null;
		int ID_Invoice=0;
		MInvoice invAux;
		int ID_InvAux;
		try
		{
			pstmtPP = DB.prepareStatement(sqlPP, get_TrxName());
			if(p_DateFrom != null && p_DateTo != null)
			{
				pstmtPP.setTimestamp(1, p_DateFrom);
				pstmtPP.setTimestamp(2, p_DateTo);
			}
			rsPP = pstmtPP.executeQuery();
			while (rsPP.next())
			{
				ID_Invoice=0;
				ID_InvAux=0;
				invAux = null;
				X_I_PreParqueo par = new X_I_PreParqueo(getCtx(), rsPP.getInt("I_PreParqueo_ID"), get_TrxName());

				ID_Invoice = DB.getSQLValue(get_TrxName(), "SELECT MAX(inv.C_Invoice_ID) FROM C_Invoice inv "
						+ "JOIN c_bpartner bp on inv.c_bpartner_id = bp.c_bpartner_id "
	    				+" WHERE inv.IssoTrx = 'N' AND bp.value||'-'|| case when bp.ValueValidator is null then '0' else bp.ValueValidator end LIKE '%"+par.getC_BPartner().getValue()+"%'"
	    				+" AND inv.DocStatus like '"+par.getDocumentNo()+"' "
						+" AND inv.GrandTotal = ? AND inv.PreParqueoStatus ='N' ",par.getGrandTotal());
	    		if(ID_Invoice > 0)
	    		{
	    			par.setC_Invoice_ID(ID_Invoice);
	    			par.set_CustomColumn("PreParqueoStatus", "P");
	    			par.saveEx(get_TrxName());
	    			//se actualiza factura
	    			//DB.executeUpdate("UPDATE C_Invoice SET PreParqueoStatus='P' WHERE C_Invoice_ID = "+ID_Invoice, get_TrxName());
	    			MInvoice inv = new MInvoice(getCtx(), ID_Invoice, get_TrxName());
	    			if(inv.getDocStatus().compareTo("DR")==0
	    					|| inv.getDocStatus().compareTo("IP")==0
	    					|| inv.getDocStatus().compareTo("IN")==0)
	    			{
	    				inv.processIt("CO");
	    				inv.set_CustomColumn("PreParqueoStatus", "P");
	    				inv.saveEx(get_TrxName());
	    			}
	    		}
	    		else
	    		{
	    			ID_Invoice = DB.getSQLValue(get_TrxName(), "SELECT MAX(inv.C_Invoice_ID) FROM C_Invoice inv "
	    					+"JOIN c_bpartner bp on bp.c_bpartner_id = inv.c_bpartner_id "
		    				+" WHERE inv.IssoTrx = 'N' AND bp.value||'-'|| case when bp.ValueValidator is null then '0' else bp.ValueValidator end LIKE '%"+par.getC_BPartner().getValue()+ "%'"
		    				//+" AND inv.DocStatus IN ('CO','CL') "
							+" AND inv.GrandTotal = ? AND inv.PreParqueoStatus='N' ",par.getGrandTotal());
	    			if(ID_Invoice > 0)
	    			{
	    				//antes de parqueo parcial de preparqueo se busca que no exista parqueo total para factura
	    				invAux = new MInvoice(getCtx(), ID_Invoice, get_TrxName());
	    				ID_InvAux = DB.getSQLValue(get_TrxName(), "SELECT MAX(I_PreParqueo_ID) FROM I_PreParqueo"
	    						+ " WHERE c_bpartner_id = "+invAux.getC_BPartner_ID()
	    	    				+" AND documentno like '"+invAux.getDocumentNo()+"' "
	    						+" AND GrandTotal = ? AND PreParqueoStatus IN ('N')",invAux.getGrandTotal());
	    				if(ID_InvAux <= 0)//no se parquea si existe un posible parqueo total
	    				{
	    					par.setC_Invoice_ID(ID_Invoice);
			    			par.set_CustomColumn("PreParqueoStatus","C");
			    			par.saveEx(get_TrxName());
			    			//se actualiza factura
			    			//DB.executeUpdate("UPDATE C_Invoice SET PreParqueoStatus='C' WHERE C_Invoice_ID = "+ID_Invoice, get_TrxName());
			    			MInvoice inv2 = new MInvoice(getCtx(), ID_Invoice, get_TrxName());
			    			if(inv2.getDocStatus().compareTo("DR")==0
			    					|| inv2.getDocStatus().compareTo("IN")==0
			    					|| inv2.getDocStatus().compareTo("IP")==0)
			    			{
			    				inv2.processIt("PR");
			    				inv2.set_CustomColumn("PreParqueoStatus", "C");
			    				inv2.saveEx(get_TrxName());
			    			}
	    				}
	    			}
	    		}
	    		//ininoles se agrega parqueo a NC
	    		//se busca si hay relacion a nota de credito
	    		if(par.getPOReference().equals(1))
	    		{
	    			int ID_NC = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Invoice_ID) FROM C_Invoice"
	    				+" WHERE IssoTrx = 'N' AND bp.value||'-'|| case when bp.ValueValidator is null then '0' else bp.ValueValidator end LIKE '%"+par.getC_BPartner().getValue() +"%'"
	    				+" AND DocumentNoNC like '"+par.getPOReference()+"' "
						+" AND PreParqueoStatus='N' AND AmountNC = ? ",par.getGrandTotal());
	    			if(ID_NC > 0)
	    			{
	    				par.setC_Invoice_ID(ID_Invoice);
		    			par.set_CustomColumn("PreParqueoStatus", "C");
		    			par.saveEx(get_TrxName());

		    			MInvoice nc = new MInvoice(getCtx(), ID_NC, get_TrxName());
		    			if(nc.getDocStatus().compareTo("DR")==0
		    					|| nc.getDocStatus().compareTo("IN")==0
		    					|| nc.getDocStatus().compareTo("IP")==0)
		    			{
		    				nc.processIt("CO");
		    				nc.set_CustomColumn("PreParqueoStatus", "C");
		    				nc.saveEx(get_TrxName());
		    			}
	    			}
	    		}
			}
		}
		catch (SQLException e)
		{
				throw new DBException(e, sqlPP);
		}
		finally
		{
			DB.close(rsPP, pstmtPP);
			rsPP = null; pstmtPP = null;
		}

		return "PROCESADO";
	}	//	doIt



}	//	CopyOrder
