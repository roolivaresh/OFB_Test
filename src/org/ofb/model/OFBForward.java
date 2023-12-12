package org.ofb.model;

import org.compiere.util.DB;

public class OFBForward {

	public static int ValidDate()
	{
		int ps = 0;
		try
		{
			ps = DB.getSQLValue(null, "Select MAX(Value) from AD_SysConfig where name='OFB_ValidDate'");
		}
		catch (Exception e)
		{
			ps = 0;
		}
		return ps;
	}

	public static int getDate(String cadena)
	{
		int cant = 0;
		for (int x=0;x<cadena.length();x++)
		{
			cant = cant+cadena.codePointAt(x);
		}
		return cant;
	}

	// Rodrigo Olivares Hurtado 2023-08-28
	public static boolean IgnorateAttachmentTSM()
	{
		String Attachment = "N";
		try
		{
			Attachment = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_IgnorateAttachmentTSM'");
			if(Attachment == null)
				Attachment = "N";
		}
		catch (Exception e)
		{
			Attachment = "N";
		}
		return Attachment.equals("Y");
	}

	//Rodrigo Olivares Hurtado 2023-08-31
	public static boolean IgnorateAssetTSM()
	{
		String Attachment = "N";
		try
		{
			Attachment = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_IgnorateAssetTSM'");
			if(Attachment == null)
				Attachment = "N";
		}
		catch (Exception e)
		{
			Attachment = "N";
		}
		return Attachment.equals("Y");
	}

	public static boolean CompleteRequisitionTSM()
	{
		String Requisition = "N";
		try
		{
			Requisition = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_CompleteRequisitionTSM'");
			if(Requisition == null)
				Requisition = "N";
		}
		catch (Exception e)
		{
			Requisition = "N";
		}
		return Requisition.equals("Y");
	}
	public static boolean OverrideNativeCashLineCreate()
	{
		String Valid = "N";
		try
		{
			Valid = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_OverrideNativeCashLineCreate'");
			if(Valid == null)
				Valid = "N";
		}
		catch (Exception e)
		{
			Valid = "N";
		}
		return Valid.equals("Y");
	}
	//Rodrigo Olivares Hurtado 2023-09-07
	public static boolean ValidStockMaterialReq()
	{
		String StockMaterialReq = "N";
		try
		{
			StockMaterialReq = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_ValidStockMaterialReq'");
			if(StockMaterialReq == null)
				StockMaterialReq = "N";
		}
		catch (Exception e)
		{
			StockMaterialReq = "Y";
		}
		return StockMaterialReq.equals("Y");
	}


	public static String PriceListWSTSM()
	{
		String Generate = "0";
		try
		{
			Generate = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_PriceListWSTSM'");
			if(Generate == null)
				Generate = "0";
		}
		catch (Exception e)
		{
			Generate = "0";
		}
		return Generate;
	}

	public static String AmbienteGDE()
	{
		String ambiente = "H";
		try
		{
			ambiente = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_AmbienteGDE'");
			if(ambiente == null)
				ambiente = "H";
		}
		catch (Exception e)
		{
			ambiente = "H";
		}
		return ambiente;
	}

	public static String GDEapiAuth()
	{
		String cadena = "";
		try
		{
			cadena = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_GDEapiAuth' ");
			if(cadena == null)
				cadena = "";
		}
		catch (Exception e)
		{
			cadena = "";
		}
		return cadena;
	}

	public static String GDEDirPath()
	{
		String ruta = "";
		try
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_GDEDirPath'");
			if(ruta == null)
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}

	public static String GDERemoteURL()
	{
		String result = "";
		try
		{
			result = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_GDERemoteURL'");
			if(result == null)
				result = "";
		}
		catch (Exception e)
		{
			result = "";
		}
		return result;
	}

	public static String GDEFTPServer()
	{
		String ruta = "";
		try
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_GDEFTPServer'");
			if(ruta == null)
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}

	public static int GDEFTPServerPort()
	{
		int resp = 21;
		String ruta = "";
		try
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_GDEFTPServerPort'");
			if(ruta == null)
				ruta = "0";
			resp = Integer.parseInt(ruta);
		}
		catch (Exception e)
		{
			ruta = "";
			resp = 21;
		}
		return resp;
	}

	public static String GDEFTPUser()
	{
		String result = "";
		try
		{
			result = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_GDEFTPUser'");
			if(result == null)
				result = "";
		}
		catch (Exception e)
		{
			result = "";
		}
		return result;
	}

	public static String GDEFTPPass()
	{
		String result = "";
		try
		{
			result = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_GDEFTPPass'");
			if(result == null)
				result = "";
		}
		catch (Exception e)
		{
			result = "";
		}
		return result;
	}

	public static String PathGDEClient()
	{
		String ruta = "";
		try
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_PathGDEClient' ");
			if(ruta == null)
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}

	public static String PathGDEServer()
	{
		String ruta = "";
		try
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_PathGDEServer' ");
			if(ruta == null)
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static boolean isAbsolutePayAmtCallout()
	{
		String value = "N";
		try
		{
			value = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_IsAbsolutePayAmtCallout'");
			if(value == null)
				value = "N";
		}
		catch (Exception e)
		{
			value = "N";
		}
		return value.equals("Y");
	}

}



