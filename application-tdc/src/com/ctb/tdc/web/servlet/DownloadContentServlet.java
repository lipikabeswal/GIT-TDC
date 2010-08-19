package com.ctb.tdc.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ctb.tdc.web.utils.ServletUtils;

/**
 * @author Tai_Truong
 */
public class DownloadContentServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    static Logger logger = Logger.getLogger(PersistenceServlet.class);
    
	/**
	 * Constructor of the object.
	 */
	public DownloadContentServlet() {
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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

        String method = ServletUtils.getMethod(request);
        
        long startTime = System.currentTimeMillis();
        
        String itemSetId = ServletUtils.getItemSetId(request);
        String encryptionKey = ServletUtils.getEncryptionKey(request);
        
        if (method.equals(ServletUtils.INITITAL_DOWNLOAD_CONTENT_METHOD))
            initialDownloadContent(response, itemSetId, encryptionKey);
        if (method.equals(ServletUtils.DOWNLOAD_CONTENT_METHOD))
            downloadContent(response, itemSetId, encryptionKey);
        
        logger.info("DownloadContentServlet: " + method + " took " + (System.currentTimeMillis() - startTime) + "\n");
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	    doGet(request, response);
	}
 
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// do nothing
	}
    
    /**
     *  Download a test from TMS
     * @param String itemSetId
     * @param String encryptionKey
     * 
     *  - checks if content is currently on system and is up to date using key
     *  - if content is not current, request content for this tc test id from TMS
     *  - saves encrypted content to local directory
     *  - return ok to client
     *
     */
    private boolean initialDownloadContent(HttpServletResponse response, String itemSetId, String encryptionKey) throws IOException {
        if (! hasCurrentContent(encryptionKey)) {
            String content = loadTest(itemSetId);
            writeContentToDisk(content);
        }
        writeResponse(response, ServletUtils.OK);
        return true;
    }

    /**
     *  Download a test from TMS
     * @param String itemSetId
     * @param String encryptionKey
     * 
     *  - checks if content is currently on system and is up to date using key
     *  - if content is not current, request content for this tc test id from TMS
     *  - saves encrypted content to local directory
     *  - return ok to client
     *
     */
    private boolean downloadContent(HttpServletResponse response, String itemSetId, String encryptionKey) throws IOException {
        if (! hasCurrentContent(encryptionKey)) {
            String content = loadTest(itemSetId);
            writeContentToDisk(content);
        }
        writeResponse(response, ServletUtils.OK);
        return true;
    }
    
    /**
     *  checks if content is currently on system and is up to date using key
     */
    private boolean hasCurrentContent(String encryptionKey) {
        
        return true;
    }

    /**
     *  load content for this test from TMS
     */
    private String loadTest(String itemSetId) {
        return "";
    }

    /**
     *  saves encrypted content to local directory
     */
    private void writeContentToDisk(String content) {
    }

    private void writeResponse(HttpServletResponse response, String xml) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(xml);            
        out.flush();
        out.close();        
    }
    
}
