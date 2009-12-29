package com.ctb.tdc.tsutility;

import java.util.ResourceBundle;

/**
 * Utility class to wrap the tsutilityResources.properties file for user messages.
 * 
 */
public class AppResourceBundleUtil {

	private static final ResourceBundle rb = ResourceBundle.getBundle("tsutilityResources");
	
	
	/**
	 * Returns the string represented by the key within the resource bundle.
	 * @return String  The value for the specified key
	 */
	public static String getString(String key) {
		return rb.getString(key);
	}
}
