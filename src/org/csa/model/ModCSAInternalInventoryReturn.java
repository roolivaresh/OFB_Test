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

import org.adempiere.core.domains.models.I_M_Inventory;
import org.compiere.model.MClient;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;

/**
 * Turn internal use inventory into negative to increase stock
 *
 * @author jleyton
 */
public class ModCSAInternalInventoryReturn implements ModelValidator {
	/**
	 * Constructor. The class is instantiated when logging in and client is
	 * selected/known
	 */
	public ModCSAInternalInventoryReturn() {
		super();
	} // MyValidator

	/** Logger */
	private static CLogger log = CLogger.getCLogger(ModCSAInternalInventoryReturn.class);
	/** Client */
	private int m_AD_Client_ID = -1;

	/**
	 * Initialize Validation
	 *
	 * @param engine validation engine
	 * @param client client
	 */
	@Override
	public void initialize(ModelValidationEngine engine, MClient client) {
		// client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		} else {
			log.info("Initializing Model Price Validator: " + this.toString());
		}

		// Tables to be monitored
		engine.addDocValidate(I_M_Inventory.Table_Name, this);

	} // initialize

	/**
	 * Model Change of a monitored Table.
	 *
	 */
	@Override
	public String modelChange(PO po, int type) throws Exception {
		return null;
	} // modelChange

	@Override
	public String docValidate(PO po, int timing) {
		log.info(po.get_TableName() + " Timing: " + timing);

		if (timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID() == I_M_Inventory.Table_ID) {
			MInventory inv = (MInventory) po;
			int c=0;
			//se evalua que el consumo sea recepcion de prestamo (ID 1000069)
			if (inv.getC_DocType_ID() == 1000069) {
				MInventoryLine[] lines = inv.getLines(true);
				for (MInventoryLine line : lines) {
					line.setQtyInternalUse(line.getQtyInternalUse().negate());
					c++;
				}
				log.config("Se convierten "+c+" lineas.");
			}

		}

		return null;
	} // docValidate

	/**
	 * User Login. Called when preferences are set
	 *
	 * @param AD_Org_ID  org
	 * @param AD_Role_ID role
	 * @param AD_User_ID user
	 * @return error message or null
	 */
	@Override
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	} // login

	/**
	 * Get Client to be monitored
	 *
	 * @return AD_Client_ID client
	 */
	@Override
	public int getAD_Client_ID() {
		return m_AD_Client_ID;
	} // getAD_Client_ID

	/**
	 * String Representation
	 *
	 * @return info
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("ModelPrice");
		return sb.toString();
	} // toString

}