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
import java.util.logging.Level;

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

import org.compiere.model.MBPartner;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 *	Generate XML Consolidated from C_BPartner
 *
 *  @author Rodrigo Olivares Hurtado
 *
 */
public class XMLProcessBPartner extends SvrProcess
{
	/** Properties						*/
	/** Properties						*/
	private Properties 		m_ctx;
	private int p_HIS_Specialty_ID = 0;
	public String p_RutBPartner = "";


    @Override
	protected void prepare()
	{
    	p_HIS_Specialty_ID =getRecord_ID();
		m_ctx = Env.getCtx();
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();

			if (name.equals("Value"))
				p_RutBPartner = element.getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
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
		int BPartnerID = DB.getSQLValue(get_TrxName(), "select coalesce(c_bpartner_id,0) from c_bpartner "
				+ "where value||'-'||ValueValidator like '"+p_RutBPartner+"'");

		msg=CreateXMLCG(BPartnerID);


		return msg;
	}	//	doIt



	public String CreateXMLCG(int BPartnerID) throws Exception
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


	    	if(BPartnerID > 0) {
	    		MBPartner bp= new MBPartner(m_ctx,BPartnerID,get_TrxName());

	    		String rutBP = bp.getValue()+"-"+bp.get_Value("ValueValidator");
	    		Element RutBPartner = document.createElement("RutBPartner");
	    		org.w3c.dom.Text pa = document.createTextNode(rutBP);
	    		RutBPartner.appendChild(pa);
	    		Info.appendChild(RutBPartner);


	    		Element NameBPartner = document.createElement("NameBPartner");
	    		org.w3c.dom.Text na = document.createTextNode(bp.getName());
	    		NameBPartner.appendChild(na);
	    		Info.appendChild(NameBPartner);

	    		Element Birthday = document.createElement("Birthday");
	    		org.w3c.dom.Text bi = document.createTextNode(bp.getBirthday().toString());
	    		Birthday.appendChild(bi);
	    		Info.appendChild(Birthday);

	    		Element Gender = document.createElement("Gender");
	    		org.w3c.dom.Text ge = document.createTextNode(bp.getGender());
	    		Gender.appendChild(ge);
	    		Info.appendChild(Gender);

	    		String nationality = DB.getSQLValueString(get_TrxName(), "select coalesce(Nationality,'-') "
	    				+ "from C_BPartner_Info_Oth bpi where isactive = 'Y' and C_BPartner_ID = " + bp.get_ID());
	    		Element Nationality = document.createElement("Nationality");
	    		org.w3c.dom.Text nt = document.createTextNode(nationality);
	    		Nationality.appendChild(nt);
	    		Info.appendChild(Nationality);

	    		String previcion = DB.getSQLValueString(get_TrxName(), "select (select coalesce(Name,'S/P') from AD_Ref_List "
	    				+ "where AD_Reference_ID=1000015 and Value = bpi.Health_Insurance) "
	    				+ "from C_BPartner_Info_Oth bpi where isactive = 'Y' and C_BPartner_ID = "+bp.get_ID());
	    		Element Previcion = document.createElement("Previcion");
	    		org.w3c.dom.Text pr = document.createTextNode(previcion);
	    		Previcion.appendChild(pr);
	    		Info.appendChild(Previcion);

	    		Element Phone = document.createElement("Phone");
	    		org.w3c.dom.Text ph = document.createTextNode("phone");
	    		Phone.appendChild(ph);
	    		Info.appendChild(Phone);

	    		Element Email = document.createElement("Email");
	    		org.w3c.dom.Text em = document.createTextNode("Email");
	    		Email.appendChild(em);
	    		Info.appendChild(Email);

	    		Element Status = document.createElement("Status");
	    		org.w3c.dom.Text st = document.createTextNode("Se encontro al cliente");
	    		Status.appendChild(st);
	    		Info.appendChild(Status);

	    	}else {
	    		Element Status = document.createElement("Status");
	    		org.w3c.dom.Text st = document.createTextNode("No se encontro al cliente");
	    		Status.appendChild(st);
	    		Info.appendChild(Status);
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
