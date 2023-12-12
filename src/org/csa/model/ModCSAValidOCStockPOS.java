package org.csa.model;

import java.math.BigDecimal;

import org.adempiere.core.domains.models.I_C_Order;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class ModCSAValidOCStockPOS implements ModelValidator {

	public ModCSAValidOCStockPOS() {
		super();
	}

	/** Logger */
	private static CLogger log = CLogger.getCLogger(ModCSAValidOCStockPOS.class);
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
		engine.addDocValidate(I_C_Order.Table_Name, this);

	}

	@Override
	public String modelChange(PO po, int type) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String docValidate(PO po, int timing) {
		log.info(po.get_TableName() + " Timing: " + timing);
		if (timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID() == I_C_Order.Table_ID) {
			MOrder order = (MOrder) po;
			if(order.getC_DocTypeTarget().getDocSubTypeSO()==null)
				return null;
			if (order.getC_DocTypeTarget().getDocSubTypeSO().equals("WR") && order.getC_DocTypeTarget_ID()!=1000079) {
				for (MOrderLine line : order.getLines()) {
					BigDecimal onHand = DB.getSQLValueBD(po.get_TrxName(), "select coalesce(SUM(s.qtyonhand),0) from M_Storage s "
							+ "Inner join M_Locator l on (s.M_Locator_ID=l.M_Locator_ID) where l.M_Warehouse_ID=? and s.M_Product_ID=? and l.isactive='Y' ",
							order.getM_Warehouse_ID(), line.getM_Product_ID());
					if(line.getQtyEntered().compareTo(onHand)>0) {
						throw new AdempiereException("Producto "+line.getM_Product().getName()+" no tiene suficiente stock"
								+ "(Solicitado:"+line.getQtyEntered()+". En stock:"+onHand+").");
					}

				}
			}

		}

		return null;
	}

	@Override
	public int getAD_Client_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		// TODO Auto-generated method stub
		return null;
	}

}
