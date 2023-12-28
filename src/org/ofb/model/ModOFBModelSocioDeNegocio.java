package org.ofb.model;

import org.adempiere.core.domains.models.I_C_BPartner;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.csa.model.ModCSAComSC;

public class ModOFBModelSocioDeNegocio implements ModelValidator {

	public ModOFBModelSocioDeNegocio() {
		super();
		// TODO Auto-generated constructor stub
	}

	/** Logger */
	private static CLogger log = CLogger.getCLogger(ModCSAComSC.class);
	/** Client */
	private int m_AD_Client_ID = -1;

	@Override
	public void initialize(ModelValidationEngine engine, MClient client) {
		// client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		} else {
			log.info("Initializing global validator: " + this.toString());
		}
        // Tables to be monitored
		engine.addModelChange(I_C_BPartner.Table_Name, this);

	}

	/**
	 * Model Change of a monitored Table.
	 * 
	 * Se ejecuta en la ventana
	 * Menú > Relación con Socios del Negocio > Reglas de Socios del Negocio >
	 * Socio del Negocio
	 * 
	 * En la pestaña cabecera "Socio del Negocio"
	 * 
	 * En el el campo Código (que en el código es value)
	 *
	 */
	@Override
	public String modelChange(PO po, int type) throws Exception {
		
		log.info(po.get_TableName() + " Type: " + type);

		if (
			(type == TYPE_BEFORE_CHANGE)
			&&
			po.get_Table_ID() == I_C_BPartner.Table_ID
		){
			
			/*
			 * Valida el campo 'value' ser RUT
			 */
			
			MBPartner parnert = (MBPartner) po;
			
			String value = parnert.getValue();
			
			monstrarMensajesPorRut(value);
			
			log.info("value: ".concat(value));
		}

		return null;
	}
	
	public void monstrarMensajesPorRut(String rut) {
		
		if(rut == null)
			throw new AdempiereException("El RUT esta vacío.");
		
		/**
		 * No cae a este lugar porque adempiere no registra correctamente que borre 
		 * el valor quedando con valores que ya pasaron y que no son a los que se
		 * ven en pantalla
		 */
		if(rut.isEmpty() || rut == "" || rut.length() == 0)
			throw new AdempiereException("El RUT tiene que tener valor.");
		
		if(rut.contains("."))
			throw new AdempiereException("El RUT no tiene que tener puntos.");
		
		if(!rut.contains("-"))
			throw new AdempiereException("El RUT no incluye guion.");
		
		if(!validarRut(rut))
			throw new AdempiereException("El RUT no es válido.");
	}

    public boolean validarRut(String rut) {
        // Eliminar espacios y puntos y convertir a mayúsculas
        rut = rut.replaceAll("\\s+|\\.", "").toUpperCase();

        // Verificar el formato del RUT
        if (!rut.matches("\\d{1,8}-[\\dkK]")) {
            return false;
        }

        // Dividir el RUT en número y dígito verificador
        String[] rutArray = rut.split("-");
        String rutNumero = rutArray[0];
        char rutVerificador = rutArray[1].charAt(0);

        // Verificar que el último carácter sea 'K' si el dígito verificador es 'K'
        if (rutVerificador == 'k') {
			throw new AdempiereException("El RUT con dígito verificador tiene que tener la K en mayuscula.");
        }

        // Calcular el dígito verificador esperado
        char verificadorCalculado = calcularDigitoVerificador(rutNumero);

        // Comparar con el dígito verificador ingresado
        return rutVerificador == verificadorCalculado;
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

	@Override
	public String docValidate(PO po, int timing) {

		return null;
	}

	/**
	 * User Login. Called when preferences are set
	 *
	 * @param AD_Org_ID  org
	 * @param AD_Role_ID role
	 * @param AD_User_ID user
	 * @return error message or null
	 */
	@Override
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	} // login

	/**
	 * Get Client to be monitored
	 *
	 * @return AD_Client_ID client
	 */
	@Override
	public int getAD_Client_ID() {
		return m_AD_Client_ID;
	} // getAD_Client_ID

	/**
	 * String Representation
	 *
	 * @return info
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("QSS_Validator");
		return sb.toString();
	} // toString

}
