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
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.ctb.tdc.web.dto.TTSSettings;

public class TTSUtil {
	
	public static HashMap mp3CacheMap = new HashMap();
	
	public static class MP3 {
		private long length;
		private InputStream stream;
		private GetMethod request;
		
		/**
		 * @return Returns the request.
		 */
		public GetMethod getRequest() {
			return request;
		}
		/**
		 * @param request The request to set.
		 */
		public void setRequest(GetMethod request) {
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
		System.out.println("TTS: got response: " + thResponse +" text : "+ text +" speedvalue : "+speedValue);
		if(thResponse.indexOf("error") >= 0) {
			System.out.println("TTS: response contains error");
			throw new Exception("error response from TextHelp: " + thResponse);
		} else {
			System.out.println("TTS: sending MP3 request");
			String mp3URL = thResponse.substring(thResponse.indexOf("mp3=") + 4);
			System.out.println("TTS: success, MP3 URL: " + mp3URL);
			MP3 mp3 = textHelpMP3(mp3URL);
			return mp3;
		}
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
			String filename = "cache/" + createFilename(text) + ".mp3";
			
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
	    	if(mp3.getRequest() != null) {
	    		mp3.getRequest().releaseConnection();
	    	}
			
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
			String thResponse = textHelpRequest("testing","-2");
			String mp3URL = thResponse.substring(thResponse.indexOf("mp3=") + 4);
			String result = textHelpMP3(mp3URL).toString();
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
			String decryptedUser = new String(decrypt(ttsSettings.getUserName()));
			ttsSettings.setUserName(decryptedUser);
			String decryptedPass = new String(decrypt(ttsSettings.getPassword()));
			ttsSettings.setPassword(decryptedPass);
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
	
    private static class SpeechRequest extends Thread {
		public String text;
		public String speedValue;
		public static String result = null;
		public static boolean completed = false;
		public boolean waiting;
		public Thread mainThread;
		
		public SpeechRequest(String text, Thread mainThread, String aSpeedValue){
			this.text = text;
			this.mainThread = mainThread;
			this.speedValue = aSpeedValue;
		}
		
		public void run() {
			int responseCode = HttpStatus.SC_OK;
			System.out.println("this.speedValue1 : " + this.speedValue);
			TTSSettings ttsSettings = getTTSSettings();
			String voice = ttsSettings.getVoiceName();
			
			if(voice == null || "".equals(voice.trim())) {
				voice = "ScanSoft Jill_Full_22kHz";
			}
			if(this.speedValue  == null || "".equals(this.speedValue.trim())) {
				this.speedValue = ttsSettings.getSpeedValue();
				if(this.speedValue == null || "".equals(this.speedValue.trim())) {
					this.speedValue = "-2";
				}
			}
			
			
			System.out.println("this.speedValue2 : " + this.speedValue);
			//if "speedValue" coming from client has some value, override the tts.properties value with this one
		/*	if(this.speedValue  != null && !"".equals(this.speedValue.trim())) {
				speedvalue = this.speedValue;
			}*/

			String speechURL = ttsSettings.getUrl();
			if(speechURL == null || "".equals(speechURL.trim())) {
				speechURL = "https://168.116.31.62/SpeechServer/";
			}
			
			PostMethod post = new PostMethod(speechURL);
			post.setFollowRedirects(false);
			NameValuePair[] params = { new NameValuePair("text", text),
					   new NameValuePair("voiceName", voice),
					   new NameValuePair("speedValue", this.speedValue)};
			post.setRequestBody(params);

			// send request to TextHelp
			try {
				HttpClientParams clientParams = new HttpClientParams();
				clientParams.setConnectionManagerTimeout(30000); // timeout in 30 seconds
				
				Protocol myhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
				Protocol.registerProtocol("https", new Protocol("https", new EasySSLProtocolSocketFactory(),443));
				
				HttpClient client = new HttpClient(clientParams);
	
				if(ttsSettings.getUrl().startsWith("https")) {
					setupClient(client, ttsSettings);
				} else {
					setupClientNonSecure(client, ttsSettings);
				}
				
				int TTSRetry = 5;
				while(TTSRetry > 0) {
					try {
						responseCode = client.executeMethod(post);
						int responseLen = 0;
						if(post.getResponseHeader("content-length") != null) {
							responseLen = Integer.valueOf(post.getResponseHeader("content-length").getValue()).intValue();
						}
						
						System.out.println("Text Status: " + responseCode + " Length: " + responseLen);
						
						if (responseCode == HttpStatus.SC_OK && responseLen > 0) {
							InputStream isPost = post.getResponseBodyAsStream();
							BufferedReader in = new BufferedReader(new InputStreamReader(isPost));
							String inputLine = null;
							result = "";
							while ((inputLine = in.readLine()) != null) {
								result += inputLine;
							}
							if(result.indexOf("mp3=") >= 0) {
								in.close();
								completed = true;
								TTSRetry = 0;
								mainThread.interrupt();
							} else {
								SpeechRequest.result = null;
								Thread.sleep(1000);
								TTSRetry--;
							}
						}
						else {
							Thread.sleep(1000);
							TTSRetry--;
						}
					}
					catch (Exception e) {
						e.printStackTrace();
						Thread.sleep(1000);
						TTSRetry--;
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				post.releaseConnection();
			}
		}
	}
    
	public static String textHelpRequest(String text, String speedValue) {
		text = text.replaceAll("<", "less than");
		System.out.println("### Speed Value: " + speedValue);
		for(int i=0;i<2;i++) {
			SpeechRequest request = new SpeechRequest(text, Thread.currentThread(), speedValue);
			try {
				request.start();
				Thread.sleep(15000);
				if(SpeechRequest.completed && SpeechRequest.result != null) {
					System.out.println("Text returning after wait: " + SpeechRequest.result);
					return SpeechRequest.result;
				}
			} catch (InterruptedException ie) {
				if(SpeechRequest.completed && SpeechRequest.result != null) {
					System.out.println("Text returning after interrupt: " + SpeechRequest.result);
					return SpeechRequest.result;
				}
			}
		}
		System.out.println("Text returning null");
		return null;
	}

	private static void setupClient(HttpClient client, TTSSettings ttsSettings) throws Exception {
		//proxy setyp
		String proxyHost = SpeechServletUtils.getProxyHost();
		//System.out.println("Proxy host: " + proxyHost);
        if ((proxyHost != null) && (proxyHost.length() > 0)) {
			// apply proxy settings
            int proxyPort    = SpeechServletUtils.getProxyPort();
            //System.out.println("Proxy port: " + proxyPort);
            String username  = SpeechServletUtils.getProxyUserName();
            //System.out.println("Proxy user: " + username);
            String password  = SpeechServletUtils.getProxyPassword();
            //System.out.println("Proxy pass: " + password);
        	SpeechServletUtils.setProxyCredentials(client, proxyHost, proxyPort, username, password);
        }
		
		client.getHostConfiguration().setHost(ttsSettings.getHost().toString());
		String ttsHost = client.getHostConfiguration().getHost();
		//System.out.println("TTS host: " + ttsHost);
		if ((ttsHost != null) && (ttsHost.length() > 0)) {
			// apply proxy settings
			int ttsPort = 443;
			//System.out.println("TTS port: " + ttsPort);
            String username  = ttsSettings.getUserName();
            //System.out.println("TTS user: " + username);
            String password  = ttsSettings.getPassword();
            //System.out.println("TTS pass: " + password);
            SpeechServletUtils.setTTSCredentials(client, ttsHost, ttsPort, username, password);				
		}
	}
	
	private static void setupClientNonSecure(HttpClient client, TTSSettings ttsSettings) throws Exception {
		//proxy setyp
		String proxyHost = SpeechServletUtils.getProxyHost();
		//System.out.println("Proxy host: " + proxyHost);
        if ((proxyHost != null) && (proxyHost.length() > 0)) {
			// apply proxy settings
            int proxyPort    = SpeechServletUtils.getProxyPort();
            //System.out.println("Proxy port: " + proxyPort);
            String username  = SpeechServletUtils.getProxyUserName();
            //System.out.println("Proxy user: " + username);
            String password  = SpeechServletUtils.getProxyPassword();
            //System.out.println("Proxy pass: " + password);
        	SpeechServletUtils.setProxyCredentials(client, proxyHost, proxyPort, username, password);
        }
		
		client.getHostConfiguration().setHost(ttsSettings.getHost().toString());
		String ttsHost = client.getHostConfiguration().getHost();
		//System.out.println("TTS host: " + ttsHost);
		if ((ttsHost != null) && (ttsHost.length() > 0)) {
			// apply proxy settings
			int ttsPort = 80;
			//System.out.println("TTS port: " + ttsPort);
            String username  = ttsSettings.getUserName();
            //System.out.println("TTS user: " + username);
            String password  = ttsSettings.getPassword();
            //System.out.println("TTS pass: " + password);
            SpeechServletUtils.setTTSCredentials(client, ttsHost, ttsPort, username, password);				
		}
	}
	
	public static class MP3Request extends Thread {
		public String url;
		public static MP3 result = null;
		public static boolean completed = false;
		public Thread mainThread;
		
		public MP3Request(String url, Thread mainThread){
			this.url = url;
			this.mainThread = mainThread;
		}
		
		public void run() {
			
			//System.out.println("mp3 request URL: " + this.url);
	
			int responseCode = HttpStatus.SC_OK;
			
			TTSSettings ttsSettings = getTTSSettings();
			
			GetMethod get = new GetMethod(this.url);
			
			// send request to TextHelp
			try {
				HttpClientParams clientParams = new HttpClientParams();
				clientParams.setConnectionManagerTimeout(30000); // timeout in 30 seconds
				HttpClient client = new HttpClient(clientParams);
				
				setupClientNonSecure(client, ttsSettings);
				
				int TTSRetry = 5;
				while(TTSRetry > 0) {
					try {
						responseCode = client.executeMethod(get);
						int responseLen = 0;
						if(get.getResponseHeader("content-length") != null) {
							responseLen = Integer.valueOf(get.getResponseHeader("content-length").getValue()).intValue();
						}
						System.out.println("Audio Status: " + responseCode + " Length: " + responseLen);
						
						if (responseCode == HttpStatus.SC_OK && responseLen > 0) {
							result = new MP3();
							result.setStream(get.getResponseBodyAsStream());
							result.setLength(get.getResponseContentLength());
							result.setRequest(get);
							TTSRetry = 0;
							completed = true;
							mainThread.interrupt();
						}
						else {
							Thread.sleep(1000);
							TTSRetry--;
						}
					}
					catch (Exception e) {
						e.printStackTrace();
						Thread.sleep(1000);
						TTSRetry--;
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("error: exception while making TextHelp request: " + e.getMessage());
			}
		}
	}
	
	public static MP3 textHelpMP3(String url) {
		for(int i=0;i<2;i++) {
			MP3Request request = new MP3Request(url, Thread.currentThread());
			try {
				request.start();
				Thread.sleep(15000);
				if(MP3Request.completed && MP3Request.result != null) {
					System.out.println("Audio returning after wait: " + MP3Request.result.getLength());
					return MP3Request.result;
				}
			} catch (InterruptedException ie) {
				if(MP3Request.completed && MP3Request.result != null) {
					System.out.println("Audio returning after interrupt: " + MP3Request.result.getLength());
					return MP3Request.result;
				}
			}
		} 
		System.out.println("Audio returning null");
		return null;
	}	
}
