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

/** Generated Model for HIS_SchedulerTime
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_HIS_SchedulerTime extends PO implements I_HIS_SchedulerTime, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230817L;

    /** Standard Constructor */
    public X_HIS_SchedulerTime (Properties ctx, int HIS_SchedulerTime_ID, String trxName)
    {
      super (ctx, HIS_SchedulerTime_ID, trxName);
      /** if (HIS_SchedulerTime_ID == 0)
        {
			sethis_schedulertime_id (0);
        } */
    }

    /** Load Constructor */
    public X_HIS_SchedulerTime (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HIS_SchedulerTime[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set HIS_SchedulerDay_ID.
		@param HIS_SchedulerDay_ID HIS_SchedulerDay_ID	  */
	@Override
	public void setHIS_SchedulerDay_ID (int HIS_SchedulerDay_ID)
	{
		if (HIS_SchedulerDay_ID < 1)
			set_Value (COLUMNNAME_HIS_SchedulerDay_ID, null);
		else
			set_Value (COLUMNNAME_HIS_SchedulerDay_ID, Integer.valueOf(HIS_SchedulerDay_ID));
	}

	/** Get HIS_SchedulerDay_ID.
		@return HIS_SchedulerDay_ID	  */
	@Override
	public int getHIS_SchedulerDay_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HIS_SchedulerDay_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set his_schedulertime_id.
		@param his_schedulertime_id his_schedulertime_id	  */
	@Override
	public void sethis_schedulertime_id (int his_schedulertime_id)
	{
		set_ValueNoCheck (COLUMNNAME_his_schedulertime_id, Integer.valueOf(his_schedulertime_id));
	}

	/** Get his_schedulertime_id.
		@return his_schedulertime_id	  */
	@Override
	public int gethis_schedulertime_id ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_his_schedulertime_id);
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

	/** Set Time (From).
		@param TimeFrom
		Starting Time
	  */
	@Override
	public void setTimeFrom (Timestamp TimeFrom)
	{
		set_Value (COLUMNNAME_TimeFrom, TimeFrom);
	}

	/** Get Time (From).
		@return Starting Time
	  */
	@Override
	public Timestamp getTimeFrom ()
	{
		return (Timestamp)get_Value(COLUMNNAME_TimeFrom);
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