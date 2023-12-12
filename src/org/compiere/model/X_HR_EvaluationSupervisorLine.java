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

/** Generated Model for HR_EvaluationSupervisorLine
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_HR_EvaluationSupervisorLine extends PO implements I_HR_EvaluationSupervisorLine, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230823L;

    /** Standard Constructor */
    public X_HR_EvaluationSupervisorLine (Properties ctx, int HR_EvaluationSupervisorLine_ID, String trxName)
    {
      super (ctx, HR_EvaluationSupervisorLine_ID, trxName);
      /** if (HR_EvaluationSupervisorLine_ID == 0)
        {
			setHR_EvaluationSupervisorLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_HR_EvaluationSupervisorLine (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 4 - System
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
      StringBuffer sb = new StringBuffer ("X_HR_EvaluationSupervisorLine[")
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

	/** Set HR_EvaluationSupervisor.
		@param HR_EvaluationSupervisor_ID HR_EvaluationSupervisor	  */
	@Override
	public void setHR_EvaluationSupervisor_ID (int HR_EvaluationSupervisor_ID)
	{
		if (HR_EvaluationSupervisor_ID < 1)
			set_Value (COLUMNNAME_HR_EvaluationSupervisor_ID, null);
		else
			set_Value (COLUMNNAME_HR_EvaluationSupervisor_ID, Integer.valueOf(HR_EvaluationSupervisor_ID));
	}

	/** Get HR_EvaluationSupervisor.
		@return HR_EvaluationSupervisor	  */
	@Override
	public int getHR_EvaluationSupervisor_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_EvaluationSupervisor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HR_EvaluationSupervisorLine.
		@param HR_EvaluationSupervisorLine_ID HR_EvaluationSupervisorLine	  */
	@Override
	public void setHR_EvaluationSupervisorLine_ID (int HR_EvaluationSupervisorLine_ID)
	{
		if (HR_EvaluationSupervisorLine_ID < 1)
			set_ValueNoCheck (COLUMNNAME_HR_EvaluationSupervisorLine_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_HR_EvaluationSupervisorLine_ID, Integer.valueOf(HR_EvaluationSupervisorLine_ID));
	}

	/** Get HR_EvaluationSupervisorLine.
		@return HR_EvaluationSupervisorLine	  */
	@Override
	public int getHR_EvaluationSupervisorLine_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_EvaluationSupervisorLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set question1_supervisor.
		@param question1_supervisor question1_supervisor	  */
	@Override
	public void setquestion1_supervisor (String question1_supervisor)
	{
		set_Value (COLUMNNAME_question1_supervisor, question1_supervisor);
	}

	/** Get question1_supervisor.
		@return question1_supervisor	  */
	@Override
	public String getquestion1_supervisor ()
	{
		return (String)get_Value(COLUMNNAME_question1_supervisor);
	}

	/** Set question2_supervisor.
		@param question2_supervisor question2_supervisor	  */
	@Override
	public void setquestion2_supervisor (String question2_supervisor)
	{
		set_Value (COLUMNNAME_question2_supervisor, question2_supervisor);
	}

	/** Get question2_supervisor.
		@return question2_supervisor	  */
	@Override
	public String getquestion2_supervisor ()
	{
		return (String)get_Value(COLUMNNAME_question2_supervisor);
	}

	/** Set question3_supervisor.
		@param question3_supervisor question3_supervisor	  */
	@Override
	public void setquestion3_supervisor (String question3_supervisor)
	{
		set_Value (COLUMNNAME_question3_supervisor, question3_supervisor);
	}

	/** Get question3_supervisor.
		@return question3_supervisor	  */
	@Override
	public String getquestion3_supervisor ()
	{
		return (String)get_Value(COLUMNNAME_question3_supervisor);
	}

	/** Set question4_supervisor.
		@param question4_supervisor question4_supervisor	  */
	@Override
	public void setquestion4_supervisor (String question4_supervisor)
	{
		set_Value (COLUMNNAME_question4_supervisor, question4_supervisor);
	}

	/** Get question4_supervisor.
		@return question4_supervisor	  */
	@Override
	public String getquestion4_supervisor ()
	{
		return (String)get_Value(COLUMNNAME_question4_supervisor);
	}

	/** Set question5_supervisor.
		@param question5_supervisor question5_supervisor	  */
	@Override
	public void setquestion5_supervisor (String question5_supervisor)
	{
		set_Value (COLUMNNAME_question5_supervisor, question5_supervisor);
	}

	/** Get question5_supervisor.
		@return question5_supervisor	  */
	@Override
	public String getquestion5_supervisor ()
	{
		return (String)get_Value(COLUMNNAME_question5_supervisor);
	}

	/** Set question6_supervisor.
		@param question6_supervisor question6_supervisor	  */
	@Override
	public void setquestion6_supervisor (String question6_supervisor)
	{
		set_Value (COLUMNNAME_question6_supervisor, question6_supervisor);
	}

	/** Get question6_supervisor.
		@return question6_supervisor	  */
	@Override
	public String getquestion6_supervisor ()
	{
		return (String)get_Value(COLUMNNAME_question6_supervisor);
	}

	/** Set question7_supervisor.
		@param question7_supervisor question7_supervisor	  */
	@Override
	public void setquestion7_supervisor (String question7_supervisor)
	{
		set_Value (COLUMNNAME_question7_supervisor, question7_supervisor);
	}

	/** Get question7_supervisor.
		@return question7_supervisor	  */
	@Override
	public String getquestion7_supervisor ()
	{
		return (String)get_Value(COLUMNNAME_question7_supervisor);
	}

	/** Set question8_supervisor.
		@param question8_supervisor question8_supervisor	  */
	@Override
	public void setquestion8_supervisor (String question8_supervisor)
	{
		set_Value (COLUMNNAME_question8_supervisor, question8_supervisor);
	}

	/** Get question8_supervisor.
		@return question8_supervisor	  */
	@Override
	public String getquestion8_supervisor ()
	{
		return (String)get_Value(COLUMNNAME_question8_supervisor);
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

	/** Set TP_Fleet_ID.
		@param TP_Fleet_ID TP_Fleet_ID	  */
	@Override
	public void setTP_Fleet_ID (int TP_Fleet_ID)
	{
		if (TP_Fleet_ID < 1)
			set_Value (COLUMNNAME_TP_Fleet_ID, null);
		else
			set_Value (COLUMNNAME_TP_Fleet_ID, Integer.valueOf(TP_Fleet_ID));
	}

	/** Get TP_Fleet_ID.
		@return TP_Fleet_ID	  */
	@Override
	public int getTP_Fleet_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TP_Fleet_ID);
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
}