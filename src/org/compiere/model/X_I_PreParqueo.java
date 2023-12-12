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

/** Generated Model for I_PreParqueo
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_I_PreParqueo extends PO implements I_I_PreParqueo, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230724L;

    /** Standard Constructor */
    public X_I_PreParqueo (Properties ctx, int I_PreParqueo_ID, String trxName)
    {
      super (ctx, I_PreParqueo_ID, trxName);
      /** if (I_PreParqueo_ID == 0)
        {
			seti_preparqueo_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_PreParqueo (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_PreParqueo[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Amount.
		@param Amount
		Amount in a defined currency
	  */
	@Override
	public void setAmount (BigDecimal Amount)
	{
		set_Value (COLUMNNAME_Amount, Amount);
	}

	/** Get Amount.
		@return Amount in a defined currency
	  */
	@Override
	public BigDecimal getAmount ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amount);
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

	@Override
	public org.adempiere.core.domains.models.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_DocType)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID
		Document type or rules
	  */
	@Override
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0)
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	@Override
	public int getC_DocType_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	@Override
	public org.adempiere.core.domains.models.I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Invoice)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID
		Invoice Identifier
	  */
	@Override
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1)
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	@Override
	public int getC_Invoice_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Date.
		@param Date1
		Date when business is not conducted
	  */
	@Override
	public void setDate1 (Timestamp Date1)
	{
		set_Value (COLUMNNAME_Date1, Date1);
	}

	/** Get Date.
		@return Date when business is not conducted
	  */
	@Override
	public Timestamp getDate1 ()
	{
		return (Timestamp)get_Value(COLUMNNAME_Date1);
	}

	/** Set Document Date.
		@param DateDoc
		Date of the Document
	  */
	@Override
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	@Override
	public Timestamp getDateDoc ()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	/** Set Date received.
		@param DateReceived
		Date a product was received
	  */
	@Override
	public void setDateReceived (Timestamp DateReceived)
	{
		set_Value (COLUMNNAME_DateReceived, DateReceived);
	}

	/** Get Date received.
		@return Date a product was received
	  */
	@Override
	public Timestamp getDateReceived ()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateReceived);
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

	/** Set Grand Total.
		@param GrandTotal
		Total amount of document
	  */
	@Override
	public void setGrandTotal (BigDecimal GrandTotal)
	{
		set_Value (COLUMNNAME_GrandTotal, GrandTotal);
	}

	/** Get Grand Total.
		@return Total amount of document
	  */
	@Override
	public BigDecimal getGrandTotal ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GrandTotal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set i_preparqueo.
		@param i_preparqueo_ID i_preparqueo	  */
	@Override
	public void seti_preparqueo_ID (int i_preparqueo_ID)
	{
		if (i_preparqueo_ID < 1)
			set_ValueNoCheck (COLUMNNAME_i_preparqueo_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_i_preparqueo_ID, Integer.valueOf(i_preparqueo_ID));
	}

	/** Get i_preparqueo.
		@return i_preparqueo	  */
	@Override
	public int geti_preparqueo_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_i_preparqueo_ID);
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

	/** Set Order Reference.
		@param POReference
		Transaction Reference Number (Sales Order, Purchase Order) of your Business Partner
	  */
	@Override
	public void setPOReference (BigDecimal POReference)
	{
		set_Value (COLUMNNAME_POReference, POReference);
	}

	/** Get Order Reference.
		@return Transaction Reference Number (Sales Order, Purchase Order) of your Business Partner
	  */
	@Override
	public BigDecimal getPOReference ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_POReference);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set preparqueostatus.
		@param preparqueostatus preparqueostatus	  */
	@Override
	public void setpreparqueostatus (boolean preparqueostatus)
	{
		set_Value (COLUMNNAME_preparqueostatus, Boolean.valueOf(preparqueostatus));
	}

	/** Get preparqueostatus.
		@return preparqueostatus	  */
	@Override
	public boolean ispreparqueostatus ()
	{
		Object oo = get_Value(COLUMNNAME_preparqueostatus);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Tax Amount.
		@param TaxAmt
		Tax Amount for a document
	  */
	@Override
	public void setTaxAmt (BigDecimal TaxAmt)
	{
		set_Value (COLUMNNAME_TaxAmt, TaxAmt);
	}

	/** Get Tax Amount.
		@return Tax Amount for a document
	  */
	@Override
	public BigDecimal getTaxAmt ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Total Lines.
		@param TotalLines
		Total of all document lines
	  */
	@Override
	public void setTotalLines (BigDecimal TotalLines)
	{
		set_Value (COLUMNNAME_TotalLines, TotalLines);
	}

	/** Get Total Lines.
		@return Total of all document lines
	  */
	@Override
	public BigDecimal getTotalLines ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalLines);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Type AD_Reference_ID=101 */
	public static final int TYPE_AD_Reference_ID=101;
	/** SQL = S */
	public static final String TYPE_SQL = "S";
	/** Java Language = J */
	public static final String TYPE_JavaLanguage = "J";
	/** Java Script = E */
	public static final String TYPE_JavaScript = "E";
	/** Set Type.
		@param Type
		Type of Validation (SQL, Java Script, Java Language)
	  */
	@Override
	public void setType (String Type)
	{

		set_Value (COLUMNNAME_Type, Type);
	}

	/** Get Type.
		@return Type of Validation (SQL, Java Script, Java Language)
	  */
	@Override
	public String getType ()
	{
		return (String)get_Value(COLUMNNAME_Type);
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