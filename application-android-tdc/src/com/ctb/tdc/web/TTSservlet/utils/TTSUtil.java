// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 7/31/2012 12:36:05 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 

package com.ctb.tdc.web.TTSservlet.utils;

import android.annotation.SuppressLint;
import android.util.Base64;

import com.ctb.tdc.web.dto.TTSSettings;

import com.ctb.tdc.web.utils.ServletUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Pattern;



import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.*;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.*;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

// Referenced classes of package com.ctb.tdc.web.utils:
//            EasyTrustStrategy, MemoryCache, ServletUtils

public class TTSUtil
{
    public static class MP3
    {

        public HttpGet getRequest()
        {
            return request;
        }

        public void setRequest(HttpGet httpget)
        {
            request = httpget;
        }

        public long getLength()
        {
            return length;
        }

        public void setLength(long l)
        {
            length = l;
        }

        public InputStream getStream()
        {
            return stream;
        }

        public void setStream(InputStream inputstream)
        {
            stream = inputstream;
        }

        private long length;
        private InputStream stream;
  
        private HttpGet request;

        public MP3()
        {
        }
    }


    public TTSUtil()
    {
    }

    public static void setProxyCredentials(DefaultHttpClient defaulthttpclient, String s, int i, String s1, String s2, String s3)
    {
        boolean flag = s != null && s.length() > 0;
        boolean flag1 = i > 0;
        boolean flag2 = s1 != null && s1.length() > 0;
        boolean flag3 = s3 != null && s3.trim().length() > 0;
        Object obj = null;
        if(flag && flag1)
        {
            HttpHost httphost = new HttpHost(s, i);
            defaulthttpclient.getParams().setParameter("http.route.default-proxy", httphost);
        } else
        if(flag)
        {
            HttpHost httphost1 = new HttpHost(s);
            defaulthttpclient.getParams().setParameter("http.route.default-proxy", httphost1);
        }
        if(flag && flag2)
        {
            AuthScope authscope;
            if(flag1)
                authscope = new AuthScope(s, i, AuthScope.ANY_REALM);
            else
                authscope = new AuthScope(s, -1, AuthScope.ANY_REALM);
            UsernamePasswordCredentials usernamepasswordcredentials = new UsernamePasswordCredentials(s1, s2);
            NTCredentials ntcredentials = new NTCredentials((new StringBuilder()).append(s3).append("/").append(s1).append(":").append(s2).toString());
            if(!flag3)
                defaulthttpclient.getCredentialsProvider().setCredentials(authscope, usernamepasswordcredentials);
            else
                defaulthttpclient.getCredentialsProvider().setCredentials(authscope, ntcredentials);
        }
    }

    public static void setTTSCredentials(DefaultHttpClient defaulthttpclient, String s, String s1, String s2)
    {
        AuthScope authscope = new AuthScope(s, -1, AuthScope.ANY_REALM);
        UsernamePasswordCredentials usernamepasswordcredentials = new UsernamePasswordCredentials(s1, s2);
        defaulthttpclient.getCredentialsProvider().setCredentials(authscope, usernamepasswordcredentials);
    }

    public static MP3 speak(String s, String s1)
        throws Exception
    {
        System.out.println("TTS: sending speech request");
        String s2 = textHelpRequest(s, s1);
        if(s2 == null)
        {
            System.out.println("TTS: No response from TextHelp - TTS server is specified incorrectly or is down");
            throw new Exception("No response from TextHelp.");
        }
        if(s2.indexOf("error") >= 0)
        {
            System.out.println("TTS: response contains error");
            throw new Exception((new StringBuilder()).append("Error response from TextHelp: ").append(s2).toString());
        }
        System.out.println("TTS: Sending MP3 request");
        String s3 = s2.substring(s2.indexOf("mp3=") + 4);
        System.out.println((new StringBuilder()).append("TTS: MP3 URL: ").append(s3).toString());
        MP3 mp3 = textHelpMP3(s3);
        if(mp3 == null)
        {
            System.out.println("TTS: no response from TextHelp - TTS server is specified incorrectly or is down");
            throw new Exception("No response from TextHelp.");
        } else
        {
            return mp3;
        }
    }

    private static String stripchars(String s)
    {
        return s.replaceAll(Pattern.quote("*"), "").replaceAll(Pattern.quote("."), "").replaceAll(Pattern.quote("/"), "");
    }

    private static String createFilename(String s)
        throws Exception
    {
        MessageDigest messagedigest = MessageDigest.getInstance("MD5");
        byte abyte0[] = s.getBytes();
        messagedigest.update(abyte0);
        byte abyte1[] = messagedigest.digest();
        int i = 32;
        if(s.length() < 32)
            i = s.length();
        return URLEncoder.encode(stripchars(new String(abyte1)));
    }

    public static String checkCache(String s)
        throws Exception
    {
        String s1 = (String)mp3CacheMap.get(s);
        if(s1 == null)
        {
            String s2 = (new StringBuilder()).append("cache/").append(createFilename(s)).append(".enc").toString();
            if((new File(s2)).exists())
            {
                s1 = s2.replaceAll(".enc", ".mp3");
                mp3CacheMap.put(s, s1);
            }
        }
        return s1;
    }

    public static void cacheFile(String s, MP3 mp3)
    {
        try
        {
            String s1 = (new StringBuilder()).append(createFilename(s)).append(".mp3").toString();
            System.out.println((new StringBuilder()).append("TTS: cache miss, new cache file: ").append(s1).toString());
            InputStream inputstream = mp3.getStream();
            FileOutputStream fileoutputstream = new FileOutputStream(s1);
            byte abyte0[] = new byte[1024];
            for(int i = 0; (i = inputstream.read(abyte0)) > 0;)
                fileoutputstream.write(abyte0, 0, i);

            fileoutputstream.close();
            inputstream.close();
            mp3CacheMap.put(s, s1);
            encryptFile(s1, s1.replaceAll(".mp3", ".enc"));
            System.out.println("TTS: write to MP3 cache successful");
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public static void encryptFile(String s, String s1)
        throws Exception
    {
        FileInputStream fileinputstream = new FileInputStream(s);
        int i = fileinputstream.available();
        byte abyte0[] = new byte[i];
        fileinputstream.read(abyte0);
        fileinputstream.close();
        byte abyte1[] = encrypt(abyte0).getBytes();
        FileOutputStream fileoutputstream = new FileOutputStream(s1);
        fileoutputstream.write(abyte1);
        fileoutputstream.close();
    }

    @SuppressLint("NewApi")
	public static void decryptFile(String byteInput, String s1)
        throws Exception
    {
     
        byte abyte0[] = decrypt(Base64.decode(byteInput,Base64.DEFAULT));
        FileOutputStream fileoutputstream = new FileOutputStream(s1);
        fileoutputstream.write(abyte0);
        fileoutputstream.close();
    }

    public static void stop()
    {
    }

    public static void main(String args[])
    {
        try
        {
            speak("testing testing 1 2 3", "-2");
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private static TTSSettings getTTSSettings()
    {
        MemoryCache memorycache = MemoryCache.getInstance();
        TTSSettings ttssettings = memorycache.getTTSSettings();
        if(ttssettings == null)
        {
            ResourceBundle resourcebundle = ResourceBundle.getBundle("tts");
            ttssettings = new TTSSettings(resourcebundle);
            String s = new String(decrypt(ttssettings.getUserName()));
            ttssettings.setUserName(s);
            String s1 = new String(decrypt(ttssettings.getPassword()));
            ttssettings.setPassword(s1);
            memorycache.setTTSSettings(ttssettings);
        }
        return ttssettings;
    }

    @SuppressLint("NewApi")
	private static String encrypt(byte abyte0[])
    {
        byte abyte1[] = null;
        try
        {
            String s = obfuscate();
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            byte abyte2[] = s.getBytes();
            messagedigest.update(abyte2);
            byte abyte3[] = messagedigest.digest();
            KeyParameter keyparameter = new KeyParameter(abyte3);
            RC4Engine rc4engine = new RC4Engine();
            rc4engine.init(true, keyparameter);
            abyte1 = new byte[abyte0.length];
            rc4engine.processBytes(abyte0, 0, abyte0.length, abyte1, 0);
            rc4engine.reset();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
       // return (new BASE64Encoder()).encode(abyte1);
        return Base64.encode(abyte1,Base64.DEFAULT).toString();
    }

    private static byte[] RC4Decrypt(byte abyte0[], int i)
        throws IOException
    {
        String s = obfuscate();
        MD5Digest md5digest = new MD5Digest();
        byte abyte1[] = new byte[s.length()];
        abyte1 = s.getBytes();
        md5digest.update(abyte1, 0, abyte1.length);
        byte abyte2[] = new byte[md5digest.getDigestSize()];
        md5digest.doFinal(abyte2, 0);
        KeyParameter keyparameter = new KeyParameter(abyte2);
        RC4Engine rc4engine = new RC4Engine();
        rc4engine.init(false, keyparameter);
        byte abyte3[] = new byte[i];
        rc4engine.processBytes(abyte0, 0, i, abyte3, 0);
        rc4engine.reset();
        return abyte3;
    }

    private static byte[] decrypt(String s)
    {
       byte abyte0[] = null;
        try
        {
           // byte abyte1[] = (new BASE64Decoder()).decodeBuffer(s);
               byte abyte1[] = (Base64.decode(s,Base64.DEFAULT));
            abyte0 = RC4Decrypt(abyte1, abyte1.length);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return abyte0;
    }

    private static byte[] decrypt(byte abyte0[])
    {
        byte abyte1[] = null;
        try
        {
            abyte1 = RC4Decrypt(abyte0, abyte0.length);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return abyte1;
    }

    private static String obfuscate()
    {
        StringBuffer stringbuffer = new StringBuffer();
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        stringbuffer.append(s.substring(0, 1));
        stringbuffer.append(s.substring(56, 57));
        stringbuffer.append(s.substring(4, 5));
        stringbuffer.append(s.substring(16, 17));
        stringbuffer.append(s.substring(32, 33));
        stringbuffer.append(s.substring(49, 50));
        stringbuffer.append(s.substring(8, 9));
        stringbuffer.append(s.substring(22, 23));
        stringbuffer.append(s.substring(54, 55));
        stringbuffer.append(s.substring(2, 3));
        stringbuffer.append(s.substring(14, 15));
        stringbuffer.append(s.substring(34, 35));
        stringbuffer.append(s.substring(58, 59));
        stringbuffer.append(s.substring(7, 8));
        stringbuffer.append(s.substring(35, 36));
        stringbuffer.append(s.substring(39, 40));
        return stringbuffer.toString();
    }

    private static String execSpeechRequest(String s, String s1)
    {
        String s2 = null;
        char c = '\310';
        System.out.println((new StringBuilder()).append("this.speedValue1 : ").append(s1).toString());
        TTSSettings ttssettings = getTTSSettings();
        String s3 = ttssettings.getVoiceName();
        if(s3 == null || "".equals(s3.trim()))
            s3 = "ScanSoft Jill_Full_22kHz";
        if(s1 == null || "".equals(s1.trim()))
        {
            s1 = ttssettings.getSpeedValue();
            if(s1 == null || "".equals(s1.trim()))
                s1 = "-2";
        }
        String s4 = ttssettings.getUrl();
        if(s4 == null || "".equals(s4.trim()))
            s4 = "https://oastts.ctb.com/SpeechServer/";
        try
        {
            HttpPost httppost = new HttpPost(s4);
           List<BasicNameValuePair> arraylist = new ArrayList<BasicNameValuePair>();
            arraylist.add(new BasicNameValuePair("text", s));
            arraylist.add(new BasicNameValuePair("voiceName", s3));
            arraylist.add(new BasicNameValuePair("speedValue", s1));
            httppost.setEntity(new UrlEncodedFormEntity(arraylist));
            HttpResponse httpresponse = client.execute(httppost);
            int i = httpresponse.getStatusLine().getStatusCode();
            int j = 0;
            if(httpresponse.getHeaders("content-length") != null)
                j = Integer.valueOf(httpresponse.getHeaders("content-length")[0].getValue()).intValue();
            System.out.println((new StringBuilder()).append("Text Status: ").append(i).append(" Length: ").append(j).toString());
            if(i == 200 && j > 0)
            {
                s2 = "";
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpresponse.getEntity().getContent()), 0x20000);
                for(String s5 = null; (s5 = bufferedreader.readLine()) != null;)
                    s2 = (new StringBuilder()).append(s2).append(s5).toString();

                if(s2.indexOf("mp3=") >= 0)
                    bufferedreader.close();
                else
                    s2 = null;
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return s2;
    }

    public static String textHelpRequest(String s, String s1)
    {
        s = s.replaceAll("<", "less than");
        String s2 = execSpeechRequest(s, s1);
        return s2;
    }

    public static MP3 execMP3Request(String s)
    {
        MP3 mp3 = null;
        char c = '\310';
        try
        {
            TTSSettings ttssettings = getTTSSettings();
            HttpGet httpget = new HttpGet(s);
            HttpResponse httpresponse = client.execute(httpget);
            int i = httpresponse.getStatusLine().getStatusCode();
            int j = 0;
            if(httpresponse.getHeaders("content-length") != null)
                j = Integer.valueOf(httpresponse.getHeaders("content-length")[0].getValue()).intValue();
            System.out.println((new StringBuilder()).append("Audio Status: ").append(i).append(" Length: ").append(j).toString());
            if(i == 200 && j > 0)
            {
                mp3 = new MP3();
                mp3.setStream(httpresponse.getEntity().getContent());
                mp3.setLength(j);
                mp3.setRequest(httpget);
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            System.out.println((new StringBuilder()).append("error: exception while making TextHelp request: ").append(exception.getMessage()).toString());
        }
        return mp3;
    }

    public static MP3 textHelpMP3(String s)
    {
        MP3 mp3 = execMP3Request(s);
        return mp3;
    }

    public static HashMap mp3CacheMap = new HashMap();
    public static DefaultHttpClient client;

    static 
    {
        try
        {
            EasyTrustStrategy easytruststrategy = new EasyTrustStrategy();
            AllowAllHostnameVerifier allowallhostnameverifier = new AllowAllHostnameVerifier();
            

            SSLSocketFactory sslsocketfactory = new SSLSocketFactory(null);
           
            PlainSocketFactory plainsocketfactory = new PlainSocketFactory();
            Scheme scheme = new Scheme("https", sslsocketfactory,443);
            Scheme scheme1 = new Scheme("http",  plainsocketfactory,80);
            SchemeRegistry schemeregistry = new SchemeRegistry();
            schemeregistry.register(scheme);
            schemeregistry.register(scheme1);
            HttpParams httpp=null;//we need to create an object of HttpParams
            ThreadSafeClientConnManager threadsafeclientconnmanager = new ThreadSafeClientConnManager(httpp,schemeregistry);
            //threadsafeclientconnmanager.setMaxTotal(1);
            BasicHttpParams basichttpparams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basichttpparams, 10000);
            HttpConnectionParams.setSoTimeout(basichttpparams, 10000);
            client = new DefaultHttpClient(threadsafeclientconnmanager, basichttpparams);
            String s = ServletUtils.getProxyHost();
            if(s != null && s.length() > 0)
            {
                int i = ServletUtils.getProxyPort();
                String s1 = ServletUtils.getProxyUserName();
                String s2 = ServletUtils.getProxyPassword();
                String s3 = ServletUtils.getProxyDomain();
                setProxyCredentials(client, s, i, s1, s2, s3);
            }
            TTSSettings ttssettings = getTTSSettings();
            setTTSCredentials(client, ttssettings.getHost(), ttssettings.getUserName(), ttssettings.getPassword());
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }
}