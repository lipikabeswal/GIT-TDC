package com.ctb.tdc.web.servlet.fixed;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noNamespace.AdssvcRequestDocument;


import org.apache.log4j.Logger;
import com.ctb.tdc.web.utils.AssetInfo;
import com.ctb.tdc.web.utils.ServletUtils;

  
/**
 * @author John_Wang
 */
public class ContentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	//static Logger logger = Logger.getLogger(ContentServlet.class);

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
		System.out.println("Go.... doGet........");
	 if (method.equals(ServletUtils.GET_ITEM_METHOD)) {
			getItem(request, response);
		} else if (method.equals(ServletUtils.GET_IMAGE_METHOD)) {
			getImage(request, response);
		} 
		else if (method.equals(ServletUtils.GET_LOCALRESOURCE_METHOD)) {
		     getLocalResource(request,response);
		} else {
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
		System.out.println("Go.... doGet........"); 
		String folder = ServletUtils.getFolder(request);
		if (folder == null || "".equals(folder.trim())) // invalid image
			throw new IOException("No folder in request.");
		
		try {	
				itemId = ServletUtils.getItemNo(request);
				System.out.println("getItem==>"+itemId);
		
			if (itemId == null || "".equals(itemId.trim())) // invalid item id
				throw new Exception("No item id in request.");
			
			try {	
				String filePath = this.RESOURCE_FOLDER_PATH + File.separator + folder
											 + File.separator + "xmls" + File.separator
				                             + "item" + itemId + ".xml";
				System.out.println("Item filepath : " + filePath);	
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
	        out.println(itemxml);            
	        out.flush();
	        out.close();

		} catch (Exception e) {
			/*logger.error("Exception occured in getItem() : "
					+ ServletUtils.printStackTrace(e));
			ServletUtils.writeResponse(response, ServletUtils.ERROR);*/
			e.printStackTrace();
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
		
		System.out.println("getImage:Started.....");
		String xml = ServletUtils.getXml(request);
		String image;
	
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
            System.out.println("getImage:Ended.....");
		} catch (Exception e) {
			/*logger.error("Exception occured in getImage() : "
					+ ServletUtils.printStackTrace(e));
			ServletUtils.writeResponse(response, ServletUtils.ERROR);*/
			e.printStackTrace();
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
		/*logger.error("Exception occured in getLocalResource() : "
				+ ServletUtils.printStackTrace(e));
		ServletUtils.writeResponse(response, ServletUtils.ERROR);*/
		 e.printStackTrace();
	 }
   }

	public static final String TDC_HOME = "tdc.home";
	public static final String RESOURCE_FOLDER_PATH = System.getProperty(TDC_HOME) + File.separator + 
		                             "webapp" + File.separator + "resources";

	
}
