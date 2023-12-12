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

/** Generated Interface for HR_EvaluationGuide
 *  @author Adempiere (generated)
 *  @version Release 3.9.4
 */
public interface I_HR_EvaluationGuide
{

    /** TableName=HR_EvaluationGuide */
    public static final String Table_Name = "HR_EvaluationGuide";

    /** AD_Table_ID=1000133 */
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

    /** Column name expectedresult1 */
    public static final String COLUMNNAME_expectedresult1 = "expectedresult1";

	/** Set expectedresult1	  */
	public void setexpectedresult1 (boolean expectedresult1);

	/** Get expectedresult1	  */
	public boolean isexpectedresult1();

    /** Column name expectedresult10 */
    public static final String COLUMNNAME_expectedresult10 = "expectedresult10";

	/** Set expectedresult10	  */
	public void setexpectedresult10 (boolean expectedresult10);

	/** Get expectedresult10	  */
	public boolean isexpectedresult10();

    /** Column name expectedresult2 */
    public static final String COLUMNNAME_expectedresult2 = "expectedresult2";

	/** Set expectedresult2	  */
	public void setexpectedresult2 (boolean expectedresult2);

	/** Get expectedresult2	  */
	public boolean isexpectedresult2();

    /** Column name expectedresult3 */
    public static final String COLUMNNAME_expectedresult3 = "expectedresult3";

	/** Set expectedresult3	  */
	public void setexpectedresult3 (boolean expectedresult3);

	/** Get expectedresult3	  */
	public boolean isexpectedresult3();

    /** Column name expectedresult4 */
    public static final String COLUMNNAME_expectedresult4 = "expectedresult4";

	/** Set expectedresult4	  */
	public void setexpectedresult4 (boolean expectedresult4);

	/** Get expectedresult4	  */
	public boolean isexpectedresult4();

    /** Column name expectedresult5 */
    public static final String COLUMNNAME_expectedresult5 = "expectedresult5";

	/** Set expectedresult5	  */
	public void setexpectedresult5 (boolean expectedresult5);

	/** Get expectedresult5	  */
	public boolean isexpectedresult5();

    /** Column name expectedresult6 */
    public static final String COLUMNNAME_expectedresult6 = "expectedresult6";

	/** Set expectedresult6	  */
	public void setexpectedresult6 (boolean expectedresult6);

	/** Get expectedresult6	  */
	public boolean isexpectedresult6();

    /** Column name expectedresult7 */
    public static final String COLUMNNAME_expectedresult7 = "expectedresult7";

	/** Set expectedresult7	  */
	public void setexpectedresult7 (boolean expectedresult7);

	/** Get expectedresult7	  */
	public boolean isexpectedresult7();

    /** Column name expectedresult8 */
    public static final String COLUMNNAME_expectedresult8 = "expectedresult8";

	/** Set expectedresult8	  */
	public void setexpectedresult8 (boolean expectedresult8);

	/** Get expectedresult8	  */
	public boolean isexpectedresult8();

    /** Column name expectedresult9 */
    public static final String COLUMNNAME_expectedresult9 = "expectedresult9";

	/** Set expectedresult9	  */
	public void setexpectedresult9 (boolean expectedresult9);

	/** Get expectedresult9	  */
	public boolean isexpectedresult9();

    /** Column name hr_evaluationguide_id */
    public static final String COLUMNNAME_hr_evaluationguide_id = "hr_evaluationguide_id";

	/** Set hr_evaluationguide_id	  */
	public void sethr_evaluationguide_id (int hr_evaluationguide_id);

	/** Get hr_evaluationguide_id	  */
	public int gethr_evaluationguide_id();

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

    /** Column name question1 */
    public static final String COLUMNNAME_question1 = "question1";

	/** Set question1	  */
	public void setquestion1 (String question1);

	/** Get question1	  */
	public String getquestion1();

    /** Column name question10 */
    public static final String COLUMNNAME_question10 = "question10";

	/** Set question10	  */
	public void setquestion10 (String question10);

	/** Get question10	  */
	public String getquestion10();

    /** Column name question2 */
    public static final String COLUMNNAME_question2 = "question2";

	/** Set question2	  */
	public void setquestion2 (String question2);

	/** Get question2	  */
	public String getquestion2();

    /** Column name question3 */
    public static final String COLUMNNAME_question3 = "question3";

	/** Set question3	  */
	public void setquestion3 (String question3);

	/** Get question3	  */
	public String getquestion3();

    /** Column name question4 */
    public static final String COLUMNNAME_question4 = "question4";

	/** Set question4	  */
	public void setquestion4 (String question4);

	/** Get question4	  */
	public String getquestion4();

    /** Column name question5 */
    public static final String COLUMNNAME_question5 = "question5";

	/** Set question5	  */
	public void setquestion5 (String question5);

	/** Get question5	  */
	public String getquestion5();

    /** Column name question6 */
    public static final String COLUMNNAME_question6 = "question6";

	/** Set question6	  */
	public void setquestion6 (String question6);

	/** Get question6	  */
	public String getquestion6();

    /** Column name question7 */
    public static final String COLUMNNAME_question7 = "question7";

	/** Set question7	  */
	public void setquestion7 (String question7);

	/** Get question7	  */
	public String getquestion7();

    /** Column name question8 */
    public static final String COLUMNNAME_question8 = "question8";

	/** Set question8	  */
	public void setquestion8 (String question8);

	/** Get question8	  */
	public String getquestion8();

    /** Column name question9 */
    public static final String COLUMNNAME_question9 = "question9";

	/** Set question9	  */
	public void setquestion9 (String question9);

	/** Get question9	  */
	public String getquestion9();

    /** Column name score1 */
    public static final String COLUMNNAME_score1 = "score1";

	/** Set score1	  */
	public void setscore1 (BigDecimal score1);

	/** Get score1	  */
	public BigDecimal getscore1();

    /** Column name score10 */
    public static final String COLUMNNAME_score10 = "score10";

	/** Set score10	  */
	public void setscore10 (BigDecimal score10);

	/** Get score10	  */
	public BigDecimal getscore10();

    /** Column name score2 */
    public static final String COLUMNNAME_score2 = "score2";

	/** Set score2	  */
	public void setscore2 (BigDecimal score2);

	/** Get score2	  */
	public BigDecimal getscore2();

    /** Column name score3 */
    public static final String COLUMNNAME_score3 = "score3";

	/** Set score3	  */
	public void setscore3 (BigDecimal score3);

	/** Get score3	  */
	public BigDecimal getscore3();

    /** Column name score4 */
    public static final String COLUMNNAME_score4 = "score4";

	/** Set score4	  */
	public void setscore4 (BigDecimal score4);

	/** Get score4	  */
	public BigDecimal getscore4();

    /** Column name score5 */
    public static final String COLUMNNAME_score5 = "score5";

	/** Set score5	  */
	public void setscore5 (BigDecimal score5);

	/** Get score5	  */
	public BigDecimal getscore5();

    /** Column name score6 */
    public static final String COLUMNNAME_score6 = "score6";

	/** Set score6	  */
	public void setscore6 (BigDecimal score6);

	/** Get score6	  */
	public BigDecimal getscore6();

    /** Column name score7 */
    public static final String COLUMNNAME_score7 = "score7";

	/** Set score7	  */
	public void setscore7 (BigDecimal score7);

	/** Get score7	  */
	public BigDecimal getscore7();

    /** Column name score8 */
    public static final String COLUMNNAME_score8 = "score8";

	/** Set score8	  */
	public void setscore8 (BigDecimal score8);

	/** Get score8	  */
	public BigDecimal getscore8();

    /** Column name score9 */
    public static final String COLUMNNAME_score9 = "score9";

	/** Set score9	  */
	public void setscore9 (BigDecimal score9);

	/** Get score9	  */
	public BigDecimal getscore9();

    /** Column name totalscore */
    public static final String COLUMNNAME_totalscore = "totalscore";

	/** Set totalscore	  */
	public void settotalscore (BigDecimal totalscore);

	/** Get totalscore	  */
	public BigDecimal gettotalscore();

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
