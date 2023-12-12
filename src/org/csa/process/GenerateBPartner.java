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
import org.compiere.model.MBPartner;
import org.compiere.model.MUser;
import org.compiere.model.X_C_BPartner_Info_Oth;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;




/**
 *
 *
 *  @author Rodrigo Olivares Hurtado
 *  @version $Id: GenerateBPartner.java,v 1.2 2023/08/14 00:51:01 jjanke Exp $
 *
 *  Generate new bpartners or updated bpartners for CSA
 */


public class GenerateBPartner extends SvrProcess {

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
		String msgnew = "No se agregarón pacientes nuevos";
		String msgup = "no se actualizarón los datos de pacientes";
		//Se busca si hay nuevos registros de pacientes en la tabla temporal
		int count = DB.getSQLValue(get_TrxName(), "SELECT coalesce(count(I_BPartnerWS_ID),0)" +
				" FROM I_BPartnerWS where i_isimported = 'N' and processed='N' ");


		if(count > 0) {
			//En el caso de que haya datos, comproramos si no estan registrado en el sistema
			int newbpartner = DB.getSQLValue(get_TrxName(), "select  count(1) from i_bpartnerws "
					+ "where i_isimported = 'N' and processed='N' and rut not in (select value||'-'||ValueValidator as rut from c_bpartner "
					+ "				 where isactive = 'Y' and value||'-'||ValueValidator is not null)");
			if (newbpartner > 0) {
				msgnew =GenerateNewBPartner();
			}
			//Si ya existe el paciente en la BD solamente actualizaremos sus datos.
			int updatedbpartner = DB.getSQLValue(get_TrxName(), "select count(1) from i_bpartnerws "
					+ "where i_isimported = 'N' and processed='N' and rut in (select value||'-'||ValueValidator as rut from c_bpartner "
					+ "				 where isactive = 'Y' and value||'-'||ValueValidator is not null)");
			if(updatedbpartner > 0) {
				msgup = UpdatedBPartner();
			}


		}

		return msgnew+" y "+msgup;
	}	//	doIt
	public String GenerateNewBPartner()
	{
		//buscamos los pacientes que no esten registrados en el sistema
		String sqlvalidaBPartner = "SELECT I_BPartnerWS_ID,rut, name, birthday,gender,health_insurance,nationality, email,phone " +
				" FROM I_BPartnerWS where i_isimported = 'N' and processed='N' "
				+ "and rut not in (select value||'-'||ValueValidator as rut from c_bpartner "
				+ "where isactive = 'Y' and value||'-'||ValueValidator is not null)" +
				" order by rut";
		int count = 0;

		log.config("sql validate = "+sqlvalidaBPartner);
		PreparedStatement pstmtbpartner = null;
		ResultSet rsbpartner = null;
		try {
			pstmtbpartner = DB.prepareStatement(sqlvalidaBPartner, get_TrxName());
			rsbpartner = pstmtbpartner.executeQuery();

			while(rsbpartner.next()) {
				//Registramos los datos de la tabla temporal en la tabla C_BPartner
				MBPartner bp = new MBPartner(getCtx(),0,get_TrxName());
				bp.setName(rsbpartner.getString("name"));
				String[] parts = rsbpartner.getString("rut").split("-");
				bp.setValue(parts[0]);
				bp.set_ValueOfColumn("ValueValidator", parts[1]);
				bp.setBirthday(rsbpartner.getTimestamp("birthday"));
				bp.setGender(rsbpartner.getString("gender"));
				bp.saveEx(get_TrxName());
				X_C_BPartner_Info_Oth bpi = new X_C_BPartner_Info_Oth(getCtx(),0,get_TrxName());
				bpi.setC_BPartner_ID(bp.get_ID());
				bpi.sethealth_insurance(rsbpartner.getString("health_insurance"));
				bpi.setnationality(rsbpartner.getString("nationality"));
				bpi.saveEx(get_TrxName());
				MUser user = new MUser(getCtx(),0,get_TrxName());
				user.setC_BPartner_ID(bp.get_ID());
				user.setName(rsbpartner.getString("email"));
				user.setEMail(rsbpartner.getString("email"));
				user.setPhone(rsbpartner.getString("phone"));
				user.saveEx(get_TrxName());


				count = count+1;
				//Actualizamos la tabla de importación para que no tome en consideración este registro.
				DB.executeUpdate("UPDATE I_BPartnerWS SET i_isimported = 'Y', processed='Y',c_bpartner_id = "+bp.get_ID()
						+ "WHERE I_BPartnerWS_ID = "+rsbpartner.getInt("I_BPartnerWS_ID"),get_TrxName());
			}

		}
		catch (SQLException e)
		{
			throw new DBException(e, sqlvalidaBPartner);
		}
		finally
		{
			DB.close(rsbpartner, pstmtbpartner);
			rsbpartner = null; pstmtbpartner = null;
		}




		return "Se agrego: "+count +" nuevo/os paciente/es";
	} //GenerateNewBPartner

	public String UpdatedBPartner()
	{
		//Buscamos los pacientes registrados en el sistema para actualizar sus datos.
		String sqlvalidaBPartner = "SELECT I_BPartnerWS_ID,rut, name, birthday,gender,health_insurance,nationality, email,phone " +
				" FROM I_BPartnerWS where i_isimported = 'N' and processed='N' "
				+ "and rut in (select value||'-'||ValueValidator as rut from c_bpartner "
				+ "where isactive = 'Y' and value||'-'||ValueValidator is not null)" +
				" order by rut";

		int bpartnerid = 0;
		int bpartnerinfoid = 0;
		int userid = 0;
		int count = 0;

		log.config("sql validate = "+sqlvalidaBPartner);
		PreparedStatement pstmtbpartner = null;
		ResultSet rsbpartner = null;
		try {
			pstmtbpartner = DB.prepareStatement(sqlvalidaBPartner, get_TrxName());
			rsbpartner = pstmtbpartner.executeQuery();

			while(rsbpartner.next()) {
				bpartnerid = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(MAX(C_BPARTNER_ID),0) FROM C_BPARTNER "
						+ "WHERE VALUE ||'-'||VALUEVALIDATOR = '"+rsbpartner.getString("rut")+"'");

				//Se actualizan los datos del paciente encontrado.
				if(bpartnerid > 0) {
					MBPartner bp = new MBPartner(getCtx(),bpartnerid,get_TrxName());
					bp.setName(rsbpartner.getString("name"));
					bp.setBirthday(rsbpartner.getTimestamp("birthday"));
					bp.setGender(rsbpartner.getString("gender"));
					bp.saveEx();
					bpartnerinfoid = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(MAX(C_BPARTNER_INFO_OTH_ID),0) FROM C_BPARTNER_INFO_OTH "
							+ "WHERE C_BPARTNER_ID = "+bpartnerid);
					X_C_BPartner_Info_Oth bpi = new X_C_BPartner_Info_Oth(getCtx(),bpartnerinfoid,get_TrxName());
					bpi.setC_BPartner_ID(bp.get_ID());
					bpi.sethealth_insurance(rsbpartner.getString("health_insurance"));
					bpi.setnationality(rsbpartner.getString("nationality"));
					bpi.saveEx(get_TrxName());
					userid = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(MAX(AD_User_ID),0) FROM AD_User WHERE C_BPARTNER_ID = "+bpartnerid);
					MUser user = new MUser(getCtx(),userid,get_TrxName());
					user.setC_BPartner_ID(bp.get_ID());
					user.setName(rsbpartner.getString("email"));
					user.setEMail(rsbpartner.getString("email"));
					user.setPhone(rsbpartner.getString("phone"));
					user.saveEx(get_TrxName());
					count = count+1;

					DB.executeUpdate("UPDATE I_BPartnerWS SET i_isimported = 'Y', processed='Y'  c_bpartner_id =" + bp.get_ID()
							+ "WHERE I_BPartnerWS_ID = "+rsbpartner.getInt("I_BPartnerWS_ID"),get_TrxName());
				}

			}

		}
		catch (SQLException e)
		{
			throw new DBException(e, sqlvalidaBPartner);
		}
		finally
		{
			DB.close(rsbpartner, pstmtbpartner);
			rsbpartner = null; pstmtbpartner = null;
		}


		return "se actualizo los datos de "+ count+" pacientes";
	}//UpdatedBPartner

}
