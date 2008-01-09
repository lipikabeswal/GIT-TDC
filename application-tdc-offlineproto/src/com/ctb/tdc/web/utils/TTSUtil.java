package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ResourceBundle;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.ctb.tdc.web.dto.ServletSettings;
import com.ctb.tdc.web.dto.TTSSettings;

public class TTSUtil {
	
	public static class MP3 {
		private long length;
		private InputStream stream;
		private PostMethod request;
		
		/**
		 * @return Returns the request.
		 */
		public PostMethod getRequest() {
			return request;
		}
		/**
		 * @param request The request to set.
		 */
		public void setRequest(PostMethod request) {
			this.request = request;
		}
		/**
		 * @return Returns the length.
		 */
		public long getLength() {
			return length;
		}
		/**
		 * @param length The length to set.
		 */
		public void setLength(long length) {
			this.length = length;
		}
		/**
		 * @return Returns the stream.
		 */
		public InputStream getStream() {
			return stream;
		}
		/**
		 * @param stream The stream to set.
		 */
		public void setStream(InputStream stream) {
			this.stream = stream;
		}
		
		
	}
	
	public static MP3 speak(final String text) {
		// remote synth using TextHelp server
		String thResponse = textHelpRequest(text);
		String mp3URL = thResponse.substring(thResponse.indexOf("mp3=") + 4);
		System.out.println(mp3URL);
		return textHelpMP3(mp3URL);
	}
	
	public static void stop() {

	}
	
	public static String textHelpRequest(String text) {
		String result = "OK";
		int responseCode = HttpStatus.SC_OK;

		// setup parameters
		MemoryCache memoryCache = MemoryCache.getInstance();
		TTSSettings ttsSettings = memoryCache.getTTSSettings();
		if(ttsSettings == null) {
			ResourceBundle ttsConfig = ResourceBundle.getBundle("tts");
			ttsSettings = new TTSSettings(ttsConfig);
			memoryCache.setTTSSettings(ttsSettings);
		}
		String voice = ttsSettings.getVoiceName();
		if(voice == null || "".equals(voice.trim())) {
			voice = "ScanSoft Jill_Full_22kHz";
		}
		String speed = ttsSettings.getSpeedValue();
		if(speed == null || "".equals(speed.trim())) {
			speed = "-2";
		}
		NameValuePair[] params = { new NameValuePair("text", text),
								   new NameValuePair("voiceName", voice),
								   new NameValuePair("speedValue", speed)};
		
		String speechURL = ttsSettings.getUrl();
		if(speechURL == null || "".equals(speechURL.trim())) {
			speechURL = "http://rwonline.texthelp.com/SpeechNAServer/";
		}
		
		PostMethod post = new PostMethod(speechURL);
		
		post.setRequestBody(params);

		// send request to TextHelp
		try {
			HttpClientParams clientParams = new HttpClientParams();
			clientParams.setConnectionManagerTimeout(30000); // timeout in 30 seconds
			HttpClient client = new HttpClient(clientParams);
			String proxyHost = getProxyHost();

			if ((proxyHost != null) && (proxyHost.length() > 0)) {
				// execute with proxy settings
				HostConfiguration hostConfiguration = client.getHostConfiguration();
				int proxyPort = getProxyPort();
				System.out.println("proxyHost = " + proxyHost + "\nproxyPort = " + proxyPort);
				hostConfiguration.setProxy(proxyHost, proxyPort);
				String username = getProxyUserName();
				String password = getProxyPassword();
				UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
				HttpState state = client.getState();
				state.setProxyCredentials(null, proxyHost, credentials);
				responseCode = client.executeMethod(hostConfiguration, post);
			}
			else {
				// execute without proxy
				responseCode = client.executeMethod(post);
			}
			System.out.println("responseCode = " + responseCode);

			if (responseCode == HttpStatus.SC_OK) {
				InputStream isPost = post.getResponseBodyAsStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(isPost));
				String inputLine = null;
				result = "";
				while ((inputLine = in.readLine()) != null) {
					result += inputLine;
				}
				in.close();
			}
			else {
				result = "error: got status " + responseCode + " from TextHelp";
			}
		}
		catch (Exception e) {
			result = "error: exception while making TextHelp request: " + e.getMessage();
		}
		finally {
			post.releaseConnection();
		}
		
		return result;
	}

	public static MP3 textHelpMP3(String url) {
		int responseCode = HttpStatus.SC_OK;
		
		PostMethod post = new PostMethod(url);
		
		MP3 mp3 = null;

		// send request to TextHelp
		try {
			HttpClientParams clientParams = new HttpClientParams();
			clientParams.setConnectionManagerTimeout(30000); // timeout in 30 seconds
			HttpClient client = new HttpClient(clientParams);
			String proxyHost = getProxyHost();

			if ((proxyHost != null) && (proxyHost.length() > 0)) {
				// execute with proxy settings
				HostConfiguration hostConfiguration = client.getHostConfiguration();
				int proxyPort = getProxyPort();
				System.out.println("proxyHost = " + proxyHost + "\nproxyPort = " + proxyPort);
				hostConfiguration.setProxy(proxyHost, proxyPort);
				String username = getProxyUserName();
				String password = getProxyPassword();
				UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
				HttpState state = client.getState();
				state.setProxyCredentials(null, proxyHost, credentials);
				responseCode = client.executeMethod(hostConfiguration, post);
			}
			else {
				// execute without proxy
				responseCode = client.executeMethod(post);
			}
			System.out.println("responseCode = " + responseCode);

			if (responseCode == HttpStatus.SC_OK) {
				mp3 = new MP3();
				mp3.setStream(post.getResponseBodyAsStream());
				mp3.setLength(post.getResponseContentLength());
				mp3.setRequest(post);
			}
			else {
				System.out.println("error: got status " + responseCode + " from TextHelp");
			}
		}
		catch (Exception e) {
			System.out.println("error: exception while making TextHelp request: " + e.getMessage());
		}
		return mp3;
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
