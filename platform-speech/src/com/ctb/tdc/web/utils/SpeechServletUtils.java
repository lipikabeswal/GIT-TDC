package com.ctb.tdc.web.utils;

import java.net.MalformedURLException;

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
	
	/**
	 * get predefined proxy ntlm domain
	 *
	 */
	public static String getProxyDomain() throws MalformedURLException {
		MemoryCache memoryCache = MemoryCache.getInstance();
		ServletSettings srvSettings = memoryCache.getSrvSettings();
		String domain = srvSettings.getProxyDomain().trim();
		if (domain.length() == 0)
			domain = null;
		return domain;
	}
	
}