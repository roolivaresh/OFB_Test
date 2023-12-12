package org.csa.model;

import org.adempiere.core.domains.models.I_M_Inventory;
import org.adempiere.core.domains.models.I_M_Movement;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class ModCSAInternalInventoryValidStock implements ModelValidator {

	public ModCSAInternalInventoryValidStock() {
		super();
	}

	/** Logger */
	private static CLogger log = CLogger.getCLogger(ModCSAInternalInventoryValidStock.class);
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
		engine.addDocValidate(I_M_Movement.Table_Name, this);

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
		if (timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID() == I_M_Movement.Table_ID) {
			MMovement movement = (MMovement) po;
			// Se realizara la validacion para movimiento de inventario
			if (movement.getC_DocType_ID() == 1000022) {
				MMovementLine[] line = movement.getLines(true);

				for (MMovementLine lines : line) {
					if (lines.getM_Product_ID() > 0) {
						// Se rescata el el stock disponible en relacion al producto, ubicacion, y
						// conjunto de atributos
						String sql ="select coalesce(sum(qtyonhand),0) from m_storage " + "where m_product_id= "
										+ lines.getM_Product_ID() + " and m_locator_id= " + lines.getM_Locator_ID();
						// jleyton valida si la linea tiene atributo, si no tiene obtiene el stock total
						if (lines.getM_AttributeSetInstance_ID() > 0)
							sql=sql.concat(" and M_AttributeSetInstance_ID = " + lines.getM_AttributeSetInstance_ID());
						int stock = DB.getSQLValue(po.get_TrxName(),sql);
						// Si el stock es insuficiente se debe mandar un mensaje notificando y no se
						// debe completar el documento
						if (stock < lines.getMovementQty().intValue()) {
							throw new AdempiereException("No hay Stock suficiente en la linea " + lines.getLine()
							+ " del producto: " + lines.getM_Product().getName() + " en la ubicacion: "
							+ lines.getM_Locator().getValue() + " " + "(Stock: " + stock + ", Solicitado: "
							+ lines.getMovementQty().intValue() + ").");
						}
					}
				}

			}

		}

		if (timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID() == I_M_Inventory.Table_ID) {
			MInventory inventory = (MInventory) po;
			// Se realizara la validacion para consumo interno
			if (inventory.getC_DocType_ID() == 1000065) {
				MInventoryLine[] line = inventory.getLines(true);

				for (MInventoryLine lines : line) {
					if (lines.getM_Product_ID() > 0) {
						// Se rescata el el stock disponible en relacion al producto, ubicacion, y
						// conjunto de atributos
						String sql = "select coalesce(sum(qtyonhand),0) from m_storage " + "where m_product_id= "
								+ lines.getM_Product_ID() + " and m_locator_id= " + lines.getM_Locator_ID();
						// jleyton valida si la linea tiene atributo, si no tiene obtiene el stock total
						if (lines.getM_AttributeSetInstance_ID() > 0)
							sql=sql.concat(" and M_AttributeSetInstance_ID = " + lines.getM_AttributeSetInstance_ID());
						int stock = DB.getSQLValue(po.get_TrxName(), sql);
						// Si el stock es insuficiente se debe mandar un mensaje notificando y no se
						// debe completar el documento
						if (stock < lines.getQtyInternalUse().intValue()) {
							throw new AdempiereException("No hay Stock suficiente en la linea " + lines.getLine()
									+ " del producto: " + lines.getM_Product().getName() + " en la ubicacion: "
									+ lines.getM_Locator().getValue() + " " + "(Stock: " + stock + ", Solicitado: "
									+ lines.getQtyInternalUse().intValue() + ").");
						}
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
