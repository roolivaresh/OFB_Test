package org.csa.model;

import java.math.BigDecimal;

import org.adempiere.core.domains.models.I_C_Order;
import org.adempiere.core.domains.models.I_M_RequisitionLine;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_BPBenefit;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class ModCSAValidCustumerReturn implements ModelValidator {

	public ModCSAValidCustumerReturn() {
		super();
	}

	/** Logger */
	private static CLogger log = CLogger.getCLogger(ModCSAValidCustumerReturn.class);
	/** Client */
	private int m_AD_Client_ID = -1;

	/**
	 * Initialize Validation
	 *
	 * @param engine validation engine
	 * @param client client
	 */
	@Override
	public void initialize(ModelValidationEngine engine, MClient client) {
		// client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		} else {
			log.info("Initializing Model Price Validator: " + this.toString());
		}

		// Tables to be monitored
		engine.addModelChange(MOrderLine.Table_Name, this);
		engine.addDocValidate(I_C_Order.Table_Name, this);
	}

	@Override
	public String modelChange(PO po, int type) throws Exception {
		log.info(po.get_TableName() + " Type: "+type);
		if(type == TYPE_AFTER_CHANGE && po.is_ValueChanged("M_Product_ID") && po.get_Table_ID() == MOrderLine.Table_ID) {
			validProductReturn(po);
		}
		if(type == TYPE_AFTER_NEW && po.get_Table_ID() == MOrderLine.Table_ID) {
			validProductReturn(po);
		}
		return null;
	}

	@Override
	public String docValidate(PO po, int timing) {
		log.info(po.get_TableName() + " Timing: " + timing);
		if (timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID() == I_C_Order.Table_ID) {
			MOrder ov = (MOrder) po;
			if (ov.getC_DocTypeTarget().getC_DocTypeInvoice().getDocBaseType().equals("ARC")) {
				int inv_id = ov.get_ValueAsInt("C_Invoice_ID");
				if (inv_id == 0)
					throw new AdempiereException("Se debe asociar la factura a rebajar.");
				MInvoice inv = new MInvoice(po.getCtx(), inv_id, po.get_TrxName());
				MOrderLine ovlines[] = ov.getLines();

				for (MOrderLine ovl : ovlines) {
					String sql = "select coalesce(max(m_product_id),0)"
							+ " from c_invoiceline cil "
							+ "where cil.c_invoice_id="+ inv_id +""
							+ "and cil.m_product_id=" + ovl.getM_Product_ID();
					int m_product_id = DB.getSQLValue(po.get_TrxName(),sql);
					// se valida si el producto existe en la factura a rebajar
					if(m_product_id==0)
						throw new AdempiereException("Producto "+ovl.getM_Product().getName()+" no existe "
								+ "en la factura "+inv.getDocumentNo());
					sql="select sum(cil.qtyentered)-coalesce((select sum(col.qtyentered) "
						+"from c_order co join c_orderline col on (col.c_order_id=co.c_order_id and col.m_product_id=cil.m_product_id) "
					    +"where  co.c_invoice_id=cil.c_invoice_id and co.docstatus='CO'),0) as qty "
					    +"from c_invoiceline cil "
					    + "where cil.c_invoice_id="+ inv_id + " "
					    +"and cil.m_product_id="+ovl.getM_Product_ID() +" "
					    +"group by cil.c_invoice_id,cil.m_product_id";
					log.config("SQL CANTIDAD DSIPONIBLE==>"+sql);
					int qtyAvailable = DB.getSQLValue(po.get_TrxName(), sql);
					// se valida si el producto tiene cantidad acumulada para rebajar
					if(qtyAvailable>=ovl.getQtyEntered().intValue()) {


					}else {
						throw new AdempiereException("Producto "+ovl.getM_Product().getName()+" No puede rebajar cantidad mayor a la facturada.\n"
								+ "Cantidad a rebajar:"+ovl.getQtyEntered().intValue()+" || "
								+ "Cantidad disponible en factura:"+qtyAvailable);
					}

				}

				// 20231004 JLeyton se debe devolver saldo al beneficio funcionario
				//obtenemos la orden original que desconto los saldos al beneficio
				MOrder order = new MOrder(po.getCtx(),inv.getC_Order_ID(),po.get_TrxName());
				//se setea la misma lista de precio usada en la orden original
				ov.setM_PriceList_ID(order.getM_PriceList_ID());
				ov.set_CustomColumn("M_PriceList_Version_ID", order.get_ValueAsInt("M_PriceList_Version_ID"));
				ov.saveEx();
				// obtenemos el periodo al cual se abonaran los saldos
				int c_period_id = DB.getSQLValue(po.get_TrxName(),
						"select c_period_id from c_period cp join c_year cy "
								+ "on cy.c_year_id=cp.c_year_id where cy.c_calendar_id=1000000 and '" + order.getDateOrdered()
								+ "' between cp.startdate and cp.enddate;");
				// abonamos primero el saldo anual si fuera el caso
				if(order.get_Value("isApply").equals(true)) {

					int ben = DB.getSQLValue(po.get_TrxName(),
							"select coalesce(max(c_bpbenefit_id),0) from c_bpbenefit where c_bpartner_id=" + order.getC_BPartner_ID()
							+ " and "+c_period_id+" between c_period_id and c_periodto_id");
					if(ben>0) {
						X_C_BPBenefit benefit = new X_C_BPBenefit(po.getCtx(), ben, po.get_TrxName());
						benefit.setAvailableAmt(benefit.getAvailableAmt().add(ov.getGrandTotal()));
						benefit.setAllocatedAmt(benefit.getAllocatedAmt().subtract(ov.getGrandTotal()));
						benefit.saveEx();
					}
				}
				// ahora se abona el beneficio mensual
				if(order.getPaymentRule().equals("P")) {
					int ben = DB.getSQLValue(po.get_TrxName(),
							"select coalesce(max(c_bpbenefit_id),0) from c_bpbenefit where c_bpartner_id=" + order.getC_BPartner_ID()
							+ " and c_periodto_id is null and c_period_id=" + c_period_id);
					if(ben>0) {
						X_C_BPBenefit benefit = new X_C_BPBenefit(po.getCtx(), ben, po.get_TrxName());
						benefit.setAvailableAmt(benefit.getAvailableAmt().add(ov.getGrandTotal()));
						benefit.setAllocatedAmt(benefit.getAllocatedAmt().subtract(ov.getGrandTotal()));
						benefit.saveEx();
					}
				}
			}
		}
		// devolver saldos al momento de anular devolucion
		if (timing == TIMING_BEFORE_VOID && po.get_Table_ID() == I_C_Order.Table_ID) {
			MOrder ov = (MOrder) po;
			if (ov.getC_DocType().getC_DocTypeInvoice().getDocBaseType().equals("ARC")) {
				int inv_id = ov.get_ValueAsInt("C_Invoice_ID");
				if (inv_id == 0)
					throw new AdempiereException("Se debe asociar la factura a rebajar.");
				MInvoice inv = new MInvoice(po.getCtx(), inv_id, po.get_TrxName());
				// 20231004 JLeyton se debe devolver saldo al beneficio funcionario
				//obtenemos la orden original que desconto los saldos al beneficio
				MOrder order = new MOrder(po.getCtx(),inv.getC_Order_ID(),po.get_TrxName());
				// obtenemos el periodo al cual se abonaran los saldos
				int c_period_id = DB.getSQLValue(po.get_TrxName(),
						"select c_period_id from c_period cp join c_year cy "
								+ "on cy.c_year_id=cp.c_year_id where cy.c_calendar_id=1000000 and '" + order.getDateOrdered()
								+ "' between cp.startdate and cp.enddate;");
				// abonamos primero el saldo anual si fuera el caso
				if(order.get_Value("isApply").equals(true)) {

					int ben = DB.getSQLValue(po.get_TrxName(),
							"select coalesce(max(c_bpbenefit_id),0) from c_bpbenefit where c_bpartner_id=" + order.getC_BPartner_ID()
							+ " and "+c_period_id+" between c_period_id and c_periodto_id");
					if(ben>0) {
						X_C_BPBenefit benefit = new X_C_BPBenefit(po.getCtx(), ben, po.get_TrxName());
						benefit.setAvailableAmt(benefit.getAvailableAmt().subtract(ov.getGrandTotal()));
						benefit.setAllocatedAmt(benefit.getAllocatedAmt().add(ov.getGrandTotal()));
						benefit.saveEx();
					}
				}
				// ahora se abona el beneficio mensual
				if(order.getPaymentRule().equals("P")) {
					int ben = DB.getSQLValue(po.get_TrxName(),
							"select coalesce(max(c_bpbenefit_id),0) from c_bpbenefit where c_bpartner_id=" + order.getC_BPartner_ID()
							+ " and c_periodto_id is null and c_period_id=" + c_period_id);
					if(ben>0) {
						X_C_BPBenefit benefit = new X_C_BPBenefit(po.getCtx(), ben, po.get_TrxName());
						benefit.setAvailableAmt(benefit.getAvailableAmt().subtract(ov.getGrandTotal()));
						benefit.setAllocatedAmt(benefit.getAllocatedAmt().add(ov.getGrandTotal()));
						benefit.saveEx();
					}
				}
			}
		}
		
		
		
		
		return null;
	}

	/**
	 * User Login. Called when preferences are set
	 *
	 * @param AD_Org_ID  org
	 * @param AD_Role_ID role
	 * @param AD_User_ID user
	 * @return error message or null
	 */
	@Override
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	} // login

	/**
	 * Get Client to be monitored
	 *
	 * @return AD_Client_ID client
	 */
	@Override
	public int getAD_Client_ID() {
		return m_AD_Client_ID;
	} // getAD_Client_ID

	/**
	 * String Representation
	 *
	 * @return info
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("ModelPrice");
		return sb.toString();
	} // toString

	
	/**
	 * Devuelve error si el producto asociado no se encuentra en el documento a rebajar
	 * @param po
	 * @return Exception
	 */
	public Exception validProductReturn(PO po) {
		MOrderLine ovl = (MOrderLine) po;
		MOrder ov = (MOrder)ovl.getC_Order();
		if (ov.getC_DocTypeTarget().getC_DocTypeInvoice().getDocBaseType().equals("ARC")) {
			int inv_id = ov.get_ValueAsInt("C_Invoice_ID");
			if (inv_id == 0)
				throw new AdempiereException("Devolucion debe asociar la factura a rebajar.");
			//se obtiene documento referencia
			MInvoice inv = new MInvoice(po.getCtx(), inv_id, po.get_TrxName());
			//se obtiene orden original
			MOrder order = new MOrder(po.getCtx(),inv.getC_Order_ID(),po.get_TrxName());
			//se setea la misma lista de precio usada en la orden original
			ov.setM_PriceList_ID(order.getM_PriceList_ID());
			ov.set_CustomColumn("M_PriceList_Version_ID", order.get_ValueAsInt("M_PriceList_Version_ID"));
			ov.saveEx();
			
			int nProd= DB.getSQLValue(po.get_TrxName(), "select count(*) from c_invoiceline"
					+ " where c_invoice_id=? and m_product_id=?",inv_id,ovl.getM_Product_ID());
			if(nProd==0) {
				throw new AdempiereException("Producto "+ovl.getM_Product().getName()+" no se "
						+ "encuentra en factura asociada.");
			}
		}
		return null;
	}
}
