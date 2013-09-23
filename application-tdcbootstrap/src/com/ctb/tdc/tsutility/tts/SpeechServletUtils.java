package com.ctb.tdc.tsutility.tts;

import java.net.MalformedURLException;
import java.util.ResourceBundle;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

import com.ctb.tdc.tsutility.tts.ServletSettings;
import com.ctb.tdc.tsutility.ui.MainWindow;

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

	static String proxyHost = null;
	static String proxyPort = null;
	static String proxyUsername = null;
	static String proxyPassword = null;

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
		return proxyHost;
	}

	/**
	 * get predefined proxy port
	 *
	 */
	public static int getProxyPort() {
		int value = 0;
		try {
			value = Integer.valueOf(proxyPort).intValue();
		} catch (NumberFormatException e) {
			
		}
		return value;
	}

	/**
	 * get predefined proxy user name
	 *
	 */
	public static String getProxyUserName() throws MalformedURLException {
		return proxyUsername;
	}

	/**
	 * get predefined proxy password
	 *
	 */
	public static String getProxyPassword() throws MalformedURLException {
		return proxyPassword;
	}
	
	public static void readProxyInfo() {
		ResourceBundle proxyRB = ResourceBundle.getBundle("proxy");
		proxyHost = proxyRB.getString("proxy.host");
		proxyPort = proxyRB.getString("proxy.port");
		proxyUsername = proxyRB.getString("proxy.username");
		proxyPassword = proxyRB.getString("proxy.password");
		
		if(MainWindow.getProxyHost() != null){
			proxyHost = MainWindow.getProxyHost();
		}
		if(MainWindow.getProxyPort()!= null){
			proxyPort = MainWindow.getProxyPort();
		}
		if(MainWindow.getProxyUsername() != null){
			proxyUsername = MainWindow.getProxyUsername();
		}
		if(MainWindow.getProxyPassword() != null){
			proxyPassword = MainWindow.getProxyPassword();
		}

		
System.out.println(proxyHost + "  " + proxyPort + "  " + proxyUsername + "  " + proxyPassword);
	}
	
}