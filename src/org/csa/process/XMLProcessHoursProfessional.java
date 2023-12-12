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
 *	Generate XML Consolidated from HIS_Scheduler
 *
 *  @author Rodrigo Olivares Hurtado
 *
 */
public class XMLProcessHoursProfessional extends SvrProcess
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
			Element Info = document.createElement("HoursProfessional");
			recibeDato.appendChild(Info);

			String sql = "select she.HIS_Scheduler_ID as HIS_Scheduler_ID,coalesce(res.name,'-') as box,she.datefrom as datefrom,"
					+ "she.description as description,spe.name as specialty, "
					+ "coalesce(cbp.value,'')||'-'||coalesce(cbp.ValueValidator,'0') as rut_professional ,coalesce(status,'-') "
					+ "from HIS_Scheduler she "
					+ "left join s_resource res on res.s_resource_id = she.s_resource_id "
					+ "left join his_specialty spe on spe.his_specialty_id = she.his_specialty_id "
					+ "left join HIS_BPartnerSpecialty bps on bps.His_Specialty_ID = spe.His_Specialty_ID "
					+ "left join c_BPartner cbp on cbp.c_bpartner_id = bps.c_bpartner_id "
					+ "where she.isactive = 'Y' ";

	    	PreparedStatement pstmt  = null;
	    	ResultSet rs = null;
	    	pstmt = DB.prepareStatement(sql, get_TrxName());
	    	rs = pstmt.executeQuery();
	    	while(rs.next()) {
	    		Element HIS_Scheduler_ID = document.createElement("HIS_Scheduler_ID");
	    		org.w3c.dom.Text id = document.createTextNode(rs.getString("HIS_Scheduler_ID"));
	    		HIS_Scheduler_ID.appendChild(id);
	    		Info.appendChild(HIS_Scheduler_ID);

	    		Element Box = document.createElement("Box");
	    		org.w3c.dom.Text bo = document.createTextNode(rs.getString("box"));
	    		Box.appendChild(bo);
	    		Info.appendChild(Box);

	    		Element DateFrom = document.createElement("DateFrom");
	    		org.w3c.dom.Text da = document.createTextNode(rs.getString("datefrom"));
	    		DateFrom.appendChild(da);
	    		Info.appendChild(DateFrom);

	    		Element Description = document.createElement("Description");
	    		org.w3c.dom.Text de = document.createTextNode(rs.getString("description"));
	    		Description.appendChild(de);
	    		Info.appendChild(Description);

	    		Element Specialty = document.createElement("Specialty");
	    		org.w3c.dom.Text sp = document.createTextNode(rs.getString("specialty"));
	    		Specialty.appendChild(sp);
	    		Info.appendChild(Specialty);

	    		Element Rut = document.createElement("rut_professional");
	    		org.w3c.dom.Text ru = document.createTextNode(rs.getString("rut_professional"));
	    		Rut.appendChild(ru);
	    		Info.appendChild(Rut);

	    		Element Status = document.createElement("Status");
	    		org.w3c.dom.Text st = document.createTextNode(rs.getString("status"));
	    		Status.appendChild(st);
	    		Info.appendChild(Status);

	    		Element linea = document.createElement("linea");
	            org.w3c.dom.Text ln = document.createTextNode("---------------------------------");
	            linea.appendChild(ln);
	            Info.appendChild(linea);

	    	}


            Source source = new DOMSource(Documento);
            Result result = new StreamResult(new File("/home/adempiere/Adempiere/XML/XMLProcessHoursProfessional.xml"));

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

		return "XML Horas Profesional Generado";
	} // createXml











}	//	InvoiceCreateInOut
