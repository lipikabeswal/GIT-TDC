package com.ctb.tdc.web.servlet;

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
import noNamespace.AdssvcResponseDocument;
import noNamespace.ErrorDocument;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;
import org.jdom.output.XMLOutputter;

import com.bea.xml.XmlException;
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
		
		long startTime = System.currentTimeMillis();
		
		
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
		
		logger.info("ContentServlet: " + method + " took " + (System.currentTimeMillis() - startTime) + "\n");

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

			if (subtestId != null && !"".equals(subtestId.trim()) && !"undefined".equals(subtestId.trim())) {
				
				String filePath = ContentFile.getContentFolderPath() + subtestId
						+ ContentFile.SUBTEST_FILE_EXTENSION;
	
				boolean validHash = false;
				
				try {
					validHash = ContentFile.validateHash(filePath, hash);
				} catch (Exception e) {
					validHash = false;
				}
				
				if (!validHash) {
					String result = "";
					MemoryCache memoryCache = MemoryCache.getInstance();
		        	int TMSRetryCount = memoryCache.getSrvSettings().getTmsMessageRetryCount();
		        	int TMSRetryInterval = memoryCache.getSrvSettings().getTmsMessageRetryInterval();
		        	int expansion = memoryCache.getSrvSettings().getTmsMessageRetryExpansionFactor();
					AdssvcResponseDocument document = null;
					ErrorDocument.Error error = null;
					int i = 1;
					while (TMSRetryCount > 0) {
						//logger.info("***** downloadSubtest " + subtestId);
						result = ServletUtils.httpClientSendRequest(ServletUtils.GET_SUBTEST_METHOD, xml);
						document = AdssvcResponseDocument.Factory.parse(result);
						error = document.getAdssvcResponse().getGetSubtest().getError();
						if (error != null) {
							if(TMSRetryCount > 1) {
								//logger.error("Retrying message: " + xml);
								Thread.sleep(TMSRetryInterval * ServletUtils.SECOND * i);
							}
							TMSRetryCount--;
						} else {
							TMSRetryCount = 0;
						}
						i = i*expansion;
					}
					if (error != null) {
						throw new TMSException(error.getErrorDetail());
					}
	
					byte[] content = document.getAdssvcResponse().getGetSubtest()
							.getContent();
					ContentFile.writeToFile(content, filePath);
				}
				byte[] decryptedContent = ContentFile.decryptFile(filePath, hash,
						key);
				response.setContentType("text/xml");
				int size = decryptedContent.length;
				response.setContentLength(size);
				ServletOutputStream myOutput = response.getOutputStream();
				myOutput.write(decryptedContent);
				myOutput.flush();
				myOutput.close();
			} 
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
	private void downloadItem(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String xml = ServletUtils.getXml(request);
		String itemId = null;
		String hash;
		
		try {
			if (xml == null) {
				itemId = ServletUtils.getItemId(request);
				hash = ServletUtils.getHash(request);
				xml = ServletUtils.buildContentRequest(request,
						ServletUtils.DOWNLOAD_ITEM_METHOD);
			}
			else {
				AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
				itemId = document.getAdssvcRequest().getDownloadItem().getItemid();
				hash = document.getAdssvcRequest().getDownloadItem().getHash();
			}
		
			if (itemId != null && !"".equals(itemId.trim())) {
				String filePath = ContentFile.getContentFolderPath() + itemId
						+ ContentFile.ITEM_FILE_EXTENSION;
				
				boolean hashValid = false;
				try {
					hashValid = ContentFile.validateHash(filePath, hash);
				} catch (Exception e) {
					hashValid = false;
				}
				
				if (!hashValid) {
					MemoryCache memoryCache = MemoryCache.getInstance();
		        	int TMSRetryCount = memoryCache.getSrvSettings().getTmsMessageRetryCount();
		        	int TMSRetryInterval = memoryCache.getSrvSettings().getTmsMessageRetryInterval();
		        	int expansion = memoryCache.getSrvSettings().getTmsMessageRetryExpansionFactor();
					int errorIndex = 0;
					String result = "";
					int i = 1;
					while (TMSRetryCount > 0) {
						//logger.info("***** downloadItem " + itemId);
						result = ServletUtils.httpClientSendRequest(ServletUtils.DOWNLOAD_ITEM_METHOD, xml);
						errorIndex = result.indexOf("<ERROR>");
						if (errorIndex >= 0) {
							if(TMSRetryCount > 1) {
								//logger.error("Retrying message: " + xml);
								try {
									Thread.sleep(TMSRetryInterval * ServletUtils.SECOND * i);
								} catch (InterruptedException ie) {
									// do nothing
								}
							}
							TMSRetryCount--;
						} else {
							TMSRetryCount = 0;
						}
						i = i*expansion;
					}
					if (errorIndex >= 0) {
						throw new TMSException(result);
					}
					
					AdssvcResponseDocument document = AdssvcResponseDocument.Factory
							.parse(result);
					ErrorDocument.Error error = document.getAdssvcResponse()
							.getDownloadItem().getError();
					if (error != null)
						throw new TMSException(error.getErrorDetail());
	
					byte[] content = document.getAdssvcResponse().getDownloadItem()
							.getContent();
					ContentFile.writeToFile(content, filePath);
				}
				ServletUtils.writeResponse(response, ServletUtils.OK);
			} 
		}
		catch (TMSException e) {
			logger.error("TMS Exception occured in downloadItem("+itemId+") : "
					+ ServletUtils.printStackTrace(e));
            String errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.getContentFailed");                            
			ServletUtils.writeResponse(response, ServletUtils.buildXmlErrorMessage("", errorMessage, ""));
		}
		catch (XmlException e) {
			logger.error("XML Exception occured in downloadItem("+itemId+") : "
					+ ServletUtils.printStackTrace(e));
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
	 * 
	 */
	private void getItem(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		MemoryCache aMemoryCache = MemoryCache.getInstance();
		HashMap assetMap = aMemoryCache.getAssetMap();
		
		//clear up image cache for each getItem call
		//assetMap.clear();

		String xml = ServletUtils.getXml(request);
		String itemId = null;
		String hash;
		String key;
		
		try {
			if (xml == null) {
				itemId = ServletUtils.getItemId(request);
				hash = ServletUtils.getHash(request);
				key = ServletUtils.getKey(request);
			}
			else {
//				AdssvcRequestDocument document = AdssvcRequestDocument.Factory.parse(xml);
//				itemId = document.getAdssvcRequest().getGetItem().getItemid();
//				hash = document.getAdssvcRequest().getGetItem().getHash();
//				key = document.getAdssvcRequest().getGetItem().getKey();

				itemId = getAttributeValue("itemid", xml);
				hash = getAttributeValue("hash", xml);
				key = getAttributeValue("key", xml);
				
//System.out.println("itemId="+itemId+" hash="+hash+" key="+key);				
			
			}

			if (itemId == null || "".equals(itemId.trim())) // invalid item id
				throw new Exception("No item id in request.");
			String filePath = ContentFile.getContentFolderPath() + itemId
					+ ContentFile.ITEM_FILE_EXTENSION;
			
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
			String itemxml = updateItem(decryptedContent, assetMap);
			itemxml = ServletUtils.doUTF8Chars(itemxml);
			ServletUtils.writeResponse(response, itemxml);

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
			
			if (!assetMap.containsKey(imageId)) 
				throw new Exception("Image with id '"+imageId+
						"' not found in memory cache. Please call getItem before getImage.");

			AssetInfo assetInfo = (AssetInfo) assetMap.get(imageId);
			if (assetInfo == null)
				throw new Exception("Image with id '"+imageId+
				"' not found in memory cache. Please call getItem before getImage.");
            String MIMEType = assetInfo.getMIMEType();
            response.setContentType( MIMEType );
            byte[] data = assetInfo.getData();
            int size = data.length;
            response.setContentLength( size );
            ServletOutputStream myOutput = response.getOutputStream();
            myOutput.write( data );
            myOutput.flush();
            myOutput.close();				
		} catch (Exception e) {
			logger.error("Exception occured in getImage() : "
					+ ServletUtils.printStackTrace(e));
            String errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.getContentFailed");                            
			ServletUtils.writeResponse(response, ServletUtils.buildXmlErrorMessage("", errorMessage, ""));
		}
	}
	
	private void getLocalResource(HttpServletRequest request,HttpServletResponse response) throws IOException {
    	String filename = request.getParameter("resourcePath");
    	  	
    	try {
    		
    		if (filename == null || "".equals(filename.trim())) 
    			throw new Exception("No  in request.");

    		String filePath = this.RESOURCE_FOLDER_PATH + File.separator  + filename;

    		ServletOutputStream myOutput = response.getOutputStream();
    		
    		// assume all local flash resources are encrypted
    		FileInputStream input = new FileInputStream( filePath.replaceAll(".swf", ".enc"));
            int size = input.available();
            byte[] src = new byte[ size ];
            input.read( src );
            input.close();
            
            byte[] decrypted = ContentFile.decrypt(src);

            int index= filename.lastIndexOf(".");
    		String ext = filename.substring(index+1);
    		AssetInfo assetInfo = new AssetInfo();
    		assetInfo.setExt(ext);
    		String mimeType = assetInfo.getMIMEType();
    		response.setContentType(mimeType);
    		
    		response.setContentLength( size );
    		
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
	public static final String RESOURCE_FOLDER_PATH = System.getProperty(TDC_HOME) + File.separator + 
		                             "webapp" + File.separator + "resources";

}
