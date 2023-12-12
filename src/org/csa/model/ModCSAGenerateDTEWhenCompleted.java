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
import java.math.RoundingMode;
import java.sql.SQLException;

import org.adempiere.core.domains.models.I_C_Invoice;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MCash;
import org.compiere.model.MCashLine;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_BPBenefit;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.csa.process.ProcessCSAGenerateDTE;

import cl.pronova.sii.siidte.tecnoback.TecnobackDTESender;

/**
 * Generar DTE al completar documento por cobrar Complete fields from a given
 * project
 * 
 * @author jleyton
 */
public class ModCSAGenerateDTEWhenCompleted implements ModelValidator {
	/**
	 * Constructor. The class is instantiated when logging in and client is
	 * selected/known
	 */
	public ModCSAGenerateDTEWhenCompleted() {
		super();
	} // MyValidator

	/** Logger */
	private static CLogger log = CLogger.getCLogger(ModCSAGenerateDTEWhenCompleted.class);
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

		// Documents to be monitored
		engine.addDocValidate(I_C_Invoice.Table_Name, this);

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
		if (timing == TIMING_AFTER_COMPLETE && po.get_Table_ID() == I_C_Invoice.Table_ID) {
			MInvoice inv = (MInvoice) po;
			// si es documento por pagar no se debe ejecutar
			if(!inv.isSOTrx())
				return null;
			// integracion con tecnoback
			TecnobackDTESender tb = new TecnobackDTESender(inv);

			if (tb.sendDte()) {
				log.config("DTE Generado: " + tb.getResultMsg());
				return null;
			}
			log.warning("No se ha generado documento: " + tb.getResultMsg());
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