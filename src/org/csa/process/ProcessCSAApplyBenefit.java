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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.X_C_BPBenefit;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 * Updates the price list and updates the order lines with new prices depending
 * on the balance of the benefit
 *
 * @author jleyton
 * @version 1.0
 */
public class ProcessCSAApplyBenefit extends SvrProcess {
	// private String p_DocStatus = null;
	private int p_C_Order_ID = 0;
	private int p_M_PriceList_ID;

	/**
	 * Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare() {
		p_C_Order_ID = getRecord_ID();
	} // prepare

	/**
	 * Perrform process.
	 *
	 * @return Message (clear text)
	 * @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception {
		MOrder ord = new MOrder(getCtx(), p_C_Order_ID, get_TrxName());

		int c_period_id = DB.getSQLValue(get_TrxName(),
				"select c_period_id from c_period cp join c_year cy "
						+ "on cy.c_year_id=cp.c_year_id where cy.c_calendar_id=1000000 and '" + ord.getDateOrdered()
						+ "' between cp.startdate and cp.enddate;");
		int ben = DB.getSQLValue(get_TrxName(),
				"select coalesce(max(c_bpbenefit_id),0) from c_bpbenefit where c_bpartner_id=" + ord.getC_BPartner_ID()
						+ " and c_periodto_id is null and c_period_id=" + c_period_id);

		String msj = "";
		if (ben > 0) {

			X_C_BPBenefit benef = new X_C_BPBenefit(getCtx(), ben, get_TrxName());
			msj=updatePaymentRule(ord,benef.getAvailableAmt());

		}else {
			//24082023 jleyton se crea mensaje de aviso de que no tiene beneficio
			//tarea https://redbooth.com/a/#!/projects/986995/tasks/57575433
			throw new AdempiereException("El funcionario no tiene beneficio asignado para el periodo del documento.");
		}

		return msj;
	} // doIt

	/**
	 * Actualiza la forma de pago.
	 *
	 * @param ord
	 *
	 */
	private String updatePaymentRule(MOrder ord, BigDecimal availableAmt) {
		MOrderLine lines[] = ord.getLines();
		BigDecimal[] pActual= new BigDecimal[lines.length];
		String msj="";
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.');
		DecimalFormat formatea = new DecimalFormat("###,###.##", otherSymbols);
		if (availableAmt.compareTo(ord.getGrandTotal()) >= 0) {
			msj = "Se aplican " + ord.getGrandTotal().toBigInteger() + " de "
					+ availableAmt.toBigInteger() + " disponible.";
//			ord.set_CustomColumn("PaidAmt", ord.getGrandTotal());
//			ord.set_CustomColumn("MixVariance", "N");
//			ord.setPaymentRule("P");
//			//ord.setProcessed(true);
//			ord.saveEx();
			//se cambia a un update para no chocar con validacion de cambio de forma de pago manual
			DB.executeUpdate("update c_order set PaidAmt="+ord.getGrandTotal()+","
					+ "MixVariance='N',PaymentRule='P' where c_order_id= "+ord.getC_Order_ID(),get_TrxName());
		} else {
			throw new AdempiereException("Saldo de $" + formatea.format(availableAmt.toBigInteger()) + " insuficiente para cubrir los $"
					+ formatea.format(ord.getGrandTotal().toBigInteger()) + " del documento.");
		}
		return msj;
	}
}
