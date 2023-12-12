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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPriceList;
import org.compiere.model.MProductPrice;
import org.compiere.model.X_C_BPBenefit;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * Updates the price list and updates the order lines with new prices depending
 * on the balance of the benefit
 *
 * @author jleyton
 * @version 1.0
 */
public class ProcessCSAApplyBenefitPriceList extends SvrProcess {
	// private String p_DocStatus = null;
	private int p_C_Order_ID = 0;
	private int p_M_PriceList_ID;

	/**
	 * Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (name.equals("M_PriceList_ID"))
				p_M_PriceList_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
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
						+ " and "+c_period_id+" between c_period_id and c_periodto_id");

		String msj = "";

		if (ben > 0) {

			X_C_BPBenefit benef = new X_C_BPBenefit(getCtx(), ben, get_TrxName());
			// se valida lista de precios y se actualiza precios en lineas
			msj=updatePriceList(ord, p_M_PriceList_ID,benef.getAvailableAmt());

		}else {
			//24082023 jleyton se crea mensaje de aviso de que no tiene beneficio
			//tarea https://redbooth.com/a/#!/projects/986995/tasks/57575433
			throw new AdempiereException("El funcionario no tiene beneficio asignado para el periodo del documento.");
		}

		return msj;
	} // doIt

	/**
	 * Update the price list and update the order lines with new prices.
	 *
	 * @param ord
	 *
	 */
	private String updatePriceList(MOrder ord, int plistid, BigDecimal availableAmt) {
		MPriceList plist = new MPriceList(getCtx(), plistid, get_TrxName());
		MOrderLine lines[] = ord.getLines();
		if (plist.getPriceListVersion(ord.getDateOrdered())==null) {
			throw new AdempiereException("No se encuentra version de lista de precio valida para la fecha: "+ ord.getDateOrdered());
		}
		int i=0;
		BigDecimal[] pActual= new BigDecimal[lines.length];

			for (MOrderLine line : lines) {
			int ppid = DB.getSQLValue(get_TrxName(),
					"select max(m_productprice_id) from m_productprice where m_pricelist_version_id="
							+ plist.getPriceListVersion(ord.getDateOrdered()).get_ID() + " and m_product_id="
							+ line.getM_Product_ID() + ";");
			if (ppid > 0) {
				MProductPrice pp = new MProductPrice(getCtx(), ppid, get_TrxName());
				pActual[i] = line.getPriceActual();
				line.setPriceEntered(pp.getPriceList());
				line.setPriceActual(pp.getPriceList());
				//se calculan los impuestos asociados
				BigDecimal lineNetAmt = (line.getPriceActual()).multiply((line.getQtyEntered()));
				BigDecimal rate = new BigDecimal(DB.getSQLValue(null, "select coalesce(rate,0) from c_tax where c_tax_id="+line.getC_Tax_ID()));
				BigDecimal taxAmt = lineNetAmt.multiply(rate).divide(new BigDecimal(100));;
				BigDecimal lineTotalAmt = lineNetAmt.multiply(rate.divide(new BigDecimal(100)).add(Env.ONE));
				// se asigna calculo impto a los campos nuevos
				line.set_ValueOfColumn("TaxAmt",taxAmt.setScale(0,RoundingMode.HALF_UP));
				line.set_ValueOfColumn("LineTotalAmt",lineTotalAmt.setScale(0,RoundingMode.HALF_UP));
				line.saveEx();
				i++;
			} else
				throw new AdempiereException("Producto " + line.getM_Product().getName() + " de la linea "
						+ line.getLine() + " no se encuentra en lista de precio " + plist.getName());

		}
		String msj="";
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.');
		DecimalFormat formatea = new DecimalFormat("###,###.##", otherSymbols);
		if (availableAmt.compareTo(ord.getGrandTotal()) >= 0) {
			try {
			DB.executeUpdate("update c_order set isapply='Y',M_PriceList_ID="+plistid+", M_PriceList_Version_ID="+plist.getPriceListVersion(ord.getDateOrdered()).get_ID() +" where c_order_id="+ord.getC_Order_ID(),get_TrxName());
			//DB.executeUpdate( "update c_orderline set processed='Y' where c_order_id="+ord.getC_Order_ID(),get_TrxName());
			msj="Se modifica lista de precio.";
			}
			catch (Exception e) {
				throw new AdempiereException("ERROR BD==>",e);
			}
		} else {
			i=0;
			//rollback
			for (MOrderLine line : lines) {
				line.setPriceEntered(pActual[i]);
				line.setPriceActual(pActual[i]);
				line.saveEx();
				i++;
			}

			throw new AdempiereException("Saldo de $" + formatea.format(availableAmt.toBigInteger()) + " insuficiente para cubrir los $"
					+ formatea.format(ord.getGrandTotal().toBigInteger()) + " del documento con descuento del beneficio.");
		}
		return msj;
	}
}
