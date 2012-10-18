package com.ctb.tdc.web.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.ctb.tdc.web.dto.TrackerData;



public class XMLUtil {

	public static final String FILE_ENCODING_ISO_8859 = "ISO-8859-1";
	public static final String FILE_ENCODING_UTF_8 = "UTF-8";

	public static Document build(String xmlstring) throws JDOMException,
			IOException {

		InputStream is = null;
		Document doc = null;
		try {
			SAXBuilder sb = new SAXBuilder();
			 doc = sb.build(new StringReader(xmlstring));
			 System.out.println(doc.toString());
			    
			//System.setProperty("file.encoding", FILE_ENCODING_ISO_8859);
			//is = new ByteArrayInputStream(xmlstring.getBytes());
			//doc = new SAXBuilder("org.apache.xerces.parsers.SAXParser", false)
				//	.build(new InputStreamReader(is));
			 
		   // doc=new SAXBuilder().build(is);

		} finally {
			ClosableHelper.close(is);
		}

		return doc;
	}

	@SuppressWarnings("unchecked")
	public static TreeMap<Long, TrackerData> getContentList(String trckerXml)
			throws JDOMException, IOException {
		Document doc = getXMLDocument(trckerXml);
		Element root = doc.getRootElement();
		List<Element> els = root.getChildren("tracker");
		TreeMap<Long, TrackerData> valuemap = new TreeMap<Long, TrackerData>();
		for (Element el : els) {
			TrackerData td = new TrackerData();
			List <Attribute> attributeList  = el.getAttributes();
			/*td.setSequenceNo(Long.valueOf(el.getChild("SequenceNo").getText()));
			td.setValue(el.getChild("Value").getText());
			Long next = el.getChild("Next").getText().trim().equalsIgnoreCase("NULL") ? null : Long.valueOf(el.getChild("Next").getText());*/
			for(Attribute attribute : attributeList) {
				if(attribute.getName().trim().equalsIgnoreCase("sequence_number"))
					td.setSequenceNo(Long.valueOf(attribute.getValue().trim()));
				else if (attribute.getName().trim().equalsIgnoreCase("value"))
					td.setValue(attribute.getValue().trim());
				else if (attribute.getName().trim().equalsIgnoreCase("next")){
					Long next = attribute.getValue().trim().equalsIgnoreCase("NULL")? null : Long.valueOf(attribute.getValue().trim());
					td.setNext(next);
				}
					
			}
			
			
			valuemap.put(td.getSequenceNo(), td);
		}

		return valuemap;
	}

	public static Document getXMLDocument(String trckerXml)
			throws JDOMException, IOException {
		Document doc = build(trckerXml);
		return doc;
	}

	@SuppressWarnings("unchecked")
	public static List<Element> getAllContentByName(String trckerXml)
			throws JDOMException, IOException {
		Document doc = build(trckerXml);
		Element root = doc.getRootElement();
		List<Element> els = root.getChildren(trckerXml);
		return els;

	}
}
