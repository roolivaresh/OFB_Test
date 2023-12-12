package org.csa.model;

import java.math.BigDecimal;

import org.adempiere.core.domains.models.I_M_Requisition;
import org.compiere.model.MClient;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class ModCSAValidReqQtyInTransit implements ModelValidator {

	public ModCSAValidReqQtyInTransit() {
		super();
	}

	/** Logger */
	private static CLogger log = CLogger.getCLogger(ModCSAComSC.class);
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
		engine.addDocValidate(I_M_Requisition.Table_Name, this);

	}

	@Override
	public String modelChange(PO po, int type) throws Exception {

		return null;
	}

	@Override
	public String docValidate(PO po, int timing) {
		log.info(po.get_TableName() + " Timing: "+timing);
		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()==I_M_Requisition.Table_ID) {
			MRequisition req = (MRequisition)po;
			//se valida para que se ejecute solo para tipo existencia
			if(req.getC_DocType_ID()!=1000058)
				return null;
			MRequisitionLine rLines[] = req.getLines();
			int bp_id;
			String sql;
			BigDecimal qtyInTransit = new BigDecimal(0);
			for(MRequisitionLine rLine:rLines) {
				//se obtiene el proveedor de las lineas sino el de la cabecera
				bp_id = rLine.getC_BPartner_ID();
				if(bp_id==0)
					bp_id =(int)req.get_Value("C_BPartner_ID");
				sql="select coalesce(sum(col.qtyreserved),0) from c_order co join c_orderline col on "
						+ "co.c_order_id=col.c_order_id where col.qtyreserved>0 and co.issotrx='N' "
						+ "and co.docstatus='CO' and co.c_bpartner_id="+bp_id+" and "
						+ "col.m_product_id="+rLine.getM_Product_ID();
				log.config("SQL Producto en Transito--> "+sql);
				qtyInTransit = DB.getSQLValueBD(po.get_TrxName(), sql);
				if(qtyInTransit.compareTo(Env.ZERO)>0) {
					return ("El producto "+rLine.getM_Product().getName()+" de la linea "
							+ rLine.getLine()+" tiene cantidades en tránsito("+qtyInTransit+").");
				}


			}

		}
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
