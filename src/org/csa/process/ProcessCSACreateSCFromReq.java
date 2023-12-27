/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.csa.process;

import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
//import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;


/**
 *
 *
 * @author Isaías Díaz Rojas
 * @version $Id: ProcessInventory.java $
 */
public class ProcessCSACreateSCFromReq extends SvrProcess {
	
	// private String p_DocStatus = null;
	private int p_Requisition_ID = 0;

	/**
	 * Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare() {
		/*
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();

			if (name.equals("Action"))
				p_Action = para[i].getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		*/
		p_Requisition_ID = getRecord_ID();
	} // prepare

	/**
	 * Perrform process.
	 *
	 * @return Message (clear text)
	 * @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception {
		String msg = "";
		
		if (p_Requisition_ID > 0) {

			MRequisition req = new MRequisition(getCtx(),p_Requisition_ID,get_TrxName());
			
			//if(req.getC_Project_ID()==100) { // PESTAÑA REQUISISION > Proyacto Standart_Standart_-1_-1 
				
				// "Órdenes de Compra" GUARDAR DATO PARA LA PESTAÑA "Orden de Compra" (Cabecera)
				// CREA UNA ORDEN DE COMPRA 
				
				MOrder ord = new MOrder(getCtx(),0,get_TrxName()); 
				
				// Datos Requeridos (Obligatorios)
				ord.setC_BPartner_ID(1000002);// socio de isaias
				ord.setC_BPartner_Location_ID(1000002);// socio de isaias - localización valparaíso
				ord.setM_PriceList_ID(101);
				ord.setSalesRep_ID(101);
				
				// Datos ya llenados pasados al nuevo o a la nueva tabla
				ord.setDateOrdered(req.getDateRequired());
				ord.setDatePromised(req.getDateRequired());
				
				// Datos Requeridos (Dependientes porque son datos de otra parte)
				ord.setDescription("Se generó desde la solicitud n: "+req.getDocumentNo());
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
						
						MOrderLine ordLine = new MOrderLine(getCtx(),0,get_TrxName());
						ordLine.setC_Order_ID(ord.get_ID());
						ordLine.setM_Product_ID(reqL.getM_Product_ID());
						ordLine.setQtyEntered(reqL.getQty());
						ordLine.setPriceEntered(reqL.getPriceActual());
						ordLine.setLineNetAmt(reqL.getLineNetAmt());
						ordLine.setC_BPartner_ID(
								//reqL.get_ValueAsInt("C_BPartner_ID")
								ord.getC_BPartner_ID()
								);
						int location_ID = ord.getC_BPartner_Location_ID();
						/*
						int BPLocationID = DB.getSQLValue(null,
								"SELECT COALESCE(MAX(C_BPartner_Location_ID),0) " +
								"FROM C_BPartner_Location " +
								"WHERE ISACTIVE = 'Y' AND C_BPartner_ID = " +
								//req.get_ValueAsInt("C_BPartner_ID")
								ord.getC_BPartner_ID()
						);
						*/
						ordLine.setC_BPartner_Location_ID(location_ID);
						int UOMID = DB.getSQLValue(null,
								"SELECT C_UOM_ID "+
								"FROM M_PRODUCT "+
							    "WHERE M_PRODUCT_ID = "+reqL.getM_Product_ID()
							    );
						ordLine.setC_UOM_ID(UOMID);
						
						// Guarda la línea de la orden en la Base de datos
						ordLine.save();
					}
				}
			//}

		}
		
		return msg;
	} // doIt
}
