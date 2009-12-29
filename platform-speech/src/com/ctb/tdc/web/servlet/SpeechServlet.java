package com.ctb.tdc.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctb.tdc.web.utils.TTSUtil;
import com.ctb.tdc.web.utils.TTSUtil.MP3;
 
/**
 * @author Nate Cohen
 */
public class SpeechServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
 //   static Logger logger = Logger.getLogger(SpeechServlet.class);
    
    private static final String OK_RESPONSE = "<status>OK</status>";
    
    private static String text;
    private static String speedValue;
    
    private static int delayCounter;
    private static long lastDelay;
    public static final int MAX_DELAYS = 3;
    
	/**
	 * Constructor of the object.
	 */
	public SpeechServlet() {
		super();
		delayCounter = 0;
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			long startTime = System.currentTimeMillis();
			MP3 mp3 = null;
			String cacheUrl = (String) TTSUtil.checkCache(speedValue + text);
			if(cacheUrl != null){
				System.out.println("TTS: cache hit, MP3 URL: " + cacheUrl);
				TTSUtil.decryptFile(cacheUrl.replaceAll(".mp3", ".enc"), cacheUrl);
			} else {
				mp3 = TTSUtil.speak(text,speedValue);
				TTSUtil.cacheFile(speedValue + text, mp3);
			}
			cacheUrl = (String) TTSUtil.mp3CacheMap.get(speedValue + text);
			mp3 = new MP3();
			File mp3File = new File(cacheUrl);
			mp3.setLength(mp3File.length());
			mp3.setStream(new FileInputStream(mp3File));
			
			long endTime = System.currentTimeMillis();
			if((endTime - startTime) > 10000 && (endTime - lastDelay) < 60000) {
				delayCounter++;
				lastDelay = endTime;
			}
			if(delayCounter >= MAX_DELAYS) {
				throw new Exception("Repeated TTS delays experienced");
			}
			
	    	response.setStatus(200);
	    	response.setContentType("audio/x-mp3");
	    	response.setHeader("Content-Disposition", "filename=speech.mp3");
	    	//response.setHeader("Cache-Control","no-cache, no-store");
	    	//response.setHeader("Expires","Fri, 30 Oct 1998 14:19:41 GMT");
	    	response.setHeader("Pragma","no-cache");
	    	int len = Integer.parseInt(Long.toString(mp3.getLength()));
	    	response.setContentLength(len);
	    	OutputStream out = response.getOutputStream();
	    	InputStream in = mp3.getStream();
	    	byte [] buffer = new byte[1024];
	    	int read = 0;
	    	while((read = in.read(buffer)) > 0) {
	    		//System.out.println("TTS: streaming " + read + " bytes to requestor");
	    		out.write(buffer, 0, read);
	    	}
	    	out.close();
	    	in.close();
			new File(cacheUrl).delete();
	    	System.out.println("TTS: finished stream, response flushed");
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
	    	response.setContentType("text/html");
	    	response.setHeader("Cache-Control","no-cache, no-store");
	    	response.setHeader("Expires","Fri, 30 Oct 1998 14:19:41 GMT");
	    	response.setHeader("Pragma","no-cache");
	    	response.setContentLength(5);
	    	OutputStream out = response.getOutputStream();
	    	out.write(new String("ERROR").getBytes());
	    	out.close();
		}
    }

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("text") != null) {
			text = request.getParameter("text");
		} else {
        	TTSUtil.stop();
        }

		if(request.getParameter("speedValue") != null) {
			speedValue = request.getParameter("speedValue");
		}
		
		response.setStatus(200);
    	response.setContentType("text/xml");
    	response.setContentLength(OK_RESPONSE.length());
    	OutputStream out = response.getOutputStream();
    	out.write(OK_RESPONSE.getBytes());
    	out.flush();
    	response.flushBuffer();
	}
 
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// do nothing
	}
  
}

