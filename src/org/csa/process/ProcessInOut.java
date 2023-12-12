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

import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInOut;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *
 *
 *  @author mfrojas
 *  @version $Id: ProcessInOut.java $
 */
public class ProcessInOut extends SvrProcess
{
	//private String			p_DocStatus = null;
	private int				p_InOut_ID = 0;
	private String				p_Action = "PR";
	//mfrojas Se agrega nuevo parametro para recibir mensaje al momento de anular.
	private String 			p_Message = "";
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 @Override
	protected void prepare()
	{
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
		p_InOut_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		if (p_InOut_ID > 0)
		{

			MInOut inout = new MInOut(getCtx(), p_InOut_ID, get_TrxName());

			//seteo de nuevo estado al procesar
			String newStatus = "DR";
			String newAction = "DR";
			//String modality = req.get_Value("Modality").toString();

			//mfrojas
			String actualStatus = inout.getDocStatus();
			int workflowID = DB.getSQLValue(get_TrxName(), "SELECT coalesce(max(ad_workflow_id),0) from ad_workflow "
					+ " WHERE c_doctype_id = ? and ad_client_id = "+Env.getAD_Client_ID(getCtx()), inout.getC_DocType_ID());

			log.config("paction "+p_Action);
			log.config("workflowID "+workflowID);
			log.config("estado actual "+actualStatus);

			if(workflowID > 0)
			{

				newStatus = p_Action;
				//Validador de permisos de rol
				int cant = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM AD_Document_Action_Access daa" +
						" INNER JOIN AD_Ref_List rl ON (daa.AD_Ref_List_ID = rl.AD_Ref_List_ID) " +
						" WHERE value = '"+newStatus+"' AND AD_Role_ID = "+Env.getAD_Role_ID(getCtx())+
						" AND C_DocType_ID = "+inout.getC_DocType_ID());

				if(cant > 0)
				{
					if(newStatus.compareTo("CO") != 0)
					{
						if(newStatus.compareTo("VO") == 0)
							inout.voidIt();
						else
							inout.setDocStatus(newStatus);
						inout.saveEx();
					}
					else if(newStatus.compareTo("CO") == 0)
					{
						inout.setDocStatus("IP");
						inout.processIt("CO");
						inout.saveEx();
					}

				}
				else
					throw new AdempiereException("Error: Permisos de rol insuficientes");

			}
			else
				throw new AdempiereException("No existe flujo asociado al estado actual del documento");
		}
	   return "Procesado";
	}	//	doIt
}
