/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.                                     *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net                                                  *
 * or https://github.com/adempiere/adempiere/blob/develop/license.html        *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for HIS_Scheduler
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_HIS_Scheduler extends PO implements I_HIS_Scheduler, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230804L;

    /** Standard Constructor */
    public X_HIS_Scheduler (Properties ctx, int HIS_Scheduler_ID, String trxName)
    {
      super (ctx, HIS_Scheduler_ID, trxName);
      /** if (HIS_Scheduler_ID == 0)
        {
			setHIS_Scheduler_ID (0);
        } */
    }

    /** Load Constructor */
    public X_HIS_Scheduler (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org
      */
    @Override
	protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    @Override
	protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    @Override
	public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_HIS_Scheduler[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	@Override
	public org.adempiere.core.domains.models.I_C_BPartner getC_BPartnerMed() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_BPartner)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_BPartner.Table_Name)
			.getPO(getC_BPartnerMed_ID(), get_TrxName());	}

	/** Set C_BPartnerMed_ID.
		@param C_BPartnerMed_ID C_BPartnerMed_ID	  */
	@Override
	public void setC_BPartnerMed_ID (int C_BPartnerMed_ID)
	{
		if (C_BPartnerMed_ID < 1)
			set_Value (COLUMNNAME_C_BPartnerMed_ID, null);
		else
			set_Value (COLUMNNAME_C_BPartnerMed_ID, Integer.valueOf(C_BPartnerMed_ID));
	}

	/** Get C_BPartnerMed_ID.
		@return C_BPartnerMed_ID	  */
	@Override
	public int getC_BPartnerMed_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartnerMed_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Date From.
		@param DateFrom
		Starting date for a range
	  */
	@Override
	public void setDateFrom (Timestamp DateFrom)
	{
		set_Value (COLUMNNAME_DateFrom, DateFrom);
	}

	/** Get Date From.
		@return Starting date for a range
	  */
	@Override
	public Timestamp getDateFrom ()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateFrom);
	}

	/** Set Date To.
		@param DateTo
		End date of a date range
	  */
	@Override
	public void setDateTo (Timestamp DateTo)
	{
		set_Value (COLUMNNAME_DateTo, DateTo);
	}

	/** Get Date To.
		@return End date of a date range
	  */
	@Override
	public Timestamp getDateTo ()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTo);
	}

	/** Set Description.
		@param Description
		Optional short description of the record
	  */
	@Override
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	@Override
	public String getDescription ()
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set GenerateDays.
		@param GenerateDays GenerateDays	  */
	@Override
	public void setGenerateDays (String GenerateDays)
	{
		set_Value (COLUMNNAME_GenerateDays, GenerateDays);
	}

	/** Get GenerateDays.
		@return GenerateDays	  */
	@Override
	public String getGenerateDays ()
	{
		return (String)get_Value(COLUMNNAME_GenerateDays);
	}

	/** Set HIS_Scheduler.
		@param HIS_Scheduler_ID HIS_Scheduler	  */
	@Override
	public void setHIS_Scheduler_ID (int HIS_Scheduler_ID)
	{
		if (HIS_Scheduler_ID < 1)
			set_ValueNoCheck (COLUMNNAME_HIS_Scheduler_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_HIS_Scheduler_ID, Integer.valueOf(HIS_Scheduler_ID));
	}

	/** Get HIS_Scheduler.
		@return HIS_Scheduler	  */
	@Override
	public int getHIS_Scheduler_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HIS_Scheduler_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HIS_SchedulerTemplate.
		@param HIS_SchedulerTemplate_ID HIS_SchedulerTemplate	  */
	@Override
	public void setHIS_SchedulerTemplate_ID (int HIS_SchedulerTemplate_ID)
	{
		if (HIS_SchedulerTemplate_ID < 1)
			set_Value (COLUMNNAME_HIS_SchedulerTemplate_ID, null);
		else
			set_Value (COLUMNNAME_HIS_SchedulerTemplate_ID, Integer.valueOf(HIS_SchedulerTemplate_ID));
	}

	/** Get HIS_SchedulerTemplate.
		@return HIS_SchedulerTemplate	  */
	@Override
	public int getHIS_SchedulerTemplate_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HIS_SchedulerTemplate_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set His_Specialty.
		@param His_Specialty_ID His_Specialty	  */
	@Override
	public void setHis_Specialty_ID (int His_Specialty_ID)
	{
		if (His_Specialty_ID < 1)
			set_Value (COLUMNNAME_His_Specialty_ID, null);
		else
			set_Value (COLUMNNAME_His_Specialty_ID, Integer.valueOf(His_Specialty_ID));
	}

	/** Get His_Specialty.
		@return His_Specialty	  */
	@Override
	public int getHis_Specialty_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_His_Specialty_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Minutes.
		@param Minutes Minutes	  */
	@Override
	public void setMinutes (int Minutes)
	{
		set_Value (COLUMNNAME_Minutes, Integer.valueOf(Minutes));
	}

	/** Get Minutes.
		@return Minutes	  */
	@Override
	public int getMinutes ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Minutes);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	@Override
	public org.adempiere.core.domains.models.I_S_Resource getS_Resource() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_S_Resource)MTable.get(getCtx(), org.adempiere.core.domains.models.I_S_Resource.Table_Name)
			.getPO(getS_Resource_ID(), get_TrxName());	}

	/** Set Resource.
		@param S_Resource_ID
		Resource
	  */
	@Override
	public void setS_Resource_ID (int S_Resource_ID)
	{
		if (S_Resource_ID < 1)
			set_Value (COLUMNNAME_S_Resource_ID, null);
		else
			set_Value (COLUMNNAME_S_Resource_ID, Integer.valueOf(S_Resource_ID));
	}

	/** Get Resource.
		@return Resource
	  */
	@Override
	public int getS_Resource_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_S_Resource_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Status AD_Reference_ID=1000093 */
	public static final int STATUS_AD_Reference_ID=1000093;
	/** Disponible = DI */
	public static final String STATUS_Disponible = "DI";
	/** Usado = US */
	public static final String STATUS_Usado = "US";
	/** Bloqueado = BL */
	public static final String STATUS_Bloqueado = "BL";
	/** Set Status.
		@param Status
		Status of the currently running check
	  */
	@Override
	public void setStatus (String Status)
	{

		set_Value (COLUMNNAME_Status, Status);
	}

	/** Get Status.
		@return Status of the currently running check
	  */
	@Override
	public String getStatus ()
	{
		return (String)get_Value(COLUMNNAME_Status);
	}

	/** Set Immutable Universally Unique Identifier.
		@param UUID
		Immutable Universally Unique Identifier
	  */
	@Override
	public void setUUID (String UUID)
	{
		set_Value (COLUMNNAME_UUID, UUID);
	}

	/** Get Immutable Universally Unique Identifier.
		@return Immutable Universally Unique Identifier
	  */
	@Override
	public String getUUID ()
	{
		return (String)get_Value(COLUMNNAME_UUID);
	}
}