package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.ctb.tdc.web.dto.TTSSettings;

public class TTSUtil {
	
	public static HashMap mp3CacheMap = new HashMap();
	
	public static DefaultHttpClient client;
	
	static {
		try {
			// setup SSL
			TrustStrategy trustStrategy = new EasyTrustStrategy(); 
			X509HostnameVerifier hostnameVerifier = new AllowAllHostnameVerifier(); 
			SSLSocketFactory sslSf = new SSLSocketFactory(trustStrategy, hostnameVerifier); 
			PlainSocketFactory pSf = new PlainSocketFactory(); 
			Scheme https = new Scheme("https", 443, sslSf); 
			Scheme http = new Scheme("http", 80, pSf); 
			SchemeRegistry schemeRegistry = new SchemeRegistry(); 
			schemeRegistry.register(https);
			schemeRegistry.register(http);
			
			ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(schemeRegistry); 
			mgr.setMaxTotal(1);
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
			HttpConnectionParams.setSoTimeout(httpParams, 10000);
			client = new DefaultHttpClient(mgr, httpParams);
			
			// setup proxy
			ServletUtils.validateProxySettings();
			String proxyHost = ServletUtils.getProxyHost();
	
			if ((proxyHost != null) && (proxyHost.length() > 0)) {
				// apply proxy settings
	            int proxyPort    = ServletUtils.getProxyPort();;
	            String username  = ServletUtils.getProxyUserName();
	            String password  = ServletUtils.getProxyPassword();   
	            String domain = ServletUtils.getProxyDomain();
	        	TTSUtil.setProxyCredentials(client, proxyHost, proxyPort, username, password, domain);
	        	
	        	System.out.println(proxyHost + ":" + proxyPort);
			}
			
			// setup BASIC auth
			//TTSSettings ttsSettings = getTTSSettings();
			//TTSUtil.setTTSCredentials(client, ttsSettings.getHost(), ttsSettings.getUserName(), ttsSettings.getPassword());
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static void setProxyCredentials(DefaultHttpClient client, String proxyHost, int proxyPort, String username, String password, String domain) {
        boolean proxyHostDefined = proxyHost != null && proxyHost.length() > 0;
        boolean proxyPortDefined = proxyPort > 0;
        boolean proxyUsernameDefined = username != null && username.length() > 0;
        boolean proxyDomainDefined = domain != null && domain.trim().length() > 0;

        HttpHost proxy = null;
        
	    if( proxyHostDefined && proxyPortDefined ) {
	    	proxy = new HttpHost(proxyHost, proxyPort);
	    	client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);  
	    }else if( proxyHostDefined )  {
	    	proxy = new HttpHost(proxyHost);
	    	client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
	    }
        if( proxyHostDefined && proxyUsernameDefined ) {
            AuthScope proxyScope;
            
            if( proxyPortDefined )
                proxyScope = new AuthScope(proxyHost, proxyPort, AuthScope.ANY_REALM);
            else
                proxyScope = new AuthScope(proxyHost, AuthScope.ANY_PORT, AuthScope.ANY_REALM);

    		UsernamePasswordCredentials upc = new UsernamePasswordCredentials(username, password);            
            NTCredentials ntc = new NTCredentials(domain + "/" + username + ":" + password);
    		
    		if(!proxyDomainDefined) {
	    		client.getCredentialsProvider().setCredentials(
	    		        proxyScope, 
	    		        upc);
    		} else {
    			client.getCredentialsProvider().setCredentials(
	    		        proxyScope,
	    		        ntc);
    		}
        }		
	}
	
	public static void setTTSCredentials(DefaultHttpClient client, String proxyHost, String username, String password) {
        AuthScope proxyScope = new AuthScope(proxyHost, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
        UsernamePasswordCredentials upc = new UsernamePasswordCredentials(username, password);            
        client.getCredentialsProvider().setCredentials(proxyScope, upc);	
	}
	
	public static class MP3 {
		private long length;
		private InputStream stream;
		private HttpGet request;
		
		/**
		 * @return Returns the request.
		 */
		public HttpGet getRequest() {
			return request;
		}
		/**
		 * @param request The request to set.
		 */
		public void setRequest(HttpGet request) {
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
	
	public static MP3 speak(final String text, final String speedValue) throws Exception {
		// remote synth using TextHelp server
		System.out.println("TTS: sending speech request");
		String thResponse = textHelpRequest(text,speedValue);
		if(thResponse == null) {
			System.out.println("TTS: No response from TextHelp - TTS server is specified incorrectly or is down");
			throw new Exception("No response from TextHelp.");
		} else if(thResponse.indexOf("error") >= 0) {
			System.out.println("TTS: response contains error");
			throw new Exception("Error response from TextHelp: " + thResponse);
		} else {
			System.out.println("TTS: Sending MP3 request");
			String mp3URL = thResponse.substring(thResponse.indexOf("mp3=") + 4);
			System.out.println("TTS: MP3 URL: " + mp3URL);
			MP3 mp3 = textHelpMP3(mp3URL);
			if(mp3 == null) {
				System.out.println("TTS: no response from TextHelp - TTS server is specified incorrectly or is down");
				throw new Exception("No response from TextHelp.");
			} else {
				return mp3;
			}
		}
		// remote synth using ReadSpeaker webservice
		/*String speechURL = "http://app.readspeaker.com/cgi-bin/rsent";
		MP3 result = null;
		try {
			client.getConnectionManager().closeIdleConnections(1, TimeUnit.MICROSECONDS);
			HttpPost post = new HttpPost(speechURL);
			post.addHeader("Referer", "http://184.73.162.113");
	
			// setup parameters
			List nameValuePairs = new ArrayList(8);
			nameValuePairs.add(new BasicNameValuePair("customerid", "5857"));
			nameValuePairs.add(new BasicNameValuePair("text", text));
			nameValuePairs.add(new BasicNameValuePair("speed", String.valueOf(((Integer.parseInt(speedValue) + 3)*100))));
			nameValuePairs.add(new BasicNameValuePair("pitch", "100"));
			nameValuePairs.add(new BasicNameValuePair("voice", "Kate"));
			nameValuePairs.add(new BasicNameValuePair("lang", "en_us"));
			nameValuePairs.add(new BasicNameValuePair("mp3bitrate", "32"));
			nameValuePairs.add(new BasicNameValuePair("output", "audio"));
			
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = client.execute(post);
			int responseCode = response.getStatusLine().getStatusCode();
			int responseLen = 0;
			if(response.getHeaders("content-length") != null) {
				responseLen = Integer.valueOf(response.getHeaders("content-length")[0].getValue()).intValue();
			}
				
			System.out.println("*****  Text Status: " + responseCode + " Length: " + responseLen);
			
			if (responseCode == HttpStatus.SC_OK && responseLen > 0) {
				System.out.println("***** Direct audio."); 
				result = new MP3();
				result.setStream(response.getEntity().getContent());
				result.setLength(responseLen);
			} else if (responseCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				
				System.out.print("***** 302 for audio. Location: ");
				String location = "";	
				Header [] headers = response.getAllHeaders();
				for(int i=0;i<headers.length;i++) {
					String name = headers[i].getName();
					String value = headers[i].getValue();
					//System.out.println(name + ": " + value);
					if("Location".equals(name)) location = value;
				}
				int read = response.getEntity().getContent().read();
				response.getEntity().getContent().close();
				
				HttpGet get = new HttpGet(location);
				//get.addHeader("Referer", "http://184.73.162.113");
				System.out.print(location + "\n");
				System.out.println("*****  Doing GET");
				HttpResponse mp3response = client.execute(get);
				System.out.println("*****  GET complete");
				responseCode = mp3response.getStatusLine().getStatusCode();
				if(mp3response.getHeaders("content-length") != null) {
					responseLen = Integer.valueOf(mp3response.getHeaders("content-length")[0].getValue()).intValue();
				}
				
				System.out.println("*****  Audio Status: " + responseCode + " Length: " + responseLen);
				
				if (responseCode == HttpStatus.SC_OK && responseLen > 0) {
					result = new MP3();
					result.setStream(mp3response.getEntity().getContent());
					result.setLength(responseLen);
					result.setRequest(get);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;*/
	}
	
	private static String stripchars(String in) {
		return in.replaceAll(Pattern.quote("*"), "").replaceAll(Pattern.quote("."), "").replaceAll(Pattern.quote("/"), "");
	}
	
	private static String createFilename(String text) throws Exception{
		MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );
        byte baKey[] = text.getBytes();
        messageDigest.update( baKey );
        byte baHash[] = messageDigest.digest();
        
		int max = 32;
		if(text.length() < 32) max = text.length();
		return URLEncoder.encode(stripchars(new String(baHash)));
	}
	
	public static String checkCache(String text) throws Exception {
		String value = (String) mp3CacheMap.get(text);
		if(value == null) {
			String filename = "cache/" + createFilename(text) + ".enc";
			if(new File(filename).exists()) {
				value = filename.replaceAll(".enc", ".mp3");
				mp3CacheMap.put(text, value);
			}
		}
		return value;
	}
	
	public static void cacheFile(String text, MP3 mp3) {
		try {
			String filename = createFilename(text) + ".mp3";
			
			System.out.println("TTS: cache miss, new cache file: " + filename);
			
			InputStream in = mp3.getStream();
			FileOutputStream out = new FileOutputStream(filename);
	    	byte [] buffer = new byte[1024];
	    	int read = 0;
	    	while((read = in.read(buffer)) > 0) {
	    		//System.out.println("TTS: streaming " + read + " bytes to cache");
	    		out.write(buffer, 0, read);
	    	}
	    	out.close();
	    	in.close();
	    	/*if(mp3.getRequest() != null) {
	    		mp3.getRequest().releaseConnection();
	    	}*/
			
			mp3CacheMap.put(text, filename);
			
			// encrypt cache
			encryptFile(filename, filename.replaceAll(".mp3", ".enc"));
	        System.out.println("TTS: write to MP3 cache successful");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void encryptFile(String infile, String outfile) throws Exception{
		FileInputStream input = new FileInputStream( infile );
        int size = input.available();
        byte[] src = new byte[ size ];
        input.read( src );
        input.close();
        
        byte[] encrypted = encrypt(src).getBytes();
        //byte[] decrypted = decrypt(encrypted);
        FileOutputStream enc = new FileOutputStream(outfile);
        enc.write( encrypted );
        enc.close();
	}
	
	public static void decryptFile (String infile, String outfile) throws Exception{
		byte[] decrypted = decrypt(new BASE64Decoder().decodeBuffer(new FileInputStream( infile )));
        FileOutputStream output = new FileOutputStream(outfile);
        output.write( decrypted );
        output.close();
	}
	
	public static void stop() {

	}
	
	public static void main(String argv[]) {
		try {
			//String thResponse = textHelpRequest("testing","-2");
			//String mp3URL = thResponse.substring(thResponse.indexOf("mp3=") + 4);
			//String result = textHelpMP3(mp3URL).toString();
			speak("testing testing 1 2 3", "-2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static TTSSettings getTTSSettings() {
		//	 setup parameters
		MemoryCache memoryCache = MemoryCache.getInstance();
		TTSSettings ttsSettings = memoryCache.getTTSSettings();
		if(ttsSettings == null) {
			ResourceBundle ttsConfig = ResourceBundle.getBundle("tts");
			ttsSettings = new TTSSettings(ttsConfig);
			memoryCache.setTTSSettings(ttsSettings);
		}
		return ttsSettings;
	}
	
	private static String encrypt( byte[] in )
    {
        byte[] result = null;
        try
        {
           	String encryptKey = obfuscate();
            MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );
            byte baKey[] = encryptKey.getBytes();
            messageDigest.update( baKey );
            byte baHash[] = messageDigest.digest();
            KeyParameter hashKeyParameter = new KeyParameter( baHash );
            RC4Engine rc4Engine = new RC4Engine();
            rc4Engine.init( true, hashKeyParameter );
            result = new byte[ in.length ];
            rc4Engine.processBytes( in, 0, in.length, result, 0);
            rc4Engine.reset();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return new BASE64Encoder().encode(result);
    }
	
	private static byte[] RC4Decrypt( byte[] baInputByteArray, int length ) throws IOException
    {
    	String encryptKey = obfuscate();
        MD5Digest digest = new MD5Digest();
        byte baKey[] = new byte[ encryptKey.length()];
        baKey = encryptKey.getBytes();
        digest.update( baKey, 0, baKey.length );
        byte baHash[] = new byte[digest.getDigestSize()];
        digest.doFinal( baHash, 0 );
        KeyParameter hashKeyParameter = new KeyParameter( baHash );
        RC4Engine rc4Engine = new RC4Engine();
        rc4Engine.init( false, hashKeyParameter );
        byte[] result = new byte[ length ];
        rc4Engine.processBytes( baInputByteArray, 0, length, result, 0);
        rc4Engine.reset();
        return result;
    }
    
    private static byte[] decrypt( String encoded ) {
        byte[] result = null;
        try
        {
        	byte[] encrypted = new BASE64Decoder().decodeBuffer(encoded);
            result = RC4Decrypt( encrypted, encrypted.length );
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        return result;
    }
    
    private static byte[] decrypt( byte[] encrypted ) {
        byte[] result = null;
        try
        {
        	result = RC4Decrypt( encrypted, encrypted.length );
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        return result;
    }
    
    private static String obfuscate(){
    	StringBuffer buff = new StringBuffer();
        String src = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        buff.append(src.substring(0, 1));
        buff.append(src.substring(56, 57));
        buff.append(src.substring(4, 5));
        buff.append(src.substring(16, 17));
        buff.append(src.substring(32, 33));
        buff.append(src.substring(49, 50));
        buff.append(src.substring(8, 9));
        buff.append(src.substring(22, 23));
        buff.append(src.substring(54, 55));
        buff.append(src.substring(2, 3));
        buff.append(src.substring(14, 15));
        buff.append(src.substring(34, 35));
        buff.append(src.substring(58, 59));
        buff.append(src.substring(7, 8));
        buff.append(src.substring(35, 36));
        buff.append(src.substring(39, 40));
    	return buff.toString();
    }
    
    private static String execSpeechRequest (String text, String speedValue){
		String result = null;
	
		int responseCode = HttpStatus.SC_OK;
		TTSSettings ttsSettings = getTTSSettings();
		
		String method = ttsSettings.getMethod();
		
		if(TTSSettings.READSPEAKERMETHOD.equals(method)) {
			return execReadspeakerSpeechRequest(text, speedValue);
		} else if (TTSSettings.TEXTHELPMETHOD.equals(method)) {
			return execTexthelpSpeechRequest(text, speedValue);
		} else {
			return null;
		}
	}
	
    private static String execTexthelpSpeechRequest (String text, String speedValue){
		String result = null;
	
		int responseCode = HttpStatus.SC_OK;
		TTSSettings ttsSettings = getTTSSettings();
		String voice = ttsSettings.getVoiceName();

		if(speedValue  == null || "".equals(speedValue.trim())) {
			speedValue = ttsSettings.getSpeedValue();
			if(speedValue == null || "".equals(speedValue.trim())) {
				speedValue = "-2";
			}
		}

		String speechURL = ttsSettings.getUrl();
		if(speechURL == null || "".equals(speechURL.trim())) {
			speechURL = "http://ctb.speechstream.net/SpeechServices/index.html";
		}
		try {
			HttpPost post = new HttpPost(speechURL);
			//post.setFollowRedirects(false);
			// setup parameters
			List nameValuePairs = new ArrayList(4);
			nameValuePairs.add(new BasicNameValuePair("userName", "ctb"));
			nameValuePairs.add(new BasicNameValuePair("text", text));
			nameValuePairs.add(new BasicNameValuePair("voiceName", voice));
			nameValuePairs.add(new BasicNameValuePair("speedValue", speedValue));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			System.out.println(speechURL);
			System.out.println(voice);
			System.out.println(speedValue);
			System.out.println(text);
			
			// send request to TextHelp
			HttpResponse response = client.execute(post);
			responseCode = response.getStatusLine().getStatusCode();
			int responseLen = 0;
			if(response.getHeaders("content-length") != null) {
				responseLen = Integer.valueOf(response.getHeaders("content-length")[0].getValue()).intValue();
			}
			
			System.out.println("Text Status: " + responseCode + " Length: " + responseLen);
			
			if (responseCode == HttpStatus.SC_OK && responseLen > 0) {
				result = "";
				BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()),131072);
				String inputLine = null;
				
				while ((inputLine = in.readLine()) != null) {
					result += inputLine;
				}
				if(result.indexOf("mp3=") >= 0) {
					in.close();
				} else {
					result = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
    
    private static String execReadspeakerSpeechRequest (String text, String speedValue){
		String result = null;
	
		int responseCode = HttpStatus.SC_OK;
		TTSSettings ttsSettings = getTTSSettings();
		String voice = ttsSettings.getVoiceName();

		if(speedValue  == null || "".equals(speedValue.trim())) {
			speedValue = ttsSettings.getSpeedValue();
			if(speedValue == null || "".equals(speedValue.trim())) {
				speedValue = "-2";
			}
		}
		if("-1".equals(speedValue)) speedValue = "175";
		else if ("-2".equals(speedValue)) speedValue = "100";
		else if ("-3".equals(speedValue)) speedValue = "75";
		else speedValue = "100";
		//speedValue = String.valueOf((Integer.parseInt(speedValue) * -50));

		String speechURL = ttsSettings.getUrl();
		if(speechURL == null || "".equals(speechURL.trim())) {
			speechURL = "http://app.readspeaker.com/cgi-bin/rsent";
		}
		try {
			HttpPost post = new HttpPost(speechURL);
			//post.setFollowRedirects(false);
			// setup parameters
			List nameValuePairs = new ArrayList(4);
			nameValuePairs.add(new BasicNameValuePair("customerid", "5857"));
			nameValuePairs.add(new BasicNameValuePair("lang", "en_us"));
			nameValuePairs.add(new BasicNameValuePair("output", "audiolink"));
			nameValuePairs.add(new BasicNameValuePair("text", text));
			nameValuePairs.add(new BasicNameValuePair("voice", voice));
			nameValuePairs.add(new BasicNameValuePair("speed", speedValue));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			System.out.println(speechURL);
			System.out.println(voice);
			System.out.println(speedValue);
			System.out.println(text);
			
			post.addHeader("x-readspeaker-api-key", "ac678cf868483de0b733cf4d81b6367b");
			
			// send request to TextHelp
			HttpResponse response = client.execute(post);
			responseCode = response.getStatusLine().getStatusCode();
			
			System.out.println(responseCode);
			
			//int responseLen = 0;
			//if(response.getHeaders("content-length") != null) {
			//	responseLen = Integer.valueOf(response.getHeaders("content-length")[0].getValue()).intValue();
			//}
			
			System.out.println("Text Status: " + responseCode); // + " Length: " + responseLen);
			
			//if (responseCode == HttpStatus.SC_OK && responseLen > 0) {
				result = "";
				BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()),131072);
				String inputLine = null;
				
				while ((inputLine = in.readLine()) != null) {
					result += inputLine;
				}
				System.out.println(result);
				if(result.indexOf("mp3") >= 0) {
					result = "mp3=" + result;
					in.close();
				} else {
					result = null;
				}
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
    
	public static String textHelpRequest(String text, String speedValue) {
		text = text.replaceAll("<", "less than");
		String result = execSpeechRequest(text, speedValue);
		return result;
	}
	
	public static MP3 execMP3Request (String url) {
		MP3 result = null;
		int responseCode = HttpStatus.SC_OK;
		try {
			TTSSettings ttsSettings = getTTSSettings();
			
			HttpGet get = new HttpGet(url);
			
			HttpResponse response = client.execute(get);
			responseCode = response.getStatusLine().getStatusCode();
			int responseLen = 0;
			if(response.getHeaders("content-length") != null) {
				responseLen = Integer.valueOf(response.getHeaders("content-length")[0].getValue()).intValue();
			}
						
			System.out.println("Audio Status: " + responseCode + " Length: " + responseLen);
						
			if (responseCode == HttpStatus.SC_OK && responseLen > 0) {
				result = new MP3();
				result.setStream(response.getEntity().getContent());
				result.setLength(responseLen);
				result.setRequest(get);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error: exception while making TextHelp request: " + e.getMessage());
		}
		return result;
	}
	
	public static MP3 textHelpMP3(String url) {
		MP3 result = execMP3Request(url);
		return result;
	}	
}
