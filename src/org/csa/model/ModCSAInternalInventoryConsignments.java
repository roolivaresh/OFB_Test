package org.csa.model;

import org.adempiere.core.domains.models.I_M_Inventory;
import org.adempiere.core.domains.models.I_M_Movement;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class ModCSAInternalInventoryConsignments implements ModelValidator {

	public ModCSAInternalInventoryConsignments() {
		super();
	}

	/** Logger */
	private static CLogger log = CLogger.getCLogger(ModCSAInternalInventoryConsignments.class);
	/** Client */
	private int m_AD_Client_ID = -1;

	/**
	 * Initialize Validation
	 *
	 * @param engine validation engine
	 * @param client client
	 * @author Rodrigo Olivares Hurtado
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
		// engine.addModelChange(MMovement.Table_Name, this);
		// engine.addModelChange(MInventory.Table_Name, this);
		engine.addDocValidate(I_M_Inventory.Table_Name, this);

	}

	@Override
	public String modelChange(PO po, int type) throws Exception {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public String docValidate(PO po, int timing) {
		log.info(po.get_TableName() + " Timing: " + timing);
		if (timing == TIMING_AFTER_COMPLETE && po.get_Table_ID() == I_M_Inventory.Table_ID) {
			MInventory inv = (MInventory) po;
			// Se realizara la validacion para consumo interno
			if (inv.getM_Warehouse_ID()==1000023
					&& (inv.getC_DocType_ID() == 1000065 || inv.getC_DocType_ID() == 1000068
					|| inv.getC_DocType_ID() == 1000069 || inv.getC_DocType_ID() == 1000071
					|| inv.getC_DocType_ID() == 1000072 || inv.getC_DocType_ID() == 1000073
					|| inv.getC_DocType_ID() == 1000083)) {

				MInventoryLine[] lines = inv.getLines(true);
				MRequisition req=null;
				String sql;
				int c_bpartner_id;
				boolean headerCreate = true;
				for (MInventoryLine line : lines) {
					if (line.getM_Product_ID() > 0) {
						sql="select coalesce(max(c_bpartner_id),0) from c_bpartner where value||'-'||ValueValidator='"+line.getM_Locator().getValue()+"';";
						c_bpartner_id = DB.getSQLValue(line.get_TrxName(), sql);
						if(c_bpartner_id==0)
							throw new AdempiereException("No existe en el sistema proveedor con rut "+line.getM_Locator().getValue()+".");

						if(headerCreate) {
							//se crea cabecera de solicitud de compra solo 1 vez
							req = new MRequisition(po.getCtx(),0,po.get_TrxName());
							req.setC_DocType_ID(1000057);
							req.setPriorityRule("3");
							req.setM_Warehouse_ID(1000023);
							req.setM_PriceList_ID(1000000);
							req.setDescription("**Generado automaticamente desde el consumo N°"+inv.getDocumentNo()+".");
							req.setDateDoc(inv.getMovementDate());
							req.set_CustomColumn("SalesRep_ID",inv.getCreatedBy());
							req.set_CustomColumn("his_admission_ID",inv.get_Value("his_admission_ID"));
							req.saveEx();
							headerCreate=false;
						}
						MRequisitionLine reql = new MRequisitionLine(req);
						reql.setC_BPartner_ID(c_bpartner_id);
						
						reql.setM_Product_ID(line.getM_Product_ID());
						reql.setQty(line.getQtyInternalUse());
						reql.setDescription(line.getDescription());
						reql.setC_Tax_ID(1000000);
						reql.saveEx();
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
