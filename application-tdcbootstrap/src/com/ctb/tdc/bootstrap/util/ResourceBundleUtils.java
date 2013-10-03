package com.ctb.tdc.bootstrap.util;

import java.util.Enumeration;
import java.util.MissingResourceException;
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
    
    private static final ResourceBundle rbBlistProcess = ResourceBundle.getBundle("BlacklistProcessNames");
    
    private static ResourceBundle rbTDCSize = null;
    
    private static ResourceBundle rbinstallerType =null;
    
     
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
    
    public static String getChecksumString(String key) {
        try
        {
        	rbTDCSize = ResourceBundle.getBundle("checksum");
        	return rbTDCSize.getString(key);
        }
        catch (Exception e) {
			return null;
		}
    }
    
    public static String getInstallerTypeString(String key) {
        try
        {
        	rbinstallerType = ResourceBundle.getBundle("installer");
        	return rbinstallerType.getString(key);
        }
        catch (Exception e) {
			return "installer";
		}
    }
    
    
    /**
     * Returns the string represented by the key within the resource bundle.
     * @return String  The value for the specified key
     */
    public static String getBlistProcessString(String key) {
        return ResourceBundleUtils.rbBlistProcess.getString(key);
    }
    
    public static Enumeration<String> getAllBlistProcessKeys() {
    	return ResourceBundleUtils.rbBlistProcess.getKeys();
    }
 
 }
