package org.csa.process;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.compiere.model.MInvoice;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import cl.pronova.sii.siidte.tecnoback.TecnobackDTESender;

public class ProcessCSAGenerateDTE extends SvrProcess{
	private int p_C_Invoice_ID;

	/**
	 * Send DTE from Pronova class
	 * @author jleyton
	 * @version 1.0
	 */
	@Override
	protected void prepare() {
		p_C_Invoice_ID = getRecord_ID();

	}

	/**
	 * Prepare - e.g., get Parameters.
	 */
	@Override
	protected String doIt() throws Exception {
		MInvoice inv = new MInvoice(getCtx(),p_C_Invoice_ID,get_TrxName());
		
		//integracion con tecnoback	
		TecnobackDTESender tb = new TecnobackDTESender(inv);
		if(tb.sendDte()) {		
			return tb.getResultMsg();
		}
		
		return "No se ha generado documento: "+tb.getResultMsg();

	}

}
