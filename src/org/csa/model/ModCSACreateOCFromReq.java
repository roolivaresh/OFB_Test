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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.adempiere.core.domains.models.I_M_Requisition;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DBException;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProductPrice;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 *	Create OC from Requisition
 *
 *  @author jleyton
 */

public class ModCSACreateOCFromReq implements ModelValidator{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCSACreateOCFromReq ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCSACreateOCFromReq.class);
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
			log.info("Initializing Model Create OC: "+this.toString());
		}

		//	Tables to be monitored
		engine.addDocValidate(I_M_Requisition.Table_Name, this);

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *
     */
	@Override
	public String modelChange (PO po, int type) throws Exception
	{
		//log.info(po.get_TableName() + " Type: "+type);

		return null;
	}	//	modelChange

	@Override
	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==I_M_Requisition.Table_ID) {
			MRequisition req = (MRequisition)po;
			if(req.getC_DocType().getDocBaseType().equals("POR")){
				MRequisitionLine[] rLines = req.getLines();
				String sql = "SELECT coalesce(mrl.C_BPartner_ID, mr.C_BPartner_ID,0) as bp_id "
						+ "FROM M_Requisition mr join M_RequisitionLine mrl on mrl.M_Requisition_ID="
						+ "mr.M_Requisition_ID where mr.M_Requisition_ID="+req.get_ID()+" group by bp_id order by bp_id";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try
				{
					pstmt = DB.prepareStatement(sql, po.get_TrxName());
					rs = pstmt.executeQuery();
					while(rs.next()){
						if(rs.getInt("bp_id")!=0) {
							MOrder oc = new MOrder(po.getCtx(),0,po.get_TrxName());
							MBPartner bp = new MBPartner(po.getCtx(),rs.getInt("bp_id"),po.get_TrxName());
							oc.setC_BPartner_ID(bp.get_ID());
							//se asigna primera direccion valida que encuentre
							int bpLocationID = bp.getPrimaryC_BPartner_Location_ID();
							//si no encuentra una direccion envia mensaje de error
							if(bpLocationID==0)
								throw new AdempiereException("El proveedor "+bp.getName()+" no tiene activa o creada una direccion");

							oc.setAD_Org_ID(req.getAD_Org_ID());
							oc.setC_BPartner_Location_ID(bpLocationID);
							oc.setC_DocTypeTarget_ID(1000016);
							Timestamp fechaHoy = new Timestamp(System.currentTimeMillis());
							oc.setDateOrdered(fechaHoy);
							oc.setDateAcct(fechaHoy);
							oc.setDatePromised(fechaHoy);
							oc.setM_Warehouse_ID(req.getM_Warehouse_ID());
							oc.setC_Currency_ID(req.getC_Currency_ID());
							oc.setIsSOTrx(false);
							oc.setM_PriceList_ID(bp.getPO_PriceList_ID()==0 ? req.getM_PriceList_ID(): bp.getPO_PriceList_ID());
							oc.setSalesRep_ID(req.get_ValueAsInt("SalesRep_ID"));
							oc.setDescription("**Generado desde solicitud N�"+req.getDocumentNo()+"**");
							oc.set_ValueOfColumn("M_Requisition_ID",req.get_ID());
							oc.set_ValueOfColumn("AD_OrgRef_ID",req.get_Value("AD_OrgRef_ID"));
							oc.save();
							for (MRequisitionLine reql : rLines) {
								//se valida y actualiza lista de precios con los nuevos valores
								// solo si es producto
								if(reql.getM_Product_ID()>0)
									validPriceList(reql, oc);

								int bpVendor = (reql.getC_BPartner_ID()>0)?reql.getC_BPartner_ID():req.get_ValueAsInt("C_BPartner_ID");
								if(rs.getInt("bp_id")==bpVendor){
									MOrderLine ocl = new MOrderLine(oc);
									ocl.setAD_Org_ID(oc.getAD_Org_ID());
									ocl.setM_Product_ID(reql.getM_Product_ID());
									ocl.setC_Charge_ID(reql.getC_Charge_ID());
									ocl.setQtyEntered(reql.getQty());
									ocl.setQtyOrdered(reql.getQty());
									if(reql.getM_Product_ID()>0)
										ocl.setC_UOM_ID(reql.getM_Product().getC_UOM_ID());
									else
										ocl.setC_UOM_ID(100);

									ocl.setPriceEntered(reql.getPriceActual());
									ocl.setPriceActual(reql.getPriceActual());
									ocl.setPriceList(reql.getPriceActual());
									ocl.setC_Tax_ID(reql.getC_Tax_ID());
									ocl.setLineNetAmt(reql.getLineNetAmt());
									ocl.setM_RequisitionLine_ID(reql.get_ID());
									ocl.setDescription(reql.getDescription());
									ocl.save();
									reql.setC_OrderLine_ID(ocl.get_ID());
									reql.save();
								}
							}
						}else
							throw new AdempiereException("Una de las lineas de la solicitud no tiene proveedor");
					}
				}
				catch (SQLException e)
				{
					throw new DBException(e, sql);
				}
				finally
				{
					DB.close(rs, pstmt);
					rs = null; pstmt = null;
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
	 * Actualiza los montos en la lista de precio activa.
	 * Si no encuentra el producto lo crea en la versi�n de lista de precios activa.
	 * Si no hay version de lista de precios activas y validas arroja mensaje de error
	 * @param reql Objeto tipo Linea de la solicitud de compra
	 * @param oc Objeto tipo Orden de compra
	 */
	public void validPriceList(MRequisitionLine reql, MOrder oc)
	{
		int ppID = DB.getSQLValue(reql.get_TrxName(),"SELECT coalesce(max(m_productprice_id),0) "
				+ "from m_pricelist_version mplv join m_productprice mpp on "
				+ "mpp.m_pricelist_version_id=mplv.m_pricelist_version_id "
				+ "where mplv.isactive='Y' and mplv.ValidFrom<'"+oc.getDateOrdered().toString()+"' and "
				+ "mpp.m_product_id="+reql.getM_Product_ID()+" "
				+ "and mplv.m_pricelist_id="+oc.getM_PriceList_ID());
		MProductPrice pp = new MProductPrice(reql.getCtx(),ppID,reql.get_TrxName());
		if(ppID==0) {
			int plvID = DB.getSQLValue(reql.get_TrxName(),"SELECT coalesce(max(mplv.m_pricelist_version_id),0) "
					+ "from m_pricelist_version mplv join m_productprice mpp on "
					+ "mpp.m_pricelist_version_id=mplv.m_pricelist_version_id "
					+ "where mplv.isactive='Y' and mplv.ValidFrom<'"+oc.getDateOrdered().toString()+"' "
					+ "and mplv.m_pricelist_id="+oc.getM_PriceList_ID());
			if(plvID==0)
				throw new AdempiereException("No existe version de lista de precio valida para los productos");
			pp.setM_PriceList_Version_ID(plvID);
			pp.setM_Product_ID(reql.getM_Product_ID());
		}
		pp.setPriceStd(reql.getPriceActual());
		pp.setPriceLimit(reql.getPriceActual());
		pp.setPriceList(reql.getPriceActual());
		pp.save();
	}
}
