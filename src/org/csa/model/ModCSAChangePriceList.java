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
import java.util.Properties;

import org.adempiere.core.domains.models.I_C_Invoice;
import org.adempiere.core.domains.models.I_C_Order;
import org.adempiere.core.domains.models.I_M_Requisition;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MCash;
import org.compiere.model.MCashLine;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.MPriceList;
import org.compiere.model.MProductPrice;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_BPBenefit;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Updates line prices when the price list is changed
 *  @author jleyton
 */
public class ModCSAChangePriceList implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCSAChangePriceList ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCSAChangePriceList.class);
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
		//	Documents to be monitored

	}	//	initialize

	/**
	 *	Model Change of a monitored Table.
	 *
	 */
	@Override
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if(type == TYPE_BEFORE_CHANGE && po.is_ValueChanged("M_PriceList_ID") && po.get_Table_ID() == I_C_Order.Table_ID) {
			MOrder ord = (MOrder) po;
			MPriceList plist = new MPriceList(po.getCtx(), ord.getM_PriceList_ID(),po.get_TrxName());
			MOrderLine lines[] = ord.getLines();
			if (plist.getPriceListVersion(ord.getDateOrdered())==null) {
				throw new AdempiereException("No se encuentra version de lista de precio valida para la fecha: "+ ord.getDateOrdered());
			}
			int i=0;
			BigDecimal[] pActual= new BigDecimal[lines.length];
			for (MOrderLine line : lines) {
				int ppid = DB.getSQLValue(null,
						"select max(m_productprice_id) from m_productprice where m_pricelist_version_id="
								+ plist.getPriceListVersion(ord.getDateOrdered()).get_ID() + " and m_product_id="
								+ line.getM_Product_ID() + ";");
				if (ppid > 0) {
					MProductPrice pp = new MProductPrice(po.getCtx(), ppid,po.get_TrxName());
					pActual[i] = line.getPriceActual();
					line.setPriceEntered(pp.getPriceList());
					line.setPriceActual(pp.getPriceList());
					line.setPriceList(pp.getPriceList());
					line.setPriceLimit(pp.getPriceLimit());
					//line.setPrice(totalLines);
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
		}
		return null;
	}	//	modelChange

	@Override
	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
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