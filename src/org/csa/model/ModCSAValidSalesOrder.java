/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
package org.csa.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.adempiere.core.domains.models.I_C_Order;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_BPBenefit;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator Sales Order for CSA
 *  @author jleyton
 */
public class ModCSAValidSalesOrder implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCSAValidSalesOrder ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCSAValidSalesOrder.class);
	/** Client			*/
	private int		m_AD_Client_ID = -1;


	/**
	 *	Initialize Validation
	 *	@param engine validation engine
	 *	@param client client
	 */
	@Override
	public void initialize (ModelValidationEngine engine, MClient client)
	{
		//client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		}
		else  {
			log.info("Initializing Model Validator: "+this.toString());
		}

		//	Tables to be monitored
		engine.addModelChange(I_C_Order.Table_Name,this);
		engine.addModelChange(MOrderLine.Table_Name,this);
		//	Documents to be monitored
		engine.addDocValidate(I_C_Order.Table_Name, this);
	}	//	initialize

	/**
	 *	Model Change of a monitored Table.
	 *
	 */
	@Override
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);

		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW)&& po.is_ValueChanged("PaymentRule") && po.get_Table_ID() == I_C_Order.Table_ID) {
			MOrder ord = (MOrder)po;
			//se valida que solo sean ordenes de venta
			if(ord.isSOTrx()) {
				String paymentRule=ord.getPaymentRule();
				int paidAmt= ord.get_ValueAsInt("PaidAmt");
				//solo se podr colocar el descuento por planilla desde el proceso.
				if( paymentRule.equals("P") && paidAmt==0) {
					throw new AdempiereException("Seleccionar descuento por planilla con el boton correspondiente");
				}
			}

		}

		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.');
		DecimalFormat formatea = new DecimalFormat("###,###.##", otherSymbols);
		// se agrega validacion de beneficio func
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW)&& po.get_Table_ID() == MOrderLine.Table_ID) {
			MOrderLine ol = (MOrderLine) po;
			MOrder ord = new MOrder(po.getCtx(),ol.getC_Order_ID(),po.get_TrxName());
			if(ord.isSOTrx()) {
				// si es devolucion no debe aplicar validacion
				if(ord.getC_DocType_ID()==1000079 || ord.getC_DocTypeTarget_ID()==1000079)
					return null;
				
				int c_period_id;
				int ben;
				BigDecimal linesAmt;
				X_C_BPBenefit benef;

				//beneficio mensual
				if(ord.getPaymentRule().equals("P")) {
					c_period_id = DB.getSQLValue(po.get_TrxName(),
							"select c_period_id from c_period cp join c_year cy "
									+ "on cy.c_year_id=cp.c_year_id where cy.c_calendar_id=1000000 and '" + ord.getDateOrdered()
									+ "' between cp.startdate and cp.enddate;");
					ben = DB.getSQLValue(null,
							"select coalesce(max(c_bpbenefit_id),0) from c_bpbenefit where c_bpartner_id=" + ord.getC_BPartner_ID()
							+ " and c_periodto_id is null and c_period_id=" + c_period_id);
					benef = new X_C_BPBenefit(po.getCtx(), ben, po.get_TrxName());
					linesAmt =new BigDecimal(DB.getSQLValue(po.get_TrxName(),
							"select coalesce(sum(linenetamt),0) from c_orderline where c_orderline_id<>"+ol.getC_OrderLine_ID()
							+ " and c_order_id="+ ord.getC_Order_ID()));
					linesAmt = (linesAmt.add(ol.getLineNetAmt())).multiply(new BigDecimal(1.19));
					if(linesAmt.compareTo(benef.getAvailableAmt())>0) {
						throw new AdempiereException("Monto total de las lineas ("+formatea.format(linesAmt)+") supera el del beneficio mensual aplicado ("+formatea.format(benef.getAvailableAmt())+").");
					}else {
						//Se obtiene total de la linea para sumarlo al monto a rebajar del beneficio
						BigDecimal lineNetAmt = (ol.getPriceActual()).multiply((ol.getQtyEntered()));
						BigDecimal rate = new BigDecimal(DB.getSQLValue(null, "select coalesce(rate,0) from c_tax where c_tax_id="+ol.getC_Tax_ID()));
						BigDecimal lineTotalAmt = lineNetAmt.multiply(rate.divide(new BigDecimal(100)).add(Env.ONE));// total de la linea
						BigDecimal actualAmt = new BigDecimal(DB.getSQLValue(po.get_TrxName(),"select sum(lineTotalAmt) "
								+ "from c_orderline where c_order_id="+ord.getC_Order_ID()+" and c_orderline_id<>"+ol.getC_OrderLine_ID()));
						//ord.set_CustomColumn("PaidAmt", ((BigDecimal)ord.get_Value("PaidAmt")).add(lineTotalAmt.setScale(0,RoundingMode.HALF_UP)));
						ord.set_CustomColumn("PaidAmt", actualAmt.add(lineTotalAmt.setScale(0,RoundingMode.HALF_UP)));
						ord.set_CustomColumn("MixVariance", "N");
						ord.saveEx();
					}
						
				}
				//beneficio anual
				if(ord.get_Value("isApply").equals(true)) {
					c_period_id = DB.getSQLValue(po.get_TrxName(),
							"select c_period_id from c_period cp join c_year cy "
									+ "on cy.c_year_id=cp.c_year_id where cy.c_calendar_id=1000000 and '" + ord.getDateOrdered()
									+ "' between cp.startdate and cp.enddate;");
					ben = DB.getSQLValue(po.get_TrxName(),
							"select coalesce(max(c_bpbenefit_id),0) from c_bpbenefit where c_bpartner_id=" + ord.getC_BPartner_ID()
							+ " and "+c_period_id+" between c_period_id and c_periodto_id");
					benef = new X_C_BPBenefit(po.getCtx(), ben, po.get_TrxName());
					linesAmt =new BigDecimal(DB.getSQLValue(po.get_TrxName(),
							"select coalesce(sum(linenetamt),0) from c_orderline where c_orderline_id<>"+ol.getC_OrderLine_ID()
							+ " and c_order_id="+ ord.getC_Order_ID()));
					linesAmt = (linesAmt.add(ol.getLineNetAmt())).multiply(new BigDecimal(1.19));
					if(linesAmt.compareTo(benef.getAvailableAmt())>0) {
						throw new AdempiereException("Monto total de las lineas ("+formatea.format(linesAmt)+") supera el del beneficio anual aplicado ("+formatea.format(benef.getAvailableAmt())+").");
					}
				}
				//validar si el producto tiene creado registro en compras (m_product_po)
				int cod_valid = DB.getSQLValue(po.get_TrxName(), "select coalesce(count(*),0)"
						+ " from m_product_po where m_product_id="+ol.getM_Product_ID());
				if(cod_valid==0)
					throw new AdempiereException("Producto "+ol.getM_Product().getName()+" no tiene "
							+ "codigo de barra asociado en compras.");
			}
		}
		return null;
	}	//	modelChange

	@Override
	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()==I_C_Order.Table_ID)
		{
			MOrder ord = (MOrder)po;
			boolean isDevolution=false;
			//se valida que solo sean ordenes de venta
			if(ord.isSOTrx()) {
				MOrderLine lines[]=ord.getLines();
				if(ord.getC_DocType().getC_DocTypeInvoice().getDocBaseType().equals("ARC"))
					isDevolution=true;
				for(MOrderLine ordl:lines) {
					//solo se deben validar lineas con productos
					if(ordl.getM_Product_ID()>0) {
						// se quita validacion de SKU ya que ahora se valida desde m_product_po
						//						if(ordl.getM_Product().getSKU()==null) {
						//							throw new AdempiereException("Producto "+ordl.getM_Product().getName()+" no tiene codigo de barras.");
						//						}
						//Para devoluciones se cambian los precios de los productos a los del documento asociado
						if(isDevolution) {
							String sql="select max(priceentered) from c_invoiceline where "
									+ "c_invoice_id="+ord.get_ValueAsInt("C_Invoice_ID")+" and "
									+ "m_product_id="+ordl.getM_Product_ID();
							int priceEntered=DB.getSQLValue(po.get_TrxName(), sql);
							ordl.setPriceEntered(new BigDecimal (priceEntered));
							ordl.setPriceActual(new BigDecimal (priceEntered));
							ordl.saveEx();
						}
						if(isDevolution) {
							MInvoice inv= new MInvoice(po.getCtx(),ord.get_ValueAsInt("C_Invoice_ID"),po.get_TrxName());
							ord.setM_PriceList_ID(inv.getM_PriceList_ID());
							ord.saveEx();
						}
					}
				}
			}
		}
		return null;
	}	//	docValidate

	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	@Override
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	}	//	login


	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID client
	 */
	@Override
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID


	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("ModelPrice");
		return sb.toString ();
	}	//	toString





}