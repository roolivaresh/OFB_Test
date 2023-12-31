/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.core.domains.models.I_C_ValidCombination;
import org.adempiere.core.domains.models.I_Fact_Acct;
import org.adempiere.core.domains.models.X_C_ElementValue;
import org.adempiere.core.domains.models.X_I_ElementValue;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * 	Natural Account
 *
 *  @author Jorg Janke
 *  @version $Id: MElementValue.java,v 1.3 2006/07/30 00:58:37 jjanke Exp $
 *
 * 	@author Teo Sarca, SC ARHIPAC SERVICE SRL
 * 			BF [ 1883533 ] Change to summary - valid combination issue
 * 			BF [ 2320411 ] Translate "Already posted to" message
 * 	@author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com 2015-09-09
 *  	<li>FR [ 9223372036854775807 ] Add Support to Dynamic Tree
 *  @see https://adempiere.atlassian.net/browse/ADEMPIERE-442
 */
public class MElementValue extends X_C_ElementValue
{
	/**
	 *
	 */
	private static final long serialVersionUID = 4765839867934329276L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_ElementValue_ID ID or 0 for new
	 *	@param trxName transaction
	 */
	public MElementValue(Properties ctx, int C_ElementValue_ID, String trxName)
	{
		super(ctx, C_ElementValue_ID, trxName);
		if (C_ElementValue_ID == 0)
		{
		//	setC_Element_ID (0);	//	Parent
		//	setName (null);
		//	setValue (null);
			setIsSummary (false);
			setAccountSign (ACCOUNTSIGN_Natural);
			setAccountType (ACCOUNTTYPE_Expense);
			setIsDocControlled(false);
			setIsForeignCurrency(false);
			setIsBankAccount(false);
			//
			setPostActual (true);
			setPostBudget (true);
			setPostEncumbrance (true);
			setPostStatistical (true);
		}
	}	//	MElementValue

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MElementValue(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MElementValue

	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param Value value
	 *	@param Name name
	 *	@param Description description
	 *	@param AccountType account type
	 *	@param AccountSign account sign
	 *	@param IsDocControlled doc controlled
	 *	@param IsSummary summary
	 *	@param trxName transaction
	 */
	public MElementValue (Properties ctx, String Value, String Name, String Description,
		String AccountType, String AccountSign,
		boolean IsDocControlled, boolean IsSummary, String trxName)
	{
		this (ctx, 0, trxName);
		setValue(Value);
		setName(Name);
		setDescription(Description);
		setAccountType(AccountType);
		setAccountSign(AccountSign);
		setIsDocControlled(IsDocControlled);
		setIsSummary(IsSummary);
	}	//	MElementValue

	/**
	 * 	Import Constructor
	 *	@param imp import
	 */
	public MElementValue (X_I_ElementValue imp)
	{
		this (imp.getCtx(), 0, imp.get_TrxName());
		setClientOrg(imp);
		set(imp);
	}	//	MElementValue

	/**
	 * 	Set/Update Settings from import
	 *	@param imp import
	 */
	public void set (X_I_ElementValue imp)
	{
		setValue(imp.getValue());
		setName(imp.getName());
		setDescription(imp.getDescription());
		setAccountType(imp.getAccountType());
		setAccountSign(imp.getAccountSign());
		setIsSummary(imp.isSummary());
		setIsDocControlled(imp.isDocControlled());
		setC_Element_ID(imp.getC_Element_ID());
		//
		setPostActual(imp.isPostActual());
		setPostBudget(imp.isPostBudget());
		setPostEncumbrance(imp.isPostEncumbrance());
		setPostStatistical(imp.isPostStatistical());
		//
	//	setC_BankAccount_ID(imp.getC_BankAccount_ID());
	//	setIsForeignCurrency(imp.isForeignCurrency());
	//	setC_Currency_ID(imp.getC_Currency_ID());
	//	setIsBankAccount(imp.isIsBankAccount());
	//	setValidFrom(null);
	//	setValidTo(null);
	}	//	set



	/**
	 * Is this a Balance Sheet Account
	 * @return boolean
	 */
	public boolean isBalanceSheet()
	{
		String accountType = getAccountType();
		return (ACCOUNTTYPE_Asset.equals(accountType)
			|| ACCOUNTTYPE_Liability.equals(accountType)
			|| ACCOUNTTYPE_OwnerSEquity.equals(accountType));
	}	//	isBalanceSheet

	/**
	 * Is this an Activa Account
	 * @return boolean
	 */
	public boolean isActiva()
	{
		return ACCOUNTTYPE_Asset.equals(getAccountType());
	}	//	isActive

	/**
	 * Is this a Passiva Account
	 * @return boolean
	 */
	public boolean isPassiva()
	{
		String accountType = getAccountType();
		return (ACCOUNTTYPE_Liability.equals(accountType)
			|| ACCOUNTTYPE_OwnerSEquity.equals(accountType));
	}	//	isPassiva

	/**
	 * 	User String Representation
	 *	@return info value - name
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ();
		sb.append(getValue()).append(" - ").append(getName());
		return sb.toString ();
	}	//	toString

	/**
	 * 	Extended String Representation
	 *	@return info
	 */
	public String toStringX ()
	{
		StringBuffer sb = new StringBuffer ("MElementValue[");
		sb.append(get_ID()).append(",").append(getValue()).append(" - ").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toStringX



	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		//
		// Transform to summary level account
		if (!newRecord && isSummary() && is_ValueChanged(COLUMNNAME_IsSummary))
		{
			//
			// Check if we have accounting facts
			boolean match = new Query(getCtx(), I_Fact_Acct.Table_Name, I_Fact_Acct.COLUMNNAME_Account_ID+"=?", get_TrxName())
								.setParameters(getC_ElementValue_ID())
								.match();
			if (match)
			{
				throw new AdempiereException("@AlreadyPostedTo@");
			}
			//
			// Check Valid Combinations - teo_sarca FR [ 1883533 ]
			String whereClause = I_C_ValidCombination.COLUMNNAME_Account_ID+"=?";
			POResultSet<MAccount> rs = new Query(getCtx(), I_C_ValidCombination.Table_Name, whereClause, get_TrxName())
					.setParameters(get_ID())
					.scroll();
			try {
				while(rs.hasNext()) {
					rs.next().deleteEx(true);
				}
			}
			finally {
				rs.close();
			}
		}
		return true;
	}	//	beforeSave

	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Yamel Senih [ 9223372036854775807 ]
		//	Change to PO
//		if (newRecord)
//		{
			// afalcone [Bugs #1837219]
//			int ad_Tree_ID= (new MElement(getCtx(), getC_Element_ID(), get_TrxName())).getAD_Tree_ID();
//			String treeType= (new MTree(getCtx(),ad_Tree_ID,get_TrxName())).getTreeType();
//			insert_Tree(treeType, getC_Element_ID());
			//	insert_Tree(MTree_Base.TREETYPE_ElementValue, getC_Element_ID()); Old

//		}
		//	End Yamel Senih

		//	Value/Name change
		if (!newRecord && (is_ValueChanged(COLUMNNAME_Value) || is_ValueChanged(COLUMNNAME_Name)))
		{
			MAccount.updateValueDescription(getCtx(), "Account_ID=" + getC_ElementValue_ID(),get_TrxName());
			if ("Y".equals(Env.getContext(getCtx(), "$Element_U1")))
				MAccount.updateValueDescription(getCtx(), "User1_ID=" + getC_ElementValue_ID(),get_TrxName());
			if ("Y".equals(Env.getContext(getCtx(), "$Element_U2")))
				MAccount.updateValueDescription(getCtx(), "User2_ID=" + getC_ElementValue_ID(),get_TrxName());
			if ("Y".equals(Env.getContext(getCtx(), "$Element_U3")))
				MAccount.updateValueDescription(getCtx(), "User3_ID=" + getC_ElementValue_ID(),get_TrxName());
			if ("Y".equals(Env.getContext(getCtx(), "$Element_U4")))
				MAccount.updateValueDescription(getCtx(), "User4_ID=" + getC_ElementValue_ID(),get_TrxName());
		}

		return success;
	}	//	afterSave

	//	Yamel Senih [ 9223372036854775807 ]
	//	Change to PO
//	@Override
//	protected boolean afterDelete (boolean success)
//	{
//		if (success)
//			delete_Tree(getC_Element().getAD_Tree().getTreeType());
//		return success;
//	}	//	afterDelete
	//	End Yamel Senih

	//OFB Consulting faaguilar Creating Combination
	public void CreateCombination()
	{
			int Scheme=DB.getSQLValue(null,"select C_AcctSchema_ID from C_AcctSchema where isactive='Y' and AD_Client_ID="+getAD_Client_ID());
			MAccount acct=MAccount.get(getCtx(),getAD_Client_ID(),0,Scheme,getC_ElementValue_ID(),0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,get_TrxName());
			acct.setAlias(getValue());
			acct.setCombination(getValue()+"-"+getName());
			acct.save();

	}


}	//	MElementValue
