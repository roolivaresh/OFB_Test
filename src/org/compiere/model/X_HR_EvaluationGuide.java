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

/** Generated Model for HR_EvaluationGuide
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_HR_EvaluationGuide extends PO implements I_HR_EvaluationGuide, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230823L;

    /** Standard Constructor */
    public X_HR_EvaluationGuide (Properties ctx, int HR_EvaluationGuide_ID, String trxName)
    {
      super (ctx, HR_EvaluationGuide_ID, trxName);
      /** if (HR_EvaluationGuide_ID == 0)
        {
			sethr_evaluationguide_id (0);
        } */
    }

    /** Load Constructor */
    public X_HR_EvaluationGuide (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_EvaluationGuide[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set expectedresult1.
		@param expectedresult1 expectedresult1	  */
	@Override
	public void setexpectedresult1 (boolean expectedresult1)
	{
		set_Value (COLUMNNAME_expectedresult1, Boolean.valueOf(expectedresult1));
	}

	/** Get expectedresult1.
		@return expectedresult1	  */
	@Override
	public boolean isexpectedresult1 ()
	{
		Object oo = get_Value(COLUMNNAME_expectedresult1);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set expectedresult10.
		@param expectedresult10 expectedresult10	  */
	@Override
	public void setexpectedresult10 (boolean expectedresult10)
	{
		set_Value (COLUMNNAME_expectedresult10, Boolean.valueOf(expectedresult10));
	}

	/** Get expectedresult10.
		@return expectedresult10	  */
	@Override
	public boolean isexpectedresult10 ()
	{
		Object oo = get_Value(COLUMNNAME_expectedresult10);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set expectedresult2.
		@param expectedresult2 expectedresult2	  */
	@Override
	public void setexpectedresult2 (boolean expectedresult2)
	{
		set_Value (COLUMNNAME_expectedresult2, Boolean.valueOf(expectedresult2));
	}

	/** Get expectedresult2.
		@return expectedresult2	  */
	@Override
	public boolean isexpectedresult2 ()
	{
		Object oo = get_Value(COLUMNNAME_expectedresult2);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set expectedresult3.
		@param expectedresult3 expectedresult3	  */
	@Override
	public void setexpectedresult3 (boolean expectedresult3)
	{
		set_Value (COLUMNNAME_expectedresult3, Boolean.valueOf(expectedresult3));
	}

	/** Get expectedresult3.
		@return expectedresult3	  */
	@Override
	public boolean isexpectedresult3 ()
	{
		Object oo = get_Value(COLUMNNAME_expectedresult3);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set expectedresult4.
		@param expectedresult4 expectedresult4	  */
	@Override
	public void setexpectedresult4 (boolean expectedresult4)
	{
		set_Value (COLUMNNAME_expectedresult4, Boolean.valueOf(expectedresult4));
	}

	/** Get expectedresult4.
		@return expectedresult4	  */
	@Override
	public boolean isexpectedresult4 ()
	{
		Object oo = get_Value(COLUMNNAME_expectedresult4);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set expectedresult5.
		@param expectedresult5 expectedresult5	  */
	@Override
	public void setexpectedresult5 (boolean expectedresult5)
	{
		set_Value (COLUMNNAME_expectedresult5, Boolean.valueOf(expectedresult5));
	}

	/** Get expectedresult5.
		@return expectedresult5	  */
	@Override
	public boolean isexpectedresult5 ()
	{
		Object oo = get_Value(COLUMNNAME_expectedresult5);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set expectedresult6.
		@param expectedresult6 expectedresult6	  */
	@Override
	public void setexpectedresult6 (boolean expectedresult6)
	{
		set_Value (COLUMNNAME_expectedresult6, Boolean.valueOf(expectedresult6));
	}

	/** Get expectedresult6.
		@return expectedresult6	  */
	@Override
	public boolean isexpectedresult6 ()
	{
		Object oo = get_Value(COLUMNNAME_expectedresult6);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set expectedresult7.
		@param expectedresult7 expectedresult7	  */
	@Override
	public void setexpectedresult7 (boolean expectedresult7)
	{
		set_Value (COLUMNNAME_expectedresult7, Boolean.valueOf(expectedresult7));
	}

	/** Get expectedresult7.
		@return expectedresult7	  */
	@Override
	public boolean isexpectedresult7 ()
	{
		Object oo = get_Value(COLUMNNAME_expectedresult7);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set expectedresult8.
		@param expectedresult8 expectedresult8	  */
	@Override
	public void setexpectedresult8 (boolean expectedresult8)
	{
		set_Value (COLUMNNAME_expectedresult8, Boolean.valueOf(expectedresult8));
	}

	/** Get expectedresult8.
		@return expectedresult8	  */
	@Override
	public boolean isexpectedresult8 ()
	{
		Object oo = get_Value(COLUMNNAME_expectedresult8);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set expectedresult9.
		@param expectedresult9 expectedresult9	  */
	@Override
	public void setexpectedresult9 (boolean expectedresult9)
	{
		set_Value (COLUMNNAME_expectedresult9, Boolean.valueOf(expectedresult9));
	}

	/** Get expectedresult9.
		@return expectedresult9	  */
	@Override
	public boolean isexpectedresult9 ()
	{
		Object oo = get_Value(COLUMNNAME_expectedresult9);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set hr_evaluationguide_id.
		@param hr_evaluationguide_id hr_evaluationguide_id	  */
	@Override
	public void sethr_evaluationguide_id (int hr_evaluationguide_id)
	{
		set_ValueNoCheck (COLUMNNAME_hr_evaluationguide_id, Integer.valueOf(hr_evaluationguide_id));
	}

	/** Get hr_evaluationguide_id.
		@return hr_evaluationguide_id	  */
	@Override
	public int gethr_evaluationguide_id ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_hr_evaluationguide_id);
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

	/** Set question1.
		@param question1 question1	  */
	@Override
	public void setquestion1 (String question1)
	{
		set_Value (COLUMNNAME_question1, question1);
	}

	/** Get question1.
		@return question1	  */
	@Override
	public String getquestion1 ()
	{
		return (String)get_Value(COLUMNNAME_question1);
	}

	/** Set question10.
		@param question10 question10	  */
	@Override
	public void setquestion10 (String question10)
	{
		set_Value (COLUMNNAME_question10, question10);
	}

	/** Get question10.
		@return question10	  */
	@Override
	public String getquestion10 ()
	{
		return (String)get_Value(COLUMNNAME_question10);
	}

	/** Set question2.
		@param question2 question2	  */
	@Override
	public void setquestion2 (String question2)
	{
		set_Value (COLUMNNAME_question2, question2);
	}

	/** Get question2.
		@return question2	  */
	@Override
	public String getquestion2 ()
	{
		return (String)get_Value(COLUMNNAME_question2);
	}

	/** Set question3.
		@param question3 question3	  */
	@Override
	public void setquestion3 (String question3)
	{
		set_Value (COLUMNNAME_question3, question3);
	}

	/** Get question3.
		@return question3	  */
	@Override
	public String getquestion3 ()
	{
		return (String)get_Value(COLUMNNAME_question3);
	}

	/** Set question4.
		@param question4 question4	  */
	@Override
	public void setquestion4 (String question4)
	{
		set_Value (COLUMNNAME_question4, question4);
	}

	/** Get question4.
		@return question4	  */
	@Override
	public String getquestion4 ()
	{
		return (String)get_Value(COLUMNNAME_question4);
	}

	/** Set question5.
		@param question5 question5	  */
	@Override
	public void setquestion5 (String question5)
	{
		set_Value (COLUMNNAME_question5, question5);
	}

	/** Get question5.
		@return question5	  */
	@Override
	public String getquestion5 ()
	{
		return (String)get_Value(COLUMNNAME_question5);
	}

	/** Set question6.
		@param question6 question6	  */
	@Override
	public void setquestion6 (String question6)
	{
		set_Value (COLUMNNAME_question6, question6);
	}

	/** Get question6.
		@return question6	  */
	@Override
	public String getquestion6 ()
	{
		return (String)get_Value(COLUMNNAME_question6);
	}

	/** Set question7.
		@param question7 question7	  */
	@Override
	public void setquestion7 (String question7)
	{
		set_Value (COLUMNNAME_question7, question7);
	}

	/** Get question7.
		@return question7	  */
	@Override
	public String getquestion7 ()
	{
		return (String)get_Value(COLUMNNAME_question7);
	}

	/** Set question8.
		@param question8 question8	  */
	@Override
	public void setquestion8 (String question8)
	{
		set_Value (COLUMNNAME_question8, question8);
	}

	/** Get question8.
		@return question8	  */
	@Override
	public String getquestion8 ()
	{
		return (String)get_Value(COLUMNNAME_question8);
	}

	/** Set question9.
		@param question9 question9	  */
	@Override
	public void setquestion9 (String question9)
	{
		set_Value (COLUMNNAME_question9, question9);
	}

	/** Get question9.
		@return question9	  */
	@Override
	public String getquestion9 ()
	{
		return (String)get_Value(COLUMNNAME_question9);
	}

	/** Set score1.
		@param score1 score1	  */
	@Override
	public void setscore1 (BigDecimal score1)
	{
		set_Value (COLUMNNAME_score1, score1);
	}

	/** Get score1.
		@return score1	  */
	@Override
	public BigDecimal getscore1 ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_score1);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set score10.
		@param score10 score10	  */
	@Override
	public void setscore10 (BigDecimal score10)
	{
		set_Value (COLUMNNAME_score10, score10);
	}

	/** Get score10.
		@return score10	  */
	@Override
	public BigDecimal getscore10 ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_score10);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set score2.
		@param score2 score2	  */
	@Override
	public void setscore2 (BigDecimal score2)
	{
		set_Value (COLUMNNAME_score2, score2);
	}

	/** Get score2.
		@return score2	  */
	@Override
	public BigDecimal getscore2 ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_score2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set score3.
		@param score3 score3	  */
	@Override
	public void setscore3 (BigDecimal score3)
	{
		set_Value (COLUMNNAME_score3, score3);
	}

	/** Get score3.
		@return score3	  */
	@Override
	public BigDecimal getscore3 ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_score3);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set score4.
		@param score4 score4	  */
	@Override
	public void setscore4 (BigDecimal score4)
	{
		set_Value (COLUMNNAME_score4, score4);
	}

	/** Get score4.
		@return score4	  */
	@Override
	public BigDecimal getscore4 ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_score4);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set score5.
		@param score5 score5	  */
	@Override
	public void setscore5 (BigDecimal score5)
	{
		set_Value (COLUMNNAME_score5, score5);
	}

	/** Get score5.
		@return score5	  */
	@Override
	public BigDecimal getscore5 ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_score5);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set score6.
		@param score6 score6	  */
	@Override
	public void setscore6 (BigDecimal score6)
	{
		set_Value (COLUMNNAME_score6, score6);
	}

	/** Get score6.
		@return score6	  */
	@Override
	public BigDecimal getscore6 ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_score6);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set score7.
		@param score7 score7	  */
	@Override
	public void setscore7 (BigDecimal score7)
	{
		set_Value (COLUMNNAME_score7, score7);
	}

	/** Get score7.
		@return score7	  */
	@Override
	public BigDecimal getscore7 ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_score7);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set score8.
		@param score8 score8	  */
	@Override
	public void setscore8 (BigDecimal score8)
	{
		set_Value (COLUMNNAME_score8, score8);
	}

	/** Get score8.
		@return score8	  */
	@Override
	public BigDecimal getscore8 ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_score8);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set score9.
		@param score9 score9	  */
	@Override
	public void setscore9 (BigDecimal score9)
	{
		set_Value (COLUMNNAME_score9, score9);
	}

	/** Get score9.
		@return score9	  */
	@Override
	public BigDecimal getscore9 ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_score9);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set totalscore.
		@param totalscore totalscore	  */
	@Override
	public void settotalscore (BigDecimal totalscore)
	{
		set_Value (COLUMNNAME_totalscore, totalscore);
	}

	/** Get totalscore.
		@return totalscore	  */
	@Override
	public BigDecimal gettotalscore ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_totalscore);
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