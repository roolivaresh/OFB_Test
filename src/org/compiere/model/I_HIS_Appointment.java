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

/** Generated Interface for HIS_Appointment
 *  @author Adempiere (generated)
 *  @version Release 3.9.4
 */
public interface I_HIS_Appointment
{

    /** TableName=HIS_Appointment */
    public static final String Table_Name = "HIS_Appointment";

    /** AD_Table_ID=1000124 */
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

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner .
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner .
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public org.adempiere.core.domains.models.I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name c_bpartnermed_id */
    public static final String COLUMNNAME_c_bpartnermed_id = "c_bpartnermed_id";

	/** Set c_bpartnermed_id	  */
	public void setc_bpartnermed_id (int c_bpartnermed_id);

	/** Get c_bpartnermed_id	  */
	public int getc_bpartnermed_id();

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

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name HIS_Appointment_ID */
    public static final String COLUMNNAME_HIS_Appointment_ID = "HIS_Appointment_ID";

	/** Set HIS_Appointment	  */
	public void setHIS_Appointment_ID (int HIS_Appointment_ID);

	/** Get HIS_Appointment	  */
	public int getHIS_Appointment_ID();

    /** Column name HIS_SchedulerTime_ID */
    public static final String COLUMNNAME_HIS_SchedulerTime_ID = "HIS_SchedulerTime_ID";

	/** Set HIS_SchedulerTime_ID	  */
	public void setHIS_SchedulerTime_ID (int HIS_SchedulerTime_ID);

	/** Get HIS_SchedulerTime_ID	  */
	public int getHIS_SchedulerTime_ID();

    /** Column name HIS_Specialty_ID */
    public static final String COLUMNNAME_HIS_Specialty_ID = "HIS_Specialty_ID";

	/** Set HIS_Specialty_ID	  */
	public void setHIS_Specialty_ID (int HIS_Specialty_ID);

	/** Get HIS_Specialty_ID	  */
	public int getHIS_Specialty_ID();

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

    /** Column name isarrived */
    public static final String COLUMNNAME_isarrived = "isarrived";

	/** Set isarrived	  */
	public void setisarrived (boolean isarrived);

	/** Get isarrived	  */
	public boolean isarrived();

    /** Column name isentry */
    public static final String COLUMNNAME_isentry = "isentry";

	/** Set isentry	  */
	public void setisentry (boolean isentry);

	/** Get isentry	  */
	public boolean isentry();

    /** Column name isexit */
    public static final String COLUMNNAME_isexit = "isexit";

	/** Set isexit	  */
	public void setisexit (boolean isexit);

	/** Get isexit	  */
	public boolean isexit();

    /** Column name reservationdate */
    public static final String COLUMNNAME_reservationdate = "reservationdate";

	/** Set reservationdate	  */
	public void setreservationdate (Timestamp reservationdate);

	/** Get reservationdate	  */
	public Timestamp getreservationdate();

    /** Column name StartTime */
    public static final String COLUMNNAME_StartTime = "StartTime";

	/** Set Start Time.
	  * Time started
	  */
	public void setStartTime (Timestamp StartTime);

	/** Get Start Time.
	  * Time started
	  */
	public Timestamp getStartTime();

    /** Column name Status */
    public static final String COLUMNNAME_Status = "Status";

	/** Set Status.
	  * Status of the currently running check
	  */
	public void setStatus (String Status);

	/** Get Status.
	  * Status of the currently running check
	  */
	public String getStatus();

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
}
