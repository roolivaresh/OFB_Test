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
 * Contributor(s): Armen Rizal (armen@goodwill.co.id) Bug Fix 1564496         *
 *****************************************************************************/
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.core.domains.models.I_M_InventoryLine;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * Inventory Movement Callouts
 *
 * @author Jorg Janke
 * @version $Id: CalloutMovement.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 *
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 *         <li>BF [ 1879568 ] CalloutMouvement QtyAvailable issues
 */
public class CalloutMovement extends CalloutEngine {
	/**
	 * Product modified Set Attribute Set Instance
	 *
	 * @param ctx       Context
	 * @param WindowNo  current Window No
	 * @param GridTab   Model Tab
	 * @param GridField Model Field
	 * @param value     The new value
	 * @return Error message or ""
	 */
	public String product(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		Integer M_Product_ID = (Integer) value;
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
			return "";
		// Set Attribute
		if (Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()
				&& Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO, "M_AttributeSetInstance_ID") != 0)
			mTab.setValue("M_AttributeSetInstance_ID",
					Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO, "M_AttributeSetInstance_ID"));
		else
			mTab.setValue("M_AttributeSetInstance_ID", null);

		checkQtyAvailable(ctx, mTab, WindowNo, M_Product_ID, null);
		return "";
	} // product

	// Begin Armen 2006/10/01
	/**
	 * Movement Line - MovementQty modified called from MovementQty
	 *
	 * @param ctx       Context
	 * @param WindowNo  current Window No
	 * @param GridTab   Model Tab
	 * @param GridField Model Field
	 * @param value     The new value
	 * @return Error message or ""
	 */
	public String qty(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		if (isCalloutActive() || value == null)
			return "";

		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		checkQtyAvailable(ctx, mTab, WindowNo, M_Product_ID, (BigDecimal) value);
		//
		return "";
	} // qty

	/**
	 * Movement Line - Locator modified
	 *
	 * @param ctx       Context
	 * @param WindowNo  current Window No
	 * @param GridTab   Model Tab
	 * @param GridField Model Field
	 * @param value     The new value
	 * @return Error message or ""
	 */
	public String locator(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		if (value == null)
			return "";
		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		checkQtyAvailable(ctx, mTab, WindowNo, M_Product_ID, null);
		return "";
	}

	/**
	 * Check available qty
	 *
	 * @param ctx          context
	 * @param mTab         Model Tab
	 * @param WindowNo     current Window No
	 * @param M_Product_ID product ID
	 * @param MovementQty  movement qty (if null will be get from context
	 *                     "MovementQty")
	 */
	private void checkQtyAvailable(Properties ctx, GridTab mTab, int WindowNo, int M_Product_ID,
			BigDecimal MovementQty) {
		// Begin Armen 2006/10/01
		if (M_Product_ID != 0) {
			MProduct product = MProduct.get(ctx, M_Product_ID);
			if (product.isStocked()) {
				if (MovementQty == null)
					MovementQty = (BigDecimal) mTab.getValue("MovementQty");
				int M_Locator_ID = Env.getContextAsInt(ctx, WindowNo, "M_Locator_ID");
				// If no locator, don't check anything and assume is ok
				if (M_Locator_ID <= 0)
					return;
				int M_AttributeSetInstance_ID = Env.getContextAsInt(ctx, WindowNo, "M_AttributeSetInstance_ID");
				BigDecimal available = MStorage.getQtyAvailable(0, M_Locator_ID, M_Product_ID,
						M_AttributeSetInstance_ID, null);
				if (available == null)
					available = Env.ZERO;
				if (available.signum() == 0)
					mTab.fireDataStatusEEvent("NoQtyAvailable", "0", false);
				else if (available.compareTo(MovementQty) < 0)
					mTab.fireDataStatusEEvent("InsufficientQtyAvailable", available.toString(), false);
			}
		}
		// End Armen
	}

	public String qtyBook(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		if (isCalloutActive())
			return "";
		Integer MovementLine = (Integer) mTab.getValue("M_MovementLine_ID");
		BigDecimal bd = null;

		if (MovementLine != null && MovementLine.intValue() != 0) {
			MMovementLine _MLine = new MMovementLine(ctx, MovementLine, null);
			Integer M_Product_ID = (Integer) mTab.getValue("M_Product_ID");
			Integer M_Locator_ID = (Integer) mTab.getValue("M_Locator_ID");
			Integer M_AttributeSetInstance_ID = (Integer) mTab.getValue("M_AttributeSetInstance_ID");
			// Integer M_AttributeSetInstance_ID = 0;
			// if product, locator or ASI has changed recalculate Book Qty
			if ((M_Product_ID != null && M_Product_ID != _MLine.getM_Product_ID())
					|| (M_Locator_ID != null && M_Locator_ID != _MLine.getM_Locator_ID())
					|| M_AttributeSetInstance_ID != null) {

				// Check ASI - if product has been changed remove old ASI
				log.warning("Product: "+M_Product_ID+"=="+_MLine.getM_Product_ID()+"????");
				if (M_Product_ID == _MLine.getM_Product_ID()) {
					M_AttributeSetInstance_ID = (Integer) mTab.getValue("M_AttributeSetInstance_ID");
					if (M_AttributeSetInstance_ID == null)
						M_AttributeSetInstance_ID = 0;
				} else {
					mTab.setValue("M_AttributeSetInstance_ID", null);
				}
				try {
					bd = setQtyBook(M_AttributeSetInstance_ID, M_Product_ID, M_Locator_ID);
					mTab.setValue("QtyBook", bd);
				} catch (Exception e) {
					return mTab.setValue("QtyBook", bd);
				}
			}
			return "";
		}

		// New Line - Get Book Value
		int M_Product_ID = 0;
		Integer Product = (Integer) mTab.getValue("M_Product_ID");
		if (Product != null)
			M_Product_ID = Product.intValue();
		if (M_Product_ID == 0)
			return "";
		int M_Locator_ID = 0;
		Integer Locator = (Integer) mTab.getValue("M_Locator_ID");
		if (Locator != null)
			M_Locator_ID = Locator.intValue();
		if (M_Locator_ID == 0)
			return "";

		// Set Attribute
		int M_AttributeSetInstance_ID = 0;
		Integer ASI = (Integer) mTab.getValue("M_AttributeSetInstance_ID");
		if (ASI != null)
			M_AttributeSetInstance_ID = ASI.intValue();
		// Product Selection
		if (I_M_InventoryLine.COLUMNNAME_M_Product_ID.equals(mField.getColumnName())) {
			if (Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO, "M_Product_ID") == M_Product_ID) {
				M_AttributeSetInstance_ID = Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO,
						"M_AttributeSetInstance_ID");
			} else {
				M_AttributeSetInstance_ID = 0;
			}
			if (M_AttributeSetInstance_ID != 0)
				mTab.setValue(I_M_InventoryLine.COLUMNNAME_M_AttributeSetInstance_ID, M_AttributeSetInstance_ID);
			else
				mTab.setValue(I_M_InventoryLine.COLUMNNAME_M_AttributeSetInstance_ID, null);
		}

		// Set QtyBook from first storage location
		// kviiksaar: Call's now the extracted function
		try {
			bd = setQtyBook(M_AttributeSetInstance_ID, M_Product_ID, M_Locator_ID);
			mTab.setValue("QtyBook", bd);
		} catch (Exception e) {
			return mTab.setValue("QtyBook", bd);
		}

		//
		log.info("M_Product_ID=" + M_Product_ID + ", M_Locator_ID=" + M_Locator_ID + ", M_AttributeSetInstance_ID="
				+ M_AttributeSetInstance_ID + " - QtyBook=" + bd);

		return "";
	}

	private BigDecimal setQtyBook(int M_AttributeSetInstance_ID, int M_Product_ID, int M_Locator_ID) throws Exception {
		// Set QtyBook from first storage location
		BigDecimal bd = null;
		String sql = "SELECT QtyOnHand FROM M_Storage " + "WHERE M_Product_ID=?" // 1
				+ " AND M_Locator_ID=?" // 2
				+ " AND M_AttributeSetInstance_ID=?";
		if (M_AttributeSetInstance_ID == 0)
			sql = "SELECT SUM(QtyOnHand) FROM M_Storage " + "WHERE M_Product_ID=?" // 1
					+ " AND M_Locator_ID=?"; // 2

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_Product_ID);
			pstmt.setInt(2, M_Locator_ID);
			if (M_AttributeSetInstance_ID != 0)
				pstmt.setInt(3, M_AttributeSetInstance_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				bd = rs.getBigDecimal(1);
				if (bd != null)
					return bd;
			} else {
				// gwu: 1719401: clear Booked Quantity to zero first in case the query returns
				// no rows,
				// for example when the locator has never stored a particular product.
				return new BigDecimal(0);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
			throw new Exception(e.getLocalizedMessage());
		}
		return new BigDecimal(0);
	}
} // CalloutMove