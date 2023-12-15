package org.ofb.model;

import java.math.BigDecimal;
import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

/**
 * CalloutIsaias
 *
 * @author Isaías
 * @version 1
 */
public class CalloutIsaias extends CalloutEngine {
	
	public String validarMonto(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		
		if (isCalloutActive() || value == null || (mTab.getValue("monto_1") == null)
				|| (mTab.getValue("monto_2") == null)) {
			return "";
		}

		BigDecimal monto1 = (BigDecimal) mTab.getValue("monto_1");
		BigDecimal monto2 = (BigDecimal) mTab.getValue("monto_2");

		BigDecimal suma = monto1.add(monto2);

		mTab.setValue("TotalAmt", suma);

		if (suma.compareTo(BigDecimal.valueOf(10000)) > 0) {
			mTab.setValue("MESAGE", "1");
		}
		else {
			mTab.setValue("MESAGE", "2");
		}

		return "";
	}
}
