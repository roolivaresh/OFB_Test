/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.csa.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.mobile.MobileEnv;
import org.compiere.model.CalloutEngine;
import org.compiere.model.CalloutRequisition;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MConversionRate;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPriceList;
import org.compiere.model.MProductPrice;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * DM Document Callouts.
 *
 * @author Rodrigo Olivares Hurtado
 * @version $Id: CalloutCSA.java,v 1.0 2023/03/14 10:01:24 Exp $
 */
public class CalloutCSA extends CalloutEngine {

	public String GetInfo(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		if (isCalloutActive() || value == null || (mTab.getValue("M_Product_ID") == null))
			return "";

		Integer ProducID = (Integer) mTab.getValue("M_Product_ID");

		int CBPartnerID = 0;
		int ReqBPartnerID = 0;

		if (ProducID > 0) {

			String SqlBPName = "select bp.name from M_Product_PO ppo "
					+ "join c_bpartner bp on bp.c_bpartner_id = ppo.c_bpartner_id " + "where ppo.m_product_id = ?";
			String BPName = DB.getSQLValueString(null, SqlBPName, ProducID); // obtener el encargado del producto.

			// obtener ultimo c_invoice del producto.
			String SqlGetInvoiceID = "select coalesce(max(inv.c_invoice_id),0) " + "from c_invoice inv "
					+ "join c_invoiceline invl on inv.c_invoice_id =invl.c_invoice_id "
					+ "where inv.C_DocType_ID=1000005 and inv.DocStatus='CO' and "
					+ " inv.isactive = 'Y' and invl.m_product_id = ?;";

			int InvoiceID = DB.getSQLValue(null, SqlGetInvoiceID, ProducID);

			String SqlADUserID = "select AD_User_ID from m_product " + "where m_product_id = ?";
			int ADUserID = DB.getSQLValue(null, SqlADUserID, ProducID); // obtiene el ejecutivo del producto
			mTab.setValue("AD_User_ID", ADUserID); // registra el ejecutivo del producto

			if (InvoiceID > 0) {
				// obtener c_invoiceline_id del producto
				String SqlGetInvoicelineID = "select coalesce(max(invl.c_invoiceline_id),0) " + "from c_invoice inv "
						+ "join c_invoiceline invl on inv.c_invoice_id =invl.c_invoice_id "
						+ "where inv.isactive = 'Y' and invl.m_product_id = " + ProducID
						+ " and invl.c_invoice_id = ?;";

				int InvoicelineID = DB.getSQLValue(null, SqlGetInvoicelineID, InvoiceID);

				MInvoice inv = new MInvoice(ctx, InvoiceID, null);
				CBPartnerID = inv.getC_BPartner_ID(); // obtiene socio de negocio de la c_invoice

				MInvoiceLine invl = new MInvoiceLine(ctx, InvoicelineID, null);
				invl.getPriceEntered(); // obtener el ultimo valor del producto en c_invoiceline.

				// LLenar el campos correspondientes
				mTab.setValue("PriceActual", invl.getPriceEntered());

			}

			// Rodrigo Olivares Hurtado 2023-03-23
			if (CBPartnerID <= 0 || Integer.valueOf(CBPartnerID) == null) {
				String SqlReqBPartnerID = "SELECT coalesce(max(C_BPartner_ID),0) FROM M_Requisition "
						+ "WHERE m_requisition_id = ?";

				ReqBPartnerID = DB.getSQLValue(null, SqlReqBPartnerID, mTab.getValue("m_requisition_id")); // obtener el
				// c_bpartner_id
				// de la
				// cabezera.
				// Rodrigo Olivares Hurtado 2023-04-05
				// Solo ingresa si es que tiene un socio de negocio en la cabecera
				if (ReqBPartnerID > 0) {
					mTab.setValue("c_bpartner_id", ReqBPartnerID);
				}
			} else {
				ReqBPartnerID = CBPartnerID;
				mTab.setValue("c_bpartner_id", ReqBPartnerID);
			}

			String SqlOrderMin = "select min(Order_Min) from M_Product_PO "
					+ "where m_product_id = ? and c_bpartner_id = " + ReqBPartnerID;

			int OrderMin = DB.getSQLValue(null, SqlOrderMin, ProducID); // obtener el minimo a ordenar.
			if (OrderMin > 0) {
				mTab.setValue("Order_Min", OrderMin); // Se registra el min a ordenar.
			}

			CalloutRequisition call = new CalloutRequisition();
			call.amt(ctx, WindowNo, mTab, mField, value);

		}

		return "";
	} // charge

	public String GetCurrencyCLP(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		if (isCalloutActive() || value == null)
			return "";

		int CurrencyCLPID = DB.getSQLValue(null,
				"SELECT COALESCE(MAX(C_Currency_ID),0) FROM C_Currency WHERE ISO_Code = 'CLP'");
		int ConversionRateID = 0;

		BigDecimal GrandTotalCLP = Env.ZERO;

		Integer RequisitionID = (Integer) mTab.getValue("M_Requisition_ID");
		Integer CurrencyID = (Integer) mTab.getValue("C_Currency_ID");
		Timestamp DateDoc = (Timestamp) mTab.getValue("DateDoc");

		if (CurrencyID != 0 && CurrencyCLPID > 0) {
			ConversionRateID = DB.getSQLValue(null,
					"SELECT COALESCE(MAX(C_Conversion_Rate_ID),0) FROM C_Conversion_Rate " + " WHERE C_Currency_ID = "
							+ CurrencyCLPID + " and C_Currency_ID_To = " + CurrencyID + "  AND '" + DateDoc
							+ "' BETWEEN ValidFrom AND ValidTo");
			if (ConversionRateID > 0) {
				MConversionRate conv = new MConversionRate(ctx, ConversionRateID, null);

				if (CurrencyID == 1000002) {// UF
					BigDecimal GrandTotal = (BigDecimal) mTab.getValue("GrandTotal");
					GrandTotalCLP = GrandTotal.multiply(conv.getDivideRate());
					mTab.setValue("CurrencyCLP", GrandTotalCLP);

				} else if (CurrencyID == 100) { // USD
					BigDecimal GrandTotal = (BigDecimal) mTab.getValue("GrandTotal");
					GrandTotalCLP = GrandTotal.multiply(conv.getDivideRate());
					mTab.setValue("CurrencyCLP", GrandTotalCLP);

				} else if (CurrencyID == 102) { // EUR
					BigDecimal GrandTotal = (BigDecimal) mTab.getValue("GrandTotal");
					GrandTotalCLP = GrandTotal.multiply(conv.getDivideRate());
					mTab.setValue("CurrencyCLP", GrandTotalCLP);

				} else if (CurrencyID == 1000001) { // UTA
					BigDecimal GrandTotal = (BigDecimal) mTab.getValue("GrandTotal");
					GrandTotalCLP = GrandTotal.multiply(conv.getDivideRate());
					mTab.setValue("CurrencyCLP", GrandTotalCLP);

				} else if (CurrencyID == 1000000) { // UTM
					BigDecimal GrandTotal = (BigDecimal) mTab.getValue("GrandTotal");
					GrandTotalCLP = GrandTotal.multiply(conv.getDivideRate());
					mTab.setValue("CurrencyCLP", GrandTotalCLP);
				}

			} else if (CurrencyID == CurrencyCLPID) { // CLP
				BigDecimal GrandTotal = (BigDecimal) mTab.getValue("GrandTotal");
				mTab.setValue("CurrencyCLP", GrandTotal);
			} else {
				mTab.setValue("CurrencyCLP", Env.ZERO);
			}

		}

		return "";
	}// GetCurrencyCLP

	public String GetTax(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		if (isCalloutActive() || value == null)
			return "";

		BigDecimal PriceActual = (BigDecimal) mTab.getValue("PriceActual");
		Integer TaxID = (Integer) mTab.getValue("C_Tax_ID");

		if (TaxID == 1000000) {// 1000000
			// Rodrigo Olivares Hurtado 2023-09-05 Se redondea a 2 decimales
			BigDecimal TaxAmt = PriceActual.multiply(new BigDecimal(0.19));
			TaxAmt = TaxAmt.setScale(2, RoundingMode.HALF_UP);
			mTab.setValue("TaxAmt", TaxAmt);
			mTab.setValue("LineTotalAmt", PriceActual.add(TaxAmt).setScale(2, RoundingMode.HALF_UP));
		} else {
			BigDecimal TaxAmt = PriceActual.multiply(new BigDecimal(0));

			mTab.setValue("TaxAmt", TaxAmt);
			mTab.setValue("LineTotalAmt", PriceActual);
		}

		return "";
	}

	/**
	 * Convierte la cantidad ingresada en la UM del proveedor
	 *
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	public String GetConvertion(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		String DocBaseType = DB.getSQLValueString(null,
				"select cdt.DocBaseType from c_order co join c_doctype cdt on cdt.c_doctype_id=co.C_DocTypeTarget_ID "
						+ "where co.c_order_id=" + mTab.getValue("C_Order_ID"));
		if (DocBaseType.equals("POO")) {
			String sql = "select coalesce(max(Order_Pack),0) from m_product_po where m_product_id="
					+ mTab.getValue("M_Product_ID") + " and c_bpartner_id=" + mTab.getValue("C_BPartner_ID");
			log.config("sql UM ==>" + sql);
			BigDecimal Order_Pack = new BigDecimal(DB.getSQLValue(null, sql));
			BigDecimal QtyEntered = (BigDecimal) mTab.getValue("QtyEntered");
			BigDecimal ConvertedAmt = (BigDecimal) mTab.getValue("ConvertedAmt");
			if (Order_Pack.compareTo(Env.ZERO) == 0) {
				log.severe("No se encuentra conversión para la unidad de medida del proveedor");
				mTab.setValue("ConvertedAmt", Env.ZERO);
			} else {
				BigDecimal QtyConverted = QtyEntered.divide(Order_Pack,2, RoundingMode.HALF_UP);
				BigDecimal QtyConverted_to = ConvertedAmt.multiply(Order_Pack);

				if (mField.getColumnName().equals("QtyEntered"))
					mTab.setValue("ConvertedAmt", QtyConverted);
				else {
					mTab.setValue("QtyEntered", QtyConverted_to);
					mTab.setValue("QtyOrdered", QtyConverted_to);
				}
			}
		}

		return null;

	}


	public String rut (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{

		String Digito =(String) mTab.getValue("ValueValidator");
		if(Digito==null)
			return "";
		if(Digito.equals("") )
			return "";

		if(value==null)
			return "";	
		Digito=Digito.trim();
		int M=0,S=1,T;
		String myRut=(String)mTab.getValue("Value");
		if(myRut==null)
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
			mTab.setValue("ValueValidator",null);
			//mTab.setValue("Value",null);

			return "";
		}
		if(myRut.equals(""))
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
			mTab.setValue("ValueValidator",null);
			//mTab.setValue("Value",null);
			return "";
		}
		if(myRut.length()==0)
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
			mTab.setValue("ValueValidator",null);
			//mTab.setValue("Value",null);
			return "";
		}

		if(myRut.equals("-"))
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
			mTab.setValue("ValueValidator",null);
			//mTab.setValue("Value",null);
			return "";
		}

		if(myRut.equals("%"))
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
			mTab.setValue("ValueValidator",null);
			//mTab.setValue("Value",null);
			return "";
		}

		if(myRut.substring(0, 1).equals("0"))
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No puede Partir con 0 ", true);
			mTab.setValue("ValueValidator",null);
			//mTab.setValue("Value",null);
			return "";
		}

		try
		{

			T=Integer.parseInt((String)mTab.getValue("Value"));
		}
		catch (Exception e)
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
			mTab.setValue("ValueValidator",null);
			//mTab.setValue("Value",null);

			return "";
		}

		for(;T!=0;T/=10)
			S=(S+T%10*(9-M++%6))%11;

		char dig=(char)(S!=0?S+47:75);

		if(Digito.charAt(0)!=dig)
		{
			mTab.setValue("ValueValidator",null);
			//mTab.setValue("Value",null);
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
		}
		else // es valido
		{
			//ver si existe // para el mismo cliente
			String sql = "select count(1) from C_BPartner where value='" + myRut + "' and AD_Client_ID="+ Env.getAD_Client_ID(ctx);

			// se agrega codigo seg�n lo indicado por ininoles (validar si el c_bpartner_id es null antes de asignar)
			if(mTab.getValue("C_BPartner_ID") != null)

			{
				int ID_Bpartner = (Integer)mTab.getValue("C_BPartner_ID");

				if(ID_Bpartner > 0)
					sql = sql + " AND C_BPartner_ID <> "+ID_Bpartner;

			}
			int existe=DB.getSQLValue("C_BPartner",sql);
			if(existe!=0)
			{
				mTab.setValue("ValueValidator",null);
				//mTab.setValue("Value",null);
				mTab.fireDataStatusEEvent ("Validacion de Rut ", "El Rut ya existe, por favor verifiquelo ", true);

			}

		}

		return "";
	}	//	charge


	public String cash2 (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		if (mTab.getValue("MixVariance").equals("N"))
			return "";


		BigDecimal GrandTotal = (BigDecimal) mTab.getValue("GrandTotal");
		BigDecimal cash  = (BigDecimal) mTab.getValue("cash");

		BigDecimal cash2 = Env.ZERO;
		cash2 = GrandTotal.subtract(cash);

		mTab.setValue("cash2", cash2);

		return "";
	}


	public String RemainingAmt (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		if (mTab.getValue("PaymentRule").equals("B")) {

			BigDecimal GrandTotal = (BigDecimal) mTab.getValue("GrandTotal");
			BigDecimal amt  = (BigDecimal) mTab.getValue("amt");

			BigDecimal RemainingAmt = Env.ZERO;
			RemainingAmt = amt.subtract(GrandTotal);
			if(GrandTotal.compareTo(amt)<0) {
				mTab.setValue("RemainingAmt", RemainingAmt);}
			else {mTab.setValue("RemainingAmt", Env.ZERO);}

		}
		return "";
	}

	public String ProductTax(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		//BigDecimal lineNetAmt = (BigDecimal) mTab.getValue("LineNetAmt");
		BigDecimal lineNetAmt = ((BigDecimal) mTab.getValue("PriceActual"))
				.multiply(((BigDecimal) mTab.getValue("QtyEntered")));
		BigDecimal rate = new BigDecimal(DB.getSQLValue(null, "select coalesce(rate,0) from c_tax where c_tax_id="+mTab.getValue("C_Tax_ID")));
		BigDecimal taxAmt = lineNetAmt.multiply(rate).divide(new BigDecimal(100));;
		BigDecimal lineTotalAmt = lineNetAmt.multiply(rate.divide(new BigDecimal(100)).add(Env.ONE));
		// se asigna calculo impto a los campos nuevos
		mTab.setValue("TaxAmt",taxAmt.setScale(0,RoundingMode.HALF_UP));
		mTab.setValue("LineTotalAmt",lineTotalAmt.setScale(0,RoundingMode.HALF_UP));

		return "";
	}

	//beneficio funcionario anual y mensual
	public String AvailableAmt(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM");
		String dateorder = date.format(formatter);
		BigDecimal Available = new BigDecimal(DB.getSQLValue(null, "select coalesce(max(cb.AvailableAmt),0) "
				+ "	from C_BPBenefit cb "
				+ " join C_Period cp on cb.C_Period_ID=cp.C_Period_ID "
				+ " where C_Periodto_ID is null and cp.name='"+dateorder+"' "
				+ " and cb.C_BPartner_ID="+mTab.getValue("C_BPartner_ID")));	


		BigDecimal annualAvailable = new BigDecimal(DB.getSQLValue(null, "select coalesce(max(cb.AvailableAmt),0) "
				+ " from C_BPBenefit cb join C_Period cp on cb.C_Period_ID=cp.C_Period_ID "
				+ " join C_Period cpt on cb.C_Periodto_ID=cpt.C_Period_ID "
				+ " where '"+dateorder+"' BETWEEN cp.name and cpt.name "
				+ " and cb.C_BPartner_ID="+mTab.getValue("C_BPartner_ID")));		
		log.severe("Disponible: "+Available);
		log.severe("Disponible anual: "+Available);

		mTab.setValue("AvailableAmt",Available);
		mTab.setValue("annualAvailableAmt",annualAvailable);
		return "";
	}
} 


