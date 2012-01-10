package com.ctb.tdc.web.servlet;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noNamespace.AdssvcRequestDocument;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;
import org.jdom.output.XMLOutputter;

import com.ctb.tdc.web.exception.DecryptionException;
import com.ctb.tdc.web.exception.HashMismatchException;
import com.ctb.tdc.web.exception.TMSException;
import com.ctb.tdc.web.utils.AssetInfo;
import com.ctb.tdc.web.utils.ContentFile;
import com.ctb.tdc.web.utils.MemoryCache;
import com.ctb.tdc.web.utils.ServletUtils;

/**
 * @author John_Wang
 */
public class IIContentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(ContentServlet.class);
	
	private static final HashMap contentHash = new HashMap();

	/**
	 * Constructor of the object.
	 */
	public IIContentServlet() {
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

		long startTime = System.currentTimeMillis();
		System.out.print("called ContentServlet method ");
		
		String method = ServletUtils.getMethod(request);
		
		if (method.equals(ServletUtils.GET_SUBTEST_METHOD)) {
			getSubtest(request, response);
		} else if (method.equals(ServletUtils.DOWNLOAD_ITEM_METHOD)) {
			downloadItem(request, response);
		} else if (method.equals(ServletUtils.GET_ITEM_METHOD)) {
			getItem(request, response);
		} else if (method.equals(ServletUtils.GET_IMAGE_METHOD)) {
			getImage(request, response);
		} else if (method.equals(ServletUtils.GET_FILE_PARTS)){
			downloadFileParts (request,response);
		}
		else if (method.equals(ServletUtils.GET_LOCALRESOURCE_METHOD)) {
		     getLocalResource(request,response);
		} else {
			ServletUtils.writeResponse(response, ServletUtils.ERROR);
		}
		
		System.out.print(method + ", elapsed time: " + (System.currentTimeMillis() - startTime + "\n"));

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

	private void downloadFileParts(HttpServletRequest request,
			HttpServletResponse response) throws IOException{

		ServletUtils.writeResponse(response, "<FILE_PART_OK />");

	}
	
	private void getSubtest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestURI = request.getRequestURI();
		String filePath = null;
		if(requestURI.indexOf("servletii1") >= 0) {
			filePath = new File(".").getAbsolutePath() + "/../data/IIsubtest1.xml";
		} else {
			filePath = new File(".").getAbsolutePath() + "/../data/IIsubtest1.xml";
		}
		System.out.println("***** Looking for subtest XML: " + filePath);
		
        String result = new String(ServletUtils.readFromFile(new File(filePath)));
		BufferedOutputStream myOutput = new BufferedOutputStream(response.getOutputStream());
		myOutput.write(result.getBytes());
		myOutput.flush();
		myOutput.close();
	}

	private void downloadItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletUtils.writeResponse(response, ServletUtils.OK);
	}

	private void getItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
		MemoryCache aMemoryCache = MemoryCache.getInstance();
		HashMap assetMap = aMemoryCache.getAssetMap();
		
		String xml = ServletUtils.getXml(request);
		String itemId = null;
		String hash;
		String key;
		
		//long startTime = System.currentTimeMillis();
		
		try {
			if (xml == null) {
				itemId = ServletUtils.getItemId(request);
			}
			else {
				itemId = getAttributeValue("itemid", xml);
			}

			String filePath = new File(".").getAbsolutePath() + "/../data/IIitem" + itemId + ".xml";
			System.out.println("***** Looking for item XML: " + filePath);
			
			byte[] decryptedContent = ServletUtils.readFromFile(new File(filePath));
			
			String itemxml = new String(decryptedContent);
			itemxml = ServletUtils.doUTF8Chars(itemxml);

			ServletUtils.writeResponse(response, itemxml);
		} catch (Exception e) {
			e.printStackTrace();
            String errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.getContentFailed");                            
			ServletUtils.writeResponse(response, ServletUtils.buildXmlErrorMessage("", errorMessage, ""));
		}
	}
	
	private void getImage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		MemoryCache aMemoryCache = MemoryCache.getInstance();
		HashMap assetMap = aMemoryCache.getAssetMap();

		String xml = ServletUtils.getXml(request);
		String imageId;
		
		try {
			if (xml == null) {
				imageId = ServletUtils.getImageId(request);
			}
			else {
				AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
				imageId = document.getAdssvcRequest().getGetImage().getImageid();
			}

			if (imageId == null || "".equals(imageId.trim())) // invalid image id
				throw new Exception("No image id in request.");
			
			String filePath = new File(".").getAbsolutePath() + "/../data/IIasset" + imageId + ".swf";
			System.out.println("***** Looking for asset: " + filePath);
			
			byte[] data = ServletUtils.readFromFile(new File(filePath));
			
            String MIMEType = "application/x-shockwave-flash";
            response.setContentType( MIMEType );
            int size = data.length;
            response.setContentLength( size );
            ServletOutputStream myOutput = response.getOutputStream();
            myOutput.write( data );
            myOutput.flush();
            myOutput.close();	
		} catch (Exception e) {
			e.printStackTrace();
            String errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.getContentFailed");                            
			ServletUtils.writeResponse(response, ServletUtils.buildXmlErrorMessage("", errorMessage, ""));
		}
	}
	
	private static final HashMap localResourceMap = new HashMap();
	
	private void getLocalResource(HttpServletRequest request,HttpServletResponse response) throws IOException {
    	String filename = request.getParameter("resourcePath");
    	  	
    	try {
    		
    		if (filename == null || "".equals(filename.trim())) 
    			throw new Exception("No  in request.");

    		String filePath = this.RESOURCE_FOLDER_PATH + File.separator  + filename;

    		ServletOutputStream myOutput = response.getOutputStream();
    		
    		byte[] decrypted = (byte[]) localResourceMap.get(filePath);
    		if(decrypted == null) {
	    		// assume all local flash resources are encrypted
	    		FileInputStream input = new FileInputStream( filePath.replaceAll(".swf", ".enc"));
	            int size = input.available();
	            byte[] src = new byte[ size ];
	            input.read( src );
	            input.close();
	            
	            decrypted = ContentFile.decrypt(src);
	            localResourceMap.put(filePath, decrypted);
    		}

            int index= filename.lastIndexOf(".");
    		String ext = filename.substring(index+1);
    		AssetInfo assetInfo = new AssetInfo();
    		assetInfo.setExt(ext);
    		String mimeType = assetInfo.getMIMEType();
    		response.setContentType(mimeType);
    		
    		response.setContentLength( decrypted.length );
    		
    		myOutput.write( decrypted );

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

	private void writeResponse(HttpServletResponse response, String xml)
			throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(xml);
		out.flush();
		out.close();
	}
	
	public static final String TDC_HOME = "tdc.home";
	public static final String RESOURCE_FOLDER_PATH = new File(".").getAbsolutePath() + "/.." + File.separator + 
		                             "data" + File.separator + "resources";

}
