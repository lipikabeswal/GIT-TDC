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
public class ContentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(ContentServlet.class);
	
	private static final HashMap contentHash = new HashMap();

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
	 * 
	 */
	private void getSubtest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String xml = ServletUtils.getXml(request);
		String subtestId = null;
		String hash;
		String key;
		
		//long startTime = System.currentTimeMillis();
		
		try {
			if (xml == null) {
				subtestId = ServletUtils.getSubtestId(request);
				hash = ServletUtils.getHash(request);
				key = ServletUtils.getKey(request);
				xml = ServletUtils.buildContentRequest(request,
						ServletUtils.GET_SUBTEST_METHOD);
			}
			else {
				AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
				subtestId = document.getAdssvcRequest().getGetSubtest().getSubtestid();
				hash = document.getAdssvcRequest().getGetSubtest().getHash();
				key = document.getAdssvcRequest().getGetSubtest().getKey();
			}
			String decryptedContent = (String) contentHash.get("subtest" + subtestId);
			if(decryptedContent == null) {
				if (subtestId == null || "".equals(subtestId.trim())) // invalid subtest id
					throw new Exception("No subtest id in request.");
				String filePath = ContentFile.getContentFolderPath() + subtestId
						+ ContentFile.SUBTEST_FILE_EXTENSION;
	
				byte[]decryptedContentBytes = ContentFile.decryptFile(filePath, hash, key);
				contentHash.put("subtest" + subtestId, new String(decryptedContentBytes));
				response.setContentType("text/xml");
				int size = decryptedContentBytes.length;
				response.setContentLength(size);
				BufferedOutputStream myOutput = new BufferedOutputStream(response.getOutputStream());
				myOutput.write(decryptedContentBytes);
				myOutput.flush();
				myOutput.close();
			} else {
				response.setContentType("text/xml");
				int size = decryptedContent.length();
				response.setContentLength(size);
				BufferedOutputStream myOutput = new BufferedOutputStream(response.getOutputStream());
				myOutput.write(decryptedContent.getBytes());
				myOutput.flush();
				myOutput.close();
			}
			//System.out.println("getSubtest elapsed time: " + (System.currentTimeMillis() - startTime));
		} 
		catch (HashMismatchException e) {
			logger.error("Exception occured in getSubtest("+subtestId+") : "
					+ ServletUtils.printStackTrace(e));
            String errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.hashMismatch");                            
			ServletUtils.writeResponse(response, ServletUtils.buildXmlErrorMessage("", errorMessage, ""));
		}
		catch (DecryptionException e) {
			logger.error("Exception occured in getSubtest("+subtestId+") : "
					+ ServletUtils.printStackTrace(e));
            String errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.decryptionFailed");                            
			ServletUtils.writeResponse(response, ServletUtils.buildXmlErrorMessage("", errorMessage, ""));
		}
		catch (TMSException e) {
			logger.error("TMS Exception occured in getSubtest("+subtestId+") : "
					+ ServletUtils.printStackTrace(e));
            String errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.getContentFailed");                            
			ServletUtils.writeResponse(response, ServletUtils.buildXmlErrorMessage("", errorMessage, ""));
		}
		catch (Exception e) {
			logger.error("Exception occured in getSubtest("+subtestId+") : "
					+ ServletUtils.printStackTrace(e));
            String errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.getContentFailed");                            
			//ServletUtils.writeResponse(response, ServletUtils.buildXmlErrorMessage("", errorMessage, ""));
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
	private void downloadItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletUtils.writeResponse(response, ServletUtils.OK);
	}

	/**
	 * Get item from local objectbank
	 * 
	 * @param HttpServletRequest
	 *            request
	 * @param HttpServletResponse
	 *            response - get item from local file and return decrypted
	 *            content to client
	 * 
	 */
	private void getItem(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
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
				hash = ServletUtils.getHash(request);
				key = ServletUtils.getKey(request);
			}
			else {
				itemId = getAttributeValue("itemid", xml);
				hash = getAttributeValue("hash", xml);
				key = getAttributeValue("key", xml);
				
			}

			if (itemId == null || "".equals(itemId.trim())) // invalid item id
				throw new Exception("No item id in request.");
			String filePath = ContentFile.getContentFolderPath() + itemId
					+ ContentFile.ITEM_FILE_EXTENSION;
			
			String itemxml = (String) contentHash.get("item" + itemId);
			if(itemxml == null) {
				byte[] decryptedContent = ContentFile.decryptFile(filePath, hash,
						key);
				if (decryptedContent == null)
					throw new DecryptionException("Cannot decrypt '" + filePath + "'");
				org.jdom.Document itemDoc = null;
				synchronized(aMemoryCache.saxBuilder) {
					itemDoc = aMemoryCache.saxBuilder.build(new ByteArrayInputStream(decryptedContent));
				}
				org.jdom.Element element = (org.jdom.Element) itemDoc.getRootElement();
				element = element.getChild("assets");
				if (element != null) {
					List imageList = element.getChildren();
					for (int i = 0; i < imageList.size(); i++) {
						element = (org.jdom.Element) imageList.get(i);
						String imageId = element.getAttributeValue("id");
						if (!assetMap.containsKey(imageId)) {
							String mimeType = element.getAttributeValue("type");
							String ext = mimeType.substring(mimeType
									.lastIndexOf("/") + 1);
							String b64data = element.getText();
							byte[] imageData = Base64.decode(b64data);
							AssetInfo aAssetInfo = new AssetInfo();
							aAssetInfo.setData(imageData);
							aAssetInfo.setExt(ext);
							assetMap.put(imageId, aAssetInfo);
						}
					}
				}
				itemxml = updateItem(decryptedContent, assetMap);
				itemxml = ServletUtils.doUTF8Chars(itemxml);
				contentHash.put("item" + itemId, itemxml);
			}
			ServletUtils.writeResponse(response, itemxml);
			
			//System.out.println("getItem elapsed time: " + (System.currentTimeMillis() - startTime));
		} 
		catch (HashMismatchException e) {
			logger.error("Exception occured in getItem("+itemId+") : "
					+ ServletUtils.printStackTrace(e));
            String errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.hashMismatch");                            
			ServletUtils.writeResponse(response, ServletUtils.buildXmlErrorMessage("", errorMessage, ""));
		}
		catch (DecryptionException e) {
			logger.error("Exception occured in getItem("+itemId+") : "
					+ ServletUtils.printStackTrace(e));
            String errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.decryptionFailed");                            
			ServletUtils.writeResponse(response, ServletUtils.buildXmlErrorMessage("", errorMessage, ""));
		}
		catch (Exception e) {
			logger.error("Exception occured in getItem("+itemId+") : "
					+ ServletUtils.printStackTrace(e));
            String errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.getContentFailed");                            
			ServletUtils.writeResponse(response, ServletUtils.buildXmlErrorMessage("", errorMessage, ""));
		}
	}
	
	private String updateItem( byte[] itemBytes, HashMap assetMap ) throws Exception
    {
		MemoryCache aMemoryCache = MemoryCache.getInstance();
		org.jdom.Document itemDoc = null;
		synchronized(aMemoryCache.saxBuilder) {
          itemDoc = aMemoryCache.saxBuilder.build( new ByteArrayInputStream( itemBytes ) );
		}
        org.jdom.Element rootElement = (org.jdom.Element) itemDoc.getRootElement();
        if (rootElement.getChild( "assets" )!=null)
        	rootElement.getChild( "assets" ).detach();
        List items = ServletUtils.extractAllElement( ".//image_widget", rootElement);
        for ( int i = 0; i < items.size(); i++ )
        {
            org.jdom.Element element = ( org.jdom.Element )items.get( i );
            String id = element.getAttributeValue( "image_ref" );
            if ( id != null && assetMap.containsKey( id ))
                element.setAttribute( "src", id );
        }
        XMLOutputter aXMLOutputter = new XMLOutputter();
        StringWriter aStringWriter = new StringWriter();
        aXMLOutputter.output( rootElement, aStringWriter );
        return aStringWriter.getBuffer().toString();
    }
	
	/**
	 * Get iamge from memory cache
	 * 
	 * @param HttpServletRequest
	 *            request
	 * @param HttpServletResponse
	 *            response - get image from memory cache
	 * 
	 */
	private void getImage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		MemoryCache aMemoryCache = MemoryCache.getInstance();
		HashMap assetMap = aMemoryCache.getAssetMap();

		String xml = ServletUtils.getXml(request);
		String imageId;
		
		//long startTime = System.currentTimeMillis();
		
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
			
			AssetInfo assetInfo = (AssetInfo) contentHash.get("image" + imageId);
			if(assetInfo == null) {
				if (!assetMap.containsKey(imageId)) 
					throw new Exception("Image with id '"+imageId+
							"' not found in memory cache. Please call getItem before getImage.");
	
				assetInfo = (AssetInfo) assetMap.get(imageId);
				if (assetInfo == null) {
					throw new Exception("Image with id '"+imageId+
					"' not found in memory cache. Please call getItem before getImage.");
				} else {
					contentHash.put("image" + imageId, assetInfo);
				}
			}
            String MIMEType = assetInfo.getMIMEType();
            response.setContentType( MIMEType );
            byte[] data = assetInfo.getData();
            int size = data.length;
            response.setContentLength( size );
            ServletOutputStream myOutput = response.getOutputStream();
            myOutput.write( data );
            myOutput.flush();
            myOutput.close();	
            
            //System.out.println("getImage elapsed time: " + (System.currentTimeMillis() - startTime));
		} catch (Exception e) {
			logger.error("Exception occured in getImage() : "
					+ ServletUtils.printStackTrace(e));
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
