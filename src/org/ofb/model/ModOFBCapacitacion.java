package org.ofb.model;

import org.adempiere.core.domains.models.I_M_Requisition;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.csa.model.ModCSAComSC;

public class ModOFBCapacitacion implements ModelValidator {

	public ModOFBCapacitacion() {
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
		engine.addModelChange(I_M_Requisition.Table_Name, this);

	}

	/**
	 * Model Change of a monitored Table.
	 *
	 */
	@Override
	public String modelChange(PO po, int type) throws Exception {
		log.info(po.get_TableName() + " Type: " + type);
		String doc = "";
		if ((type == TYPE_AFTER_CHANGE ) && po.get_Table_ID() == I_M_Requisition.Table_ID) {
			MRequisition req = (MRequisition)po;
			
			
				
			
			
			
			if(req.getC_Project_ID() == 100) {
				
				int orderID = DB.getSQLValue(po.get_TrxName(), "select coalesce(max(c_order_id),0) from c_order  where m_requisition_id ="+req.get_ID());
				
				MOrder ord = new MOrder(po.getCtx(),orderID,po.get_TrxName());
				
				ord.setC_BPartner_ID(1000000);
				ord.setC_BPartner_Location_ID(1000000);
				ord.setM_PriceList_ID(101);
				ord.setSalesRep_ID(101);
				ord.setDateOrdered(req.getDateRequired());
				ord.setDatePromised(req.getDateRequired());
				ord.setDescription("Se genero desde la solicitud n:"+req.getDocumentNo());
				ord.setC_DocTypeTarget_ID(126);
				ord.setC_DocType_ID(126);
				ord.setIsSOTrx(false);
				ord.set_CustomColumn("M_Requisition_ID", req.get_ID());
				ord.save();
				
				//req.setHelp("id:"+ord.get_ID());
				//req.save();
				
				MRequisitionLine[] rLines = req.getLines();
				for (MRequisitionLine reql : rLines) {
					if(reql.getM_Product_ID() > 0) {
						
						int orderLineID = DB.getSQLValue(po.get_TrxName(), "select coalesce(max(c_orderline_id),0) from c_orderline  where c_order_id ="+ord.get_ID()+" and m_product_id = "+reql.getM_Product_ID());
						
						MOrderLine ordl=new  MOrderLine(po.getCtx(),orderLineID,po.get_TrxName());
						ordl.setC_Order_ID(ord.get_ID());
						ordl.setM_Product_ID(reql.getM_Product_ID());
						ordl.setQtyEntered(reql.getQty());
						ordl.setPriceEntered(reql.getPriceActual());
						ordl.setLineNetAmt(reql.getLineNetAmt());
						ordl.setC_BPartner_ID(ord.getC_BPartner_ID());
						int BPLocationID = DB.getSQLValue(null, "SELECT COALESCE(MAX(C_BPartner_Location_ID),0) FROM C_BPartner_Location WHERE ISACTIVE = 'Y' AND C_BPartner_ID =" +ord.getC_BPartner_ID());
						ordl.setC_BPartner_Location_ID(BPLocationID);
						int UOMID = DB.getSQLValue(null, "SELECT C_UOM_ID FROM M_PRODUCT WHERE M_PRODUCT_ID = "+reql.getM_Product_ID());
						ordl.setC_UOM_ID(UOMID);
						ordl.save();
					}
				}
			}else {
				throw new AdempiereException ("El proyecto debe ser standard.");
			}
			
			
			
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
