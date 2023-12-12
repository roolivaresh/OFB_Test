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
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.KeyNamePair;

/** Generated Model for HR_EvaluationSupervisor
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_HR_EvaluationSupervisor extends PO implements I_HR_EvaluationSupervisor, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230811L;

    /** Standard Constructor */
    public X_HR_EvaluationSupervisor (Properties ctx, int HR_EvaluationSupervisor_ID, String trxName)
    {
      super (ctx, HR_EvaluationSupervisor_ID, trxName);
      /** if (HR_EvaluationSupervisor_ID == 0)
        {
			setDocAction (null);
			setDocStatus (null);
			setDocumentNo (null);
			setHR_EvaluationSupervisor_ID (0);
			setofbbutton2 (null);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_HR_EvaluationSupervisor (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_EvaluationSupervisor[")
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

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID=135;
	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";
	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";
	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";
	/** Post = PO */
	public static final String DOCACTION_Post = "PO";
	/** Void = VO */
	public static final String DOCACTION_Void = "VO";
	/** Close = CL */
	public static final String DOCACTION_Close = "CL";
	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";
	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";
	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";
	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";
	/** <None> = -- */
	public static final String DOCACTION_None = "--";
	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";
	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";
	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";
	/** Drafted = DR */
	public static final String DOCACTION_Drafted = "DR";
	/** Set Document Action.
		@param DocAction
		The targeted status of the document
	  */
	@Override
	public void setDocAction (String DocAction)
	{

		set_Value (COLUMNNAME_DocAction, DocAction);
	}

	/** Get Document Action.
		@return The targeted status of the document
	  */
	@Override
	public String getDocAction ()
	{
		return (String)get_Value(COLUMNNAME_DocAction);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Set Document Status.
		@param DocStatus
		The current status of the document
	  */
	@Override
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	@Override
	public String getDocStatus ()
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Document No.
		@param DocumentNo
		Document sequence number of the document
	  */
	@Override
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	@Override
	public String getDocumentNo ()
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set HR_EvaluationGuide_ID.
		@param HR_EvaluationGuide_ID HR_EvaluationGuide_ID	  */
	@Override
	public void setHR_EvaluationGuide_ID (int HR_EvaluationGuide_ID)
	{
		if (HR_EvaluationGuide_ID < 1)
			set_Value (COLUMNNAME_HR_EvaluationGuide_ID, null);
		else
			set_Value (COLUMNNAME_HR_EvaluationGuide_ID, Integer.valueOf(HR_EvaluationGuide_ID));
	}

	/** Get HR_EvaluationGuide_ID.
		@return HR_EvaluationGuide_ID	  */
	@Override
	public int getHR_EvaluationGuide_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_EvaluationGuide_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HR_EvaluationSupervisor.
		@param HR_EvaluationSupervisor_ID HR_EvaluationSupervisor	  */
	@Override
	public void setHR_EvaluationSupervisor_ID (int HR_EvaluationSupervisor_ID)
	{
		if (HR_EvaluationSupervisor_ID < 1)
			set_ValueNoCheck (COLUMNNAME_HR_EvaluationSupervisor_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_HR_EvaluationSupervisor_ID, Integer.valueOf(HR_EvaluationSupervisor_ID));
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

	/** Set OFBButton.
		@param OFBButton OFBButton	  */
	@Override
	public void setOFBButton (String OFBButton)
	{
		set_Value (COLUMNNAME_OFBButton, OFBButton);
	}

	/** Get OFBButton.
		@return OFBButton	  */
	@Override
	public String getOFBButton ()
	{
		return (String)get_Value(COLUMNNAME_OFBButton);
	}

	/** Set ofbbutton2.
		@param ofbbutton2 ofbbutton2	  */
	@Override
	public void setofbbutton2 (String ofbbutton2)
	{
		set_Value (COLUMNNAME_ofbbutton2, ofbbutton2);
	}

	/** Get ofbbutton2.
		@return ofbbutton2	  */
	@Override
	public String getofbbutton2 ()
	{
		return (String)get_Value(COLUMNNAME_ofbbutton2);
	}

	/** Set Processed.
		@param Processed
		The document has been processed
	  */
	@Override
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	@Override
	public boolean isProcessed ()
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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