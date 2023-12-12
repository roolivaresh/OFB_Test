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

import org.adempiere.core.domains.models.I_M_Storage;
import org.compiere.model.MClient;
import org.compiere.model.MStorage;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 * Validator for CSA When a product has no attribute set instance, the smallest
 * ID is selected to leave all quantities in a single attribute.
 *
 * @author jleyton
 */
public class ModCSAUpdateStorage implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCSAUpdateStorage ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCSAUpdateStorage.class);
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
		engine.addModelChange(I_M_Storage.Table_Name, this);

		//

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *
     */
	@Override
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW )&& po.get_Table_ID() == I_M_Storage.Table_ID)
		{
			MStorage st = (MStorage)po;
			if(st.getM_Product().getM_AttributeSet_ID() <=0)//no posee conjunto de atributo
			{
				//se cuenta cantidad en m_storage
				int cant = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM M_Storage "
						+ " WHERE M_Product_ID="+st.getM_Product_ID()
						+ " AND M_Locator_ID="+st.getM_Locator_ID());
				if(cant > 1)//existe mas de un storage
				{
					//se busca menor instancia (podria ser 0)
					int insID = DB.getSQLValue(po.get_TrxName(), "SELECT MIN(m_attributesetinstance_id) FROM M_Storage "
							+" WHERE IsActive='Y' AND M_Product_ID="+st.getM_Product_ID()
							+" AND M_Locator_ID="+st.getM_Locator_ID());
					//se suman cantidades completas
					BigDecimal qtyOH = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qtyonhand) FROM M_Storage "
							+" WHERE M_Product_ID="+st.getM_Product_ID()
							+" AND M_Locator_ID="+st.getM_Locator_ID());

					BigDecimal qtyR = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qtyreserved) FROM M_Storage "
							+" WHERE M_Product_ID="+st.getM_Product_ID()
							+" AND M_Locator_ID="+st.getM_Locator_ID());

					BigDecimal qtyO = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qtyordered) FROM M_Storage "
							+" WHERE M_Product_ID="+st.getM_Product_ID()
							+" AND M_Locator_ID="+st.getM_Locator_ID());

					//se actualiza storage con las cantidades totales
					DB.executeUpdate("UPDATE M_Storage SET qtyonhand="+qtyOH+",qtyreserved="+qtyR+",qtyordered="+qtyO
							+" WHERE M_Product_ID="+st.getM_Product_ID()+" AND M_Locator_ID="+st.getM_Locator_ID()
							+" AND m_attributesetinstance_id="+insID,po.get_TrxName());

					//se dejan demas storages en 0
					DB.executeUpdate("UPDATE M_Storage SET qtyonhand=0,qtyreserved=0,qtyordered=0,isactive='N'"
							+" WHERE M_Product_ID="+st.getM_Product_ID()+" AND M_Locator_ID="+st.getM_Locator_ID()
							+" AND m_attributesetinstance_id <> "+insID,po.get_TrxName());
				}
			}
			//
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