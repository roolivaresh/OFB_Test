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

import org.adempiere.core.domains.models.I_C_Invoice;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MCash;
import org.compiere.model.MCashLine;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_BPBenefit;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Create Payments for CSA
 *  @author jleyton
 */
public class ModCSACreatePaymentInvoice implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCSACreatePaymentInvoice ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCSACreatePaymentInvoice.class);
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
			log.info("Initializing Model Price Validator: "+this.toString());
		}

		//	Tables to be monitored

		//	Documents to be monitored
		engine.addDocValidate(I_C_Invoice.Table_Name, this);



	}	//	initialize

	/**
	 *	Model Change of a monitored Table.
	 *
	 */
	@Override
	public String modelChange (PO po, int type) throws Exception
	{
		return null;
	}	//	modelChange

	@Override
	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==I_C_Invoice.Table_ID)
		{
			MInvoice inv = (MInvoice)po;
			//validamos si ya tiene la leyenda de sencillo
			if(inv.getDescription()!=null) {
				if(!inv.getDescription().contains("Ley 20966")) {
					ajusteSencillo(inv);
				}
			} else
				ajusteSencillo(inv);
			
			if(inv.isSOTrx() && inv.getC_Order_ID()>0 && inv.getC_DocType().getDocBaseType().compareTo("ARC")!=0) {
				MOrder ord = new MOrder(po.getCtx(), inv.getC_Order_ID(), po.get_TrxName());
				if(!ord.getC_DocTypeTarget().getDocSubTypeSO().equals("WR"))
					return "";
				BigDecimal pay1=ord.getGrandTotal();
				BigDecimal pay2=Env.ZERO;
				// aqui se determina si es un pago mixto o normal
				if(ord.get_Value("MixVariance").equals(true)) {
					pay1=(BigDecimal) ord.get_Value("cash");
					if(pay1==null)
						pay1=Env.ZERO;
					if(pay1.compareTo(Env.ZERO)==0)
						throw new AdempiereException("Monto 1er Pago no puede ser 0.");
					CreatePay(ord, inv, po, pay1,ord.getPaymentRule());
					pay2=ord.getGrandTotal().subtract(pay1);
					if(pay2==null)
						pay1=Env.ZERO;
					if(pay2.compareTo(Env.ZERO)==0)
						throw new AdempiereException("Monto 2do Pago no puede ser 0.");
					CreatePay(ord, inv, po, pay2,ord.get_ValueAsString("PaymentRule2"));
				}
				else {
					CreatePay(ord, inv, po, pay1,ord.getPaymentRule());
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

	/**
	 * Esta funcion crea el pago del documento dependiendo de la seleccion de la orden.
	 * @param ord
	 * @param inv
	 * @param po
	 * @param payment
	 * @param paymentrule
	 */
	public void CreatePay(MOrder ord, MInvoice inv,PO po, BigDecimal payment, String paymentrule) {
		//jleyton si es pago efectivo llama a la creacion de un diario de caja
		if(paymentrule.compareTo("B") == 0) {
			int cashBookId = DB.getSQLValue(po.get_TrxName(),"select coalesce(max(cb.c_cashbook_id),0) from c_cashbook cb "
					+ " where cb.ad_user_id="+ord.getSalesRep_ID());
			if(cashBookId==0)
				throw new AdempiereException("Usuario "+ord.getSalesRep().getName()+" no tiene caja chica asociada.");
			MCash cash = MCash.get(po.getCtx(), cashBookId, inv.getDateInvoiced(), po.get_TrxName());
			MCashLine cashLine = new MCashLine (cash);
			cashLine.setInvoice(inv);
			//05-10-2023 JLeyton Se redondea efectivo segun norma de Chile
			if(!(payment==null)) {
				payment= payment.divide(new BigDecimal(10))
						.setScale(0,RoundingMode.HALF_DOWN)
						.multiply(new BigDecimal(10));
			}else
				payment=Env.ZERO;
			cashLine.setAmount(payment);
			cashLine.saveEx(po.get_TrxName());
			log.config("Se crea Diario de caja :"+cash.getName());
			discountBenefit(ord, po, null);
		}else {
			MPayment pay = new MPayment(po.getCtx(), 0, po.get_TrxName());
			pay.setC_BPartner_ID(inv.getC_BPartner_ID());
			pay.setDescription(ord.getDescription());
			//se crea con tipo de documento AR Receipt
			pay.setC_DocType_ID(1000008);
			pay.setDateTrx(inv.getDateInvoiced());
			pay.setDateAcct(inv.getDateAcct());
			pay.setC_Invoice_ID(inv.get_ID());

			//if de tendertype con paymentrule
			if(paymentrule.compareTo("S") == 0)
				pay.setTenderType("K");
			else if(paymentrule.compareTo("K") == 0)
				pay.setTenderType("C");
			else if(paymentrule.compareTo("D") == 0)
				pay.setTenderType("D");
			else
				pay.setTenderType(paymentrule);

			pay.setPayAmt(payment);
			pay.setC_Currency_ID(ord.getC_Currency_ID());
			// se crea con cuenta bancaria por defecto
			pay.setC_BankAccount_ID(1000000);
			pay.setIsOverUnderPayment(true);
			pay.setAD_Org_ID(inv.getAD_Org_ID());
			pay.saveEx();
			if(ord.getPaymentRule().equals("P"))
				discountBenefit(ord, po, pay);

			if(pay.processIt("CO")) {
				pay.saveEx();
				if(pay.getPayAmt().compareTo(inv.getGrandTotal())==0)
					inv.setIsPaid(true);
				inv.saveEx();
			}
		}
		if(ord.get_Value("isApply").equals(true))
			discountBenefitPriceList(ord, po);
	}

	public void discountBenefit(MOrder ord, PO po, MPayment pay) {
		//se obtiene el ID del periodo del beneficio aplicado si fuera el caso
		BigDecimal paidAmt = (BigDecimal)ord.get_Value("PaidAmt");

		if(paidAmt.compareTo(Env.ZERO)>0){
			int c_period_id = DB.getSQLValue(po.get_TrxName(),
					"select c_period_id from c_period cp join c_year cy "
							+ "on cy.c_year_id=cp.c_year_id where cy.c_calendar_id=1000000 and '" + ord.getDateOrdered()
							+ "' between cp.startdate and cp.enddate;");
			int ben = DB.getSQLValue(po.get_TrxName(),
					"select coalesce(max(c_bpbenefit_id),0) from c_bpbenefit where c_bpartner_id=" + ord.getC_BPartner_ID()
					+ " and c_periodto_id is null and c_period_id=" + c_period_id);
			if(ben>0) {
				pay.set_CustomColumn("C_BPBenefit_ID", ben);
				X_C_BPBenefit benefit = new X_C_BPBenefit(po.getCtx(), ben, po.get_TrxName());
				benefit.setAvailableAmt(benefit.getAvailableAmt().subtract(paidAmt));
				benefit.setAllocatedAmt(benefit.getAllocatedAmt().add(paidAmt));
				benefit.saveEx();
			}
		}

	}
	public void discountBenefitPriceList(MOrder ord, PO po) {
		int c_period_id = DB.getSQLValue(po.get_TrxName(),
				"select c_period_id from c_period cp join c_year cy "
						+ "on cy.c_year_id=cp.c_year_id where cy.c_calendar_id=1000000 and '" + ord.getDateOrdered()
						+ "' between cp.startdate and cp.enddate;");
		int ben = DB.getSQLValue(po.get_TrxName(),
				"select coalesce(max(c_bpbenefit_id),0) from c_bpbenefit where c_bpartner_id=" + ord.getC_BPartner_ID()
				+ " and "+c_period_id+" between c_period_id and c_periodto_id");
		if(ben>0) {
			X_C_BPBenefit benefit = new X_C_BPBenefit(po.getCtx(), ben, po.get_TrxName());
			benefit.setAvailableAmt(benefit.getAvailableAmt().subtract(ord.getGrandTotal()));
			benefit.setAllocatedAmt(benefit.getAllocatedAmt().add(ord.getGrandTotal()));
			benefit.saveEx();
		}
	}
	
	public void ajusteSencillo(MInvoice inv) {
		if(inv.getPaymentRule().equals("B")) {
			BigDecimal montoRedondeado = inv.getGrandTotal().divide(new BigDecimal(10))
					.setScale(0,RoundingMode.HALF_DOWN)
					.multiply(new BigDecimal(10));
			BigDecimal ajusteSencillo=inv.getGrandTotal().subtract(montoRedondeado);
			if(ajusteSencillo.abs().compareTo(Env.ZERO)>0) {
				if(inv.getDescription()==null) {
					inv.setDescription("Redondeo Ley 20966: ("+ajusteSencillo.abs()+").");
					inv.saveEx();
				}
				else {
					inv.setDescription(inv.getDescription().concat("||Redondeo Ley 20966: ("+ajusteSencillo.abs()+")."));
					inv.saveEx();
				}		
			}
		}
	}



}