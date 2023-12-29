package org.ofb.model;

import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

/**
 * CalloutIsaias
 *
 * @author Isaías
 * @version 1
 */
public class CalloutTarea2 extends CalloutEngine {
	
	/*
	 * Use el mismo campo Value para ingresar el RUT (la parte núerica).
	 * 
	 * Ingrese un nuevo campo a la ventana para ingresar un dígito verificador.
	 * 
	 * Es un CallOut para que al momento de ir a otro campo valide si el digito verificador
	 * ingresado corresponde al RUT.
	 * 
	 */
	public String validarRUT(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
		
		if (isCalloutActive() || value == null || (mTab.getValue("value") == null)
				 || (mTab.getValue("dv") == null)) {
			return "";
		}
		
		String valor = (String) mTab.getValue("value");
		String digito = (String) mTab.getValue("dv");
		
		char calculoDV = calcularDigitoVerificador(valor);
		String calculoDVtexto = String.valueOf(calculoDV); 
		
		if(digito.equals(calculoDVtexto) == false) {
			mTab.fireDataStatusEEvent ("Validación de RUT ", "El RUT no es válido", true);
			mTab.setValue("dv", ""); // Es un campo obligatorio por ende cuando se borre no se puede guardar
		}

		return "";
	}

    private char calcularDigitoVerificador(String rutNumero) {
        int rut = Integer.parseInt(rutNumero);
        int m = 0, s = 0;
        for (; rut != 0; rut /= 10) {
            s = (s + rut % 10 * ((m % 6) + 2)) % 11;
            m++;
        }
        return (char) (s == 0 ? '0' : s == 1 ? 'K' : (char) ((11 - s) + '0'));
    }
}
