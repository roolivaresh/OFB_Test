package org.ofb.model;

import java.math.BigDecimal;

import org.adempiere.core.domains.models.I_C_Order;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.csa.model.ModCSAComSC;

public class ModOFBModelOrdenesDeCompraParte1 implements ModelValidator {

	public ModOFBModelOrdenesDeCompraParte1() {
		super();
		// TODO Auto-generated constructor stub
	}

	/** Logger */
	private static CLogger log = CLogger.getCLogger(ModCSAComSC.class);
	/** Client */
	private int m_AD_Client_ID = -1;

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
		engine.addModelChange(I_C_Order.Table_Name, this);

	}

	/**
	 * Model Change of a monitored Table.
	 *
	 */
	@Override
	public String modelChange(PO po, int type) throws Exception {
		
		log.info(po.get_TableName() + " Type: " + type);

		if (
			(type == TYPE_BEFORE_CHANGE)
			&&
			po.get_Table_ID() == I_C_Order.Table_ID
		){
			
			MOrder order = (MOrder) po;
			
			String namePriorityRule = order.getPriorityRule();
			
			namePriorityRule = changeNamePriority(namePriorityRule);
			
			BigDecimal numberGrandTotal = order.getGrandTotal();
			
			order.setDescription(namePriorityRule.concat(" - ").concat(numberGrandTotal.toString()));
		}

		return null;
	}
	
private String changeNamePriority(String namePriorityRule) {
	
	switch(namePriorityRule)
	{
		case "3":
			namePriorityRule = "Alta";
			break;
		case "7":
			namePriorityRule = "Baja";
			break;
		case "5":
			namePriorityRule = "Media";
			break;
		case "9":
			namePriorityRule = "Menor";
			break;
		case "1":
			namePriorityRule = "Urgente";
			break;

	default:
		
		throw new AdempiereException("No se ha considerado un nombre de la lista en el metodo 'changeNamePriority' de la clase 'ModOFBModelOrdenesDeCompraParte1'.");
	}
	
	return namePriorityRule;
}

	@Override
	public String docValidate(PO po, int timing) {

		return null;
	}

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
