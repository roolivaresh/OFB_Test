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

/** Generated Interface for HR_EvaluationDriversLine
 *  @author Adempiere (generated)
 *  @version Release 3.9.4
 */
public interface I_HR_EvaluationDriversLine
{

    /** TableName=HR_EvaluationDriversLine */
    public static final String Table_Name = "HR_EvaluationDriversLine";

    /** AD_Table_ID=1000132 */
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

    /** Column name answer1 */
    public static final String COLUMNNAME_answer1 = "answer1";

	/** Set answer1	  */
	public void setanswer1 (boolean answer1);

	/** Get answer1	  */
	public boolean isanswer1();

    /** Column name answer10 */
    public static final String COLUMNNAME_answer10 = "answer10";

	/** Set answer10	  */
	public void setanswer10 (boolean answer10);

	/** Get answer10	  */
	public boolean isanswer10();

    /** Column name answer2 */
    public static final String COLUMNNAME_answer2 = "answer2";

	/** Set answer2	  */
	public void setanswer2 (boolean answer2);

	/** Get answer2	  */
	public boolean isanswer2();

    /** Column name answer3 */
    public static final String COLUMNNAME_answer3 = "answer3";

	/** Set answer3	  */
	public void setanswer3 (boolean answer3);

	/** Get answer3	  */
	public boolean isanswer3();

    /** Column name answer4 */
    public static final String COLUMNNAME_answer4 = "answer4";

	/** Set answer4	  */
	public void setanswer4 (boolean answer4);

	/** Get answer4	  */
	public boolean isanswer4();

    /** Column name answer5 */
    public static final String COLUMNNAME_answer5 = "answer5";

	/** Set answer5	  */
	public void setanswer5 (boolean answer5);

	/** Get answer5	  */
	public boolean isanswer5();

    /** Column name answer6 */
    public static final String COLUMNNAME_answer6 = "answer6";

	/** Set answer6	  */
	public void setanswer6 (boolean answer6);

	/** Get answer6	  */
	public boolean isanswer6();

    /** Column name answer7 */
    public static final String COLUMNNAME_answer7 = "answer7";

	/** Set answer7	  */
	public void setanswer7 (boolean answer7);

	/** Get answer7	  */
	public boolean isanswer7();

    /** Column name answer8 */
    public static final String COLUMNNAME_answer8 = "answer8";

	/** Set answer8	  */
	public void setanswer8 (boolean answer8);

	/** Get answer8	  */
	public boolean isanswer8();

    /** Column name answer9 */
    public static final String COLUMNNAME_answer9 = "answer9";

	/** Set answer9	  */
	public void setanswer9 (boolean answer9);

	/** Get answer9	  */
	public boolean isanswer9();

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

    /** Column name HR_EvaluationDrivers_ID */
    public static final String COLUMNNAME_HR_EvaluationDrivers_ID = "HR_EvaluationDrivers_ID";

	/** Set HR_EvaluationDrivers	  */
	public void setHR_EvaluationDrivers_ID (int HR_EvaluationDrivers_ID);

	/** Get HR_EvaluationDrivers	  */
	public int getHR_EvaluationDrivers_ID();

    /** Column name HR_EvaluationDriversLine_ID */
    public static final String COLUMNNAME_HR_EvaluationDriversLine_ID = "HR_EvaluationDriversLine_ID";

	/** Set HR_EvaluationDriversLine	  */
	public void setHR_EvaluationDriversLine_ID (int HR_EvaluationDriversLine_ID);

	/** Get HR_EvaluationDriversLine	  */
	public int getHR_EvaluationDriversLine_ID();

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

    /** Column name question1_drive */
    public static final String COLUMNNAME_question1_drive = "question1_drive";

	/** Set question1_drive	  */
	public void setquestion1_drive (String question1_drive);

	/** Get question1_drive	  */
	public String getquestion1_drive();

    /** Column name question10_drive */
    public static final String COLUMNNAME_question10_drive = "question10_drive";

	/** Set question10_drive	  */
	public void setquestion10_drive (String question10_drive);

	/** Get question10_drive	  */
	public String getquestion10_drive();

    /** Column name question2_drive */
    public static final String COLUMNNAME_question2_drive = "question2_drive";

	/** Set question2_drive	  */
	public void setquestion2_drive (String question2_drive);

	/** Get question2_drive	  */
	public String getquestion2_drive();

    /** Column name question3_drive */
    public static final String COLUMNNAME_question3_drive = "question3_drive";

	/** Set question3_drive	  */
	public void setquestion3_drive (String question3_drive);

	/** Get question3_drive	  */
	public String getquestion3_drive();

    /** Column name question4_drive */
    public static final String COLUMNNAME_question4_drive = "question4_drive";

	/** Set question4_drive	  */
	public void setquestion4_drive (String question4_drive);

	/** Get question4_drive	  */
	public String getquestion4_drive();

    /** Column name question5_drive */
    public static final String COLUMNNAME_question5_drive = "question5_drive";

	/** Set question5_drive	  */
	public void setquestion5_drive (String question5_drive);

	/** Get question5_drive	  */
	public String getquestion5_drive();

    /** Column name question6_drive */
    public static final String COLUMNNAME_question6_drive = "question6_drive";

	/** Set question6_drive	  */
	public void setquestion6_drive (String question6_drive);

	/** Get question6_drive	  */
	public String getquestion6_drive();

    /** Column name question7_drive */
    public static final String COLUMNNAME_question7_drive = "question7_drive";

	/** Set question7_drive	  */
	public void setquestion7_drive (String question7_drive);

	/** Get question7_drive	  */
	public String getquestion7_drive();

    /** Column name question8_drive */
    public static final String COLUMNNAME_question8_drive = "question8_drive";

	/** Set question8_drive	  */
	public void setquestion8_drive (String question8_drive);

	/** Get question8_drive	  */
	public String getquestion8_drive();

    /** Column name question9_drive */
    public static final String COLUMNNAME_question9_drive = "question9_drive";

	/** Set question9_drive	  */
	public void setquestion9_drive (String question9_drive);

	/** Get question9_drive	  */
	public String getquestion9_drive();

    /** Column name Result */
    public static final String COLUMNNAME_Result = "Result";

	/** Set Result.
	  * Result of the action taken
	  */
	public void setResult (BigDecimal Result);

	/** Get Result.
	  * Result of the action taken
	  */
	public BigDecimal getResult();

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
