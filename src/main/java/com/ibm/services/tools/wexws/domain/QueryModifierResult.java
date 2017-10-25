package com.ibm.services.tools.wexws.domain;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ibm.services.tools.wexws.WexWsConstants;

public class QueryModifierResult {

	private String interpretationXML;

	public String getInterpretationXML() {
		return interpretationXML;
	}

	public void setInterpretationXML(String interpretationXML) {
		this.interpretationXML = interpretationXML;
	}
	
	public String getXmlTermsAsString() {
		String finalQuery = "";

		if (interpretationXML == null) {
			return "";
		}
		
		try {
			org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(interpretationXML)));
			doc.getDocumentElement().normalize();
			// System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("term");
			boolean first = true;
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				// System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (WexWsConstants.QUERY_MODIFIER_SEPARATOR.trim().equals(eElement.getAttribute("str"))) {
						continue;
					}
					if (!first) {
						finalQuery += ",";
					}
					// System.out.println("processing : " + eElement.getAttribute("processing"));
					// System.out.println("str : " + eElement.getAttribute("str"));
					finalQuery += eElement.getAttribute("str");
					first = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("Final query JP -> " + finalQuery.trim());
		return finalQuery.trim();
	}
}
