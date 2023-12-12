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

/** Generated Model for C_BPBenefit
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_C_BPBenefit extends PO implements I_C_BPBenefit, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230627L;

    /** Standard Constructor */
    public X_C_BPBenefit (Properties ctx, int C_BPBenefit_ID, String trxName)
    {
      super (ctx, C_BPBenefit_ID, trxName);
      /** if (C_BPBenefit_ID == 0)
        {
			setC_BPBenefit_ID (0);
        } */
    }

    /** Load Constructor */
    public X_C_BPBenefit (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_BPBenefit[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Allocated Amountt.
		@param AllocatedAmt
		Amount allocated to this document
	  */
	@Override
	public void setAllocatedAmt (BigDecimal AllocatedAmt)
	{
		set_Value (COLUMNNAME_AllocatedAmt, AllocatedAmt);
	}

	/** Get Allocated Amountt.
		@return Amount allocated to this document
	  */
	@Override
	public BigDecimal getAllocatedAmt ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AllocatedAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Available Amount.
		@param AvailableAmt
		Amount available for allocation for this document
	  */
	@Override
	public void setAvailableAmt (BigDecimal AvailableAmt)
	{
		set_Value (COLUMNNAME_AvailableAmt, AvailableAmt);
	}

	/** Get Available Amount.
		@return Amount available for allocation for this document
	  */
	@Override
	public BigDecimal getAvailableAmt ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AvailableAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set C_BPBenefit.
		@param C_BPBenefit_ID C_BPBenefit	  */
	@Override
	public void setC_BPBenefit_ID (int C_BPBenefit_ID)
	{
		if (C_BPBenefit_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_BPBenefit_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_BPBenefit_ID, Integer.valueOf(C_BPBenefit_ID));
	}

	/** Get C_BPBenefit.
		@return C_BPBenefit	  */
	@Override
	public int getC_BPBenefit_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPBenefit_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	@Override
	public org.adempiere.core.domains.models.I_C_Period getC_Period() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Period)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID
		Period of the Calendar
	  */
	@Override
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1)
			set_Value (COLUMNNAME_C_Period_ID, null);
		else
			set_Value (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	@Override
	public int getC_Period_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Total Amount.
		@param TotalAmt
		Total Amount
	  */
	@Override
	public void setTotalAmt (BigDecimal TotalAmt)
	{
		set_Value (COLUMNNAME_TotalAmt, TotalAmt);
	}

	/** Get Total Amount.
		@return Total Amount
	  */
	@Override
	public BigDecimal getTotalAmt ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalAmt);
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