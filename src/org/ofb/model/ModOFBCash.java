package org.ofb.model;

import org.adempiere.core.domains.models.I_C_Cash;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MCash;
import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.csa.model.ModCSAComSC;

public class ModOFBCash implements ModelValidator {

	public ModOFBCash() {
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
//		Tables to be monitored
		engine.addModelChange(I_C_Cash.Table_Name, this);

	}

	/**
	 * Model Change of a monitored Table.
	 *
	 */
	@Override
	public String modelChange(PO po, int type) throws Exception {
		log.info(po.get_TableName() + " Type: " + type);
		String sql;
		if ((type == TYPE_BEFORE_NEW)	&& po.get_Table_ID() == I_C_Cash.Table_ID) {
			MCash cash = (MCash) po;
			//******************** SI YA HAY UN DIARIO EN BORRADOR ***********************************
			// se obtiene ultimo diario de caja sin completar
			sql = "select coalesce(max(c_cash_id),0) from c_cash where c_cashbook_id=" + cash.getC_CashBook_ID()
					+ " and docstatus not in ('CO','CL','VO') and c_cash_id<>"+cash.get_ID();
			log.config("-->>Draw Cash SQL:" + sql);
			int drawCash_id = DB.getSQLValue(po.get_TrxName(), sql);
			// si existe enviar error de que ya hay un diario creado para ese libro
			if (drawCash_id > 0)
				throw new AdempiereException("Ya existe un diario de caja "
						+ " sin completar asociado al libro " + cash.getC_CashBook().getName() + ".");
			//********************* SI LA FECHA ES MENOR QUE UN DIARIO COMPLETO **********************
			sql = "select coalesce(max(c_cash_id),0) from c_cash where c_cashbook_id=" + cash.getC_CashBook_ID()
			+ " and StatementDate>'" + cash.getStatementDate().toString() + "' and docstatus in ('CO')"
					+ "and c_cash_id<>"+cash.get_ID();
			int lastCash_id = DB.getSQLValue(po.get_TrxName(), sql);
			if(lastCash_id>0) {
				MCash lastCash = new MCash(po.getCtx(), lastCash_id, po.get_TrxName());
				throw new AdempiereException("No se puede crear diario con fecha anterior a otro creado para el mismo libro"
						+ " (Conflicto con Diario N°:"+lastCash.getDocumentNo()+").");
			}

		}
		// **************** ASIGNACION DE MONTO INCIAL **********************
		if (type == TYPE_AFTER_NEW && po.get_Table_ID() == I_C_Cash.Table_ID) {
			MCash cash = (MCash) po;
			// se busca el ultimo diario de caja completado para asignar el monto incial del
			// nuevo diario
			sql = "select coalesce(max(c_cash_id),0) from c_cash where c_cashbook_id=" + cash.getC_CashBook_ID()
					+ " and StatementDate<='" + cash.getStatementDate().toString() + "' and docstatus in ('CO')";
			log.config("-->>Last Complete Cash SQL:" + sql);
			int lastCash_id = DB.getSQLValue(po.get_TrxName(), sql);
			if (lastCash_id > 0) {
				MCash lastCash = new MCash(po.getCtx(), lastCash_id, po.get_TrxName());
				cash.setBeginningBalance(lastCash.getEndingBalance());
			} else
				// sino lo encuentra le asigna saldo inicial 0
				cash.setBeginningBalance(Env.ZERO);

			cash.saveEx();
		}

		return null;
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
