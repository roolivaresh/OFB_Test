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
 *	Validator for CSA
 *  Complete fields from a given project
 *  @author Rodrigo Olivares Hurtado
 */
public class ModCSAValidReq implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCSAValidReq ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCSAValidReq.class);
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
		if((type == TYPE_BEFORE_CHANGE )&& po.get_Table_ID() == I_M_Requisition.Table_ID
				&& po.is_ValueChanged("DocStatus"))
		{
			MRequisition req = (MRequisition)po;
			//Se comprueba si la solicitud no tiene lineas
			int countreqline = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM M_RequisitionLine WHERE M_Requisition_ID = "+req.get_ID());

			if (countreqline == 0) {
				//Si la solicitud no tiene lineas y su proximo nodo es EA, VE, RS no se debe pasar a aquellos nodos
				if (req.getDocStatus().equals("EA")) {
					throw new AdempiereException("La solicitud de compra: "+ req.getDocumentNo()+" debe tener por lo menos un registro en la linea");
				}else if(req.getDocStatus().equals("VE")) {
					throw new AdempiereException("La solicitud de compra: "+ req.getDocumentNo()+" debe tener por lo menos un registro en la linea");
				}else if(req.getDocStatus().equals("RS")) {
					throw new AdempiereException("La solicitud de compra: "+ req.getDocumentNo()+" debe tener por lo menos un registro en la linea");
				}else if(req.getDocStatus().equals("COS")) {
					throw new AdempiereException("La solicitud de compra: "+ req.getDocumentNo()+" debe tener por lo menos un registro en la linea");
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