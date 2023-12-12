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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.adempiere.core.domains.models.I_M_Requisition;
import org.adempiere.exceptions.DBException;
import org.compiere.model.MClient;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.MWarehouse;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Generates a internal use inventory document with the request parameters
 *
 *  @author mfrojas
 *  @UpdatedBy jleyton
 */
public class ModCSAWarehouseRequest implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCSAWarehouseRequest ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCSAWarehouseRequest.class);
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
			log.info("Initializing global validator: "+this.toString());
		}

		//	Tables to be monitored
		//	Documents to be monitored
		engine.addDocValidate(I_M_Requisition.Table_Name, this);

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *
     */
	public static final String DOCSTATUS_Drafted = "DR";
	public static final String DOCSTATUS_Completed = "CO";
	public static final String DOCSTATUS_InProgress = "IP";
	public static final String DOCSTATUS_Voided = "VO";


	@Override
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);

		return null;
	}	//	modelChange

	/**
	 *	Validate Document.
	 *	Called as first step of DocAction.prepareIt
     *	when you called addDocValidate for the table.
     *	Note that totals, etc. may not be correct.
	 *	@param po persistent object
	 *	@param timing see TIMING_ constants
     *	@return error message or null
	 */
	@Override
	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==I_M_Requisition.Table_ID)
		{
			MRequisition req = (MRequisition) po;
			if(req.getC_DocType().getDocBaseType().equals("RRC"))
			{
				MRequisitionLine[] lines = req.getLines();
				MInventory inv = null;
				MWarehouse wh = MWarehouse.get(req.getCtx(), req.getM_Warehouse_ID(), req.get_TrxName());
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				for (MRequisitionLine line : lines) {


					//mfrojas 20210709
					//Si la cantidad en la solicitud es cero, entonces no se debe copiar la linea al consumo interno.

					if(line.getQty().compareTo(Env.ZERO) == 0)
						continue;

					if (line.getM_Product_ID() >0)
					{
						if(inv == null)
						{
							inv = new MInventory(req.getCtx(),0,req.get_TrxName());
							inv.setAD_Org_ID(req.getAD_Org_ID());
							inv.setM_Warehouse_ID(req.getM_Warehouse_ID());
							inv.setC_DocType_ID(1000065);
							inv.setDescription(req.getDescription());
							inv.saveEx();
							//ininoles se setea nuevo campo en cabecera de m_inventory
							try{
								inv.set_CustomColumn("M_Requisition_ID", req.get_ID());
								inv.saveEx();
							}
							catch (Exception e)
							{
								log.log(Level.SEVERE,"No se pudo asignar la variable M_Requisition_ID en M_Inventory",e);
							}
							//end ininoles

						}
						String sqlLocator = " SELECT ms.qtyonhand,ms.m_attributesetinstance_ID"
								+ "	FROM m_storage ms"
								+ " WHERE M_Product_ID="+line.getM_Product_ID()
								+ " AND M_Locator_ID="+line.getM_Product().getM_Locator_ID()+" AND qtyOnHand > 0"
								+ " ORDER BY ms.qtyonhand ASC";

						//se verifica si producto usa fecha de vencimiento
						if(line.getM_Product().getM_AttributeSet().isGuaranteeDate())
						{
							//se reemplaza sql agregando orden de fecha de vencimiento
							sqlLocator = "SELECT ms.qtyonhand,ma.m_attributesetinstance_ID" +
									" FROM m_storage ms" +
									" INNER JOIN m_attributesetinstance ma " +
									" ON (ms.m_attributesetinstance_ID = ma.m_attributesetinstance_ID)" +
									" WHERE M_Product_ID= " + line.getM_Product_ID()+
									" AND M_Locator_ID= "+line.getM_Product().getM_Locator_ID()+" AND qtyOnHand > 0"+
									" AND guaranteedate > now() " +
									" ORDER BY guaranteedate ASC";
						}
						BigDecimal canTPend = line.getQty();
						BigDecimal cantLine = Env.ZERO;
						PreparedStatement pstmtDet = DB.prepareStatement(sqlLocator, po.get_TrxName());
						ResultSet rsDet = null;
						try
						{
							rsDet = pstmtDet.executeQuery();
							while(rsDet.next() && canTPend.compareTo(Env.ZERO) > 0)
							{
								cantLine = Env.ZERO;
								if(rsDet.getBigDecimal("qtyonhand").compareTo(canTPend) < 0)
									cantLine = rsDet.getBigDecimal("qtyonhand");
								else
									cantLine = canTPend;
								MInventoryLine il = new MInventoryLine(req.getCtx(),0,req.get_TrxName());
								il.setM_Inventory_ID(inv.getM_Inventory_ID() );
								il.setAD_Org_ID(line.getAD_Org_ID());
								il.setM_Product_ID(line.getM_Product_ID());
								//Rodrigo Olivares Hurtado 2023-07-21
								//cambio de m_locator_id por el de locator_id del producto.
								il.setM_Locator_ID(line.getM_Product().getM_Locator_ID());
								//il.setM_Locator_ID(req.get_ValueAsInt("M_Locator_ID"));
								il.setQtyInternalUse(cantLine);
								il.setC_Charge_ID(1000000);
								il.setDescription(line.getDescription());
								il.setQtyBook(rsDet.getBigDecimal("qtyonhand"));
								il.set_CustomColumn("M_RequisitionLine_ID", line.getM_RequisitionLine_ID());
								if(rsDet.getInt("M_Attributesetinstance_ID") > 0)
									il.setM_AttributeSetInstance_ID(rsDet.getInt("M_Attributesetinstance_ID"));
								//se setea cantidad total
								il.set_CustomColumn("qtyTotal", line.getQty());
								il.saveEx();
								canTPend = canTPend.subtract(cantLine);
							}
							rsDet.close();
							pstmtDet.close();
						}
						catch (SQLException e)
						{
							throw new DBException(e, sqlLocator);
						}
						finally
						{
							DB.close(rsDet, pstmtDet);
							rsDet = null; pstmtDet = null;
						}
					}
				}
				if(inv!=null)
				{
					try{
						req.set_CustomColumn("M_Inventory_ID", inv.getM_Inventory_ID());
					}
					catch (Exception e)
					{
						log.log(Level.SEVERE,"No se pudo asignar la variable M_Inventory_ID en M_Requisition",e);
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
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString
}