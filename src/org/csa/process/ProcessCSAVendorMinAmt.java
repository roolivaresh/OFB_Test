package org.csa.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DBException;
import org.compiere.model.MBPartner;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class ProcessCSAVendorMinAmt extends SvrProcess {

	private int p_M_Requisition_ID;
	private int p_M_Order_ID;

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (name.equals("M_Requisition_ID"))
				p_M_Requisition_ID = element.getParameterAsInt();
			else if (name.equals("C_Order_ID"))
				p_M_Order_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {

		String sql="";
		// se determina si la validacion es en la OC o SC
		if (p_M_Order_ID > 0)
			sql = "select co.c_bpartner_id as socio,co.grandtotal as monto from c_order co  "
					+ "where co.c_order_id="+p_M_Order_ID;
		else if (p_M_Requisition_ID > 0)
			sql = "select coalesce(mrl.c_bpartner_id,mr.c_bpartner_id,0) as socio,sum(mrl.LineTotalAmt) as monto "
					+ "from m_requisition mr join m_requisitionline mrl on mr.m_requisition_id=mrl.m_requisition_id "
					+ "where mr.m_requisition_id="+ p_M_Requisition_ID + " group by socio";
		else
			return "No hay documento asociado";

		PreparedStatement psBPList = null;
		ResultSet rsBPList = null;
		String msj="Validacion Compra Minima \n";
		DecimalFormat formatea = new DecimalFormat("###,###.##");
		try {
			log.config("SQL USADO-->"+sql);
			psBPList = DB.prepareStatement(sql, get_TrxName());
			rsBPList = psBPList.executeQuery();
			int bp_id;

			while(rsBPList.next()) {
				bp_id= rsBPList.getInt("socio");
				if(bp_id==0) {
					throw new AdempiereException("Una de las lineas no tiene definido Proveedor.");
				}
				MBPartner bp = new MBPartner(getCtx(),bp_id,get_TrxName());
				if(rsBPList.getInt("monto")<bp.getSalesVolume())
				msj+=bp.getName()+"= Solicitada:$"+formatea.format(rsBPList.getInt("monto")) +" || Minima:$"+formatea.format(bp.getSalesVolume())+" "
						+ "|| Diferencia:$"+formatea.format(bp.getSalesVolume()-rsBPList.getInt("monto"))+"\n";

			}

		} catch (SQLException e) {
			throw new DBException(e,sql);
		} finally {
			DB.close(rsBPList, psBPList);
			psBPList = null;
			rsBPList = null;
		}

		throw new AdempiereException(msj);
	}

}
