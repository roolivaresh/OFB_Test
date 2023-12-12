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
import java.util.Properties;

import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for TP_Fleet
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_TP_Fleet extends PO implements I_TP_Fleet, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230810L;

    /** Standard Constructor */
    public X_TP_Fleet (Properties ctx, int TP_Fleet_ID, String trxName)
    {
      super (ctx, TP_Fleet_ID, trxName);
      /** if (TP_Fleet_ID == 0)
        {
			setAD_User_ID (0);
			setisholiday (false);
			setisintegration (false);
			setisoperational (false);
			setTP_Fleet_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TP_Fleet (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TP_Fleet[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	@Override
	public org.adempiere.core.domains.models.I_AD_User getAD_User() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_AD_User)MTable.get(getCtx(), org.adempiere.core.domains.models.I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set User/Contact.
		@param AD_User_ID
		User within the system - Internal or Business Partner Contact
	  */
	@Override
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1)
			set_Value (COLUMNNAME_AD_User_ID, null);
		else
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	@Override
	public int getAD_User_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set bp_working.
		@param bp_working bp_working	  */
	@Override
	public void setbp_working (int bp_working)
	{
		set_Value (COLUMNNAME_bp_working, Integer.valueOf(bp_working));
	}

	/** Get bp_working.
		@return bp_working	  */
	@Override
	public int getbp_working ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_bp_working);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Category.
		@param Category Category	  */
	@Override
	public void setCategory (String Category)
	{
		set_Value (COLUMNNAME_Category, Category);
	}

	/** Get Category.
		@return Category	  */
	@Override
	public String getCategory ()
	{
		return (String)get_Value(COLUMNNAME_Category);
	}

	/** Set categoryclient.
		@param categoryclient categoryclient	  */
	@Override
	public void setcategoryclient (String categoryclient)
	{
		set_Value (COLUMNNAME_categoryclient, categoryclient);
	}

	/** Get categoryclient.
		@return categoryclient	  */
	@Override
	public String getcategoryclient ()
	{
		return (String)get_Value(COLUMNNAME_categoryclient);
	}

	/** Set day_week.
		@param day_week day_week	  */
	@Override
	public void setday_week (boolean day_week)
	{
		set_Value (COLUMNNAME_day_week, Boolean.valueOf(day_week));
	}

	/** Get day_week.
		@return day_week	  */
	@Override
	public boolean isday_week ()
	{
		Object oo = get_Value(COLUMNNAME_day_week);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set dayweekavg.
		@param dayweekavg dayweekavg	  */
	@Override
	public void setdayweekavg (BigDecimal dayweekavg)
	{
		set_Value (COLUMNNAME_dayweekavg, dayweekavg);
	}

	/** Get dayweekavg.
		@return dayweekavg	  */
	@Override
	public BigDecimal getdayweekavg ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_dayweekavg);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set isholiday.
		@param isholiday isholiday	  */
	@Override
	public void setisholiday (boolean isholiday)
	{
		set_Value (COLUMNNAME_isholiday, Boolean.valueOf(isholiday));
	}

	/** Get isholiday.
		@return isholiday	  */
	@Override
	public boolean isholiday ()
	{
		Object oo = get_Value(COLUMNNAME_isholiday);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set isintegration.
		@param isintegration isintegration	  */
	@Override
	public void setisintegration (boolean isintegration)
	{
		set_Value (COLUMNNAME_isintegration, Boolean.valueOf(isintegration));
	}

	/** Get isintegration.
		@return isintegration	  */
	@Override
	public boolean isintegration ()
	{
		Object oo = get_Value(COLUMNNAME_isintegration);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set isoperational.
		@param isoperational isoperational	  */
	@Override
	public void setisoperational (boolean isoperational)
	{
		set_Value (COLUMNNAME_isoperational, Boolean.valueOf(isoperational));
	}

	/** Get isoperational.
		@return isoperational	  */
	@Override
	public boolean isoperational ()
	{
		Object oo = get_Value(COLUMNNAME_isoperational);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set max_tons.
		@param max_tons max_tons	  */
	@Override
	public void setmax_tons (BigDecimal max_tons)
	{
		set_Value (COLUMNNAME_max_tons, max_tons);
	}

	/** Get max_tons.
		@return max_tons	  */
	@Override
	public BigDecimal getmax_tons ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_max_tons);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set tp_costingtype.
		@param tp_costingtype tp_costingtype	  */
	@Override
	public void settp_costingtype (String tp_costingtype)
	{
		set_Value (COLUMNNAME_tp_costingtype, tp_costingtype);
	}

	/** Get tp_costingtype.
		@return tp_costingtype	  */
	@Override
	public String gettp_costingtype ()
	{
		return (String)get_Value(COLUMNNAME_tp_costingtype);
	}

	/** Set TP_Fleet_ID.
		@param TP_Fleet_ID TP_Fleet_ID	  */
	@Override
	public void setTP_Fleet_ID (int TP_Fleet_ID)
	{
		if (TP_Fleet_ID < 1)
			set_ValueNoCheck (COLUMNNAME_TP_Fleet_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_TP_Fleet_ID, Integer.valueOf(TP_Fleet_ID));
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

	/** Set tp_hdrtype.
		@param tp_hdrtype tp_hdrtype	  */
	@Override
	public void settp_hdrtype (String tp_hdrtype)
	{
		set_Value (COLUMNNAME_tp_hdrtype, tp_hdrtype);
	}

	/** Get tp_hdrtype.
		@return tp_hdrtype	  */
	@Override
	public String gettp_hdrtype ()
	{
		return (String)get_Value(COLUMNNAME_tp_hdrtype);
	}

	/** Set tp_location.
		@param tp_location tp_location	  */
	@Override
	public void settp_location (String tp_location)
	{
		set_Value (COLUMNNAME_tp_location, tp_location);
	}

	/** Get tp_location.
		@return tp_location	  */
	@Override
	public String gettp_location ()
	{
		return (String)get_Value(COLUMNNAME_tp_location);
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

	/** Set workshift.
		@param workshift workshift	  */
	@Override
	public void setworkshift (String workshift)
	{
		set_Value (COLUMNNAME_workshift, workshift);
	}

	/** Get workshift.
		@return workshift	  */
	@Override
	public String getworkshift ()
	{
		return (String)get_Value(COLUMNNAME_workshift);
	}
}