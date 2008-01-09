package com.ctb.tdc.web.servlet.fixed;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ctb.tdc.web.servlet.PersistenceServlet;
import com.ctb.tdc.web.utils.TTSUtil;
import com.ctb.tdc.web.utils.TTSUtil.MP3;
 
/**
 * @author Nate Cohen
 */
public class SpeechServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    static Logger logger = Logger.getLogger(PersistenceServlet.class);
    
    private static final String OK_RESPONSE = "<status>OK</status>";
    
    private static String text;
    
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
		MP3 mp3 = TTSUtil.speak(text);
    	response.setStatus(200);
    	response.setContentType("audio/x-mp3");
    	response.setHeader("Content-Disposition", "filename=speech.mp3");
    	response.setContentLength(Integer.parseInt(Long.toString(mp3.getLength())));
    	OutputStream out = response.getOutputStream();
    	InputStream in = mp3.getStream();
    	byte [] buffer = new byte[1024];
    	int read = 0;
    	while((read = in.read(buffer)) > 0) {
    		out.write(buffer, 0, read);
    	}
    	in.close();
    	mp3.getRequest().releaseConnection();
    	out.flush();
    	response.flushBuffer();
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