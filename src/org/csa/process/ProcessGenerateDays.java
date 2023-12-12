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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.X_HIS_Scheduler;
import org.compiere.model.X_HIS_SchedulerDay;
import org.compiere.model.X_HIS_SchedulerTemplateTime;
import org.compiere.model.X_HIS_SchedulerTime;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
/**
 *
 *
 *  @author SColoma
 *  @version $Id: ProcessCCShiftChange.java $
 */

public class ProcessGenerateDays extends SvrProcess
{
	/**
	 *  Prepare - e.g., get Parameters.
	 */

	private int 			m_Record_ID = 0;


	@Override
	protected void prepare()
	{
		m_Record_ID = getRecord_ID();

	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		X_HIS_Scheduler horario = new X_HIS_Scheduler(Env.getCtx(),m_Record_ID,get_TrxName());
		if(horario.getHIS_Scheduler_ID()<=0)
			throw new IllegalArgumentException("El horario no posee plantilla");
/*		if(horario.getStatus().equals("CO"))
			throw new IllegalArgumentException("El horario ya se encuentra completo, no puede ser modificado");
		if(buscarReserva(horario.getMED_Schedule_ID())>0)
			throw new IllegalArgumentException("No puede modificar el horario ya que existen horas con reservas");*/
		//asignar box
		if(horario.get_ValueAsInt("S_Resource_ID") <= 0)
			throw new IllegalArgumentException("Debe asociar un box de atencion");



		X_HIS_SchedulerTemplateTime[] plantillas = getPlantillas(horario.getHIS_SchedulerTemplate_ID() );

		Timestamp dia = horario.getDateFrom();
		int boxatencion = horario.getS_Resource_ID();
		while(dia.compareTo(horario.getDateTo())<=0)
		{
			int pos = getPlantilla (plantillas, dia );
			if(pos<0)
			{
				dia=  TimeUtil.getNextDay(dia);
				continue;
			}

			X_HIS_SchedulerDay newDia = new X_HIS_SchedulerDay(Env.getCtx(),0,get_TrxName());
			newDia.setWeekDay(plantillas[pos].getWeekDay());
			newDia.setDateTrx(dia);
			newDia.setCycleA2(plantillas[pos].getCycleA2());
			newDia.setCycleA3(plantillas[pos].getCycleA3());
			newDia.setCycleB2(plantillas[pos].getCycleB2());
			newDia.setCycleB3(plantillas[pos].getCycleB3());
			newDia.setHIS_Scheduler_ID(horario.getHIS_Scheduler_ID());
			newDia.setAD_Org_ID(newDia.getAD_Org_ID());
			newDia.save();

			// Generacion de las horas de los dias
			if(newDia.getCycleA2()!=null && newDia.getCycleA2()!=null)
			{
				Calendar a1= Calendar.getInstance();
				a1.setTimeInMillis(newDia.getCycleA2().getTime());

				Calendar a2= Calendar.getInstance();
				a2.setTimeInMillis(newDia.getCycleA3().getTime());

				Calendar desde1= Calendar.getInstance();
				desde1.setTimeInMillis(newDia.getDateTrx().getTime());
				desde1.set(Calendar.HOUR_OF_DAY, a1.get(Calendar.HOUR_OF_DAY));
				desde1.set(Calendar.MINUTE, a1.get(Calendar.MINUTE));

				Calendar hasta1= Calendar.getInstance();
				hasta1.setTimeInMillis(newDia.getDateTrx().getTime());
				hasta1.set(Calendar.HOUR_OF_DAY, a2.get(Calendar.HOUR_OF_DAY));
				hasta1.set(Calendar.MINUTE, a2.get(Calendar.MINUTE));


				while(desde1.compareTo(hasta1)<=0)
				{
					String sqlvalidate = "SELECT coalesce(max(C_BPartnerMed_ID),0) from HIS_Scheduler " +
							" WHERE C_BPartnerMed_ID != "+horario.getC_BPartnerMed_ID()+
							" AND HIS_Scheduler_ID in (SELECT HIS_Scheduler_ID "
							+ "							from HIS_SchedulerDay "
							+ "							where HIS_SchedulerDay_ID in (select HIS_SchedulerDay_ID "
							+ "							   								from HIS_SchedulerTime "
							+ "							   								where TimeFrom = '"+new Timestamp(desde1.getTimeInMillis())+"' "
							+ " 														AND S_Resource_ID = "+boxatencion+"))";
					log.config(sqlvalidate);
					int medinconflict = DB.getSQLValue(get_TrxName(), sqlvalidate);
					if( medinconflict > 0)
					{
						String bpartnername = DB.getSQLValueString(get_TrxName(), "SELECT name from c_bpartner where c_bpartner_id = ? ", medinconflict);
						throw new AdempiereException ("El doctor "+bpartnername+" utiliza el box solicitado en el horario "+new Timestamp(desde1.getTimeInMillis())+" ");
					}
					X_HIS_SchedulerTime hora = new X_HIS_SchedulerTime(Env.getCtx(),0,get_TrxName());
					hora.setStatus("DI");
					hora.setTimeFrom( new Timestamp (desde1.getTimeInMillis()));
					//hora.setMED_Specialty_ID(horario.getHis_Specialty_ID());
					hora.setHIS_SchedulerDay_ID(newDia.getHIS_SchedulerDay_ID());
					hora.setAD_Org_ID(newDia.getAD_Org_ID());
					hora.set_CustomColumn("S_Resource_ID", boxatencion);
					hora.save();

					desde1.add(Calendar.MINUTE, horario.getMinutes());
				}
			}

			if(newDia.getCycleB2()!=null && newDia.getCycleB2()!=null)
			{
				Calendar b1= Calendar.getInstance();
				b1.setTimeInMillis(newDia.getCycleB2().getTime());

				Calendar b2= Calendar.getInstance();
				b2.setTimeInMillis(newDia.getCycleB3().getTime());

				Calendar desde2= Calendar.getInstance();
				desde2.setTimeInMillis(newDia.getDateTrx().getTime());
				desde2.set(Calendar.HOUR_OF_DAY, b1.get(Calendar.HOUR_OF_DAY));
				desde2.set(Calendar.MINUTE, b1.get(Calendar.MINUTE));

				Calendar hasta2= Calendar.getInstance();
				hasta2.setTimeInMillis(newDia.getDateTrx().getTime());
				hasta2.set(Calendar.HOUR_OF_DAY, b2.get(Calendar.HOUR_OF_DAY));
				hasta2.set(Calendar.MINUTE, b2.get(Calendar.MINUTE));

				while(desde2.compareTo(hasta2)<=0)
				{
					String sqlvalidate = "SELECT coalesce(max(C_BPartnerMed_ID),0) from HIS_Scheduler " +
							" WHERE C_BPartnerMed_ID != "+horario.getC_BPartnerMed_ID()+
							" AND HIS_Scheduler_ID in (SELECT HIS_Scheduler_ID "
							+ "							from HIS_SchedulerDay "
							+ "							where HIS_SchedulerDay_ID in (select HIS_SchedulerDay_ID "
							+ "							   								from HIS_SchedulerTime "
							+ "							   								where TimeFrom = '"+new Timestamp(desde2.getTimeInMillis())+"' "
							+ " 														AND S_Resource_ID = "+boxatencion+"))";
					int medinconflict = DB.getSQLValue(get_TrxName(), sqlvalidate);
					if( medinconflict > 0)
					{
						String bpartnername = DB.getSQLValueString(get_TrxName(), "SELECT name from c_bpartner where c_bpartner_id = ? ", medinconflict);
						throw new AdempiereException ("El doctor "+bpartnername+" utiliza el box solicitado en el horario "+new Timestamp(desde2.getTimeInMillis())+" ");
					}
					X_HIS_SchedulerTime hora = new X_HIS_SchedulerTime(Env.getCtx(),0,get_TrxName());
					hora.setStatus("DI");
					hora.setTimeFrom( new Timestamp (desde2.getTimeInMillis()));
					//hora.setMED_Specialty_ID(horario.getMED_Specialty_ID());
					hora.setHIS_SchedulerDay_ID(newDia.getHIS_SchedulerDay_ID());
					hora.setAD_Org_ID(newDia.getAD_Org_ID());
					hora.set_CustomColumn("S_Resource_ID", boxatencion);

					hora.saveEx();

					desde2.add(Calendar.MINUTE, horario.getMinutes());
				}
			}

			 dia=  TimeUtil.getNextDay(dia);
		}
			return "Dias Creados";
	}

	private int  getPlantilla (X_HIS_SchedulerTemplateTime[] plantillas, Timestamp dia )
	{
		int pos= -1;
		 Calendar cal= Calendar.getInstance();
		 cal.setTimeInMillis(dia.getTime());
		 String  numeroDia= ""+(cal.get(Calendar.DAY_OF_WEEK)-1);

		 for(int i =0 ; i<plantillas.length;i++)
		 {
			 if(numeroDia.equals(plantillas[i].getWeekDay()))
				 pos=i;
		 }

		 return pos;

	}

	private X_HIS_SchedulerTemplateTime[] getPlantillas(int plantilla_ID)
	{
		PreparedStatement pstmt = null;
		ArrayList<X_HIS_SchedulerTemplateTime> lista = new ArrayList<>();
		  String mysql="select * from his_schedulertemplatetime where his_schedulertemplate_ID=?";
		   try
			{
				pstmt = DB.prepareStatement(mysql, get_TrxName());
				pstmt.setInt(1, plantilla_ID);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					X_HIS_SchedulerTemplateTime dia = new X_HIS_SchedulerTemplateTime(Env.getCtx(),rs,get_TrxName());
					lista.add(dia);
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}

		   return lista.toArray(new X_HIS_SchedulerTemplateTime[lista.size()]);
	}

}
