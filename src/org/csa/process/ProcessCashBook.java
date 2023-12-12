package org.csa.process;

import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MCash;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *
 *
 *  @author mfrojas
 *  @version $Id: ProcessInventory.java $
 */
public class ProcessCashBook extends SvrProcess
{
	//private String			p_DocStatus = null;
	private int				p_Cash_ID = 0;
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
		p_Cash_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		if (p_Cash_ID > 0)
		{
			MCash cash = new MCash(getCtx(), p_Cash_ID, get_TrxName());
			//seteo de nuevo estado al procesar
			String newStatus = "DR";
			String newAction = "DR";

			String actualStatus = cash.getDocStatus();
			int workflowID = DB.getSQLValue(get_TrxName(), "SELECT coalesce(max(ad_workflow_id),0) from ad_workflow "
					+ " WHERE ad_client_id = "+Env.getAD_Client_ID(getCtx())+" and upper(name) like '%CAJA%'");

			log.config("paction "+p_Action);
			log.config("workflowID "+workflowID);
			log.config("estado actual "+actualStatus);

			if(workflowID > 0)
			{

				newStatus = p_Action;

				if(newStatus.compareTo("CO") != 0)
				{
					if(newStatus.compareTo("VO") == 0)
					{
						cash.voidIt();
						cash.setEndingBalance(Env.ZERO);
						cash.setBeginningBalance(Env.ZERO);
						cash.setDocStatus("VO");
						cash.setProcessed(true);
						cash.saveEx();
					}else if(newStatus.compareTo("CC") == 0)
					{
						cash.setDocStatus("CC");
						cash.setDocAction("CO");
						cash.setProcessed(true);
						DB.executeUpdate("update C_CashLine set Processed ='Y' where C_Cash_ID="+cash.getC_Cash_ID(),get_TrxName());
						cash.saveEx();
					}
					else {
						cash.setDocStatus(newStatus);
						cash.saveEx();
					}


				}
				else if(newStatus.compareTo("CO") == 0)
				{
					cash.setDocStatus("IP");
					cash.setDocAction("CO");
					cash.processIt("CO");
					cash.saveEx();
				}



			}
			else
				throw new AdempiereException("No existe flujo asociado al estado actual del documento");
		}
		return "Procesado";
	}	//	doIt
}
