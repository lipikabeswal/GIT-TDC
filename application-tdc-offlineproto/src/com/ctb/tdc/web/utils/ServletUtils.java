package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

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

    // returned values
    public static final String OK = "200 OK";
    public static final String NO_CONTENT = "204 No Content";
    
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
    
}
