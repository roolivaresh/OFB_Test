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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for HR_EvaluationDriversLine
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_HR_EvaluationDriversLine extends PO implements I_HR_EvaluationDriversLine, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230810L;

    /** Standard Constructor */
    public X_HR_EvaluationDriversLine (Properties ctx, int HR_EvaluationDriversLine_ID, String trxName)
    {
      super (ctx, HR_EvaluationDriversLine_ID, trxName);
      /** if (HR_EvaluationDriversLine_ID == 0)
        {
			setHR_EvaluationDriversLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_HR_EvaluationDriversLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_EvaluationDriversLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set answer1.
		@param answer1 answer1	  */
	@Override
	public void setanswer1 (boolean answer1)
	{
		set_Value (COLUMNNAME_answer1, Boolean.valueOf(answer1));
	}

	/** Get answer1.
		@return answer1	  */
	@Override
	public boolean isanswer1 ()
	{
		Object oo = get_Value(COLUMNNAME_answer1);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set answer10.
		@param answer10 answer10	  */
	@Override
	public void setanswer10 (boolean answer10)
	{
		set_Value (COLUMNNAME_answer10, Boolean.valueOf(answer10));
	}

	/** Get answer10.
		@return answer10	  */
	@Override
	public boolean isanswer10 ()
	{
		Object oo = get_Value(COLUMNNAME_answer10);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set answer2.
		@param answer2 answer2	  */
	@Override
	public void setanswer2 (boolean answer2)
	{
		set_Value (COLUMNNAME_answer2, Boolean.valueOf(answer2));
	}

	/** Get answer2.
		@return answer2	  */
	@Override
	public boolean isanswer2 ()
	{
		Object oo = get_Value(COLUMNNAME_answer2);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set answer3.
		@param answer3 answer3	  */
	@Override
	public void setanswer3 (boolean answer3)
	{
		set_Value (COLUMNNAME_answer3, Boolean.valueOf(answer3));
	}

	/** Get answer3.
		@return answer3	  */
	@Override
	public boolean isanswer3 ()
	{
		Object oo = get_Value(COLUMNNAME_answer3);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set answer4.
		@param answer4 answer4	  */
	@Override
	public void setanswer4 (boolean answer4)
	{
		set_Value (COLUMNNAME_answer4, Boolean.valueOf(answer4));
	}

	/** Get answer4.
		@return answer4	  */
	@Override
	public boolean isanswer4 ()
	{
		Object oo = get_Value(COLUMNNAME_answer4);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set answer5.
		@param answer5 answer5	  */
	@Override
	public void setanswer5 (boolean answer5)
	{
		set_Value (COLUMNNAME_answer5, Boolean.valueOf(answer5));
	}

	/** Get answer5.
		@return answer5	  */
	@Override
	public boolean isanswer5 ()
	{
		Object oo = get_Value(COLUMNNAME_answer5);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set answer6.
		@param answer6 answer6	  */
	@Override
	public void setanswer6 (boolean answer6)
	{
		set_Value (COLUMNNAME_answer6, Boolean.valueOf(answer6));
	}

	/** Get answer6.
		@return answer6	  */
	@Override
	public boolean isanswer6 ()
	{
		Object oo = get_Value(COLUMNNAME_answer6);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set answer7.
		@param answer7 answer7	  */
	@Override
	public void setanswer7 (boolean answer7)
	{
		set_Value (COLUMNNAME_answer7, Boolean.valueOf(answer7));
	}

	/** Get answer7.
		@return answer7	  */
	@Override
	public boolean isanswer7 ()
	{
		Object oo = get_Value(COLUMNNAME_answer7);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set answer8.
		@param answer8 answer8	  */
	@Override
	public void setanswer8 (boolean answer8)
	{
		set_Value (COLUMNNAME_answer8, Boolean.valueOf(answer8));
	}

	/** Get answer8.
		@return answer8	  */
	@Override
	public boolean isanswer8 ()
	{
		Object oo = get_Value(COLUMNNAME_answer8);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set answer9.
		@param answer9 answer9	  */
	@Override
	public void setanswer9 (boolean answer9)
	{
		set_Value (COLUMNNAME_answer9, Boolean.valueOf(answer9));
	}

	/** Get answer9.
		@return answer9	  */
	@Override
	public boolean isanswer9 ()
	{
		Object oo = get_Value(COLUMNNAME_answer9);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/** Set HR_EvaluationDrivers.
		@param HR_EvaluationDrivers_ID HR_EvaluationDrivers	  */
	@Override
	public void setHR_EvaluationDrivers_ID (int HR_EvaluationDrivers_ID)
	{
		if (HR_EvaluationDrivers_ID < 1)
			set_Value (COLUMNNAME_HR_EvaluationDrivers_ID, null);
		else
			set_Value (COLUMNNAME_HR_EvaluationDrivers_ID, Integer.valueOf(HR_EvaluationDrivers_ID));
	}

	/** Get HR_EvaluationDrivers.
		@return HR_EvaluationDrivers	  */
	@Override
	public int getHR_EvaluationDrivers_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_EvaluationDrivers_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HR_EvaluationDriversLine.
		@param HR_EvaluationDriversLine_ID HR_EvaluationDriversLine	  */
	@Override
	public void setHR_EvaluationDriversLine_ID (int HR_EvaluationDriversLine_ID)
	{
		if (HR_EvaluationDriversLine_ID < 1)
			set_ValueNoCheck (COLUMNNAME_HR_EvaluationDriversLine_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_HR_EvaluationDriversLine_ID, Integer.valueOf(HR_EvaluationDriversLine_ID));
	}

	/** Get HR_EvaluationDriversLine.
		@return HR_EvaluationDriversLine	  */
	@Override
	public int getHR_EvaluationDriversLine_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_EvaluationDriversLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Name.
		@param Name
		Alphanumeric identifier of the entity
	  */
	@Override
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	@Override
	public String getName ()
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair()
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set question1_drive.
		@param question1_drive question1_drive	  */
	@Override
	public void setquestion1_drive (String question1_drive)
	{
		set_Value (COLUMNNAME_question1_drive, question1_drive);
	}

	/** Get question1_drive.
		@return question1_drive	  */
	@Override
	public String getquestion1_drive ()
	{
		return (String)get_Value(COLUMNNAME_question1_drive);
	}

	/** Set question10_drive.
		@param question10_drive question10_drive	  */
	@Override
	public void setquestion10_drive (String question10_drive)
	{
		set_Value (COLUMNNAME_question10_drive, question10_drive);
	}

	/** Get question10_drive.
		@return question10_drive	  */
	@Override
	public String getquestion10_drive ()
	{
		return (String)get_Value(COLUMNNAME_question10_drive);
	}

	/** Set question2_drive.
		@param question2_drive question2_drive	  */
	@Override
	public void setquestion2_drive (String question2_drive)
	{
		set_Value (COLUMNNAME_question2_drive, question2_drive);
	}

	/** Get question2_drive.
		@return question2_drive	  */
	@Override
	public String getquestion2_drive ()
	{
		return (String)get_Value(COLUMNNAME_question2_drive);
	}

	/** Set question3_drive.
		@param question3_drive question3_drive	  */
	@Override
	public void setquestion3_drive (String question3_drive)
	{
		set_Value (COLUMNNAME_question3_drive, question3_drive);
	}

	/** Get question3_drive.
		@return question3_drive	  */
	@Override
	public String getquestion3_drive ()
	{
		return (String)get_Value(COLUMNNAME_question3_drive);
	}

	/** Set question4_drive.
		@param question4_drive question4_drive	  */
	@Override
	public void setquestion4_drive (String question4_drive)
	{
		set_Value (COLUMNNAME_question4_drive, question4_drive);
	}

	/** Get question4_drive.
		@return question4_drive	  */
	@Override
	public String getquestion4_drive ()
	{
		return (String)get_Value(COLUMNNAME_question4_drive);
	}

	/** Set question5_drive.
		@param question5_drive question5_drive	  */
	@Override
	public void setquestion5_drive (String question5_drive)
	{
		set_Value (COLUMNNAME_question5_drive, question5_drive);
	}

	/** Get question5_drive.
		@return question5_drive	  */
	@Override
	public String getquestion5_drive ()
	{
		return (String)get_Value(COLUMNNAME_question5_drive);
	}

	/** Set question6_drive.
		@param question6_drive question6_drive	  */
	@Override
	public void setquestion6_drive (String question6_drive)
	{
		set_Value (COLUMNNAME_question6_drive, question6_drive);
	}

	/** Get question6_drive.
		@return question6_drive	  */
	@Override
	public String getquestion6_drive ()
	{
		return (String)get_Value(COLUMNNAME_question6_drive);
	}

	/** Set question7_drive.
		@param question7_drive question7_drive	  */
	@Override
	public void setquestion7_drive (String question7_drive)
	{
		set_Value (COLUMNNAME_question7_drive, question7_drive);
	}

	/** Get question7_drive.
		@return question7_drive	  */
	@Override
	public String getquestion7_drive ()
	{
		return (String)get_Value(COLUMNNAME_question7_drive);
	}

	/** Set question8_drive.
		@param question8_drive question8_drive	  */
	@Override
	public void setquestion8_drive (String question8_drive)
	{
		set_Value (COLUMNNAME_question8_drive, question8_drive);
	}

	/** Get question8_drive.
		@return question8_drive	  */
	@Override
	public String getquestion8_drive ()
	{
		return (String)get_Value(COLUMNNAME_question8_drive);
	}

	/** Set question9_drive.
		@param question9_drive question9_drive	  */
	@Override
	public void setquestion9_drive (String question9_drive)
	{
		set_Value (COLUMNNAME_question9_drive, question9_drive);
	}

	/** Get question9_drive.
		@return question9_drive	  */
	@Override
	public String getquestion9_drive ()
	{
		return (String)get_Value(COLUMNNAME_question9_drive);
	}

	/** Set Result.
		@param Result
		Result of the action taken
	  */
	@Override
	public void setResult (BigDecimal Result)
	{
		set_Value (COLUMNNAME_Result, Result);
	}

	/** Get Result.
		@return Result of the action taken
	  */
	@Override
	public BigDecimal getResult ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Result);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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