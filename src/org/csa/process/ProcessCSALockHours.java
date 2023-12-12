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
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.*;


/**
 *	
 *	
 *  @author mfrojas 
 *  @version $Id: ProcessInOut.java $
 */
public class ProcessCSALockHours extends SvrProcess
{
	//private String			p_DocStatus = null;
	
	private String				p_Action = "PR";
	private Timestamp 			p_DateFrom;
	private Timestamp 			p_DateTo;
	private Timestamp 			p_HoursFrom;
	private Timestamp 			p_HoursTo;
	private int					p_Specialty;
	private int 				p_BPartnerMed;
	//mfrojas Se agrega nuevo parametro para recibir mensaje al momento de anular.
	private String 			p_Message = "";
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			
			if (name.equals("Action"))
				p_Action = para[i].getParameterAsString();
			else if (name.equals("DateDoc")) {
				p_DateFrom = para[i].getParameterAsTimestamp();
				p_DateTo = para[i].getParameterToAsTimestamp();
			}else if (name.equals("HoursDoc")) {
				p_HoursFrom = para[i].getParameterAsTimestamp();
				p_HoursTo = para[i].getParameterAsTimestamp();
			}else if(name.equals("C_BPartnerMed_ID")) {
				p_BPartnerMed = para[i].getParameterAsInt();
			}else if (name.equals("HIS_Specialty_ID")) {
				p_Specialty = para[i].getParameterAsInt();
			}
				
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		Timestamp aa = p_HoursFrom;
		String msg = "";
		
			int countSchedulerTime = DB.getSQLValue(get_TrxName(), "select COALESCE(COUNT(HIS_SchedulerTime_ID),0) FROM HIS_SchedulerTime st "
					+ "JOIN HIS_SchedulerDay sd ON st.HIS_SchedulerDay_ID = sd.HIS_SchedulerDay_ID "
					+ "JOIN HIS_Scheduler sc ON sc.HIS_Scheduler_ID = sd.HIS_Scheduler_ID "
					+ "WHERE st.Status = 'DR' and st.isactive = 'Y' "
					+ "AND sd.DateTrx BETWEEN '"+p_DateFrom+"' AND '"+p_DateTo+"' "
					+ "AND st.TimeFrom BETWEEN '"+p_HoursFrom+"' AND '"+p_HoursTo+"' "
					+ "AND sc.C_BPartnerMed_ID = "+p_BPartnerMed+" AND sc.HIS_Specialty_ID = "+p_Specialty);
			
			if(countSchedulerTime> 0) {
				msg =msg+SchedulerTime();
			}
			
			int countAppointment = DB.getSQLValue(get_TrxName(),"SELECT COALESCE(COUNT(HIS_Appointment_ID),0) FROM HIS_Appointment "
					+ "WHERE ReservationDate BETWEEN '"+p_DateFrom+"' AND '"+p_DateTo+"' "
					+ "AND StartTime BETWEEN '"+p_HoursFrom+"' AND 'p_HoursTo' "
					+ "AND C_BPartnerMed_ID = "+p_BPartnerMed+" AND HIS_Specialty_ID = "+p_Specialty);
			
			if(countAppointment> 0) {
				msg = msg+Appointment();
			}
			
			//X_HIS_Appointment happ = new X_HIS_Appointment(getCtx(), 0, get_TrxName());
			//X_HIS_SchedulerTime hst = new X_HIS_SchedulerTime(getCtx(), 0, get_TrxName());
			
			
			
			
		
	   return "Procesado";
	}	//	doIt
	
	public String SchedulerTime() throws Exception {
		String sql = DB.getSQLValueString(get_TrxName(), "select COALESCE(HIS_SchedulerTime_ID,0) FROM HIS_SchedulerTime st "
				+ "JOIN HIS_SchedulerDay sd ON st.HIS_SchedulerDay_ID = sd.HIS_SchedulerDay_ID "
				+ "JOIN HIS_Scheduler sc ON sc.HIS_Scheduler_ID = sd.HIS_Scheduler_ID "
				+ "WHERE st.Status = 'DR' and st.isactive = 'Y' "
				+ "AND sd.DateTrx BETWEEN '"+p_DateFrom+"' AND '"+p_DateTo+"' "
				+ "AND st.TimeFrom BETWEEN '"+p_HoursFrom+"' AND '"+p_HoursTo+"' "
				+ "AND sc.C_BPartnerMed_ID = "+p_BPartnerMed+" AND sc.HIS_Specialty_ID = "+p_Specialty);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				X_HIS_SchedulerTime hst = new X_HIS_SchedulerTime(getCtx(), rs.getInt("HIS_SchedulerTime_ID"), get_TrxName());
				if(hst.get_ID() >0) {
					hst.setStatus("CO");
					hst.saveEx();
				}
			}
			
		}catch (Exception e)
		{
			
		}
		finally
		{
			pstmt.close();
			rs.close();
		}
		
		
		return "Se actualizo el estado de las citas";
	}//SchedulerTime
	
	public String Appointment() throws Exception{
		
		String sql = DB.getSQLValueString(get_TrxName(),"SELECT COALESCE(HIS_Appointment_ID,0) FROM HIS_Appointment "
				+ "WHERE ReservationDate BETWEEN '"+p_DateFrom+"' AND '"+p_DateTo+"' "
				+ "AND StartTime BETWEEN '"+p_HoursFrom+"' AND 'p_HoursTo' "
				+ "AND C_BPartnerMed_ID = "+p_BPartnerMed+" AND HIS_Specialty_ID = "+p_Specialty);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				X_HIS_Appointment happ = new X_HIS_Appointment(getCtx(), rs.getInt("HIS_Appointment_ID"), get_TrxName());
				if(happ.get_ID() >0) {
					happ.setStatus("CO");
					happ.saveEx();
				}
			}
			
		}catch (Exception e)
		{
			
		}
		finally
		{
			pstmt.close();
			rs.close();
		}
		
		return "";
	}//Appointment
}
