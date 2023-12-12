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

/** Generated Model for HIS_Appointment
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_HIS_Appointment extends PO implements I_HIS_Appointment, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230817L;

    /** Standard Constructor */
    public X_HIS_Appointment (Properties ctx, int HIS_Appointment_ID, String trxName)
    {
      super (ctx, HIS_Appointment_ID, trxName);
      /** if (HIS_Appointment_ID == 0)
        {
			setHIS_Appointment_ID (0);
        } */
    }

    /** Load Constructor */
    public X_HIS_Appointment (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HIS_Appointment[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	@Override
	public org.adempiere.core.domains.models.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_BPartner)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID
		Identifies a Business Partner
	  */
	@Override
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1)
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	@Override
	public int getC_BPartner_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set c_bpartnermed_id.
		@param c_bpartnermed_id c_bpartnermed_id	  */
	@Override
	public void setc_bpartnermed_id (int c_bpartnermed_id)
	{
		set_Value (COLUMNNAME_c_bpartnermed_id, Integer.valueOf(c_bpartnermed_id));
	}

	/** Get c_bpartnermed_id.
		@return c_bpartnermed_id	  */
	@Override
	public int getc_bpartnermed_id ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_c_bpartnermed_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set HIS_Appointment.
		@param HIS_Appointment_ID HIS_Appointment	  */
	@Override
	public void setHIS_Appointment_ID (int HIS_Appointment_ID)
	{
		if (HIS_Appointment_ID < 1)
			set_ValueNoCheck (COLUMNNAME_HIS_Appointment_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_HIS_Appointment_ID, Integer.valueOf(HIS_Appointment_ID));
	}

	/** Get HIS_Appointment.
		@return HIS_Appointment	  */
	@Override
	public int getHIS_Appointment_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HIS_Appointment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HIS_SchedulerTime_ID.
		@param HIS_SchedulerTime_ID HIS_SchedulerTime_ID	  */
	@Override
	public void setHIS_SchedulerTime_ID (int HIS_SchedulerTime_ID)
	{
		if (HIS_SchedulerTime_ID < 1)
			set_Value (COLUMNNAME_HIS_SchedulerTime_ID, null);
		else
			set_Value (COLUMNNAME_HIS_SchedulerTime_ID, Integer.valueOf(HIS_SchedulerTime_ID));
	}

	/** Get HIS_SchedulerTime_ID.
		@return HIS_SchedulerTime_ID	  */
	@Override
	public int getHIS_SchedulerTime_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HIS_SchedulerTime_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HIS_Specialty_ID.
		@param HIS_Specialty_ID HIS_Specialty_ID	  */
	@Override
	public void setHIS_Specialty_ID (int HIS_Specialty_ID)
	{
		if (HIS_Specialty_ID < 1)
			set_Value (COLUMNNAME_HIS_Specialty_ID, null);
		else
			set_Value (COLUMNNAME_HIS_Specialty_ID, Integer.valueOf(HIS_Specialty_ID));
	}

	/** Get HIS_Specialty_ID.
		@return HIS_Specialty_ID	  */
	@Override
	public int getHIS_Specialty_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HIS_Specialty_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set isarrived.
		@param isarrived isarrived	  */
	@Override
	public void setisarrived (boolean isarrived)
	{
		set_Value (COLUMNNAME_isarrived, Boolean.valueOf(isarrived));
	}

	/** Get isarrived.
		@return isarrived	  */
	@Override
	public boolean isarrived ()
	{
		Object oo = get_Value(COLUMNNAME_isarrived);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set isentry.
		@param isentry isentry	  */
	@Override
	public void setisentry (boolean isentry)
	{
		set_Value (COLUMNNAME_isentry, Boolean.valueOf(isentry));
	}

	/** Get isentry.
		@return isentry	  */
	@Override
	public boolean isentry ()
	{
		Object oo = get_Value(COLUMNNAME_isentry);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set isexit.
		@param isexit isexit	  */
	@Override
	public void setisexit (boolean isexit)
	{
		set_Value (COLUMNNAME_isexit, Boolean.valueOf(isexit));
	}

	/** Get isexit.
		@return isexit	  */
	@Override
	public boolean isexit ()
	{
		Object oo = get_Value(COLUMNNAME_isexit);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set reservationdate.
		@param reservationdate reservationdate	  */
	@Override
	public void setreservationdate (Timestamp reservationdate)
	{
		set_Value (COLUMNNAME_reservationdate, reservationdate);
	}

	/** Get reservationdate.
		@return reservationdate	  */
	@Override
	public Timestamp getreservationdate ()
	{
		return (Timestamp)get_Value(COLUMNNAME_reservationdate);
	}

	/** Set Start Time.
		@param StartTime
		Time started
	  */
	@Override
	public void setStartTime (Timestamp StartTime)
	{
		set_Value (COLUMNNAME_StartTime, StartTime);
	}

	/** Get Start Time.
		@return Time started
	  */
	@Override
	public Timestamp getStartTime ()
	{
		return (Timestamp)get_Value(COLUMNNAME_StartTime);
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