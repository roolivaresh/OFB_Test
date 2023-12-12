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

import org.adempiere.core.domains.models.I_C_Order;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;

/**
 * Validator QtyEntered and PriceEntered in Purchase Order OFB
 *
 * @author Jose Leyton
 */
public class ModCSAValidQtyPriceOC implements ModelValidator {
	/**
	 * Constructor. The class is instantiated when logging in and client is
	 * selected/known
	 */
	public ModCSAValidQtyPriceOC() {
		super();
	} // MyValidator

	/** Logger */
	private static CLogger log = CLogger.getCLogger(ModCSAValidQtyPriceOC.class);
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
			log.info("Initializing global validator: " + this.toString());
		}

		// Tables to be monitored
		// Documents to be monitored
		engine.addDocValidate(I_C_Order.Table_Name, this);

	} // initialize

	/**
	 * Model Change of a monitored Table.
	 *
	 */
	public static final String DOCSTATUS_Drafted = "DR";
	public static final String DOCSTATUS_Completed = "CO";
	public static final String DOCSTATUS_InProgress = "IP";
	public static final String DOCSTATUS_Voided = "VO";

	@Override
	public String modelChange(PO po, int type) throws Exception {
		log.info(po.get_TableName() + " Type: " + type);

		return null;
	} // modelChange

	/**
	 * Validate Document. Called as first step of DocAction.prepareIt when you
	 * called addDocValidate for the table. Note that totals, etc. may not be
	 * correct.
	 *
	 * @param po     persistent object
	 * @param timing see TIMING_ constants
	 * @return error message or null
	 */
	@Override
	public String docValidate(PO po, int timing) {
		log.info(po.get_TableName() + " Timing: " + timing);
		if ((timing == TIMING_BEFORE_COMPLETE || timing == TIMING_BEFORE_PREPARE)
				&& po.get_Table_ID() == I_C_Order.Table_ID) {
			MOrder ord = (MOrder) po;
			if (ord.getC_DocTypeTarget().getDocBaseType().equals("SOO")
					|| ord.getC_DocTypeTarget().getDocBaseType().equals("POO")) {
				MOrderLine[] lines = ord.getLines();
				for (MOrderLine line : lines) {
					if (line.getM_Product_ID() > 0) {
						if (line.getQtyEntered() == null || Integer.valueOf(line.getQtyEntered().intValue()) <= 0) {
							throw new AdempiereException("Se debe ingresar cantidad mayor a 0 para el producto: "
									+ line.getM_Product().getName() + " en la linea: " + line.getLine());
						}

						if (line.getPriceEntered() == null || Integer.valueOf(line.getPriceEntered().intValue()) == 0) {
							throw new AdempiereException("Se debe ingresar precio distinto de 0 para el producto: "
									+ line.getM_Product().getName() + " en la linea: " + line.getLine());
						}
					} else if (line.getC_Charge_ID() > 0) {
						if (line.getQtyEntered() == null || Integer.valueOf(line.getQtyEntered().intValue()) == 0) {
							throw new AdempiereException(
									"Se debe ingresar cantidad para el cargo en la linea: " + line.getLine());
						}
						if (line.getPriceEntered() == null || Integer.valueOf(line.getPriceEntered().intValue()) == 0) {
							throw new AdempiereException(
									"Se debe ingresar un valor para el cargo en la linea: " + line.getLine());
						}
					} else {
						throw new AdempiereException(
								"Se debe ingresar un producto o un cargo para la linea: " + line.getLine());
					}
				}
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
		StringBuffer sb = new StringBuffer("QSS_Validator");
		return sb.toString();
	} // toString
}