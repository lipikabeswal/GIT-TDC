package com.ctb.tdc.web.servlet.fixed;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noNamespace.AdssvcRequestDocument;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.ctb.tdc.web.utils.AssetInfo;
import com.ctb.tdc.web.utils.ContentRetriever;
import com.ctb.tdc.web.utils.ServletUtils;

/**
 * @author John_Wang
 */
public class ContentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static String trackerXml;
	static Logger logger = Logger.getLogger(ContentServlet.class);

	/**
	 * Constructor of the object.
	 */
	public ContentServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 
	
		String method = ServletUtils.getMethod(request);
		if (method.equals(ServletUtils.GET_SUBTEST_METHOD)) {
			getSubtest(request, response);
		} else if (method.equals(ServletUtils.DOWNLOAD_ITEM_METHOD)) {
			downloadItem(request, response);
		} else if (method.equals(ServletUtils.GET_ITEM_METHOD)) {
			getItem(request, response);
		} else if (method.equals(ServletUtils.GET_IMAGE_METHOD)) {
			getImage(request, response);
		} 
		else if (method.equals(ServletUtils.GET_LOCALRESOURCE_METHOD)) {
		     getLocalResource(request,response);
		}else if (method.equals("getItemCount")) {
			getItemCount(request,response);
		}else if (method.equals("downloadFileParts")){
			downloadFileParts (request,response);
		}else {
			ServletUtils.writeResponse(response, ServletUtils.ERROR);
		}

	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occure
	 */
	public void init() throws ServletException {
		// do nothing
	}

	/**
	 * Get subtest from TMS
	 * 
	 * @param HttpServletRequest
	 *            request
	 * @param HttpServletResponse
	 *            response - checks if subtest is currently on system and is up
	 *            to date using key - if content is not current, request content
	 *            for this subtest id from TMS - saves encrypted content to
	 *            local directory - return decrypted content to client
	 * @throws Exception 
	 * 
	 */
	

	private void getItemCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = null;
		out = response.getWriter();
		out.write("<result>1</result>");
		out.flush();
		out.close();

		
	}
	private void getSubtest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		
		String xml = ServletUtils.getXml(request);
		String subtestId;
		String thisLine;
		String itemxml;
		
		String folder = ServletUtils.getFolder(request);
		if (folder == null || "".equals(folder.trim())) // invalid image
			throw new IOException("No folder in request.");
		
		try {
			AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
			subtestId = document.getAdssvcRequest().getGetSubtest().getSubtestid();
			
			if (subtestId == null || "".equals(subtestId.trim())) // invalid subtest id
				throw new Exception("No subtest id in request.");
			// String filePath = this.TEST_FOLDER_PATH + folder + FILE.separator 
			//        "subtest" + subtestId + ".xml";
			String filePath = this.RESOURCE_FOLDER_PATH + File.separator + folder + File.separator
			        + "xmls" + File.separator + "subtest" + ".xml";
			//System.out.println("subtest: filepath=" + filePath);
			FileInputStream fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			StringBuffer buf = new StringBuffer();
			while ((thisLine = br.readLine()) != null) {
			    buf.append(thisLine);
			}
			itemxml = buf.toString();
			in.close();
	//System.out.println("subtestxml..."+itemxml);
			
	        
			
			org.jdom.Document itemDoc = null;
			org.jdom.Document trackerDoc = null;
		
			SAXBuilder saxBuilder = new SAXBuilder();
			
		    itemDoc = saxBuilder.build(new ByteArrayInputStream(itemxml.getBytes()));
		   // System.out.println("Item Docuemnt :  " + itemDoc);
			org.jdom.Element element = (org.jdom.Element) itemDoc.getRootElement();
			
			org.jdom.Attribute attribute = new Attribute("itemCount","0");
			
			org.jdom.Element objectElement = element.getChild("ob_element_list");
			objectElement.setAttribute("itemCount",new Integer(10).toString());
			
	
			//System.out.println("cr.getTracker: "  + cr.getTrackerXML());
/*			trackerXml = ContentRetriever.getTrackerXML("9622602","11945B9D01984FEE487127A07CD3C0E1");
			trackerDoc = saxBuilder.build(new ByteArrayInputStream(trackerXml.getBytes()));
			int numberOfFileParts = trackerDoc.getRootElement().getChildren("tracker").size();
			List children = itemDoc.getRootElement().getChildren();
			
			//org.jdom.Element trackerElement1 = 	new org.jdom.Element("Tracker").setAttribute("sequence_no","1").setAttribute("value","").setAttribute("next","2");
			//children.add(trackerElement1);
			//children.add()
			Content trackerFiles = null;
			for (int i=0; i < numberOfFileParts; i++){
			trackerFiles=	trackerDoc.getRootElement().getChild("tracker").detach();
			children.add(trackerFiles);
			}*/
			//objectElement.getAttribute("itemCount").setValue("25");
			//element.addContent(objectElement);
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			new XMLOutputter().output(itemDoc, out);
	       // out.println(itemxml);            
	        out.flush();
	        out.close();
		} 
		catch (Exception e) {
			logger.error("Exception occured in getItem() : "
					+ ServletUtils.printStackTrace(e));
			ServletUtils.writeResponse(response, ServletUtils.ERROR);
		}
	
	}

	/**
	 * Download item from TMS
	 * 
	 * @param HttpServletRequest
	 *            request
	 * @param HttpServletResponse
	 *            response - checks if item is currently on system and is up to
	 *            date using key - if content is not current, request content
	 *            for this item id from TMS - saves encrypted content to local
	 *            directory - return OK or ERROR to client
	 * 
	 */
	private void downloadItem(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
			
		response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        out.println("<OK/>");            
        out.flush();
        out.close();
	}

	
	/**
	 * 
	 * Method Included for downloading the File Parts from the file Server.
	 * This method is added for improving the performance during download.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	
	private void downloadFileParts(HttpServletRequest request,
			HttpServletResponse response) throws IOException{

		System.out.println("Download File parts method called");
		try{
		/*	//getContent(String fileName);
			String xml = ServletUtils.getXml(request);
			String downloadFilePart = getAttributeValue("name", xml);
			String sequence_number = getAttributeValue("sequence_number", xml);
			String next = getAttributeValue("next", xml);
			System.out.println("Download File Parts: " + downloadFilePart + " :: "+ sequence_number +" :: " + next );
			System.out.println("Content Retriever" + downloadFilePart);
			String status = ContentRetriever.getContent(downloadFilePart);
			System.out.println("NEXT value:" + next);
			if (next.equalsIgnoreCase("NULL")){
				System.out.println("inside if value:" + next);
				ContentRetriever.mergeFile(trackerXml,"9622602","11945B9D01984FEE487127A07CD3C0E1");
				ContentRetriever.unCompressFile("9622602","11945B9D01984FEE487127A07CD3C0E1");
				
			}
			*/
			
			ServletUtils.writeResponse(response, ServletUtils.FILE_PART_OK);
		}
		catch (Exception e) {
			e.printStackTrace();
		    String errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.getContentFailed");                            
			ServletUtils.writeResponse(response, ServletUtils.buildXmlErrorMessage("", errorMessage, ""));
		}
		
	}
	/**
	 * Get item from local objectbank
	 * 
	 * @param HttpServletRequest
	 *            request
	 * @param HttpServletResponse
	 *            response - get item from local file and return decrypted
	 *            content to client
	 * @throws Exception 
	 * 
	 */
	private void getItem(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String itemId;
		String thisLine;
		String itemxml = null;
		//String [] dummyArray = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
		Random rnd = new Random();
		String folder = ServletUtils.getFolder(request);
		if (folder == null || "".equals(folder.trim())) // invalid image
			throw new IOException("No folder in request.");
		
		try {	
			String xml = ServletUtils.getXml(request);
			AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
			itemId = document.getAdssvcRequest().getGetItem().getItemid();
		
			if (itemId == null || "".equals(itemId.trim())) // invalid item id
				throw new Exception("No item id in request.");
			
			try {	
				
				
				
				
				//itemId =dummyArray[rnd.nextInt(dummyArray.length)];
				
				System.out.println("Content Servlet Item Id " + itemId);
				String filePath = this.RESOURCE_FOLDER_PATH + File.separator + folder
											 + File.separator + "xmls" + File.separator
				                             + "item" + itemId + ".xml";
				//System.out.println("Item filepath : " + filePath);	
				FileInputStream fstream = new FileInputStream(filePath);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				StringBuffer buf = new StringBuffer();
				while ((thisLine = br.readLine()) != null) {
				    buf.append(thisLine);
				}
				itemxml = buf.toString();
				in.close();
		    } catch (Exception e){  //Catch exception if any
				      System.err.println("Error: " + e.getMessage());
			}
	
			response.setContentType("text/xml");
	        PrintWriter out = response.getWriter();
	        itemxml = ServletUtils.doUTF8Chars(itemxml );
	        out.println(itemxml);            
	        out.flush();
	        out.close();

		} catch (Exception e) {
			logger.error("Exception occured in getItem() : "
					+ ServletUtils.printStackTrace(e));
			ServletUtils.writeResponse(response, ServletUtils.ERROR);
		}
	}
	
	
	/**
	 * Get iamge from memory cache
	 * 
	 * @param HttpServletRequest
	 *            request
	 * @param HttpServletResponse
	 *            response - get image from memory cache
	 * @throws Exception 
	 * 
	 */
	private void getImage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String xml = ServletUtils.getXml(request);
		String image;
		System.out.println("GetImage Fired");
		String folder = ServletUtils.getFolder(request);
		if (folder == null || "".equals(folder.trim())) // invalid image
			throw new IOException("No folder in request.");
		
		try {
			if (xml == null) {
				image = ServletUtils.getImageId(request);
			}
			else {
				AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
				image = document.getAdssvcRequest().getGetImage().getImageid();
			}

			if (image == null || "".equals(image.trim())) // invalid image
				throw new Exception("No image id in request.");
			
			String filePath = this.RESOURCE_FOLDER_PATH + File.separator + folder 
			          + File.separator + "images" + File.separator + image;
			//System.out.println("Image filepath : " + filePath);
			
			FileInputStream fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			
			ServletOutputStream myOutput = response.getOutputStream();
			byte[] data = new byte[4096];
			int cnt = 0;
			int size = 0;
			response.setContentType("application/x-shockwave-flash");
			while ((cnt = in.read(data, 0, 4096)) == 4096) {
				size += cnt;
				myOutput.write( data );
			}
			size += cnt;
			size = ((size / 4096) + 1) * 4096;
			myOutput.write( data );
			in.close();
           
            response.setContentLength( size ); 
            myOutput.flush();
            myOutput.close();	
            
		} catch (Exception e) {
			logger.error("Exception occured in getImage() : "
					+ ServletUtils.printStackTrace(e));
			ServletUtils.writeResponse(response, ServletUtils.ERROR);
		}
	}

	private void getLocalResource(HttpServletRequest request,HttpServletResponse response) throws IOException {
    	String filename = request.getParameter("resourcePath");
    	
    	
    	try {
    		
    		if (filename == null || "".equals(filename.trim())) 
    			throw new Exception("No  in request.");

    		String filePath = this.RESOURCE_FOLDER_PATH + File.separator  + filename;

    		FileInputStream fstream = new FileInputStream(filePath);
    		DataInputStream in = new DataInputStream(fstream);

    		ServletOutputStream myOutput = response.getOutputStream();
    		byte[] data = new byte[4096];
    		int cnt = 0;
    		int size = 0;
    		int index= filename.lastIndexOf(".");
    		String ext = filename.substring(index+1);
    		AssetInfo assetInfo = new AssetInfo();
    		assetInfo.setExt(ext);
    		String mimeType = assetInfo.getMIMEType();
    		response.setContentType(mimeType);

    		while ((cnt = in.read(data, 0, 4096)) == 4096) {
    			size += cnt;
    			myOutput.write( data );
    		}
    		size += cnt;
    		size = ((size / 4096) + 1) * 4096;
    		response.setContentLength( size );
    		myOutput.write( data );
    		in.close();
    		myOutput.flush();
    		myOutput.close();	

	        
	 } catch (Exception e) {
		logger.error("Exception occured in getLocalResource() : "
				+ ServletUtils.printStackTrace(e));
		ServletUtils.writeResponse(response, ServletUtils.ERROR);
	 }
   }
	private String getAttributeValue(String attributeName, String xml) {
		String result = null;
		int startIndex = xml.indexOf(attributeName);
		if (startIndex>=0) {
			startIndex += attributeName.length();
			startIndex = xml.indexOf("\"", startIndex);
			startIndex +=1;
			int endIndex = xml.indexOf("\"", startIndex);
			if (endIndex >startIndex)
				result = xml.substring(startIndex, endIndex);
		}
		return result;
		
	}

	public static final String TDC_HOME = "tdc.home";
	public static final String RESOURCE_FOLDER_PATH = "C://OAS_Workspace_91//application-tdc-backup"+ File.separator + 
		                             "webapp" + File.separator + "resources";

	
}
