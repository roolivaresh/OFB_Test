package org.ofb.model;

import java.math.BigDecimal;
import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MCash;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	OFB Callouts.
 *
 *  @author JLeyton
 *  @version 1
 */
public class CalloutRodrigo extends CalloutEngine{

	public String cashStartBalance(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		if(isCalloutActive() || value==null || (mTab.getValue("C_CashBook_ID")==null))
			return "";
		String sql = "select coalesce(max(c_cash_id),0) from c_cash where c_cashbook_id=" + mTab.getValue("C_CashBook_ID")
		+ " and StatementDate<='" + mTab.getValue("StatementDate")+ "' and docstatus in ('CO')";
		log.config("-->>CalloutSQL:"+sql);
		int lastCash_id = DB.getSQLValue(null, sql);
		if (lastCash_id > 0) {
			MCash lastCash = new MCash(ctx, lastCash_id, null);
			mTab.setValue("BeginningBalance",lastCash.getEndingBalance());
		} else
			// sino lo encuentra le asigna saldo inicial 0
			mTab.setValue("BeginningBalance",Env.ZERO);


		return "";
	}
	
	public String Cap(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		if(isCalloutActive() || value==null || (mTab.getValue("monto_1")==null))
			return "";
		
		BigDecimal monto1 = (BigDecimal) mTab.getValue("monto_1");
		BigDecimal monto2 = (BigDecimal)  mTab.getValue("monto_2");
		
		BigDecimal total  = monto1.add(monto2);
		
		mTab.setValue("Name", total.toString());


		return "";
	}


}
