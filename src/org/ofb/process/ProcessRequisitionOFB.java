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
package org.ofb.process;

import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MChat;
import org.compiere.model.MChatEntry;
import org.compiere.model.MRequisition;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.ofb.model.OFBForward;

/**
 *
 *
 * @author mfrojas
 * @version $Id: ProcessInventory.java $
 */
public class ProcessRequisitionOFB extends SvrProcess {
	// private String p_DocStatus = null;
	private int p_Requisition_ID = 0;
	private String p_Action = "PR";
	// mfrojas Se agrega nuevo parametro para recibir mensaje al momento de anular.
	private String p_Message = "";

	/**
	 * Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();

			if (name.equals("Action"))
				p_Action = element.getParameterAsString();
			else if (name.equals("Message"))
				p_Message = element.getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
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
		if (p_Requisition_ID > 0) {
			MRequisition req = new MRequisition(getCtx(), p_Requisition_ID, get_TrxName());

			// seteo de nuevo estado al procesar
			String newStatus = "DR";
			String newAction = "DR";
			// String modality = req.get_Value("Modality").toString();

			// mfrojas
			String actualStatus = req.getDocStatus();
			int workflowID = DB.getSQLValue(get_TrxName(),
					"SELECT coalesce(max(ad_workflow_id),0) from ad_workflow "
							+ " WHERE c_doctype_id = ? and ad_client_id = " + Env.getAD_Client_ID(getCtx()),
					req.getC_DocType_ID());

			log.config("paction " + p_Action);
			log.config("workflowID " + workflowID);
			log.config("estado actual " + actualStatus);

			if (workflowID > 0) {

				newStatus = p_Action;
				// Validador de permisos de rol
				int cant = DB.getSQLValue(get_TrxName(),
						"SELECT COUNT(1) FROM AD_Document_Action_Access daa"
								+ " INNER JOIN AD_Ref_List rl ON (daa.AD_Ref_List_ID = rl.AD_Ref_List_ID) "
								+ " WHERE value = '" + newStatus + "' AND AD_Role_ID = " + Env.getAD_Role_ID(getCtx())
								+ " AND C_DocType_ID = " + req.getC_DocType_ID());

				if (cant > 0) {
					if (newStatus.compareTo("CO") != 0) {
						if (newStatus.compareTo("VO") == 0) {
							req.voidIt();
							req.setGrandTotal(Env.ZERO);
							req.setDocStatus("VO");
							req.setProcessed(true);
							req.saveEx();
						//Rodrigo Olivares 2023-08-28 Se agrega validacion para que no pida archivo adjunto en TSM
						} else if (newStatus.compareTo("EA") == 0 && req.getAttachment() == null && !OFBForward.IgnorateAttachmentTSM())
							throw new AdempiereException("Para pasar al siguiente estado necesita adjuntar un documento");
						//Rodrigo Olivares Hurtado 2023-07-11
						//Se agrega validacion para el documento Solicitud de Compra Existencias y nodo 20 revision solicitud de compra
						else if(newStatus.compareTo("RS") == 0 && req.getC_DocType_ID()==1000058){
							int CountNoProduct = DB.getSQLValue(get_TrxName(), "select coalesce(count(M_RequisitionLine_ID),0) from M_RequisitionLine "
									+ "where M_Product_ID is null and M_Requisition_ID= "+req.get_ID());
							//si hay una linea sin producto se debe notificar mediante mensaje pop-up
							if(CountNoProduct > 0) {
								//Rodrigo Olivares Hurtado 2023-08-16 actualización del mensaje
								throw new AdempiereException("Hay "+ CountNoProduct +" linea sin producto. Pasar a nodo 15 para la creacion del producto.");
							}else {//2023-07-12 se guarda el estado correspondiente
								req.setDocStatus(newStatus);
								req.saveEx();
							}

						}else {
							req.setDocStatus(newStatus);
							req.saveEx();
						}

					} else if (newStatus.compareTo("CO") == 0) {
						//se agrega funcion para setear el AD_User_ID cuando est� vacio
						//
						if(OFBForward.CompleteRequisitionTSM()) {
							if(req.getTotalLines() == Env.ZERO) {
								throw new AdempiereException("El total de lineas debe ser mayor a 0");
							}
						}
						ad_userValidate(req, 1000024);
						req.setDocStatus("IP");
						req.processIt("CO");
						req.saveEx();

					}
					// jleyton funcion para crear chat al pasar a algunos estados. CSA
					chatChangeStatus(req, workflowID, actualStatus, newStatus);

				} else
					throw new AdempiereException("Error: Permisos de rol insuficientes");

			} else
				throw new AdempiereException("No existe flujo asociado al estado actual del documento");
		}
		return "Procesado";
	} // doIt

	/**
	 * Se genera chat para colocar el motivo de un cambio de estado a otro
	 *
	 * @param req
	 * @param workflowID
	 * @param actualStatus
	 * @param newStatus
	 */
	private void chatChangeStatus(MRequisition req, int workflowID, String actualStatus, String newStatus) {
		// se obtienen las prioridades de cada nodo para saber si generar chat o no
		int node = DB.getSQLValue(get_TrxName(), "SELECT max(priority) from AD_WF_Node WHERE " + "DocAction like '"
				+ actualStatus + "' AND AD_Workflow_ID=" + workflowID);
		int nodeAction = DB.getSQLValue(get_TrxName(), "SELECT max(priority) from AD_WF_Node WHERE "
				+ "DocAction like '" + newStatus + "' AND AD_Workflow_ID=" + workflowID);
		if (node > nodeAction) {
			// se valida si hay mensaje al cambiar estados para que se guarde en el chat
			if (p_Message != null) {
				int idChat = DB.getSQLValue(get_TrxName(), "select coalesce(max(ch.CM_Chat_ID),0) from cm_chat ch "
						+ "where ch.ad_table_id=702 and ch.record_id=" + req.get_ID());
				MChat chat = new MChat(getCtx(), idChat, get_TrxName());
				// si no existe chat en la solicitud se setean los valores de la cabecera del
				// nuevo chat
				if (idChat == 0) {
					chat.setDescription(req.get_TableName() + ": " + req.getDocumentNo());
					chat.setAD_Org_ID(req.getAD_Org_ID());
					chat.setConfidentialType("A");
					chat.setAD_Table_ID(req.get_Table_ID());
					chat.setModerationType("N");
					chat.setRecord_ID(req.get_ID());
				}
				chat.save();
				// se genera la linea de la entrada de chat
				MChatEntry chatEntry = new MChatEntry(getCtx(), 0, get_TrxName());
				chatEntry.setCM_Chat_ID(chat.get_ID());
				chatEntry.setAD_Org_ID(chat.getAD_Org_ID());
				chatEntry.setModeratorStatus("P");
				chatEntry.setConfidentialType("A");
				chatEntry.setChatEntryType("N");
				chatEntry.setCharacterData(p_Message);
				chat.save();
				chatEntry.save();
			} else {
				throw new AdempiereException(
						"Error: Debe Ingresar mensaje con el motivo" + " cuando pasa a un estado anterior");
			}
		}
	}

	/**
	 * Actualiza el campo AD_User_ID con el ID proporcionado cuando este est� vacio
	 *
	 * @param req
	 * @param id
	 */
	private void ad_userValidate(MRequisition req, int id) {
		if (req.getAD_User_ID() == 0)
			req.setAD_User_ID(id);
		req.saveEx();
	}
}
