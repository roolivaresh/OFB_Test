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

/** Generated Interface for TP_Fleet
 *  @author Adempiere (generated)
 *  @version Release 3.9.4
 */
public interface I_TP_Fleet
{

    /** TableName=TP_Fleet */
    public static final String Table_Name = "TP_Fleet";

    /** AD_Table_ID=1000134 */
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

    /** Column name AD_User_ID */
    public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

	/** Set User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID);

	/** Get User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID();

	public org.adempiere.core.domains.models.I_AD_User getAD_User() throws RuntimeException;

    /** Column name bp_working */
    public static final String COLUMNNAME_bp_working = "bp_working";

	/** Set bp_working	  */
	public void setbp_working (int bp_working);

	/** Get bp_working	  */
	public int getbp_working();

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

    /** Column name Category */
    public static final String COLUMNNAME_Category = "Category";

	/** Set Category	  */
	public void setCategory (String Category);

	/** Get Category	  */
	public String getCategory();

    /** Column name categoryclient */
    public static final String COLUMNNAME_categoryclient = "categoryclient";

	/** Set categoryclient	  */
	public void setcategoryclient (String categoryclient);

	/** Get categoryclient	  */
	public String getcategoryclient();

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

    /** Column name day_week */
    public static final String COLUMNNAME_day_week = "day_week";

	/** Set day_week	  */
	public void setday_week (boolean day_week);

	/** Get day_week	  */
	public boolean isday_week();

    /** Column name dayweekavg */
    public static final String COLUMNNAME_dayweekavg = "dayweekavg";

	/** Set dayweekavg	  */
	public void setdayweekavg (BigDecimal dayweekavg);

	/** Get dayweekavg	  */
	public BigDecimal getdayweekavg();

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

    /** Column name isholiday */
    public static final String COLUMNNAME_isholiday = "isholiday";

	/** Set isholiday	  */
	public void setisholiday (boolean isholiday);

	/** Get isholiday	  */
	public boolean isholiday();

    /** Column name isintegration */
    public static final String COLUMNNAME_isintegration = "isintegration";

	/** Set isintegration	  */
	public void setisintegration (boolean isintegration);

	/** Get isintegration	  */
	public boolean isintegration();

    /** Column name isoperational */
    public static final String COLUMNNAME_isoperational = "isoperational";

	/** Set isoperational	  */
	public void setisoperational (boolean isoperational);

	/** Get isoperational	  */
	public boolean isoperational();

    /** Column name max_tons */
    public static final String COLUMNNAME_max_tons = "max_tons";

	/** Set max_tons	  */
	public void setmax_tons (BigDecimal max_tons);

	/** Get max_tons	  */
	public BigDecimal getmax_tons();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name tp_costingtype */
    public static final String COLUMNNAME_tp_costingtype = "tp_costingtype";

	/** Set tp_costingtype	  */
	public void settp_costingtype (String tp_costingtype);

	/** Get tp_costingtype	  */
	public String gettp_costingtype();

    /** Column name TP_Fleet_ID */
    public static final String COLUMNNAME_TP_Fleet_ID = "TP_Fleet_ID";

	/** Set TP_Fleet_ID	  */
	public void setTP_Fleet_ID (int TP_Fleet_ID);

	/** Get TP_Fleet_ID	  */
	public int getTP_Fleet_ID();

    /** Column name tp_hdrtype */
    public static final String COLUMNNAME_tp_hdrtype = "tp_hdrtype";

	/** Set tp_hdrtype	  */
	public void settp_hdrtype (String tp_hdrtype);

	/** Get tp_hdrtype	  */
	public String gettp_hdrtype();

    /** Column name tp_location */
    public static final String COLUMNNAME_tp_location = "tp_location";

	/** Set tp_location	  */
	public void settp_location (String tp_location);

	/** Get tp_location	  */
	public String gettp_location();

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

    /** Column name workshift */
    public static final String COLUMNNAME_workshift = "workshift";

	/** Set workshift	  */
	public void setworkshift (String workshift);

	/** Get workshift	  */
	public String getworkshift();
}
