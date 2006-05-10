package com.ctb.tdc.web.utils;

import javax.servlet.http.HttpServletRequest;

public class ServletUtils {

    // methods
    public static final String LOAD_SUBTEST_METHOD = "loadSubtest";
    public static final String LOAD_ITEM_METHOD = "loadItem";
    public static final String LOAD_IMAGE_METHOD = "loadImage";
    public static final String LOGIN_METHOD = "login";
    public static final String SAVE_METHOD = "save";
    public static final String FEEDBACK_METHOD = "feedback";

    // parameters
    public static final String METHOD_PARAM = "method";
    public static final String ITEM_SET_ID_PARAM = "itemSetId";
    public static final String IMAGE_ID_PARAM = "imageId";
    public static final String ENCRYPTION_KEY_PARAM = "encryptionKey";
    public static final String XML_PARAM = "xml";
    
    // helpers
    public static String getMethod(HttpServletRequest request) {
        return request.getParameter(METHOD_PARAM);
    }

    public static String getItemSetId(HttpServletRequest request) {
        return request.getParameter(ITEM_SET_ID_PARAM);
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
}
