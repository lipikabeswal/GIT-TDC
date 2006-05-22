package com.ctb.tdc.web.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.ctb.tdc.web.dto.AuditVO;
import com.ctb.tdc.web.dto.ServletSettings;

/**
 * @author Tai_Truong
 */
public class ServletUtils {
    public static final String SERVLET_NAME = "tdc";

    public static final String URL_HOST = "http://152.159.127.61";
    public static final String URL_PORT = "8080";
    public static final String URL_WEBAPP_LOGIN = "/TestDeliveryWeb/begin.do";
    public static final String URL_WEBAPP_SAVE_RESPONSE = "/TestDeliveryWeb/response.do";
    public static final String URL_WEBAPP_SAVE_LIFECYCLE = "/TestDeliveryWeb/lifecycle.do";
    public static final String URL_WEBAPP_FEEDBACK = "/TestDeliveryWeb/feedback.do";
    public static final String URL_WEBAPP_UPLOAD_AUDIT_FILE = "/TestDeliveryWeb/audit.do";
    
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
    public static final String OK = "200 OK";

    // date time
    public final static String DATETIME_FORMAT="MM/dd/yy hh:mm a";

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
            StringTokenizer st = new StringTokenizer(":");
            testRosterId = st.nextToken();            
        }
        return testRosterId;
    }

    public static String parseAccessCode(String xml) {   
        String accessCode = NONE;
        String lsid = parseLsid(xml);   
        if (! lsid.equals(NONE)) {
            StringTokenizer st = new StringTokenizer(":");
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
        AuditVO audit = new AuditVO(fileName, lsid, mseq, event, xml);
        return audit;
    }
    
    public static AuditVO createAuditVO(String xml, String event) {
        String lsid = parseLsid(xml);
        String fileName = FileUtils.buildFileName(lsid);
        String mseq = parseMseq(xml);
        AuditVO audit = new AuditVO(fileName, lsid, mseq, event, xml);
        return audit;
    }
    
    public static String getWebAppName(String method, String xml) {
        String webApp = URL_WEBAPP_LOGIN;
        /*
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
        */
        return webApp;
    }
    
    public static void initMemoryCache() {
        MemoryCache memoryCache = MemoryCache.getInstance();
        ResourceBundle rb = ResourceBundle.getBundle(SERVLET_NAME);
        ServletSettings srvSettings = new ServletSettings(rb);
        memoryCache.setSrvSettings(srvSettings);

        HashMap stateHashMap = new HashMap();            
        memoryCache.setStateHashMap(stateHashMap);
    }

    public static URL getTmsURL(String method, String xml) throws MalformedURLException {
        MemoryCache memoryCache = MemoryCache.getInstance();
        ServletSettings srvSettings = memoryCache.getSrvSettings();
        String tmsHost = srvSettings.getTmsHost();
        String tmsWebApp = getWebAppName(method, xml);
        URL tmsURL = new URL(tmsHost + tmsWebApp);
        return tmsURL;
    }
    
}
