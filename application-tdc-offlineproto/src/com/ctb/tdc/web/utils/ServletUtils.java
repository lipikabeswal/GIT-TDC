package com.ctb.tdc.web.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

import com.ctb.tdc.web.dto.AuditVO;
import com.ctb.tdc.web.dto.ServletSettings;

/**
 * @author Tai_Truong
 */
public class ServletUtils {
    public static final String SERVLET_NAME = "tdc";

    // url for servlet and tms actions
    public static final String URL_PERSISTENCE_SERVLET = "/servlet/PersistenceServlet";
    public static final String URL_LOADCONTENT_SERVLET = "/servlet/LoadContentServlet";
    public static final String URL_DOWNLOADCONTENT_SERVLET = "/servlet/DownloadContentServlet";
    public static final String URL_WEBAPP_LOGIN = "/TestDeliveryWeb/login.do";
    public static final String URL_WEBAPP_FEEDBACK = "/TestDeliveryWeb/feedback.do";
    public static final String URL_WEBAPP_SAVE = "/TestDeliveryWeb/save.do";
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
    public static final String OK = "<OK>200 OK</OK>";
    public static final String METHOD_ERROR = "<ERROR>601 Invalid method</ERROR>";
    public static final String UPLOAD_FILE_ERROR = "<ERROR>602 Failed to upload file</ERROR>";
    public static final String ACK_ERROR = "<ERROR>603 No Acknowledge from TMS</ERROR>";

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
            int endIndex = xml.indexOf("</v>");
            if ((startIndex > 0) && (endIndex > 0) && (endIndex < xml.length())) {
                itemResponse = xml.substring(endIndex-1, endIndex);
            }
        }
        return itemResponse;
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
     * parse encryptionKey value in xml
     * 
     */
    public static String parseEncryptionKey(String xml) {        
        return parseTag("encryptionKey=", xml);
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
    public static AuditVO createAuditVO(String fileName, String lsid, String mseq, String event, String xml) {
        String encodedXml = AuditFileEncrytor.encrypt(xml);
        AuditVO audit = new AuditVO(fileName, lsid, mseq, event, encodedXml);
        return audit;
    }
    
     /**
     * create AuditVO
     * 
     */
    public static AuditVO createAuditVO(String xml, String event) {
        String lsid = parseLsid(xml);
        String fileName = FileUtils.buildFileName(lsid);
        String mseq = parseMseq(xml);
        String encodedXml = AuditFileEncrytor.encrypt(xml);
        AuditVO audit = new AuditVO(fileName, lsid, mseq, event, encodedXml);
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
    public static String getTmsURLString(String method) throws MalformedURLException {
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
    public static URL getTmsURL(String method) throws MalformedURLException {
        String tmsUrlString = getTmsURLString(method);
        URL tmsURL = new URL(tmsUrlString);
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
     * upload the audit file to TMS
     * 
     */
    public static String uploadAuditFile(String xml) throws MalformedURLException {
        String uploadStatus = OK;
        String testRosterId = parseTestRosterId(xml);
        String itemSetId = parseItemSetId(xml);
        String tmsURL = getTmsURLString(UPLOAD_AUDIT_FILE_METHOD);
        tmsURL += "?";
        tmsURL += TEST_ROSTER_ID_PARAM + "=" + testRosterId;
        tmsURL += "&";
        tmsURL += ITEM_SET_ID_PARAM + "=" + itemSetId;
        
        PostMethod filePost = new PostMethod(tmsURL); 
        try {
            String lsid = parseLsid(xml);
            String fileName = AuditFile.buildFileName(lsid);            
            File file = new File(fileName);
            Part[] parts = { new FilePart(AUDIT_FILE_PARAM, file) }; 
            filePost.setRequestEntity( new MultipartRequestEntity(parts, filePost.getParams()) ); 
            HttpClient client = new HttpClient(); 
            int status = client.executeMethod(filePost);             
            if (status != HttpStatus.SC_OK) 
                uploadStatus = UPLOAD_FILE_ERROR;
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            filePost.releaseConnection();
        }
        return uploadStatus;
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
     * isSaveResponse
     * 
     */
    public static boolean isSaveResponse(String xml) {
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
     * this method temporary for POC testing only, read parameters from test page
     * this will be delete when release
     */
    public static AuditVO getPOCParameters(HttpServletRequest request) {
        AuditVO result = new AuditVO(null, null, null, null, null);
        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("login")) {
                String user_name = request.getParameter("user_name");
                String password = request.getParameter("password");
                String access_code = request.getParameter("access_code");
                if (user_name != null && password != null && access_code != null) {
                    result.setEvent(ServletUtils.LOGIN_METHOD);
                    result.setXml("<tmssvc_request method=\"login\"><login_request user_name=\"" + user_name + "\" password=\"" + password + "\" access_code=\"" + access_code + "\" /></tmssvc_request>");            
                }
            }
            if (action.equals("response")) {
                String res = request.getParameter("response");
                String lsid = request.getParameter("lsid");
                String mseq = request.getParameter("mseq");
                if (res != null && lsid != null && mseq != null) {
                    result.setEvent(ServletUtils.SAVE_METHOD);
                    result.setXml("<adssvc_request method=\"save_testing_session_data\"><save_testing_session_data><tsd lsid=\"" + lsid + "\" scid=\"24009\" mseq=\"" + mseq + "\"><ist dur=\"2\" awd=\"1\" mrk=\"0\" iid=\"OKPT_SR.EOI.BIO.001\"><rv t=\"identifier\" n=\"RESPONSE\"><v>" + res + "</v></rv></ist></tsd></save_testing_session_data></adssvc_request>");            
                }
            }
            if (action.equals("upload")) {
                String file_name = request.getParameter("file_name");
                if (file_name != null) {
                    result.setEvent(ServletUtils.UPLOAD_AUDIT_FILE_METHOD);
                    result.setXml("<adssvc_request method=\"save_testing_session_data\"><save_testing_session_data><tsd lsid=\"" + file_name + "\" scid=\"24009\" ><ist dur=\"2\" awd=\"1\" mrk=\"0\" iid=\"OKPT_SR.EOI.BIO.001\"></ist></tsd></save_testing_session_data></adssvc_request>");            
                }
            }
        }
        else {
            result.setEvent(getMethod(request));
            result.setXml(getXml(request));            
        }
        return result;
    }
    
}
