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
import org.compiere.model.X_HIS_SchedulerTime;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;




/**
 *
 *
 *  @author Rodrigo Olivares Hurtado
 *  @version $Id: UpdatedSchedulerTime.java,v 1.2 2023/08/17 00:51:01 jjanke Exp $
 *
 *   updated SchedulerTime for CSA
 */


public class UpdatedSchedulerTime extends SvrProcess {

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
		String msgnew = "No se actualizo el estado de la hora";
		//Se busca si hay nuevos registros de pacientes en la tabla temporal
		int count = DB.getSQLValue(get_TrxName(), "SELECT coalesce(count(I_SchedulerTimeWS_ID),0)" +
				" FROM I_SchedulerTimeWS where i_isimported = 'N' and processed='N' ");


		if(count > 0) {
			//En el caso de que haya datos, comproramos si no estan registrado en el sistema
			msgnew =GenerateUpdatedSchedulerTime();
		}

		return msgnew;
	}	//	doIt
	public String GenerateUpdatedSchedulerTime()
	{
		//buscamos los estado no actualizados
		String sqlSchedulerTime = "SELECT I_SchedulerTimeWS_ID,his_schedulertime_id,status " +
				" FROM I_SchedulerTimeWS where i_isimported = 'N' and processed='N' ";
		int count = 0;

		log.config("sql validate = "+sqlSchedulerTime);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlSchedulerTime, get_TrxName());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				//Registramos los datos de la tabla temporal en la tabla HIS_SchedulerTime

				X_HIS_SchedulerTime st = new X_HIS_SchedulerTime(getCtx(),rs.getInt("I_SchedulerTimeWS_ID"),get_TrxName());
				st.setStatus(rs.getString("status"));
				st.saveEx();
				count = count+1;
				//Actualizamos la tabla de importación para que no tome en consideración este registro.
				DB.executeUpdate("UPDATE I_SchedulerTimeWS SET i_isimported = 'Y', processed='Y' WHERE I_SchedulerTimeWS_ID = "+rs.getInt("I_SchedulerTimeWS_ID"),get_TrxName());
			}

		}
		catch (SQLException e)
		{
			throw new DBException(e, sqlSchedulerTime);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}




		return "Se actualizo el estado de: "+count +" horas";
	} //GenerateNewBPartner


}
