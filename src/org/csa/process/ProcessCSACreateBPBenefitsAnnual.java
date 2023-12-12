/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.csa.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.X_C_BPBenefit;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * Create monthly partner benefit records
 *
 * @author SColoma
 * @version 1.0
 */
public class ProcessCSACreateBPBenefitsAnnual extends SvrProcess {
	// private String p_DocStatus = null;
	private int p_C_BPartner_ID = 0;
	private BigDecimal p_Amt = Env.ZERO;
	private int p_C_Period_ID = 0;
	private int p_C_Period_ID_To = 0;
	private int p_C_Job_ID = 0;

	/**
	 * Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (name.equals("Amt"))
				p_Amt = element.getParameterAsBigDecimal();
			else if (name.equals("C_Period_ID")) {
				p_C_Period_ID = element.getParameterAsInt();
				p_C_Period_ID_To = element.getParameter_ToAsInt();
			} else if(name.equals("C_Job_ID")) {
				p_C_Job_ID = element.getParameterAsInt();
			} else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_BPartner_ID = getRecord_ID();

	} // prepare

	/**
	 * Perrform process.
	 *
	 * @return Message (clear text)
	 * @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception {
		// se valida que haya un rango de periodo
		if (p_C_Period_ID == 0 || p_C_Period_ID_To == 0)
			return "Sin rango de Periodos";
		int count = 0, count_u = 0, count_bp=0;

		// se discrimina si se realiza para varios o solo 1 funcionario
		if(p_C_BPartner_ID==0) {
			String sql;

			// se determina si se aplica a funcionarios sin cargo o con cargo especifico
			if(p_C_Job_ID==0)
				sql ="select cbp.c_bpartner_id from ad_user au join c_bpartner cbp on au.c_bpartner_id =cbp.c_bpartner_id"
						+ " where cbp.isemployee='Y' and au.c_job_id is null and cbp.ad_client_id="+getAD_Client_ID();
			else
				sql ="select cbp.c_bpartner_id from ad_user au join c_bpartner cbp on au.c_bpartner_id =cbp.c_bpartner_id"
						+ " where cbp.isemployee='Y' and cbp.ad_client_id="+getAD_Client_ID()+" and au.c_job_id="+p_C_Job_ID;
			log.config("SQL Beneficio Funcionarios-->"+sql);
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				rs = pstmt.executeQuery();
				while(rs.next()){

						int created = DB.getSQLValue(get_TrxName(),
								"select coalesce(max(c_bpbenefit_id),0) from c_bpbenefit where c_bpartner_id=" + rs.getInt("c_bpartner_id")
								+ " and c_periodto_id is null and c_period_id="+p_C_Period_ID+"  and C_PeriodTo_ID="+p_C_Period_ID_To);
						//se crea o actualiza el registro de beneficios
						X_C_BPBenefit benef = new X_C_BPBenefit(getCtx(), created, get_TrxName());
						benef.setAD_Org_ID(0);
						benef.setTotalAmt(p_Amt);
						benef.setAvailableAmt(p_Amt);
						benef.setAllocatedAmt(Env.ZERO);
						//solo se llena el socio y periodo cuando se crea un beneficio nuevo
						if (created == 0) {
							benef.setC_BPartner_ID(rs.getInt("c_bpartner_id"));
							benef.setC_Period_ID(p_C_Period_ID);
							benef.set_CustomColumn("C_PeriodTo_ID", p_C_Period_ID_To);
							count++;
						} else
							count_u++;

						benef.saveEx();

					count_bp++;

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


		}else {
			//se genera ciclo para recorrer los periodos del rango
			for (int i = p_C_Period_ID; i <= p_C_Period_ID_To; i++) {
				int created = DB.getSQLValue(get_TrxName(),
						"select coalesce(max(c_bpbenefit_id),0) from c_bpbenefit where c_bpartner_id=" + p_C_BPartner_ID
						+ " and c_periodto_id is null and c_period_id=" +p_C_Period_ID+"  and C_PeriodTo_ID="+p_C_Period_ID_To);
				//se crea o actualiza el registro de beneficios
				X_C_BPBenefit benef = new X_C_BPBenefit(getCtx(), created, get_TrxName());
				benef.setAD_Org_ID(0);
				benef.setTotalAmt(p_Amt);
				benef.setAvailableAmt(p_Amt);
				benef.setAllocatedAmt(Env.ZERO);
				//solo se llena el socio y periodo cuando se crea un beneficio nuevo
				if (created == 0) {
					benef.setC_BPartner_ID(p_C_BPartner_ID);
					benef.setC_Period_ID(p_C_Period_ID);
					benef.set_CustomColumn("C_PeriodTo_ID", p_C_Period_ID_To);
					count++;
				} else
					count_u++;
				benef.saveEx();
			}
			count_bp=1;
		}

		return "Generados:" + count + " || Actualizados:" + count_u+" || Para "+count_bp+" funcionarios";
	} // doIt
}
