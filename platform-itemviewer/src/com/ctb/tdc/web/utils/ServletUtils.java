package com.ctb.tdc.web.utils;
  
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.zip.Adler32;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.log4j.Logger;
import org.jdom.Element;


import com.ctb.tdc.web.dto.ServletSettings;
import com.ctb.tdc.web.dto.SubtestKeyVO;

/**
 * @author Tai_Truong
 *
 * This class provides helper methods for local servlets
 * PersistenceServlet.java
 * LoadContentServlet.java
 * DownloadContentServlet.java
 * ContentServlet.java
 *
 */
public class ServletUtils {
	public static final String SERVLET_NAME = "tdc";
	public static final String PROXY_NAME = "proxy";
	static Logger logger = Logger.getLogger(ServletUtils.class);

	public static HttpClient client;
	
	static {
		try {
			MultiThreadedHttpConnectionManager mgr = new MultiThreadedHttpConnectionManager();
			mgr.getParams().setDefaultMaxConnectionsPerHost(1);
			mgr.getParams().setMaxTotalConnections(1);
			client = new HttpClient(mgr);
			client.getParams().setConnectionManagerTimeout(10000);
			
		//	Protocol myhttps = new Protocol("https", new com.ctb.tdc.web.utils.EasySSLProtocolSocketFactory(), 443);
			//Protocol.registerProtocol("https", new Protocol("https", new com.ctb.tdc.web.utils.EasySSLProtocolSocketFactory(),443));
			
		//	String proxyHost = getProxyHost();
	
			
		} catch(Exception e) {
			logger.error("Exception occured in ServletUtils initializer : " + printStackTrace(e));
			throw new RuntimeException(e.getMessage());
		}
	}
	
//	url for servlet and tms actions
	public static final String URL_PERSISTENCE_SERVLET = "/servlet/PersistenceServlet";
	public static final String URL_LOADCONTENT_SERVLET = "/servlet/LoadContentServlet";
	public static final String URL_DOWNLOADCONTENT_SERVLET = "/servlet/DownloadContentServlet";
	public static final String URL_WEBAPP_LOGIN = "/TestDeliveryWeb/CTB/login.do";
	public static final String URL_WEBAPP_FEEDBACK = "/TestDeliveryWeb/CTB/feedback.do";
	public static final String URL_WEBAPP_SAVE = "/TestDeliveryWeb/CTB/save.do";
	public static final String URL_WEBAPP_UPLOAD_AUDIT_FILE = "/TestDeliveryWeb/CTB/uploadAuditFile.do";
	public static final String URL_WEBAPP_WRITE_TO_AUDIT_FILE = "/TestDeliveryWeb/CTB/writeToAuditFile.do";
	public static final String URL_WEBAPP_GET_STATUS_METHOD = "/TestDeliveryWeb/CTB/getStatus.do";
	public static final String URL_WEBAPP_GET_LOAD_TEST_CONFIG = "/TestDeliveryWeb/CTB/getLoadTestConfig.do";
	public static final String URL_WEBAPP_UPLOAD_STATISTICS = "/TestDeliveryWeb/CTB/uploadStatistics.do";
	public static final String URL_WEBAPP_UPLOAD_SYSTEM_INFO = "/TestDeliveryWeb/CTB/uploadSystemInfo.do";
	
//	methods
	public static final String NONE_METHOD = "none";
	public static final String DOWNLOAD_CONTENT_METHOD = "downloadContent";
	public static final String INITITAL_DOWNLOAD_CONTENT_METHOD = "initialDownloadContent";
	public static final String LOAD_SUBTEST_METHOD = "loadSubtest";
	public static final String LOAD_ITEM_METHOD = "loadItem";
	public static final String LOAD_IMAGE_METHOD = "loadImage";
	public static final String LOAD_LOCAL_IMAGE_METHOD = "loadLocalImage";
	public static final String LOGIN_METHOD = "login";
	public static final String SAVE_METHOD = "save";
	public static final String FEEDBACK_METHOD = "feedback";
	public static final String UPLOAD_AUDIT_FILE_METHOD = "uploadAuditFile";
	public static final String WRITE_TO_AUDIT_FILE_METHOD = "writeToAuditFile";
	public static final String VERIFY_SETTINGS_METHOD = "verifySettings";
	public static final String GET_STATUS_METHOD = "getStatus";
	public static final String GET_SUBTEST_METHOD = "getSubtest";
	public static final String DOWNLOAD_ITEM_METHOD = "downloadItem";
	public static final String GET_ITEM_METHOD = "getItem";
	public static final String GET_IMAGE_METHOD = "getImage";
	public static final String GET_LOCALRESOURCE_METHOD = "getLocalResource";
	public static final String LOAD_TEST_METHOD = "getLoadTestConfig";
	public static final String UPLOAD_STATISTICS_METHOD = "uploadStatistics";
	public static final String UPLOAD_SYSTEM_INFO_METHOD = "uploadSystemInfo";

//	parameters
	public static final String FOLDER_PARAM = "folder";
	public static final String USER_PARAM = "user";
	public static final String METHOD_PARAM = "method";
	public static final String ITEMNUM_PARAM ="itemNum" ;
	public static final String TEST_ROSTER_ID_PARAM = "testRosterId";
	public static final String ACCESS_CODE_PARAM = "accessCode";
	public static final String ITEM_SET_ID_PARAM = "itemSetId";
	public static final String ITEM_ID_PARAM = "itemId";
	public static final String IMAGE_ID_PARAM = "imageId";
	public static final String ENCRYPTION_KEY_PARAM = "encryptionKey";
	public static final String XML_PARAM = "requestXML";
	public static final String AUDIT_FILE_PARAM = "auditFile";
	public static final String CHECKSUM_PARAM = "checksum";
	public static final String LOAD_LOCAL_IMAGE_PARAM = "fileName";
	public static final String SUBTEST_ID_PARAM = "subtestId";
	public static final String HASH_PARAM = "hash";
	public static final String KEY_PARAM = "key";

//	events
	public static final String RECEIVE_EVENT = "RCV";
	public static final String ACTKNOWLEDGE_EVENT = "ACK";

//	returned values
	public static final String OK = "<OK />";
	public static final String ERROR = "<ERROR />";

//	misc
	public static final String NONE = "-";
	public static final long SECOND = 1000;

//	helper methods

	//private static String lastMseq;
	
	public static synchronized void writeResponse(HttpServletResponse response, String xml) {
		writeResponse(response, xml, null);
	}
	
	/**
	 * write xml content to response
	 *
	 */
	public static void writeResponse(HttpServletResponse response, String xml, String mseq) {
		try {
			//if((mseq == null || lastMseq == null) || !mseq.equals(lastMseq)) {
				response.setContentType("text/xml");
				response.setStatus(response.SC_OK);
				PrintWriter out = response.getWriter();
				out.println(xml);
				out.flush();
				out.close();
				response.flushBuffer();
			//	lastMseq = mseq;
			//}
		} catch (Exception e) {
			// do nothing, response already written
		}
	}

	/**
	 * parse response value in xml
	 *
	 */
	public static String parseResponse(String xml) {
		String itemResponse = NONE;
		if (xml != null) {
			int startIndex = xml.indexOf("<v>");
			int endIndex = xml.lastIndexOf("</v>");
			if ((startIndex > 0) && (endIndex > 0) && (endIndex < xml.length())) {
				if ((startIndex + 3) >= endIndex)
					itemResponse = "";
				else
					itemResponse = xml.substring(startIndex + 3, endIndex);
			}
		}
		return itemResponse;
	}

	/**
	 * parse response value in xml
	 *
	 */
	public static String parseModelData(String xml) {
		String modelData = "";
		if (xml != null) {
			int startIndex = xml.indexOf("<audit_file_text>");
			int endIndex = xml.lastIndexOf("</audit_file_text>");
			if ((startIndex > 0) && (endIndex > 0) && (endIndex < xml.length())) {
				if ((startIndex + 17) >= endIndex)
					modelData = "";
				else
					modelData = xml.substring(startIndex + 17, endIndex);
			}
		}
		return modelData;
	}

	/**
	 * parse status value in xml if equals 'OK'
	 *
	 */
	public static boolean isStatusOK(String xml) {
		int index = xml.indexOf("status=\"OK\"");
		return (index > 0);
	}

	/**
     * parse to find if value in xml equals 'lms_finish'
     * 
     */
    public static boolean isEndSubtest(String xml) {
        int index = xml.indexOf("lev e=\"lms_finish\"");
        return (index > 0);
    }
    
    /**
	 * parse status_code value in xml if equals 'OK'
	 *
	 */
	public static boolean isLoginStatusOK(String xml) {
		int index = xml.indexOf("status_code=\"200\"");
		return (index > 0);
	}

	/**
	 * parse event value in xml
	 *
	 */
	public static String parseEvent(String xml) {
		return parseTag("lev e=", xml);
	}

	/**
	 * parse mseq value in xml
	 *
	 */
	public static String parseMseq(String xml) {
		return parseTag("mseq=", xml);
	}

	/**
	 * parse lsid value in xml
	 *
	 */
	public static String parseLsid(String xml) {
		return parseTag("lsid=", xml);
	}

	/**
	 * parse item id value in xml
	 *
	 */
	public static String parseItemId(String xml) {
		return parseTag("iid=", xml);
	}

	/**
	 * parse test roster id value in xml
	 *
	 */
	public static String parseTestRosterId(String xml) {
		String testRosterId = NONE;
		String lsid = parseLsid(xml);
		if (! lsid.equals(NONE)) {
			StringTokenizer st = new StringTokenizer(lsid, ":");
			testRosterId = st.nextToken();
		}
		return testRosterId;
	}

	/**
	 * parse access code value in xml
	 *
	 */
	public static String parseAccessCode(String xml) {
		String accessCode = NONE;
		String lsid = parseLsid(xml);
		if (! lsid.equals(NONE)) {
			StringTokenizer st = new StringTokenizer(lsid, ":");
			accessCode = st.nextToken();
			accessCode = st.nextToken();
		}
		return accessCode;
	}

	/**
	 * parse item set id value in xml
	 *
	 */
	public static String parseItemSetId(String xml) {
		return parseTag("scid=", xml);
	}

	/**
	 * parse a tag value in xml
	 *
	 */
	public static String parseTag(String tagName, String xml) {
		String tagValue = NONE;
		if (xml != null) {
			int index = xml.indexOf(tagName);
			if (index > 0) {
				int startIndex = index + tagName.length() + 1;
				int endIndex = startIndex;
				while (true) {
					int ch = xml.charAt(endIndex);
					if ((ch == 34) || (ch == 39) || (endIndex > xml.length()-1))
						break;
					endIndex++;
				}
				tagValue = xml.substring(startIndex, endIndex);
			}
		}
		return tagValue;
	}

	/**
	 * create AuditVO
	 *
	 *//*
	public static AuditVO createAuditVO(String xml, boolean isItemResponse) {
		AuditVO audit = null;
		String fileName = buildFileName(xml);
		String mseq = parseMseq(xml);
		if (isItemResponse) {
			String itemId = parseItemId(xml);
			String response = parseResponse(xml);
			audit = new AuditVO(fileName, mseq, itemId, response);
		}
		else {
			String modelData = parseModelData(xml);
			audit = new AuditVO(fileName, mseq, modelData);
		}
		return audit;
	}*/

	/**
	 * initialize memory cache object from values read from resource bundle
	 *
	 */
	public static void readServletSettings() {
		//validateServletSettings();
	}
  
	/**
	 * validate value settings, initialize memory cache object from values read from resource bundle
	 *
	 */
	

	/**
	 * buildErrorMessage
	 *
	 */
	public static String buildXmlErrorMessage(String header, String message, String code) {
		String xml = "<ERROR>";
		xml += "<HEADER>" + header + "</HEADER>";
		xml += "<MESSAGE>" + message + "</MESSAGE>";
		xml += "<CODE>" + code + "</CODE>";
		xml += "</ERROR>";
		return xml;
	}

	/**
	 * buildErrorMessage
	 *
	 */
	public static String getErrorMessage(String error) {
		ResourceBundle rb = ResourceBundle.getBundle("tdcResources");
		return rb.getString(error);
	}

	/**
	 * get predefined webapp name for a method
	 *
	 */
	public static String getWebAppName(String method) {
		String webApp = URL_WEBAPP_SAVE;
		if (method.equals(LOGIN_METHOD))
			webApp = URL_WEBAPP_LOGIN;
		else
			if (method.equals(FEEDBACK_METHOD))
				webApp = URL_WEBAPP_FEEDBACK;
			else
				if (method.equals(SAVE_METHOD))
					webApp = URL_WEBAPP_SAVE;
				else
					if (method.equals(UPLOAD_AUDIT_FILE_METHOD))
						webApp = URL_WEBAPP_UPLOAD_AUDIT_FILE;
					else
						if (method.equals(WRITE_TO_AUDIT_FILE_METHOD))
							webApp = URL_WEBAPP_WRITE_TO_AUDIT_FILE;
						else
							if (method.equals(GET_STATUS_METHOD))
								webApp = URL_WEBAPP_GET_STATUS_METHOD;
							else
								if (method.equals(LOAD_TEST_METHOD))
									webApp = URL_WEBAPP_GET_LOAD_TEST_CONFIG;
								else
									if (method.equals(UPLOAD_STATISTICS_METHOD))
										webApp = URL_WEBAPP_UPLOAD_STATISTICS;
									else
										if (method.equals(UPLOAD_SYSTEM_INFO_METHOD))
											webApp = URL_WEBAPP_UPLOAD_SYSTEM_INFO;
		return webApp;
	}

	

	/**
	 * getMethod
	 *
	 */
	public static String getMethod(HttpServletRequest request) {
		return request.getParameter(METHOD_PARAM);
	}

	public static String getFolder(HttpServletRequest request) {
		return request.getParameter(FOLDER_PARAM);
	}
	/**
	 * getXml
	 *
	 */
	public static String getItemNo(HttpServletRequest request) {
		return request.getParameter(ITEMNUM_PARAM);
	}
 
	public static String getUser(HttpServletRequest request) {
		return request.getParameter(USER_PARAM);
	}

	/**
	 * getItemSetId
	 *
	 */
	public static String getItemSetId(HttpServletRequest request) {
		return request.getParameter(ITEM_SET_ID_PARAM);
	}

	/**
	 * getItemId
	 *
	 */
	public static String getItemId(HttpServletRequest request) {
		return request.getParameter(ITEM_ID_PARAM);
	}

	/**
	 * getImageId
	 *
	 */
	public static String getImageId(HttpServletRequest request) {
		return request.getParameter(IMAGE_ID_PARAM);
	}

	/**
	 * getEncryptionKey
	 *
	 */
	public static String getEncryptionKey(HttpServletRequest request) {
		return request.getParameter(ENCRYPTION_KEY_PARAM);
	}

	/**
	 * getXml
	 *
	 */
	public static String getXml(HttpServletRequest request) {
		return request.getParameter(XML_PARAM);
	}

	/**
	 * getSubtestId
	 *
	 */
	public static String getSubtestId(HttpServletRequest request) {
		return request.getParameter(SUBTEST_ID_PARAM);
	}

	/**
	 * getHash
	 *
	 */
	public static String getHash(HttpServletRequest request) {
		return request.getParameter(HASH_PARAM);
	}

	/**
	 * getKey
	 *
	 */
	public static String getKey(HttpServletRequest request) {
		return request.getParameter(KEY_PARAM);
	}


	/**
	 * hasResponse
	 *
	 */
	public static boolean hasResponse(String xml) {
		String response = parseResponse(xml);
		return (! response.equals(NONE));
	}

	/**
	 * get MIME based on file extension
	 *
	 */
	public static String getMIMEType(String ext) {
		String mimeType = "image/gif";
		if ("swf".equals(ext))
			mimeType = "application/x-shockwave-flash";
		if ("gif".equals(ext))
			mimeType = "image/gif";
		if ("jpg".equals(ext))
			mimeType = "image/jpg";
		return mimeType;
	}

	/**
	 * construct a file name from xml
	 *
	 *//*
	public static String buildFileName(String xml) {
		String fullFileName = null;
		String lsid = parseLsid(xml);
		if ((lsid != null) && (!lsid.equals("-"))) {
			String fileName = lsid.replace(':', '_');
			String tdcHome = System.getProperty(AuditFile.TDC_HOME);
			fullFileName = tdcHome + AuditFile.AUDIT_FOLDER + fileName + AuditFile.AUDIT_EXTENSION;
		}
		return fullFileName;
	}
*/
	/**
	 * urlConnectionSendRequest
	 * @param String xml
	 * @param String method
	 *
	 * Connect to TMS and send request using URLConnection
	 *
	 */
/*	public static String urlConnectionSendRequest(String method, String xml) {
		String result = OK;
		try {
//			get TMS url to make connection
			URL tmsURL = getTmsURL(method);
			URLConnection tmsConnection = tmsURL.openConnection();
			tmsConnection.setDoOutput(true);
			PrintWriter out = new PrintWriter(tmsConnection.getOutputStream());

//			send to TMS
			out.println(METHOD_PARAM + "=" + method + "&" + XML_PARAM + "=" + xml);
			out.close();

//			get response from TMS
			BufferedReader in = new BufferedReader(new InputStreamReader(tmsConnection.getInputStream()));
			String inputLine = null;
			result = "";
			while ((inputLine = in.readLine()) != null) {
				result += inputLine;
			}
			in.close();
		}
		catch (Exception e) {
			logger.error("Exception occured in urlConnectionSendRequest() : " + printStackTrace(e));
			result = ERROR;
		}
		return result;
	} */


	public static byte[] readFromFile(File file)
	{
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream aBufferedInputStream = new BufferedInputStream(fis);
			int size = aBufferedInputStream.available();
			buffer = new byte[size];
			aBufferedInputStream.read(buffer);
			aBufferedInputStream.close();
		} catch (Exception e) {
			logger.error("Exception occured in readFromFile() : " + printStackTrace(e));
		}
		return buffer;
	}

	public static String replaceAll( String src, String toBeReplace, String replaceWith )
	{
		String result = src;
		int index = 0;
		int difference = replaceWith.length();
		while ( ( index = result.indexOf( toBeReplace, index )) >= 0 )
		{
			result = result.substring( 0, index ) + replaceWith + result.substring( index + toBeReplace.length() );
			index += difference;
		}
		return result;
	}

	public static String doUTF8Chars( String input )
	{
		final int lineFeed = 10;
		final int carriageReturn = 13;
		final int tab = 9;
		final int plusSign = 43;
		final int maxASCII = 127;
		final int space = 127;
		StringBuffer retVal = new StringBuffer( input.length() * 2 );
		boolean isPreviousCharSpace = false;
		String s;
		for(int i = 0; i < input.length(); i++)
		{
			char c = input.charAt( i );
			int intc = c;
			if( intc != tab && intc != lineFeed && intc != carriageReturn )
			{
				if( intc <= maxASCII && intc != plusSign )
				{
					if( intc == space )
					{
						if( !isPreviousCharSpace )
						{
							retVal.append( c );
							isPreviousCharSpace = true;
						}
					}
					else
					{
						isPreviousCharSpace = false;
						retVal.append( c );
					}
				}
				else
				{
					isPreviousCharSpace = false;
					retVal.append( "&#" ).append( intc ).append( ';' );
				}
			}
		}
		s = retVal.toString();
		s = replaceAll( s, "&#+;", "&#x002B;" );
		s = replaceAll( s, "+", "&#x002B;" );
		//Defect# 64272: added for "<" Defect. 
		s = s.replaceAll("&#x003C", "&LT;");
		s = s.replaceAll("&lt;", "&LT;");

		return s;
	}

	public static List extractAllElement(String pattern, Element element ) throws Exception
	{
//		TO-DO: this will only work with simple './/name' queries as is . . .
		ArrayList results = new ArrayList();
		pattern = pattern.substring(pattern.indexOf(".//") + 3);
		List children = element.getChildren();
		Iterator iterator = children.iterator();
		while(iterator.hasNext()) {
			Element elem = (Element) iterator.next();
			if(pattern.equals(elem.getName())) {
				results.add(elem);
			}
			results.addAll(extractAllElement(".//" + pattern, elem));
		}
		return results;

//		This doesn't work with current JDOM
		/** XPath assetXPath = XPath.newInstance( pattern );
List elementList = assetXPath.selectNodes( element );
return elementList;*/
	}

	public static long getChecksum(File file)
	{
		long value = -1L;
		try {
			byte[] fileContent = readFromFile(file);
			if (fileContent != null) {
				Adler32 adler = new Adler32();
				adler.update(fileContent);
				value = adler.getValue();
			}
		} catch (Exception e) {
			logger.error("Exception occured in getChecksum() : " + printStackTrace(e));
		}
		return value;
	}

	public static boolean isFileSizeTooBig(String fileName)
	{
		File file = new File(fileName);
		long fileSize = file.length();
		if (fileSize >= 200000)
			return true;
		return false;
	}

	public static String printStackTrace( Exception e )
	{
		String result = null;
		try
		{
			StringWriter stringWriter = new StringWriter();
			PrintWriter printer = new PrintWriter( stringWriter );
			e.printStackTrace( printer );
			printer.flush();
			printer.close();
			result = stringWriter.getBuffer().toString();
		}
		catch ( Exception e1 )
		{
			result = "Nested Exception inside ServletUtils::getStackTrace";
		}
		return result;
	}

	public static String buildPersistenceParameters(HttpServletRequest request, String method) {
		String xml = null;
		if (method != null) {
			if (method.equals("login")) {
				String user_name = request.getParameter("user_name");
				String password = request.getParameter("password");
				String access_code = request.getParameter("access_code");
				if (user_name != null && password != null && access_code != null) {
					xml = "<tmssvc_request method=\"login\" xmlns=\"\">" +
					"<login_request user_name=\"" + user_name + "\" password=\"" + password + "\" access_code=\"" + access_code + "\" os_enum=\"Mac\" browser_agent_enum=\"MSIE\" user_agent_string=\"string\" sds_date_time=\"2013-11-23T06:44:07\" sds_id=\"string\" token=\"string\"/>" +
					"</tmssvc_request>";
				}
			}
			else
				if (method.equals("save")) {
					String res = request.getParameter("response");
					String lsid = request.getParameter("lsid");
					String mseq = request.getParameter("mseq");
					String itemId = request.getParameter("itemId");
					if (res != null && lsid != null && mseq != null && itemId != null) {
						xml = "<adssvc_request method=\"save_testing_session_data\"><save_testing_session_data><tsd lsid=\"" + lsid + "\" scid=\"24009\" mseq=\"" + mseq + "\"><ist dur=\"2\" awd=\"1\" mrk=\"0\" iid=\"" + itemId + "\"><rv t=\"identifier\" n=\"RESPONSE\"><v>" + res + "</v></rv></ist></tsd></save_testing_session_data></adssvc_request>";
					}
				}
				else
					if (method.equals("uploadAuditFile")) {
						String file_name = request.getParameter("file_name");
						if (file_name != null) {
							xml = "<adssvc_request method=\"save_testing_session_data\"><save_testing_session_data><tsd lsid=\"" + file_name + "\" scid=\"24009\" ><ist dur=\"2\" awd=\"1\" mrk=\"0\" iid=\"OKPT_SR.EOI.BIO.001\"></ist></tsd></save_testing_session_data></adssvc_request>";
						}
					}
		}
		return xml;
	}

	public static String buildLoadContentParameters(HttpServletRequest request, String method) {
		String result = null;
		if (method.equals("loadSubtest")) {
			result = request.getParameter("itemSetIdParam");
		}
		else
			if (method.equals("loadItem")) {
				result = request.getParameter("itemIdParam");
			}
			else
				if (method.equals("loadImage")) {
					result = request.getParameter("imageIdParam");
				}
		return result;
	}

	public static String buildContentRequest(HttpServletRequest request, String method) {
		String xml = null;
		if (method != null) {
			if (method.equals(GET_SUBTEST_METHOD)) {
				String subtestId = ServletUtils.getSubtestId(request);
				String hash = ServletUtils.getHash(request);
				
				String key = ServletUtils.getKey(request);
				if (subtestId != null && hash != null && key != null) {
					xml = "<adssvc_request method=\"getSubtest\" sdsid=\"string\" token=\"string\" xmlns=\"\">" +
					"<get_subtest subtestid=\""+subtestId+"\" hash=\""+hash+"\" key=\""+key+"\"/>" +
					"</adssvc_request>";
				}
			}
			else if (method.equals(DOWNLOAD_ITEM_METHOD)) {
				String itemId = ServletUtils.getItemId(request);
				String hash = ServletUtils.getHash(request);
				String key = ServletUtils.getKey(request);
				itemId = "81502";
			    hash = "AAF02CDAD1CFCA9C7F259E811299297B";
			    key = "n7673nBJ2n27bB4oAfme7Ugl5VV42g8";
				if (itemId != null && hash != null && key != null) {
					xml = "<adssvc_request method=\"downloadItem\" sdsid=\"string\" token=\"string\" xmlns=\"\">" +
					"<download_item itemid=\""+itemId+"\" hash=\""+hash+"\" key=\""+key+"\"/>" +
					"</adssvc_request>";
				}
			}
		}
		return xml;
	}

	
		
	public static void shutdown() {
		synchronized(client) {
			((MultiThreadedHttpConnectionManager) ServletUtils.client.getHttpConnectionManager()).shutdown();
		}
	}
}