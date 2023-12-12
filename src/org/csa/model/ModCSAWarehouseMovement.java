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
import org.compiere.model.MLocator;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
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
 *	Generates a movement document with the request parameters
 *
 *  @author mfrojas
 *  @UpdatedBy jleyton
 */
public class ModCSAWarehouseMovement implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCSAWarehouseMovement ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCSAWarehouseMovement.class);
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
			//Si el tipo de documento base es de solicitud de movimiento de materiales.
			if(req.getC_DocType().getDocBaseType().equals("RMV"))
			{
				MRequisitionLine[] lines = req.getLines();
				MMovement mov = null;

				MInventory inv = null;
				MWarehouse whfrom = MWarehouse.get(req.getCtx(), req.getM_Warehouse_ID(), req.get_TrxName());
				MWarehouse whto = MWarehouse.get(req.getCtx(), req.get_ValueAsInt("M_WarehouseSource_ID"));



				for (MRequisitionLine line : lines) {
					if (line.getM_Product_ID() >0)
					{
						//Rodrigo Olivares Hurtado 2023-08-04
						//Se cambia la ubicacion de la solicitud por el del producto.
						MLocator locfrom = MLocator.get(req.getCtx(),line.getM_Product().getM_Locator_ID());
						MLocator locto = MLocator.get(req.getCtx(), line.getM_Product().getM_Locator_ID());
						if(mov == null)
						{
							mov = new MMovement(req.getCtx(),0,req.get_TrxName());
							mov.setAD_Org_ID(req.getAD_Org_ID());
							mov.setMovementDate(req.getDateDoc());
							mov.setDescription("Generado automaticamente desde solicitud "+req.getDocumentNo());
							mov.set_CustomColumn("M_Warehouse_ID", req.getM_Warehouse_ID());
							mov.set_CustomColumn("M_WarehouseSource_ID", req.get_Value("M_WarehouseSource_ID"));
							mov.saveEx();
							try{
								mov.set_CustomColumn("M_Requisition_ID",req.get_ID());
								mov.saveEx();
							}
							catch (Exception e)
							{
								log.log(Level.SEVERE,"No se pudo asignar la vvariable M_Requisition_ID en M_Movement ", e);
							}
							mov.saveEx();
						}
						//se valida stock antes de crear linea
						if(line.getM_Product_ID()> 0)
						{
							//se busca stock de producto
							String sqlgetstock = "SELECT coalesce(sum(ms.qtyonhand),0) FROM m_Storage ms WHERE "
									+ " ms.m_product_id = "+line.getM_Product_ID()+" AND ms.m_locator_id = "+locfrom.get_ID();
							log.config("producto "+line.getM_Product_ID());
							log.config("locator "+locfrom.get_ID());
							log.config("sql "+sqlgetstock);
							BigDecimal stock = Env.ZERO;
							stock = DB.getSQLValueBD(po.get_TrxName(), sqlgetstock);
							//solo se crean lineas con stock
							//ininoles se usara stock aun cuando no sea todo lo pedido 10-06-2022
							if(stock.compareTo(Env.ZERO) > 0)
							{
								//se pregunta si producto usa fecha de vencimiento
								if(line.getM_Product().getM_AttributeSet().isGuaranteeDate())
								{
									//se reemplaza sql agregando orden de fecha de vencimiento
									String sqlLocator = "SELECT ms.qtyonhand,ma.m_attributesetinstance_ID" +
											" FROM m_storage ms" +
											" INNER JOIN m_attributesetinstance ma " +
											" ON (ms.m_attributesetinstance_ID = ma.m_attributesetinstance_ID)" +
											" WHERE M_Product_ID= " + line.getM_Product_ID()+
											" AND M_Locator_ID= "+line.getM_Product().getM_Locator_ID()+" AND qtyOnHand > 0"+
											" AND guaranteedate > now() " +
											" ORDER BY guaranteedate ASC";

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
											MMovementLine mline = new MMovementLine(req.getCtx(),0,req.get_TrxName());
											mline.setM_Movement_ID(mov.getM_Movement_ID());
											mline.setAD_Org_ID(mov.getAD_Org_ID());
											mline.setM_Product_ID(line.getM_Product_ID());
											mline.setMovementQty(cantLine);
											mline.setM_Locator_ID(locfrom.get_ID());
											mline.setM_LocatorTo_ID(locto.get_ID());
											mline.set_CustomColumn("QtyBook", rsDet.getBigDecimal("qtyonhand"));
											if(rsDet.getInt("M_Attributesetinstance_ID") > 0)
												mline.setM_AttributeSetInstance_ID(rsDet.getInt("M_Attributesetinstance_ID"));
											mline.setDescription(line.getDescription());
											mline.set_CustomColumn("M_RequisitionLine_ID", line.getM_RequisitionLine_ID());
											mline.saveEx();
											canTPend = canTPend.subtract(cantLine);
										}
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
								else
								{
									MMovementLine mline = new MMovementLine(req.getCtx(),0,req.get_TrxName());
									mline.setM_Movement_ID(mov.getM_Movement_ID());
									mline.setAD_Org_ID(mov.getAD_Org_ID());
									mline.setM_Product_ID(line.getM_Product_ID());
									if(line.getQty().compareTo(stock) > 0)
										mline.setMovementQty(stock);
									else
										mline.setMovementQty(line.getQty());
									mline.setM_Locator_ID(locfrom.get_ID());
									mline.setM_LocatorTo_ID(locto.get_ID());

									if(line.getM_Product().getM_AttributeSet_ID() > 0)
									{
										int attributesetinstance = DB.getSQLValue(po.get_TrxName(), "SELECT coalesce(max(m_attributesetinstance_id),0) "
												+ " FROM m_attributesetinstance where m_attributeset_Id = "+line.getM_Product().getM_AttributeSet_ID()+" AND "
												+ " Guaranteedate in (SELECT min(guaranteedate) from m_attributesetinstance where guaranteedate > now() AND "
												+ " m_attributesetinstance_id in (select coalesce(m_attributesetinstance_id,0) from m_Transaction "
												+ " WHERE m_product_id = "+line.getM_Product_ID()+") ) AND "
												+ " M_attributesetinstance_id in (select coalesce(m_Attributesetinstance_id,0) from m_Transaction "
												+ " where m_product_id = "+line.getM_Product_ID()+")");

										mline.setM_AttributeSetInstance_ID(attributesetinstance);
									}
									mline.setDescription(line.getDescription());
									mline.set_CustomColumn("M_RequisitionLine_ID", line.getM_RequisitionLine_ID());

									mline.saveEx();
								}
							}
						}
					}
				}
				if(mov!=null)
				{
					try{
						req.set_CustomColumn("M_Movement_ID", mov.getM_Movement_ID());
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