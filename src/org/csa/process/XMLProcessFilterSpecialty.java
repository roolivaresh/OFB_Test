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
package org.csa.process;

import java.io.File;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 *	Generate XML Consolidated from HIS_Specialty
 *
 *  @author Rodrigo Olivares Hurtado
 *
 */
public class XMLProcessFilterSpecialty extends SvrProcess
{
	/** Properties						*/
	/** Properties						*/
	private Properties 		m_ctx;
	private int p_HIS_Specialty_ID = 0;
	public String urlPdf = "";


    @Override
	protected void prepare()
	{
    	p_HIS_Specialty_ID =getRecord_ID();
		m_ctx = Env.getCtx();
	}	//	prepare


	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		String msg = "";
			msg=CreateXMLCG();
		return msg;
	}	//	doIt



	public String CreateXMLCG() throws Exception
    {


		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document document = implementation.createDocument(null, "HIS_Specialty", null);
            document.setXmlVersion("1.0");
            document.setTextContent("text/xml");

            Element Documento = document.createElement("Documento");
			document.getDocumentElement().appendChild(Documento);
            //Documento.appendChild(Documento);
			Element Header = document.createElement("Encabezado");
            Documento.appendChild(Header);
			Element Body = document.createElement("Body");
            Documento.appendChild(Body);
			Element recibeDato = document.createElement("recibeDato");
            Body.appendChild(recibeDato);
			Element Info = document.createElement("Specialty");
			recibeDato.appendChild(Info);

			String sql = "select HIS_Specialty_ID, Value, Name from HIS_Specialty where isactive = 'Y' ";
	    	PreparedStatement pstmt  = null;
	    	ResultSet rs = null;
	    	pstmt = DB.prepareStatement(sql, get_TrxName());
	    	rs = pstmt.executeQuery();
	    	while(rs.next()) {
	    		Element Value = document.createElement("Value");
	    		org.w3c.dom.Text va = document.createTextNode(rs.getString("Value"));
	    		Value.appendChild(va);
	    		Info.appendChild(Value);

	    		Element Name = document.createElement("Name");
	    		org.w3c.dom.Text na = document.createTextNode(rs.getString("Name"));
	    		Name.appendChild(na);
	    		Info.appendChild(Name);


	    	}

	    	Element linea = document.createElement("linea");
            org.w3c.dom.Text ln = document.createTextNode("---------------------------------");
            linea.appendChild(ln);
            Info.appendChild(linea);


	    	String sql2 = "Select coalesce(bp.value,'0')||'-'||coalesce(bp.ValueValidator,'0') as rut, bp.name as profecional, sp.name as especialidad from c_bpartner bp "
	    			+ "join HIS_BPartnerSpecialty bps on bps.c_bpartner_id = bp.c_bpartner_id "
	    			+ "join His_Specialty sp on sp.His_Specialty_ID = bps.His_Specialty_ID "
	    			+ "where bp.isactive = 'Y'  and bp.c_bpartner_id in (Select distinct(c_bpartner_id) from HIS_BPartnerSpecialty)";
	    	PreparedStatement pstmt2  = null;
	    	ResultSet rs2 = null;
	    	pstmt2 = DB.prepareStatement(sql2, get_TrxName());
	    	rs2 = pstmt2.executeQuery();
	    	while(rs2.next()) {
	    		Element Rut = document.createElement("rut");
	    		org.w3c.dom.Text ru = document.createTextNode(rs2.getString("rut"));
	    		Rut.appendChild(ru);
	    		Info.appendChild(Rut);

	    		Element Profecional = document.createElement("profecional");
	    		org.w3c.dom.Text pro = document.createTextNode(rs2.getString("profecional"));
	    		Profecional.appendChild(pro);
	    		Info.appendChild(Profecional);

	    		Element Especialidad = document.createElement("especialidad");
	    		org.w3c.dom.Text es = document.createTextNode(rs2.getString("especialidad"));
	    		Especialidad.appendChild(es);
	    		Info.appendChild(Especialidad);
	    	}


            Source source = new DOMSource(Documento);
            Result result = new StreamResult(new File("/home/adempiere/Adempiere/XML/XMLProcessFilterSpecialty.xml"));

            Transformer transformer  = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);


            StringWriter request = new StringWriter();
            transformer.transform(source, new StreamResult(request));
            log.config("XML "+request.getBuffer().toString());
            String xmlstring = request.getBuffer().toString();

            Source response = null;

            /*try
			{
				final CXFConnector wsc = new CXFConnector();
				wsc.setSoapAction("http://tempuri.org/ADEMPIERE.PRONOVA.reposicion.Test");
				wsc.setRequest(xmlstring);
				wsc.setBinding(SOAPBinding.SOAP11HTTP_BINDING);
				wsc.setEndpointAddress("http://10.90.100.158:52773/csp/healthshare/qa/ADEMPIERE.PRONOVA.reposition.cls");
				wsc.setServiceName("recibeHL7");
				wsc.setPortName("recibeHL7Soap");
				wsc.setTargetNS("http://tempuri.org");
				wsc.executeConnector();
				response = wsc.getResponse();

			}
            catch(Exception e)
			{
				log.config("exception");
				log.config("string "+e.toString());

				throw new Exception("No se pudo conectar");
			}*/


		}
		catch(ParserConfigurationException |TransformerException ex)
        {
            System.out.print(ex.getMessage());
        }

		return "XML Especialidad Generado";
	} // createXml











}	//	InvoiceCreateInOut
