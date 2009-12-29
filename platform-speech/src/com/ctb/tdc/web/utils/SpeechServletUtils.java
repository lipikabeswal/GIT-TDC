package com.ctb.tdc.web.utils;

import java.net.MalformedURLException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

import com.ctb.tdc.web.dto.ServletSettings;

/**
 * @author Tai_Truong
 *
 * This class provides helper methods for local servlets
 * PersistenceServlet.java
 * LoadContentServlet.java
 * DownloadContentServlet.java
 * ContentServlet.java
 *
 */

public class SpeechServletUtils {

	public static void setTTSCredentials(HttpClient client, String ttsHost, int ttsPort, String username, String password) {
        try{
        	AuthScope ttsScope;
        	ttsScope = new AuthScope(ttsHost, ttsPort, AuthScope.ANY_REALM);
        	client.getParams().setAuthenticationPreemptive(true);
        	client.getState().setCredentials(ttsScope, new UsernamePasswordCredentials(username,password));	
        } catch(Exception e) {
        	e.printStackTrace();
    	}
	}
	
	public static void setProxyCredentials(HttpClient client, String proxyHost, int proxyPort, String username, String password) {
        try {
			boolean proxyHostDefined = proxyHost != null && proxyHost.length() > 0;
	        boolean proxyPortDefined = proxyPort > 0;
	        boolean proxyUsernameDefined = username != null && username.length() > 0;
	
		    if( proxyHostDefined && proxyPortDefined ) 
		    	client.getHostConfiguration().setProxy(proxyHost, proxyPort);
		    else 
		    if( proxyHostDefined )  
		    	client.getHostConfiguration().setProxyHost(new ProxyHost(proxyHost) );
	        
	        if( proxyHostDefined && proxyUsernameDefined ) {
	            AuthScope proxyScope;
	            
	            if( proxyPortDefined )
	                proxyScope = new AuthScope(proxyHost, proxyPort, AuthScope.ANY_REALM);
	            else
	                proxyScope = new AuthScope(proxyHost, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
	
	    		UsernamePasswordCredentials upc = new UsernamePasswordCredentials(username, password);            
	            //client.getParams().setAuthenticationPreemptive(true);
	            client.getState().setProxyCredentials(proxyScope, upc);
	        }	
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	/**
	 * get predefined proxy host
	 *
	 */
	public static String getProxyHost() throws MalformedURLException {
		MemoryCache memoryCache = MemoryCache.getInstance();
		ServletSettings srvSettings = memoryCache.getSrvSettings();
		return srvSettings.getProxyHost();
	}

	/**
	 * get predefined proxy port
	 *
	 */
	public static int getProxyPort() {
		MemoryCache memoryCache = MemoryCache.getInstance();
		ServletSettings srvSettings = memoryCache.getSrvSettings();
		return srvSettings.getProxyPort();
	}

	/**
	 * get predefined proxy user name
	 *
	 */
	public static String getProxyUserName() throws MalformedURLException {
		MemoryCache memoryCache = MemoryCache.getInstance();
		ServletSettings srvSettings = memoryCache.getSrvSettings();
		String userName = srvSettings.getProxyUserName().trim();
		if (userName.length() == 0)
			userName = null;
		return userName;
	}

	/**
	 * get predefined proxy password
	 *
	 */
	public static String getProxyPassword() throws MalformedURLException {
		MemoryCache memoryCache = MemoryCache.getInstance();
		ServletSettings srvSettings = memoryCache.getSrvSettings();
		String password = srvSettings.getProxyPassword().trim();
		if (password.length() == 0)
			password = null;
		return password;
	}
	
}