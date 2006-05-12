package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

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

    // returned values
    public static final String OK = "200 OK";
    public static final String NO_CONTENT = "204 No Content";

    // date time
    private final static String DATETIME_FORMAT="MM/dd/yy hh:mm a";
    
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
    
    public static boolean getFile(String fileName, PrintWriter out) {
        boolean result = true;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while((line = reader.readLine()) != null){
                out.println(line);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public static String parseEvent(String xml) {
        String event = null;
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

    public static String parseItemResponse(String xml) {
        String itemResponse = null;
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
        String mseq = "mseq"; // for now
        return mseq;
    }

    public static String parseType(String xml) {
        String mseq = "type"; // for now
        return mseq;
    }

    public static String parseLsid(String xml) {
        String mseq = "lsid"; // for now
        return mseq;
    }
    
}
