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

/** Generated Model for c_bpartner_info_oth
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_C_BPartner_Info_Oth extends PO implements I_C_BPartner_Info_Oth, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230816L;

    /** Standard Constructor */
    public X_C_BPartner_Info_Oth (Properties ctx, int c_bpartner_info_oth_ID, String trxName)
    {
      super (ctx, c_bpartner_info_oth_ID, trxName);
      /** if (c_bpartner_info_oth_ID == 0)
        {
			setc_bpartner_info_oth_ID (0);
        } */
    }

    /** Load Constructor */
    public X_C_BPartner_Info_Oth (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_c_bpartner_info_oth[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set c_bpartner_info_oth.
		@param c_bpartner_info_oth_ID c_bpartner_info_oth	  */
	@Override
	public void setc_bpartner_info_oth_ID (int c_bpartner_info_oth_ID)
	{
		if (c_bpartner_info_oth_ID < 1)
			set_ValueNoCheck (COLUMNNAME_c_bpartner_info_oth_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_c_bpartner_info_oth_ID, Integer.valueOf(c_bpartner_info_oth_ID));
	}

	/** Get c_bpartner_info_oth.
		@return c_bpartner_info_oth	  */
	@Override
	public int getc_bpartner_info_oth_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_c_bpartner_info_oth_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set education.
		@param education education	  */
	@Override
	public void seteducation (String education)
	{
		set_Value (COLUMNNAME_education, education);
	}

	/** Get education.
		@return education	  */
	@Override
	public String geteducation ()
	{
		return (String)get_Value(COLUMNNAME_education);
	}

	/** Set ethnicity.
		@param ethnicity ethnicity	  */
	@Override
	public void setethnicity (String ethnicity)
	{
		set_Value (COLUMNNAME_ethnicity, ethnicity);
	}

	/** Get ethnicity.
		@return ethnicity	  */
	@Override
	public String getethnicity ()
	{
		return (String)get_Value(COLUMNNAME_ethnicity);
	}

	/** Set health_insurance.
		@param health_insurance health_insurance	  */
	@Override
	public void sethealth_insurance (String health_insurance)
	{
		set_Value (COLUMNNAME_health_insurance, health_insurance);
	}

	/** Get health_insurance.
		@return health_insurance	  */
	@Override
	public String gethealth_insurance ()
	{
		return (String)get_Value(COLUMNNAME_health_insurance);
	}

	/** Set health_insurance_level.
		@param health_insurance_level health_insurance_level	  */
	@Override
	public void sethealth_insurance_level (String health_insurance_level)
	{
		set_Value (COLUMNNAME_health_insurance_level, health_insurance_level);
	}

	/** Get health_insurance_level.
		@return health_insurance_level	  */
	@Override
	public String gethealth_insurance_level ()
	{
		return (String)get_Value(COLUMNNAME_health_insurance_level);
	}

	/** Set health_insurance_type.
		@param health_insurance_type health_insurance_type	  */
	@Override
	public void sethealth_insurance_type (String health_insurance_type)
	{
		set_Value (COLUMNNAME_health_insurance_type, health_insurance_type);
	}

	/** Get health_insurance_type.
		@return health_insurance_type	  */
	@Override
	public String gethealth_insurance_type ()
	{
		return (String)get_Value(COLUMNNAME_health_insurance_type);
	}

	/** Set nationality.
		@param nationality nationality	  */
	@Override
	public void setnationality (String nationality)
	{
		set_Value (COLUMNNAME_nationality, nationality);
	}

	/** Get nationality.
		@return nationality	  */
	@Override
	public String getnationality ()
	{
		return (String)get_Value(COLUMNNAME_nationality);
	}

	/** Set occupation.
		@param occupation occupation	  */
	@Override
	public void setoccupation (String occupation)
	{
		set_Value (COLUMNNAME_occupation, occupation);
	}

	/** Get occupation.
		@return occupation	  */
	@Override
	public String getoccupation ()
	{
		return (String)get_Value(COLUMNNAME_occupation);
	}

	/** Set occupation_detail.
		@param occupation_detail occupation_detail	  */
	@Override
	public void setoccupation_detail (String occupation_detail)
	{
		set_Value (COLUMNNAME_occupation_detail, occupation_detail);
	}

	/** Get occupation_detail.
		@return occupation_detail	  */
	@Override
	public String getoccupation_detail ()
	{
		return (String)get_Value(COLUMNNAME_occupation_detail);
	}

	/** Set occupational_category.
		@param occupational_category occupational_category	  */
	@Override
	public void setoccupational_category (String occupational_category)
	{
		set_Value (COLUMNNAME_occupational_category, occupational_category);
	}

	/** Get occupational_category.
		@return occupational_category	  */
	@Override
	public String getoccupational_category ()
	{
		return (String)get_Value(COLUMNNAME_occupational_category);
	}

	/** Set religion.
		@param religion religion	  */
	@Override
	public void setreligion (String religion)
	{
		set_Value (COLUMNNAME_religion, religion);
	}

	/** Get religion.
		@return religion	  */
	@Override
	public String getreligion ()
	{
		return (String)get_Value(COLUMNNAME_religion);
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