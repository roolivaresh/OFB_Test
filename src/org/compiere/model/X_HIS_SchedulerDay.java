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

/** Generated Model for HIS_SchedulerDay
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_HIS_SchedulerDay extends PO implements I_HIS_SchedulerDay, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230728L;

    /** Standard Constructor */
    public X_HIS_SchedulerDay (Properties ctx, int HIS_SchedulerDay_ID, String trxName)
    {
      super (ctx, HIS_SchedulerDay_ID, trxName);
      /** if (HIS_SchedulerDay_ID == 0)
        {
			setHIS_SchedulerDay_ID (0);
        } */
    }

    /** Load Constructor */
    public X_HIS_SchedulerDay (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HIS_SchedulerDay[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set CycleA2.
		@param CycleA2 CycleA2	  */
	@Override
	public void setCycleA2 (Timestamp CycleA2)
	{
		set_Value (COLUMNNAME_CycleA2, CycleA2);
	}

	/** Get CycleA2.
		@return CycleA2	  */
	@Override
	public Timestamp getCycleA2 ()
	{
		return (Timestamp)get_Value(COLUMNNAME_CycleA2);
	}

	/** Set CycleA3.
		@param CycleA3 CycleA3	  */
	@Override
	public void setCycleA3 (Timestamp CycleA3)
	{
		set_Value (COLUMNNAME_CycleA3, CycleA3);
	}

	/** Get CycleA3.
		@return CycleA3	  */
	@Override
	public Timestamp getCycleA3 ()
	{
		return (Timestamp)get_Value(COLUMNNAME_CycleA3);
	}

	/** Set CycleB2.
		@param CycleB2 CycleB2	  */
	@Override
	public void setCycleB2 (Timestamp CycleB2)
	{
		set_Value (COLUMNNAME_CycleB2, CycleB2);
	}

	/** Get CycleB2.
		@return CycleB2	  */
	@Override
	public Timestamp getCycleB2 ()
	{
		return (Timestamp)get_Value(COLUMNNAME_CycleB2);
	}

	/** Set CycleB3.
		@param CycleB3 CycleB3	  */
	@Override
	public void setCycleB3 (Timestamp CycleB3)
	{
		set_Value (COLUMNNAME_CycleB3, CycleB3);
	}

	/** Get CycleB3.
		@return CycleB3	  */
	@Override
	public Timestamp getCycleB3 ()
	{
		return (Timestamp)get_Value(COLUMNNAME_CycleB3);
	}

	/** Set Transaction Date.
		@param DateTrx
		Transaction Date
	  */
	@Override
	public void setDateTrx (Timestamp DateTrx)
	{
		set_Value (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	@Override
	public Timestamp getDateTrx ()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
	}

	/** Set HIS_SchedulerDay.
		@param HIS_SchedulerDay_ID HIS_SchedulerDay	  */
	@Override
	public void setHIS_SchedulerDay_ID (int HIS_SchedulerDay_ID)
	{
		if (HIS_SchedulerDay_ID < 1)
			set_ValueNoCheck (COLUMNNAME_HIS_SchedulerDay_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_HIS_SchedulerDay_ID, Integer.valueOf(HIS_SchedulerDay_ID));
	}

	/** Get HIS_SchedulerDay.
		@return HIS_SchedulerDay	  */
	@Override
	public int getHIS_SchedulerDay_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HIS_SchedulerDay_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HIS_Scheduler.
		@param HIS_Scheduler_ID HIS_Scheduler	  */
	@Override
	public void setHIS_Scheduler_ID (int HIS_Scheduler_ID)
	{
		if (HIS_Scheduler_ID < 1)
			set_Value (COLUMNNAME_HIS_Scheduler_ID, null);
		else
			set_Value (COLUMNNAME_HIS_Scheduler_ID, Integer.valueOf(HIS_Scheduler_ID));
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

	/** WeekDay AD_Reference_ID=167 */
	public static final int WEEKDAY_AD_Reference_ID=167;
	/** Sunday = 7 */
	public static final String WEEKDAY_Sunday = "7";
	/** Monday = 1 */
	public static final String WEEKDAY_Monday = "1";
	/** Tuesday = 2 */
	public static final String WEEKDAY_Tuesday = "2";
	/** Wednesday = 3 */
	public static final String WEEKDAY_Wednesday = "3";
	/** Thursday = 4 */
	public static final String WEEKDAY_Thursday = "4";
	/** Friday = 5 */
	public static final String WEEKDAY_Friday = "5";
	/** Saturday = 6 */
	public static final String WEEKDAY_Saturday = "6";
	/** Set Day of the Week.
		@param WeekDay
		Day of the Week
	  */
	@Override
	public void setWeekDay (String WeekDay)
	{

		set_Value (COLUMNNAME_WeekDay, WeekDay);
	}

	/** Get Day of the Week.
		@return Day of the Week
	  */
	@Override
	public String getWeekDay ()
	{
		return (String)get_Value(COLUMNNAME_WeekDay);
	}
}