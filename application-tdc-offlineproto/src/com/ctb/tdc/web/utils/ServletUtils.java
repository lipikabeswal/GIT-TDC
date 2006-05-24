package com.ctb.tdc.web.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

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

    public static final String URL_WEBAPP_LOGIN = "/TestDeliveryWeb/begin.do";
    public static final String URL_WEBAPP_SAVE_RESPONSE = "/TestDeliveryWeb/response.do";
    public static final String URL_WEBAPP_SAVE_LIFECYCLE = "/TestDeliveryWeb/lifecycle.do";
    public static final String URL_WEBAPP_FEEDBACK = "/TestDeliveryWeb/feedback.do";
    public static final String URL_WEBAPP_UPLOAD_AUDIT_FILE = "/TestDeliveryWeb/CTB/uploadAuditFile.do";
    
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
    public static final String OK = "<OK>200</OK>";
    public static final String METHOD_ERROR = "<Error>Invalid method</Error>";
    public static final String UPLOAD_FILE_ERROR = "<Error>Failed to upload file</Error>";
    public static final String ACK_ERROR = "<Error>No Acknowledge from TMS</Error>";

    // misc
    public static final String NONE = "-";
    
    // helpers
    public static String getMethod(HttpServletRequest request) {
        return request.getParameter(METHOD_PARAM);
    }

    public static String getItemSetId(HttpServletRequest request) {
        return request.getParameter(ITEM_SET_ID_PARAM);
    }

    public static String getItemId(HttpServletRequest request) {
        return request.getParameter(ITEM_ID_PARAM);
    }
    
    public static String getImageId(HttpServletRequest request) {
        return request.getParameter(IMAGE_ID_PARAM);
    }
    
    public static String getEncryptionKey(HttpServletRequest request) {
        return request.getParameter(ENCRYPTION_KEY_PARAM);
    }

    public static String getXml(HttpServletRequest request) {
        return request.getParameter(XML_PARAM);
    }
     
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
    
    public static boolean isSaveResponse(String xml) {
        String response = parseResponse(xml);
        return (! response.equals(NONE));
    }
    
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
    
    public static String parseEvent(String xml) {
        return parseTag("lev e=", xml);
    }
    
    public static String parseMseq(String xml) {
        return parseTag("mseq=", xml);
    }

    public static String parseLsid(String xml) {        
        return parseTag("lsid=", xml);
    }

    public static String parseTestRosterId(String xml) {   
        String testRosterId = NONE;
        String lsid = parseLsid(xml);   
        if (! lsid.equals(NONE)) {
            StringTokenizer st = new StringTokenizer(lsid, ":");
            testRosterId = st.nextToken();            
        }
        return testRosterId;
    }

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

    public static AuditVO createAuditVO(String fileName, String lsid, String mseq, String event, String xml) {
        xml = Base64.encode(xml);
        AuditVO audit = new AuditVO(fileName, lsid, mseq, event, xml);
        return audit;
    }
    
    public static AuditVO createAuditVO(String xml, String event) {
        xml = Base64.encode(xml);
        String lsid = parseLsid(xml);
        String fileName = FileUtils.buildFileName(lsid);
        String mseq = parseMseq(xml);
        AuditVO audit = new AuditVO(fileName, lsid, mseq, event, xml);
        return audit;
    }
    
    public static String getWebAppName(String method, String xml) {
        String webApp = URL_WEBAPP_LOGIN;
        if (method.equals(LOGIN_METHOD))
            webApp = URL_WEBAPP_LOGIN;
        else
        if (method.equals(FEEDBACK_METHOD))
            webApp = URL_WEBAPP_FEEDBACK;
        else
        if (method.equals(UPLOAD_AUDIT_FILE_METHOD))
            webApp = URL_WEBAPP_UPLOAD_AUDIT_FILE;
        else        
        if (method.equals(SAVE_METHOD)) {
            if (isSaveResponse(xml))
                webApp = URL_WEBAPP_SAVE_RESPONSE;
            else
                webApp = URL_WEBAPP_SAVE_LIFECYCLE;
        }
        return webApp;
    }
    
    public static void initMemoryCache() {
        MemoryCache memoryCache = MemoryCache.getInstance();
        if (! memoryCache.isLoaded()) {
            ResourceBundle rb = ResourceBundle.getBundle(SERVLET_NAME);
            memoryCache.setSrvSettings(new ServletSettings(rb));
            memoryCache.setStateMap(new HashMap());        
            memoryCache.setLoaded(true);
        }
    }

    public static String getTmsURLString(String method, String xml) throws MalformedURLException {
        MemoryCache memoryCache = MemoryCache.getInstance();
        ServletSettings srvSettings = memoryCache.getSrvSettings();
        String tmsHost = srvSettings.getTmsHost();
        String tmsPort = srvSettings.getTmsPort().trim(); 
        if (tmsPort.length() > 0)
            tmsPort = ":" + tmsPort;
        String tmsWebApp = getWebAppName(method, xml);
        String fullUrl = tmsHost + tmsPort + tmsWebApp;
        return fullUrl;
    }

    public static URL getTmsURL(String method, String xml) throws MalformedURLException {
        String fullUrl = getTmsURLString(method, xml);
        URL tmsURL = new URL(fullUrl);
        return tmsURL;
    }
    
    public static String uploadAuditFile_URLConnection(String xml) {
        String result = NONE;
        try {
            URL tmsURL = getTmsURL(UPLOAD_AUDIT_FILE_METHOD, xml);
            
            URLConnection tmsConnection = tmsURL.openConnection();
            tmsConnection.setDoOutput(true);
            PrintWriter out = new PrintWriter(tmsConnection.getOutputStream());            

            String lsid = parseLsid(xml);
            String fileName = AuditFile.buildFileName(lsid);            
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            char [] buff = new char[(int)file.length()];
            fileReader.read(buff);
            fileReader.close();
            
            String testRosterId = parseTestRosterId(xml);
            String accessCode = parseAccessCode(xml);
            String params = "";
            params += TEST_ROSTER_ID_PARAM + "=" + testRosterId;
            params += "&";
            params += ACCESS_CODE_PARAM + "=" + accessCode;
            params += "&";
            params += AUDIT_FILE_PARAM + "=";
            params += String.valueOf(buff);
            out.println(params);
            out.close();    
            
            String inputLine;
            DataInputStream dis = new DataInputStream(tmsConnection.getInputStream());
            while ((inputLine = dis.readLine()) != null) {
                System.out.println(inputLine);
                result += inputLine;
            }
            dis.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String uploadAuditFile_HttpClient(String xml) throws MalformedURLException {
        String result = OK;
        String testRosterId = parseTestRosterId(xml);
        String accessCode = parseAccessCode(xml);
        String tmsURL = getTmsURLString(UPLOAD_AUDIT_FILE_METHOD, xml);
        tmsURL += "?";
        tmsURL += TEST_ROSTER_ID_PARAM + "=" + testRosterId;
        tmsURL += "&";
        tmsURL += ACCESS_CODE_PARAM + "=" + accessCode;
        
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
                result = UPLOAD_FILE_ERROR;
            System.out.println(filePost.getResponseBodyAsString());
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            filePost.releaseConnection();
        }
        return result;
    }
    
}
