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

import org.adempiere.core.domains.models.I_M_Requisition;
import org.compiere.model.MClient;
import org.compiere.model.MInventory;
import org.compiere.model.MLocator;
import org.compiere.model.MMovement;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.MWarehouse;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

/**
 *	Validator default OFB
 *
 *  @author Rodrigo Olivares Hurtado
 */
public class ModCSAComSC implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCSAComSC ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCSAComSC.class);
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
		if((timing == TIMING_BEFORE_COMPLETE || timing == TIMING_BEFORE_PREPARE) && po.get_Table_ID()==I_M_Requisition.Table_ID)
		{
			MRequisition req = (MRequisition) po;
			//Si el tipo de documento base es de solicitud de movimiento de materiales.
				MRequisitionLine[] lines = req.getLines();
				MMovement mov = null;

				MInventory inv = null;
				MWarehouse whfrom = MWarehouse.get(req.getCtx(), req.getM_Warehouse_ID(), req.get_TrxName());
				MWarehouse whto = MWarehouse.get(req.getCtx(), req.get_ValueAsInt("M_WarehouseSource_ID"));

				MLocator locfrom = MLocator.get(req.getCtx(), req.get_ValueAsInt("M_Locator_ID"));
				MLocator locto = MLocator.get(req.getCtx(), req.get_ValueAsInt("M_LocatorTo_ID"));

				if(req.getM_Warehouse_ID() == 0 || Integer.valueOf(req.getM_Warehouse_ID()) == null) {
					return "Se debe registrar una bodega";
				}

				for (MRequisitionLine line : lines) {
					if (req.getC_DocType().getC_DocBaseType_ID() == 50019) {
							if (line.getM_Product_ID() >0 )
							{

								if(line.getQty() == null || Integer.valueOf(line.getQty().intValue())== 0) {
									return "Se debe ingresar cantidad para el producto: "+ line.getM_Product().getName()+ " en la linea: " +line.getLine();

								}
								//Rodrigo Olivares Hurtado 2023-11-21 se ajusta validacion para que reconozcan decimales
								if(line.getPriceActual() == null ||line.getPriceActual().compareTo(Env.ZERO) ==0 ) {
									return "Se debe ingresar un valor para el producto: "+ line.getM_Product().getName()+ " en la linea: " +line.getLine();

								}


							} else if (line.getC_Charge_ID() > 0) {
								if(line.getQty() == null || Integer.valueOf(line.getQty().intValue())== 0) {
									return "Se debe ingresar cantidad para el cargo en la linea: " +line.getLine();
								}
								//Rodrigo Olivares Hurtado 2023-11-21 se ajusta validacion para que reconozcan decimales
								if(line.getPriceActual() == null ||line.getPriceActual().compareTo(Env.ZERO) ==0) {
									return "Se debe ingresar un valor para el cargo en la linea: " +line.getLine();

								}


							}
							else {
								return "Se debe ingresar un producto o un cargo para la linea: "+line.getLine();
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