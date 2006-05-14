package com.ctb.tdc.web.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.ctb.tdc.web.dto.AuditVO;

public class ServletUtils {

    // methods
    public static final String DOWNLOAD_CONTENT_METHOD = "downloadContent";
    public static final String INITITAL_DOWNLOAD_CONTENT_METHOD = "initialDownloadContent";
    public static final String LOAD_SUBTEST_METHOD = "loadSubtest";
    public static final String LOAD_ITEM_METHOD = "loadItem";
    public static final String LOAD_IMAGE_METHOD = "loadImage";
    public static final String LOGIN_METHOD = "login";
    public static final String SAVE_METHOD = "save";
    public static final String FEEDBACK_METHOD = "feedback";

    // parameters
    public static final String METHOD_PARAM = "method";
    public static final String ITEM_SET_ID_PARAM = "itemSetId";
    public static final String ITEM_ID_PARAM = "itemId";
    public static final String IMAGE_ID_PARAM = "imageId";
    public static final String ENCRYPTION_KEY_PARAM = "encryptionKey";
    public static final String XML_PARAM = "xml";

    // events
    public static final String LOGIN_EVENT = "lms_login";
    public static final String RESPONSE_EVENT = "lms_response";
    public static final String START_EVENT = "lms_start";
    public static final String FINISH_EVENT = "lms_finish";
    public static final String PAUSE_EVENT = "lms_pause";
    public static final String HEARTBEAT_EVENT = "lms_heartbeat";
    public static final String FEEDBACK_EVENT = "lms_feedback";
    public static final String UNKNOWN_EVENT = "lms_unknown";

    public static final String TMS_REQUEST_EVENT = "tms_request";
    public static final String TMS_ACK_EVENT = "tms_ack";
    
    // returned values
    public static final String OK = "200 OK";

    // date time
    public final static String DATETIME_FORMAT="MM/dd/yy hh:mm a";

    // misc
    public static final String UNKNOWN = "-";
    
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
    
    public static String formatDateToDateString(Date date){
        String result = null;
        if (date == null)
            return result;

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(DATETIME_FORMAT);
        try{
            result = sdf.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }    
    
    public static String parseEvent(String xml) {
        String event = UNKNOWN_EVENT;
        if (xml != null) {
            if (xml.indexOf(LOGIN_EVENT) > 0) event = LOGIN_EVENT;
            if (xml.indexOf(RESPONSE_EVENT) > 0) event = RESPONSE_EVENT;
            if (xml.indexOf(START_EVENT) > 0) event = START_EVENT;
            if (xml.indexOf(FINISH_EVENT) > 0) event = FINISH_EVENT;
            if (xml.indexOf(PAUSE_EVENT) > 0) event = PAUSE_EVENT;
            if (xml.indexOf(HEARTBEAT_EVENT) > 0) event = HEARTBEAT_EVENT;
            if (xml.indexOf(FEEDBACK_EVENT) > 0) event = FEEDBACK_EVENT;
        }
        return event;
    }

    public static String parseResponse(String xml) {
        String itemResponse = UNKNOWN;
        if (xml != null) {
            int startIndex = xml.indexOf("<v>");
            int endIndex = xml.indexOf("</v>");
            if ((startIndex > 0) && (endIndex > 0) && (endIndex < xml.length())) {
                itemResponse = xml.substring(endIndex-1, endIndex);
            }
        }
        return itemResponse;
    }

    public static String parseMseq(String xml) {
        String mseq = UNKNOWN;
        if (xml != null) {
            int index = xml.indexOf("mseq=");
            if (index > 0) {
                int startIndex = index + 6;
                int endIndex = startIndex;
                while (true) {
                    int ch = xml.charAt(endIndex);                    
                    if ((ch == 34) || (ch == 39) || (endIndex > xml.length()-1))
                        break;
                    endIndex++;
                }
                mseq = xml.substring(startIndex, endIndex);
            }
        }
        return mseq;
    }

    public static String parseLsid(String xml) {
        String lsid = UNKNOWN;
        if (xml != null) {
            int index = xml.indexOf("lsid=");
            if (index > 0) {
                int startIndex = index + 6;
                int endIndex = startIndex;
                while (true) {
                    int ch = xml.charAt(endIndex);                    
                    if ((ch == 34) || (ch == 39) || (endIndex > xml.length()-1))
                        break;
                    endIndex++;
                }
                lsid = xml.substring(startIndex, endIndex);
            }
        }
        return lsid;
    }
     
    public static AuditVO buildVOFromXML(String xml, String type) {
        String mseq = parseMseq(xml);
        String lsid = parseLsid(xml);
        String response = parseResponse(xml);
        String date = formatDateToDateString(new Date());
        AuditVO audit = new AuditVO(mseq, type, date, lsid, response);
        return audit;
    }

    public static AuditVO buildVOFromString(String src) {
        StringTokenizer st = new StringTokenizer(src, "\t");
        String mseq = st.nextToken();
        String type = st.nextToken();
        String date = st.nextToken();
        String lsid = st.nextToken();
        String response = st.nextToken();
        AuditVO audit = new AuditVO(mseq.trim(), type.trim(), date.trim(), lsid.trim(), response.trim());
        return audit;
    }

    public static AuditVO buildVOFromType(String type) {
        String date = formatDateToDateString(new Date());
        AuditVO audit = new AuditVO(UNKNOWN, type, date, UNKNOWN+"\t\t", UNKNOWN);
        return audit;
    }
    
}
