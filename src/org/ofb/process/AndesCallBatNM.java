/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.ofb.process;

import java.io.IOException;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.ofb.model.OFBForward;

/**
 *	Generate XML from MInOut
 *
 *  @author Italo Niñoles Ininoles
 *  @version $Id: ExportDTEMInOut.java,v 1.2 05/09/2014 $
 */
public class AndesCallBatNM extends SvrProcess
{
	/** Properties						*/

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			if (para[i].getParameter() == null)
				;
		}
	}	//	prepare


	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		String rutaBat = OFBForward.PathBatIMacroNM();

		String msg = "";

		// Isaias Diaz Rojas 2024-04-09
		int duraciónEjecucionBat = OFBForward.BATFileExecutionDuration();
		String rutaBatUiVision = OFBForward.PathBatUiVision();
		try {
            // Ejecutar el primer script
			//String e1 = "D:\\ejecutarUiVisionMio.bat";
			String e1 = "D:\\call1.bat";
            ProcessBuilder pb1 = new ProcessBuilder("cmd", "/c", e1);
            Process p1 = pb1.start();
            p1.waitFor();  // Esperar a que el primer script termine

            // Ejecutar el segundo script
            /*
            */
            //String e2 = "D:\\ejecutarUiVisionMio2.bat";
            String e2 = "D:\\call2.bat";
            ProcessBuilder pb2 = new ProcessBuilder("cmd", "/c", e2);
            Process p2 = pb2.start();
            p2.waitFor();  // Esperar a que el segundo script termine

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
		//msg += "1.1: " + ejecucionBat(rutaBatUiVision);
		//msg += "1.2: " + ejecucionBat("D:\\ejecutarUiVisionMio2.bat");
		log.config("Duración en milisegundos archivo/s BAT: " + duraciónEjecucionBat);

		//String sqlFile = "SELECT FileName FROM C_AccountCredentials WHERE IsActive = 'Y'";
		String sqlFile = "C:\\ImacrosBancos\\09_ctas_bancochile2.0.iim";
		//msg += " - 2: " + ejecucion(sqlFile, rutaBat);

		// Se ejecuta segunda imacro para bancoestado ininoles 24052020
		String sqlFile2 = "C:\\ImacrosBancos\\04_ctas_bancoestadoV2.iim";
		//msg += " - 3: " + ejecucion(sqlFile2, rutaBat);

		// Delay a espera de terminar de ejecutar imacro
		try
    	{
			//Thread.sleep(duraciónEjecucionBat);
			//Thread.sleep(180000); // 3 minutos para los 2 iMacros
    	}
		catch (Exception e)
    	{
    		log.config("Error al esperar tiempo");
    	}

		return msg;
	}	//	doIt

	public String ejecucion(String rutaIM, String rutaBat)
	{
		Runtime aplicacion = Runtime.getRuntime();
	    try
	    {
	    	String eje = "cmd.exe /K "+rutaBat+" "+rutaIM;
	    	aplicacion.exec(eje);
	    	/*
	    	try
	    	{
	    		Thread.sleep (200000);
	    	}
	    	catch (Exception e)
	    	{
	    		log.config("Error al esperar tiempo");
	    	}
	    	*/
	    }
	    catch(Exception e)
	    {
	    	return e.toString();
	    }
	    return "Proceso iMacro OK";
	}

	public String ejecucionBat(String rutaBat)
	{
		try
		{
			Runtime aplicacion = Runtime.getRuntime();

			// Ejecutar el archivo .bat usando Runtime.getRuntime().exec()
			Process proceso = aplicacion.exec(rutaBat);

			// Esperar a que el proceso termine (solo es que se ejecute el enlace, no UiVision)
			int resultado = proceso.waitFor();

			// Escribir por consila el resultado
			log.config("El proceso terminó con código de salida: " + resultado);
		}
		catch(Exception e)
		{
			return e.toString();
		}

		return "Proceso Ui Vision OK";
	}

} // InvoiceCreateInOut
