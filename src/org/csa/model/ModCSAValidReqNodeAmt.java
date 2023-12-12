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
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MRequisition;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
/**
 *	Generates a internal use inventory document with the request parameters
 *
 *  @author SeColoma
 */
public class ModCSAValidReqNodeAmt implements ModelValidator{

	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCSAValidReqNodeAmt ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCSAWarehouseRequest.class);
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
		engine.addModelChange(I_M_Requisition.Table_Name, this);

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *
     */
	@Override
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);

		if((type == TYPE_AFTER_CHANGE )&& po.get_Table_ID() == I_M_Requisition.Table_ID
				&& po.is_ValueChanged("DocStatus"))
		{
			//Rodrigo Olivares Hurtado 2023-08-29 se toma en cuenta el cambio de monedo
			//Si el tipo de moneda es diferente al CLP se debe usar el CurrencyCLP
			BigDecimal GrandTotal = null;

			MRequisition req = (MRequisition)po;
			int CurrencyID = DB.getSQLValue(po.get_TrxName(), "SELECT COALESCE(MAX(C_CURRENCY_ID),0) FROM M_REQUISITION "
					+ "WHERE M_REQUISITION_ID = "+ req.get_ID());


			if(CurrencyID == 228) {//CLP
				GrandTotal = req.getGrandTotal();
			}else {
				GrandTotal = new BigDecimal(req.get_ValueAsString("CurrencyCLP"));
			}
			//
			if(po.get_ValueOld("DocStatus").equals("ER") && req.getDocStatus().equals("AF")
					&& GrandTotal.compareTo(new BigDecimal(500000))<0)
				//Rodrigo Olivares Hurtado 2023-08-17 actualización del mensaje
				throw new AdempiereException("El total es menor a $500.000, pasar por el estado '70 Validación de Solicitud de Compra'");

			if(po.get_ValueOld("DocStatus").equals("ER") && req.getDocStatus().equals("VS")
					&& GrandTotal.compareTo(new BigDecimal(500000))>=0)
				//Rodrigo Olivares Hurtado 2023-08-17 actualización del mensaje
				throw new AdempiereException("El total es mayor o igual a $500.000, pasar por el estado '40 Aprobación de Compra Finanzas'");
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
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString
}