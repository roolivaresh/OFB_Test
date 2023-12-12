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

import org.adempiere.core.domains.models.I_C_OrderLine;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 *	Validator for CSA
 *  complete column CurrencyCLP
 *  @author Rodrigo Olivares Hurtado
 */
public class ModCSAValidOCLineCustumerReturn implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCSAValidOCLineCustumerReturn ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCSAValidOCLineCustumerReturn.class);
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
		engine.addModelChange(I_C_OrderLine.Table_Name, this);
		//engine.addModelChange(MRequisition.Table_Name, this);

		//engine.addDocValidate(MRequisition.Table_Name, this);



	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *
     */
	@Override
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if((type == TYPE_AFTER_NEW ||type == TYPE_AFTER_CHANGE )&& po.get_Table_ID() == I_C_OrderLine.Table_ID)
		{
			MOrderLine line = (MOrderLine) po;
			MOrder order = new MOrder(po.getCtx(), line.getC_Order_ID(), po.get_TrxName());
			
			if (order.getC_DocTypeTarget().getC_DocTypeInvoice().getDocBaseType().equals("ARC")) {
				int inv_id = order.get_ValueAsInt("C_Invoice_ID");
				if (inv_id == 0)
					throw new AdempiereException("Se debe asociar la factura a rebajar.");
				MInvoice inv = new MInvoice(po.getCtx(), inv_id, po.get_TrxName());

					String sql = "select coalesce(max(m_product_id),0)"
							+ " from c_invoiceline cil "
							+ "where cil.c_invoice_id="+ inv_id +""
							+ "and cil.m_product_id=" + line.getM_Product_ID();
					int m_product_id = DB.getSQLValue(po.get_TrxName(),sql);
					// se valida si el producto existe en la factura a rebajar
					if(m_product_id==0)
						throw new AdempiereException("Producto "+line.getM_Product().getName()+" no existe "
								+ "en la factura "+inv.getDocumentNo());
					sql="select sum(cil.qtyentered)-coalesce((select sum(col.qtyentered) "
						+"from c_order co join c_orderline col on (col.c_order_id=co.c_order_id and col.m_product_id=cil.m_product_id) "
					    +"where  co.c_invoice_id=cil.c_invoice_id and co.docstatus='CO'),0) as qty "
					    +"from c_invoiceline cil "
					    + "where cil.c_invoice_id="+ inv_id + " "
					    +"and cil.m_product_id="+line.getM_Product_ID() +" "
					    +"group by cil.c_invoice_id,cil.m_product_id";
					log.config("SQL CANTIDAD DSIPONIBLE==>"+sql);
					int qtyAvailable = DB.getSQLValue(po.get_TrxName(), sql);
					// se valida si el producto tiene cantidad acumulada para rebajar
					if(qtyAvailable>=line.getQtyEntered().intValue()) {


					}else {
						throw new AdempiereException("Producto "+line.getM_Product().getName()+" No puede rebajar cantidad mayor a la facturada.\n"
								+ "Cantidad a rebajar:"+line.getQtyEntered().intValue()+" || "
								+ "Cantidad disponible en factura:"+qtyAvailable);
					}

				
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