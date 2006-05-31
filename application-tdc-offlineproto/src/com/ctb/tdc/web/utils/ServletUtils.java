package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
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
                    itemResponse = xml.substring(endIndex-1, endIndex);
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
                    modelData = xml.substring(endIndex-1, endIndex);
            }
        }
        return modelData;
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
     * get predefined TMS Proxy URL as string for a method
     * 
     */
    public static String getProxyURLString(String method) throws MalformedURLException {
        MemoryCache memoryCache = MemoryCache.getInstance();
        ServletSettings srvSettings = memoryCache.getSrvSettings();
        String proxyHostPort = srvSettings.getProxyHostPort();
        String tmsWebApp = getWebAppName(method);
        return (proxyHostPort + tmsWebApp);
    }

     /**
     * get predefined TMS Proxy URL for a method
     * 
     */
    public static URL getProxyURL(String method) throws MalformedURLException {
        String proxyUrlString = getProxyURLString(method);
        URL proxyURL = new URL(proxyUrlString);
        return proxyURL;
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
            client.executeMethod(post);             
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
    
    
}

