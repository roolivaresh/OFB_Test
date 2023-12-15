package org.ofb.model;

import org.adempiere.core.domains.models.I_M_Requisition;
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

public class ModOFBModelOrdenDeCompra implements ModelValidator {

	public ModOFBModelOrdenDeCompra() {
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

		//********************  ***********************************
		if (
			(type == TYPE_AFTER_CHANGE) // Si se realiza el cambio despues del registro
			&&
			po.get_Table_ID() == I_M_Requisition.Table_ID // Si el ID de la tabla es el ID del I_M_Requisition
		){
			
			// Conectamos atrabes de Java a las columnas de la tabla
			MRequisition req = (MRequisition) po;
			
			if(req.getC_Project_ID()==100) { // PESTAÑA REQUISISION > Proyacto Standart_Standart_-1_-1 
				
				// "Órdenes de Compra" GUARDAR DATO PARA LA PESTAÑA "Orden de Compra" (Cabecera)
				// CREA UNA ORDEN DE COMPRA 
				
				MOrder ord = new MOrder(po.getCtx(),0,po.get_TrxName()); 
				
				// Datos Requeridos (Obligatorios)
				ord.setC_BPartner_ID(1000002);// socio de isaias
				ord.setC_BPartner_Location_ID(1000002);// socio de isaias - localización valparaíso
				ord.setM_PriceList_ID(101);
				ord.setSalesRep_ID(101);
				
				// Datos ya llenados pasados al nuevo o a la nueva tabla
				ord.setDateOrdered(req.getDateRequired());
				ord.setDatePromised(req.getDateRequired());
				
				// Datos Requeridos (Dependientes porque son datos de otra parte)
				ord.setDescription("Se generó desde la solicitud n:"+req.getDocumentNo());
				ord.setC_DocTypeTarget_ID(126);
				ord.setC_DocType_ID(126);
				ord.setIsSOTrx(false);
				
				// Guardar
				ord.save();
				
				/*
				req.setHelp("id: "+ord.get_ID());
				req.save();
				*/
				
				// "Órdenes de Compra" GUARDAR DATO PARA LA PESTAÑA "Línea Orden Compra"
				// RECORRE POR CADA LÍNEA Y SI LA ENCUENTRA LA MUESTRA
				
				MRequisitionLine[] reqLines = req.getLines();
				
				for(MRequisitionLine reqL : reqLines) {
					
					if(reqL.getM_Product_ID() > 0) {
						
						MOrderLine ordLine = new MOrderLine(po.getCtx(),0,po.get_TrxName());
						ordLine.setC_Order_ID(ord.get_ID());
						ordLine.setM_Product_ID(reqL.getM_Product_ID());
						ordLine.setQtyEntered(reqL.getQty());
						ordLine.setPriceEntered(reqL.getPriceActual());
						ordLine.setLineNetAmt(reqL.getLineNetAmt());
						ordLine.setC_BPartner_ID(
								//reqL.get_ValueAsInt("C_BPartner_ID")
								ord.getC_BPartner_ID()
								);
						int BPLocationID = DB.getSQLValue(null,
								"SELECT COALESCE(MAX(C_BPartner_Location_ID),0)" +
								"FROM C_BPartner_Location" +
								"WHERE ISACTIVE = 'Y' AND C_BPartner_ID =" +
								//req.get_ValueAsInt("C_BPartner_ID")
								ord.getC_BPartner_ID()
						);
						ordLine.setC_BPartner_Location_ID(BPLocationID);
						int UOMID = DB.getSQLValue(null,
								"SELECT C_UOM_ID"+
								"FROM M_PRODUCT"+
							    "WHERE M_PRODUCT_ID = "+reqL.getM_Product_ID()
							    );
						ordLine.setC_UOM_ID(UOMID);
						
						// Guarda la linea de la orden en la Base de datos
						ordLine.save();
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
