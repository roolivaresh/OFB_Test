/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
package org.csa.model;


import java.math.BigDecimal;

import org.adempiere.core.domains.models.I_M_Requisition;
import org.adempiere.core.domains.models.I_M_RequisitionLine;
import org.compiere.model.MClient;
import org.compiere.model.MConversionRate;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for CSA
 *  complete column CurrencyCLP
 *  @author Rodrigo Olivares Hurtado
 */
public class ModCSACurrencyCLP implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCSACurrencyCLP ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCSACurrencyCLP.class);
	/** Client			*/
	private int		m_AD_Client_ID = -1;


	/**
	 *	Initialize Validation
	 *	@param engine validation engine
	 *	@param client client
	 */
	@Override
	public void initialize (ModelValidationEngine engine, MClient client)
	{
		//client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		}
		else  {
			log.info("Initializing Model Price Validator: "+this.toString());
		}

		//	Tables to be monitored
		engine.addModelChange(I_M_RequisitionLine.Table_Name, this);
		//engine.addModelChange(MRequisition.Table_Name, this);

		//engine.addDocValidate(MRequisition.Table_Name, this);



	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *
     */
	@Override
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		//Rodrigo Olivares Hurtado 2023-08-29 Se ajusta para actualice cabecera cuando se crea y se borra una linea
		if((type == TYPE_AFTER_NEW ||type == TYPE_AFTER_CHANGE ||type == TYPE_AFTER_DELETE )&& po.get_Table_ID() == I_M_RequisitionLine.Table_ID)
		{
			MRequisitionLine reql  = null;
			MRequisition req = null;
			if(po.get_Table_ID() == I_M_RequisitionLine.Table_ID) {
				 reql = (MRequisitionLine)po;
				 req = new MRequisition(po.getCtx(),reql.getM_Requisition_ID(),po.get_TrxName());
			}else if (po.get_Table_ID() == I_M_Requisition.Table_ID){
				req = (MRequisition)po;
			}


			int CurrencyCLPID = DB.getSQLValue(po.get_TrxName(), "SELECT COALESCE(MAX(C_Currency_ID),0) FROM C_Currency WHERE ISO_Code = 'CLP'");
			int ConversionRateID = 0;
			int curency = 0;
			BigDecimal GrandTotalCLP = Env.ZERO;


			if(req.getC_Currency_ID()!= 0 && CurrencyCLPID > 0) {
				curency = DB.getSQLValue(po.get_TrxName(), "SELECT C_Currency_ID FROM M_REQUISITION WHERE M_REQUISITION_ID = "+req.get_ID());
				ConversionRateID = DB.getSQLValue(po.get_TrxName(), "SELECT COALESCE(MAX(C_Conversion_Rate_ID),0) FROM C_Conversion_Rate "
						+ " WHERE C_Currency_ID = "+CurrencyCLPID+ " and C_Currency_ID_To = "+curency
						+ "  AND '"+ req.getDateDoc() +"' BETWEEN ValidFrom AND ValidTo");
				if(ConversionRateID > 0) {
					MConversionRate conv = new MConversionRate (po.getCtx(),ConversionRateID,po.get_TrxName());

					if (curency == 1000002) { //UF
						GrandTotalCLP = req.getGrandTotal().multiply(conv.getDivideRate());
						req.set_ValueOfColumn("CurrencyCLP", GrandTotalCLP);
						req.saveEx(po.get_TrxName());

					}else if (curency == 100) { //USD
						GrandTotalCLP = req.getGrandTotal().multiply(conv.getDivideRate());
						req.set_ValueOfColumn("CurrencyCLP", GrandTotalCLP);
						req.saveEx(po.get_TrxName());

					}else if(curency == 102) { //EUR
						GrandTotalCLP = req.getGrandTotal().multiply(conv.getDivideRate());
						req.set_ValueOfColumn("CurrencyCLP", GrandTotalCLP);
						req.saveEx(po.get_TrxName());

					} else if(curency == 1000001) { //UTA
						GrandTotalCLP = req.getGrandTotal().multiply(conv.getDivideRate());
						req.set_ValueOfColumn("CurrencyCLP", GrandTotalCLP);
						req.saveEx(po.get_TrxName());

					}else if(curency == 1000000) { // UTM
						GrandTotalCLP = req.getGrandTotal().multiply(conv.getDivideRate());
						req.set_ValueOfColumn("CurrencyCLP", GrandTotalCLP);
						req.saveEx(po.get_TrxName());

					}

				}else if(curency == CurrencyCLPID) {//CLP
					req.set_ValueOfColumn("CurrencyCLP", req.getGrandTotal());
					req.saveEx(po.get_TrxName());
				}





			}


		}


		return null;
	}	//	modelChange

	@Override
	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);

		return null;
	}	//	docValidate

	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	@Override
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	}	//	login


	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID client
	 */
	@Override
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID


	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("ModelPrice");
		return sb.toString ();
	}	//	toString




}