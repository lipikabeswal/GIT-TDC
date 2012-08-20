package com.ctb.tdc.web.servlet.fixed;

import java.io.FileInputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class testDom {
	
	
	public static void main(String[] args) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new FileInputStream("C:\\foo.xml"));
		Element root = doc.getRootElement();
		System.out.println("root" + root);
		
		root.setAttribute("name", "shop for geeks");
		root.setAttribute("location", "Tokyo, Japan");
		
		System.out.println("root to string" );
		
		
		new XMLOutputter().output(doc, System.out);

		// do the similar for other elements
		XMLOutputter outputter = new XMLOutputter();
		//outputter.output(new Document(root), new FileOutputStream ("foo2.xml"));
	}
	
}
