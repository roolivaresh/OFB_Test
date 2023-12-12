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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.util.KeyNamePair;

/** Generated Interface for HIS_SchedulerDay
 *  @author Adempiere (generated)
 *  @version Release 3.9.4
 */
public interface I_HIS_SchedulerDay
{

    /** TableName=HIS_SchedulerDay */
    public static final String Table_Name = "HIS_SchedulerDay";

    /** AD_Table_ID=1000011 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name CycleA2 */
    public static final String COLUMNNAME_CycleA2 = "CycleA2";

	/** Set CycleA2	  */
	public void setCycleA2 (Timestamp CycleA2);

	/** Get CycleA2	  */
	public Timestamp getCycleA2();

    /** Column name CycleA3 */
    public static final String COLUMNNAME_CycleA3 = "CycleA3";

	/** Set CycleA3	  */
	public void setCycleA3 (Timestamp CycleA3);

	/** Get CycleA3	  */
	public Timestamp getCycleA3();

    /** Column name CycleB2 */
    public static final String COLUMNNAME_CycleB2 = "CycleB2";

	/** Set CycleB2	  */
	public void setCycleB2 (Timestamp CycleB2);

	/** Get CycleB2	  */
	public Timestamp getCycleB2();

    /** Column name CycleB3 */
    public static final String COLUMNNAME_CycleB3 = "CycleB3";

	/** Set CycleB3	  */
	public void setCycleB3 (Timestamp CycleB3);

	/** Get CycleB3	  */
	public Timestamp getCycleB3();

    /** Column name DateTrx */
    public static final String COLUMNNAME_DateTrx = "DateTrx";

	/** Set Transaction Date.
	  * Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx);

	/** Get Transaction Date.
	  * Transaction Date
	  */
	public Timestamp getDateTrx();

    /** Column name HIS_SchedulerDay_ID */
    public static final String COLUMNNAME_HIS_SchedulerDay_ID = "HIS_SchedulerDay_ID";

	/** Set HIS_SchedulerDay	  */
	public void setHIS_SchedulerDay_ID (int HIS_SchedulerDay_ID);

	/** Get HIS_SchedulerDay	  */
	public int getHIS_SchedulerDay_ID();

    /** Column name HIS_Scheduler_ID */
    public static final String COLUMNNAME_HIS_Scheduler_ID = "HIS_Scheduler_ID";

	/** Set HIS_Scheduler	  */
	public void setHIS_Scheduler_ID (int HIS_Scheduler_ID);

	/** Get HIS_Scheduler	  */
	public int getHIS_Scheduler_ID();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name UUID */
    public static final String COLUMNNAME_UUID = "UUID";

	/** Set Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public void setUUID (String UUID);

	/** Get Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public String getUUID();

    /** Column name WeekDay */
    public static final String COLUMNNAME_WeekDay = "WeekDay";

	/** Set Day of the Week.
	  * Day of the Week
	  */
	public void setWeekDay (String WeekDay);

	/** Get Day of the Week.
	  * Day of the Week
	  */
	public String getWeekDay();
}