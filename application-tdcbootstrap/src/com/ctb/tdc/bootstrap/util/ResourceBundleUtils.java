package com.ctb.tdc.bootstrap.util;

import java.util.ResourceBundle;

/**
 * Utility class to wrap the bootstrapResources.properties file for UI messages.
 * 
 * @author Giuseppe Gennaro
 */
public class ResourceBundleUtils {

	private static final ResourceBundle rb = ResourceBundle.getBundle("bootstrapResources");

    private static final ResourceBundle rbProxy = ResourceBundle.getBundle("proxy");
    
    private static final ResourceBundle rbVersion = ResourceBundle.getBundle("version");
   
	/**
	 * Returns the string represented by the key within the resource bundle.
	 * @return String  The value for the specified key
	 */
	public static String getString(String key) {
		return ResourceBundleUtils.rb.getString(key);
	}

    /**
     * Returns the string represented by the key within the resource bundle.
     * @return String  The value for the specified key
     */
    public static String getProxyString(String key) {
        return ResourceBundleUtils.rbProxy.getString(key);
    }
 
    /**
     * Returns the string represented by the key within the resource bundle.
     * @return String  The value for the specified key
     */
    public static String getVersionString(String key) {
        return ResourceBundleUtils.rbVersion.getString(key);
    }
 
 }
