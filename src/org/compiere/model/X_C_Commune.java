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

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.KeyNamePair;

/** Generated Model for C_Commune
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_C_Commune extends PO implements I_C_Commune, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20231002L;

    /** Standard Constructor */
    public X_C_Commune (Properties ctx, int C_Commune_ID, String trxName)
    {
      super (ctx, C_Commune_ID, trxName);
      /** if (C_Commune_ID == 0)
        {
			setC_Commune_ID (0);
			setC_Country_ID (0);
			setName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_C_Commune (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client
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
      StringBuffer sb = new StringBuffer ("X_C_Commune[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Commune.
		@param C_Commune_ID Commune	  */
	@Override
	public void setC_Commune_ID (int C_Commune_ID)
	{
		if (C_Commune_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_Commune_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_Commune_ID, Integer.valueOf(C_Commune_ID));
	}

	/** Get Commune.
		@return Commune	  */
	@Override
	public int getC_Commune_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Commune_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	@Override
	public org.adempiere.core.domains.models.I_C_Country getC_Country() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Country)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Country.Table_Name)
			.getPO(getC_Country_ID(), get_TrxName());	}

	/** Set Country.
		@param C_Country_ID
		Country
	  */
	@Override
	public void setC_Country_ID (int C_Country_ID)
	{
		if (C_Country_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_Country_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_Country_ID, Integer.valueOf(C_Country_ID));
	}

	/** Get Country.
		@return Country
	  */
	@Override
	public int getC_Country_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Country_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_Province.
		@param C_Province_ID C_Province	  */
	@Override
	public void setC_Province_ID (int C_Province_ID)
	{
		if (C_Province_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_Province_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_Province_ID, Integer.valueOf(C_Province_ID));
	}

	/** Get C_Province.
		@return C_Province	  */
	@Override
	public int getC_Province_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Province_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Default.
		@param IsDefault
		Default value
	  */
	@Override
	public void setIsDefault (boolean IsDefault)
	{
		set_Value (COLUMNNAME_IsDefault, Boolean.valueOf(IsDefault));
	}

	/** Get Default.
		@return Default value
	  */
	@Override
	public boolean isDefault ()
	{
		Object oo = get_Value(COLUMNNAME_IsDefault);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Search Key.
		@param Value
		Search key for the record in the format required - must be unique
	  */
	@Override
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	@Override
	public String getValue ()
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}