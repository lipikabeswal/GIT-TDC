package com.ctb.tdc.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

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
    
    private static HashMap textMap = new HashMap(10);
    
	/**
	 * Constructor of the object.
	 */
	public SpeechServlet() {
		super();
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
			String client = request.getSession().getId();
			System.out.println("*****  Retrieving mp3 for session " + client);
			MP3 mp3 = (MP3) textMap.get(client);
	
	    	if(mp3 != null) {
		    	synchronized(mp3.getFileName()) {
		    		response.setStatus(200);
			    	response.setContentType("audio/x-mp3");
			    	response.setHeader("Content-Disposition", "filename=speech.mp3");
			    	//response.setHeader("Cache-Control","no-cache, no-store");
			    	//response.setHeader("Expires","Fri, 30 Oct 1998 14:19:41 GMT");
			    	response.setHeader("Pragma","no-cache");
			    	int len = Integer.parseInt(Long.toString(mp3.getLength()));
			    	response.setContentLength(len);
			    	OutputStream out = response.getOutputStream();
			    	FileInputStream in = new FileInputStream(mp3.getFileName());
			    	
			    	byte [] buffer = new byte[1024];
			    	int read = 0;
			    	while((read = in.read(buffer)) > 0) {
			    		//System.out.println("TTS: streaming " + read + " bytes to requestor");
			    		out.write(buffer, 0, read);
			    	}
			    	out.close();
			    	in.close();
		    	}
	    	} else {
	    		response.setStatus(200);
		    	response.setContentType("audio/x-mp3");
		    	response.setHeader("Content-Disposition", "filename=speech.mp3");
		    	response.setHeader("Pragma","no-cache");
		    	response.setContentLength(0);
		    	OutputStream out = response.getOutputStream();
		    	out.write(new byte[0]);
		    	out.close();
	    	}
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
		try {
			String client = request.getSession(true).getId();
			
			if(request.getParameter("text") != null) {
				String text = request.getParameter("text");
				String audioFormat = "mp3"; //default audio format
			    String speedValue = "-2";
			    
				if(request.getParameter("speedValue") != null) {
					speedValue = request.getParameter("speedValue");
				}
				//for Mac audioFormat is .mp3 and for others .ogg
				if(request.getParameter("audioFormat") != null){
					audioFormat = request.getParameter("audioFormat");
				}
				
				MP3 mp3 = new MP3();
				String cacheUrl = (String) TTSUtil.checkCache(speedValue + text);
				if(cacheUrl != null){
					System.out.println("TTS: cache hit, MP3 URL: " + cacheUrl);
					//TTSUtil.decryptFile(cacheUrl.replaceAll(".mp3", ".enc"), cacheUrl);
				} else {
					TTSUtil ttsutil = new TTSUtil();
					mp3 = ttsutil.speak(text,speedValue,audioFormat);
					ttsutil = null;
					TTSUtil.cacheFile(speedValue + text, mp3);
				}
				String filename = TTSUtil.createFilename(speedValue + text) + ".enc";
				cacheUrl = "cache/" + filename;
				
				File mp3File = new File(cacheUrl);
				mp3.setLength(mp3File.length());
				mp3.setFileName(cacheUrl);
				
				System.out.println("*****  Storing mp3 for session " + client);
				textMap.put(client, mp3);
			} else {
	        	TTSUtil.stop();
	        }
		} catch (Exception e) {
			e.printStackTrace();
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

