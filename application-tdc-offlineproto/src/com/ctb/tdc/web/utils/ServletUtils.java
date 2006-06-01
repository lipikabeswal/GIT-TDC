package com.ctb.tdc.web.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom.Element;

import com.ctb.tdc.web.dto.AuditVO;
import com.ctb.tdc.web.dto.ServletSettings;
import com.ctb.tdc.web.dto.SubtestKeyVO;

/**
 * @author Tai_Truong
 */
public class ServletUtils {
    public static final String SERVLET_NAME = "tdc";

    // url for servlet and tms actions
    public static final String URL_PERSISTENCE_SERVLET = "/servlet/PersistenceServlet";
    public static final String URL_LOADCONTENT_SERVLET = "/servlet/LoadContentServlet";
    public static final String URL_DOWNLOADCONTENT_SERVLET = "/servlet/DownloadContentServlet";
    public static final String URL_WEBAPP_LOGIN = "/TestDeliveryWeb/CTB/login.do";
    public static final String URL_WEBAPP_FEEDBACK = "/TestDeliveryWeb/CTB/feedback.do";
    public static final String URL_WEBAPP_SAVE = "/TestDeliveryWeb/CTB/save.do";
    public static final String URL_WEBAPP_UPLOAD_AUDIT_FILE = "/TestDeliveryWeb/CTB/uploadAuditFile.do";
    public static final String URL_WEBAPP_WRITE_TO_AUDIT_FILE = "/TestDeliveryWeb/CTB/writeToAuditFile.do";
    
    // methods
    public static final String DOWNLOAD_CONTENT_METHOD = "downloadContent";
    public static final String INITITAL_DOWNLOAD_CONTENT_METHOD = "initialDownloadContent";
    public static final String LOAD_SUBTEST_METHOD = "loadSubtest";
    public static final String LOAD_ITEM_METHOD = "loadItem";
    public static final String LOAD_IMAGE_METHOD = "loadImage";
    public static final String LOGIN_METHOD = "login";
    public static final String SAVE_METHOD = "save";
    public static final String FEEDBACK_METHOD = "feedback";
    public static final String UPLOAD_AUDIT_FILE_METHOD = "uploadAuditFile";
    public static final String WRITE_TO_AUDIT_FILE_METHOD = "writeToAuditFile";

    // parameters
    public static final String METHOD_PARAM = "method";
    public static final String TEST_ROSTER_ID_PARAM = "testRosterId";
    public static final String ACCESS_CODE_PARAM = "accessCode";
    public static final String ITEM_SET_ID_PARAM = "itemSetId";
    public static final String ITEM_ID_PARAM = "itemId";
    public static final String IMAGE_ID_PARAM = "imageId";
    public static final String ENCRYPTION_KEY_PARAM = "encryptionKey";
    public static final String XML_PARAM = "requestXML";
    public static final String AUDIT_FILE_PARAM = "auditFile";

    // events
    public static final String RECEIVE_EVENT = "RCV"; 
    public static final String ACTKNOWLEDGE_EVENT = "ACK";
        
    // returned values
    public static final String OK = "<OK />";
    public static final String ERROR = "<ERROR />";

    // misc
    public static final String NONE = "-";
    
    // helper methods
    
     /**
     * write xml content to response 
     * 
     */
    public static void writeResponse(HttpServletResponse response, String xml) throws IOException {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        out.println(xml);            
        out.flush();
        out.close();        
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
        String modelData = NONE;
        if (xml != null) {
            int startIndex = xml.indexOf("<model_data>");
            int endIndex = xml.lastIndexOf("</model_data>");
            if ((startIndex > 0) && (endIndex > 0) && (endIndex < xml.length())) {
                if ((startIndex + 3) >= endIndex)
                    modelData = "";
                else
                    modelData = xml.substring(startIndex + 3, endIndex);
            }
        }
        return modelData;
    }
    
     /**
     * parse status value in xml
     * 
     */
    public static boolean isStatusOK(String xml) {
        int index = xml.indexOf("status=\"OK\"");
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
        return parseTag("itemSetId=", xml);
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
     */
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
    }
   
     /**
     * initialize memory cache object from values read from resource bundle
     * 
     */
    public static void initMemoryCache() {
        MemoryCache memoryCache = MemoryCache.getInstance();
        if (! memoryCache.isLoaded()) {
            ResourceBundle rb = ResourceBundle.getBundle(SERVLET_NAME);
            memoryCache.setSrvSettings(new ServletSettings(rb));
            memoryCache.setStateMap(new HashMap());        
            memoryCache.setLoaded(true);
        }
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
        return webApp;
    }
    
     /**
     * get predefined TMS URL as string for a method
     * 
     */
    public static String getTmsURLString(String method) {
        MemoryCache memoryCache = MemoryCache.getInstance();
        ServletSettings srvSettings = memoryCache.getSrvSettings();
        String tmsHostPort = srvSettings.getTmsHostPort();
        String tmsWebApp = getWebAppName(method);
        return (tmsHostPort + tmsWebApp);
    }

     /**
     * get predefined TMS URL for a method
     * 
     */
    public static URL getTmsURL(String method) {
        URL tmsURL = null;
        try {
            String tmsUrlString = getTmsURLString(method);
            tmsURL = new URL(tmsUrlString);
        } 
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return tmsURL;
    }

     /**
     * get predefined proxy host
     * 
     */
    public static String getProxyHost() throws MalformedURLException {
        MemoryCache memoryCache = MemoryCache.getInstance();
        ServletSettings srvSettings = memoryCache.getSrvSettings();
        String host = srvSettings.getProxyHost().trim();
        if (host.length() == 0)
            host = null;
        return host;
    }

     /**
     * get predefined proxy port
     * 
     */
    public static int getProxyPort() throws MalformedURLException {
        MemoryCache memoryCache = MemoryCache.getInstance();
        ServletSettings srvSettings = memoryCache.getSrvSettings();
        int port = Integer.valueOf(srvSettings.getProxyPort()).intValue();
        return port;
    }

     /**
     * get predefined proxy user name
     * 
     */
    public static String getProxyUserName() throws MalformedURLException {
        MemoryCache memoryCache = MemoryCache.getInstance();
        ServletSettings srvSettings = memoryCache.getSrvSettings();
        return srvSettings.getProxyUserName();
    }

     /**
     * get predefined proxy password
     * 
     */
    public static String getProxyPassword() throws MalformedURLException {
        MemoryCache memoryCache = MemoryCache.getInstance();
        ServletSettings srvSettings = memoryCache.getSrvSettings();
        return srvSettings.getProxyPassword();
    }
    
     /**
     * getMethod
     * 
     */
    public static String getMethod(HttpServletRequest request) {
        return request.getParameter(METHOD_PARAM);
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
     */
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
    
    /**
     * urlConnectionSendRequest
     * @param String xml
     * @param String method
     * 
     * Connect to TMS and send request using URLConnection
     * 
     */
    public static String urlConnectionSendRequest(String method, String xml) {
        String result = OK;
        try {
            // get TMS url to make connection
            URL tmsURL = getTmsURL(method);
            URLConnection tmsConnection = tmsURL.openConnection();
            tmsConnection.setDoOutput(true);
            PrintWriter out = new PrintWriter(tmsConnection.getOutputStream());

            // send to TMS
            out.println(METHOD_PARAM + "=" + method + "&" + XML_PARAM + "=" + xml);
            out.close();

            // get response from TMS
            BufferedReader in = new BufferedReader(new InputStreamReader(tmsConnection.getInputStream()));
            String inputLine = null;       
            result = "";
            while ((inputLine = in.readLine()) != null) {
                result += inputLine;
            }
            in.close();          
        } 
        catch (Exception e) {
            e.printStackTrace();
            result = ERROR;
        }        
        return result;
    }
    
    /**
     * httpConnectionSendRequest
     * @param String xml
     * @param String method
     * 
     * Connect to TMS and send request using HttpClient
     * 
     */
    public static String httpConnectionSendRequest(String method, String xml) {
        String result = OK;

        // create post method with url based on method
        String tmsURL = getTmsURLString(method);
        PostMethod post = new PostMethod(tmsURL);             
            
        // setup parameters
        NameValuePair[] params = { new NameValuePair(METHOD_PARAM, method),
                                   new NameValuePair(XML_PARAM, xml) };
        post.setRequestBody(params);
            
        // send request to TMS
        try {
            HttpClient client = new HttpClient(); 
            
            String proxyHost = getProxyHost();
            if (proxyHost != null) {
                // execute with proxy settings
                HostConfiguration hostConfiguration = client.getHostConfiguration();
                int proxyPort = getProxyPort();
                hostConfiguration.setProxy(proxyHost, proxyPort);            
                String username = getProxyUserName();
                String password = getProxyPassword();            
                UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
                HttpState state = client.getState();
                state.setProxyCredentials(null, proxyHost, credentials);
                client.executeMethod(hostConfiguration, post);    
            }
            else {
                // execute normal
                client.executeMethod(post);    
            }
            
            InputStream isPost = post.getResponseBodyAsStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(isPost));
            String inputLine = null;   
            result = "";
            while ((inputLine = in.readLine()) != null) {
                result += inputLine;
            }
            in.close();          
        } 
        catch (Exception e) {
            e.printStackTrace();
            result = ERROR;
        }
        finally {
            post.releaseConnection();
        }        
        return result;
    }
    
    public static void processContentKeys( String xml )throws Exception
    {
        final String endPattern = "</manifest>";
        int start = xml.indexOf( "<manifest " );
        int end = xml.indexOf( "</manifest>" );
        if ( start >= 0 && end > 10 )
        {
            String manifest = xml.substring( start, end + endPattern.length() );
            org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
            ByteArrayInputStream bais = new ByteArrayInputStream( manifest.getBytes( "UTF-8" ));
            org.jdom.Document assessmentDoc = saxBuilder.build( bais );
            Element inElement = assessmentDoc.getRootElement();
            List subtests = inElement.getChildren( "sco" );
            MemoryCache aMemoryCache = MemoryCache.getInstance();
            HashMap subtestInfoMap = aMemoryCache.getSubtestInfoMap();
            for ( int i = 0; i < subtests.size(); i++ )
            {
                Element subtest = ( Element )subtests.get( i );
                String itemSetId = subtest.getAttributeValue( "id" );
                String adsItemSetId = subtest.getAttributeValue( "adsid" );
                String asmtHash = subtest.getAttributeValue( "asmt_hash" );
                String asmtEncryptionKey = subtest.getAttributeValue( "asmt_encryption_key" );
                String item_encryption_key = subtest.getAttributeValue( "item_encryption_key" );
                if ( itemSetId != null && adsItemSetId != null 
                        && asmtHash != null && asmtEncryptionKey != null 
                        && item_encryption_key != null )
                {
                    SubtestKeyVO aSubtestKeyVO = new SubtestKeyVO();
                    aSubtestKeyVO.setItemSetId( itemSetId );
                    aSubtestKeyVO.setAdsItemSetId( adsItemSetId );
                    aSubtestKeyVO.setAsmtHash( asmtHash );
                    aSubtestKeyVO.setAsmtEncryptionKey( asmtEncryptionKey );
                    aSubtestKeyVO.setItem_encryption_key( item_encryption_key );
                    subtestInfoMap.put( itemSetId, aSubtestKeyVO );
                }
            }
        }
    }
    
    public static byte[] readFromFile( File filePath ) throws Exception
    {
	    BufferedInputStream aBufferedInputStream = new BufferedInputStream( new FileInputStream( filePath ) );
        int size = aBufferedInputStream.available();
        byte[] buffer = new byte[ size ];
        aBufferedInputStream.read( buffer );
        aBufferedInputStream.close();
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
        return s; 
    }
    
    public static List extractAllElement(String pattern, Element element ) throws Exception
    {
        // TO-DO: this will only work with simple './/name' queries as is . . .
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
        
        // This doesn't work with current JDOM
        /** XPath assetXPath = XPath.newInstance( pattern );
        List elementList = assetXPath.selectNodes( element );
        return elementList;*/
    }
}

