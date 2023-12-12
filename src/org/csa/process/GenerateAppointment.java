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


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.adempiere.exceptions.DBException;
import org.compiere.model.X_HIS_Appointment;
import org.compiere.model.X_HIS_Scheduler;
import org.compiere.model.X_HIS_SchedulerDay;
import org.compiere.model.X_HIS_SchedulerTime;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;




/**
 *
 *
 *  @author Rodrigo Olivares Hurtado
 *  @version $Id: GenerateAppointment.java,v 1.2 2023/08/17 00:51:01 jjanke Exp $
 *
 *  Generate new Appointment or for CSA
 */


public class GenerateAppointment extends SvrProcess {

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{

	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		String msgnew = "No se agrego una nueva cita";
		//Se busca si hay nuevos registros de pacientes en la tabla temporal
		int count = DB.getSQLValue(get_TrxName(), "SELECT coalesce(count(I_AppointmentWS_ID),0)" +
				" FROM I_AppointmentWS where i_isimported = 'N' and processed='N' ");


		if(count > 0) {
			//En el caso de que haya datos, comproramos si no estan registrado en el sistema
			msgnew =GenerateNewAppointment();
		}

		return msgnew;
	}	//	doIt
	public String GenerateNewAppointment()
	{
		//buscamos los pacientes que no esten registrados en el sistema
		String sqlAppointment = "SELECT I_AppointmentWS_ID,his_schedulertime_id,rut " +
				" FROM I_AppointmentWS where i_isimported = 'N' and processed='N' ";
		int count = 0;

		log.config("sql validate = "+sqlAppointment);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlAppointment, get_TrxName());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				//Registramos los datos de la tabla temporal en la tabla HIS_Appointment
				X_HIS_Appointment app = new X_HIS_Appointment(getCtx(),0,get_TrxName());
				int schedulerTimeID = rs.getInt("his_schedulertime_id");
				if(schedulerTimeID > 0) {

					int bpartnerid = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(MAX(C_BPARTNER_ID),0) FROM C_BPARTNER WHERE VALUE||'-'||VALUEVALIDATOR = '"+rs.getString("rut") +"'");
					if(bpartnerid > 0) {
						X_HIS_SchedulerTime st = new X_HIS_SchedulerTime(getCtx(),schedulerTimeID,get_TrxName());
						X_HIS_SchedulerDay sd = new X_HIS_SchedulerDay(getCtx(),st.getHIS_SchedulerDay_ID(),get_TrxName());
						X_HIS_Scheduler ss = new X_HIS_Scheduler(getCtx(),sd.getHIS_Scheduler_ID(),get_TrxName());

						app.setHIS_SchedulerTime_ID(st.get_ID());
						app.setC_BPartner_ID(bpartnerid);
						app.setC_BPartner_ID(bpartnerid);
						app.setStartTime(ss.getDateFrom());
						app.setHIS_Specialty_ID(ss.getHis_Specialty_ID());
						app.setc_bpartnermed_id(ss.getC_BPartnerMed_ID());
						app.setStatus(st.getStatus());
						app.save();
						count = count+1;
						//Actualizamos la tabla de importación para que no tome en consideración este registro.
						DB.executeUpdate("UPDATE I_AppointmentWS SET i_isimported = 'Y', processed='Y',his_appointment_id = " +app.get_ID()
								+ "WHERE I_AppointmentWS_ID = "+rs.getInt("I_AppointmentWS_ID"),get_TrxName());
					}else {

					}

				}


			}

		}
		catch (SQLException e)
		{
			throw new DBException(e, sqlAppointment);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}




		return "Se agrego: "+count +" nueva/as cita/as";
	} //GenerateNewBPartner


}
