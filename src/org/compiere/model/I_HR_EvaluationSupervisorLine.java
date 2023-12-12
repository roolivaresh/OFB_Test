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

/** Generated Interface for HR_EvaluationSupervisorLine
 *  @author Adempiere (generated)
 *  @version Release 3.9.4
 */
public interface I_HR_EvaluationSupervisorLine
{

    /** TableName=HR_EvaluationSupervisorLine */
    public static final String Table_Name = "HR_EvaluationSupervisorLine";

    /** AD_Table_ID=1000136 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 4 - System
     */
    BigDecimal accessLevel = BigDecimal.valueOf(4);

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

    /** Column name HR_EvaluationSupervisor_ID */
    public static final String COLUMNNAME_HR_EvaluationSupervisor_ID = "HR_EvaluationSupervisor_ID";

	/** Set HR_EvaluationSupervisor	  */
	public void setHR_EvaluationSupervisor_ID (int HR_EvaluationSupervisor_ID);

	/** Get HR_EvaluationSupervisor	  */
	public int getHR_EvaluationSupervisor_ID();

    /** Column name HR_EvaluationSupervisorLine_ID */
    public static final String COLUMNNAME_HR_EvaluationSupervisorLine_ID = "HR_EvaluationSupervisorLine_ID";

	/** Set HR_EvaluationSupervisorLine	  */
	public void setHR_EvaluationSupervisorLine_ID (int HR_EvaluationSupervisorLine_ID);

	/** Get HR_EvaluationSupervisorLine	  */
	public int getHR_EvaluationSupervisorLine_ID();

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

    /** Column name question1_supervisor */
    public static final String COLUMNNAME_question1_supervisor = "question1_supervisor";

	/** Set question1_supervisor	  */
	public void setquestion1_supervisor (String question1_supervisor);

	/** Get question1_supervisor	  */
	public String getquestion1_supervisor();

    /** Column name question2_supervisor */
    public static final String COLUMNNAME_question2_supervisor = "question2_supervisor";

	/** Set question2_supervisor	  */
	public void setquestion2_supervisor (String question2_supervisor);

	/** Get question2_supervisor	  */
	public String getquestion2_supervisor();

    /** Column name question3_supervisor */
    public static final String COLUMNNAME_question3_supervisor = "question3_supervisor";

	/** Set question3_supervisor	  */
	public void setquestion3_supervisor (String question3_supervisor);

	/** Get question3_supervisor	  */
	public String getquestion3_supervisor();

    /** Column name question4_supervisor */
    public static final String COLUMNNAME_question4_supervisor = "question4_supervisor";

	/** Set question4_supervisor	  */
	public void setquestion4_supervisor (String question4_supervisor);

	/** Get question4_supervisor	  */
	public String getquestion4_supervisor();

    /** Column name question5_supervisor */
    public static final String COLUMNNAME_question5_supervisor = "question5_supervisor";

	/** Set question5_supervisor	  */
	public void setquestion5_supervisor (String question5_supervisor);

	/** Get question5_supervisor	  */
	public String getquestion5_supervisor();

    /** Column name question6_supervisor */
    public static final String COLUMNNAME_question6_supervisor = "question6_supervisor";

	/** Set question6_supervisor	  */
	public void setquestion6_supervisor (String question6_supervisor);

	/** Get question6_supervisor	  */
	public String getquestion6_supervisor();

    /** Column name question7_supervisor */
    public static final String COLUMNNAME_question7_supervisor = "question7_supervisor";

	/** Set question7_supervisor	  */
	public void setquestion7_supervisor (String question7_supervisor);

	/** Get question7_supervisor	  */
	public String getquestion7_supervisor();

    /** Column name question8_supervisor */
    public static final String COLUMNNAME_question8_supervisor = "question8_supervisor";

	/** Set question8_supervisor	  */
	public void setquestion8_supervisor (String question8_supervisor);

	/** Get question8_supervisor	  */
	public String getquestion8_supervisor();

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

    /** Column name TP_Fleet_ID */
    public static final String COLUMNNAME_TP_Fleet_ID = "TP_Fleet_ID";

	/** Set TP_Fleet_ID	  */
	public void setTP_Fleet_ID (int TP_Fleet_ID);

	/** Get TP_Fleet_ID	  */
	public int getTP_Fleet_ID();

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
