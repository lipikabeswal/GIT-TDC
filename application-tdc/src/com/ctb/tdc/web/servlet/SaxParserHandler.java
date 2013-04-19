package com.ctb.tdc.web.servlet;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParserHandler extends DefaultHandler {

	
	String userName = null;
	String password = null;
	String accessCode = null;
	
	public void startElement(String uri, String localName,
			String qName, Attributes attributes)
	throws SAXException {
		 if(qName.equalsIgnoreCase("login_request")){			
			password = attributes.getValue("password");
			userName = attributes.getValue("user_name");
			accessCode = attributes.getValue("access_code");
		} 
	}
	public void endElement(String uri, String localName,
			String qName)
	throws SAXException {

		

	}

	public void characters(char ch[], int start, int length)
	throws SAXException {
		
	}
}