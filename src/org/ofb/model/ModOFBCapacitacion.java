package org.ofb.model;

import org.adempiere.core.domains.models.I_M_Requisition;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
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
		String doc="";
		if ((type == TYPE_AFTER_CHANGE)	&& po.get_Table_ID() == I_M_Requisition.Table_ID) {
			MRequisition requisition = (MRequisition) po;
		
			if(requisition.getC_Project_ID()==100) { // el proyecto debe ser standard
				//"Ordenes de Compra" GUARDAR DATO PARA LA PESTAÑA "Orden de Compra" (Cabecera)
				// Crea una orden de compra 
				
				MOrder order = new MOrder(po.getCtx(),0,po.get_TrxName());
			
				// datos requeridos (obligatorios)
				// Socio de negocio
				order.setC_BPartner_ID(1000000);
				order.setC_BPartner_Location_ID(1000000);
				// Orden de compra : lista de precios 
				order.setM_PriceList_ID(102);
				// Orden de compra : agente compañia
				order.setSalesRep_ID(101);
				
				// Datos llenados pasados al nuevo o a la nueva tabla
				order.setDateOrdered(requisition.getDateRequired());
				order.setDatePromised(requisition.getDateRequired());
				
				// Datos requeridos (Dependientes porque son datos de otra parte)
				order.setDescription("Se genero desde la solicitud n:" + requisition.getDocumentNo()+
						" - Descripción de Requisición: "+requisition.getDescription());
				order.setC_DocTypeTarget_ID(126);
				order.setC_DocType_ID(126);
				order.setIsSOTrx(false);
				//order.set_CustomColumn("M_Requisition_ID", requisition.get_ID());
				
				// Guardar
				order.save();

				
				//Linea orden compra 				
				MRequisitionLine[] rLines = requisition.getLines();
				for (MRequisitionLine reql : rLines) {
					if (reql.getM_Product_ID() > 0) {
						
						MOrderLine ord1=new MOrderLine(po.getCtx(),0,po.get_TrxName());
						ord1.setC_Order_ID(order.get_ID());
						ord1.setM_Product_ID(reql.getM_Product_ID());
						ord1.setQtyEntered(reql.getQty());
						ord1.setPriceEntered(reql.getPriceActual());
						ord1.setLineNetAmt(reql.getLineNetAmt());
						ord1.setC_BPartner_ID(reql.get_ValueAsInt("C_BPartner_ID"));
						int BPLocationID = DB.getSQLValue(null, "SELECT COALESCE(MAX(C_BPartner_Location_ID),0) FROM C_BPartner_Location WHERE ISACTIVE = 'Y' AND C_BPartner_ID =" +order.get_ValueAsInt("C_BPartner_ID"));
						ord1.setC_BPartner_Location_ID(BPLocationID);
						int UOMID = DB.getSQLValue(null,"SELECT C_UOM_ID FROM M_PRODUCT WHERE M_PRODUCT_ID = "+reql.getM_Product_ID());
						ord1.setC_UOM_ID(UOMID);
						// guarda la linea de la orden de la BD
						ord1.save();			
					}
				}
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