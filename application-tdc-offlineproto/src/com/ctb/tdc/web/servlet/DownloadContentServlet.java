package com.ctb.tdc.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctb.tdc.web.utils.ServletUtils;

/**
 * @author Tai_Truong
 */
public class DownloadContentServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
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
		// Put your code here
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
        String itemSetId = ServletUtils.getItemSetId(request);
        String encryptionKey = ServletUtils.getEncryptionKey(request);
        
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("<HEAD><TITLE>DownloadContentServlet</TITLE></HEAD>");
        out.println("<BODY>");
        
        String result = "";
        if (method.equals(ServletUtils.DOWNLOAD_CONTENT_METHOD))
            result = downloadContent(itemSetId, encryptionKey);
        if (method.equals(ServletUtils.GET_DOWNLOAD_STATUS_METHOD))
            result = getDownloadStatus(itemSetId);        
        out.println(result);
        
        out.println("</BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
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
		// Put your code here
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
    private String getDownloadStatus(String itemSetId) {
        String result = "getDownloadStatus invoked with parameters:<br/>";
        result += "itemSetId=" + itemSetId;
        return result;
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
    private String downloadContent(String itemSetId, String encryptionKey) {
        String result = "downloadContent invoked with parameters:<br/>";
        result += "itemSetId=" + itemSetId + "<br/>" + "encryptionKey=" + encryptionKey;
        
        if (! hasCurrentContent(encryptionKey)) {
            result = loadTest(itemSetId);
            writeContentToDisk(result);
        }
        acknowledge(ServletUtils.OK);
        
        return result;
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
        String result = null;
        
        return result;
    }

    /**
     *  saves encrypted content to local directory
     */
    private void writeContentToDisk(String itemSetId) {
    }

    /**
     *  send acknowledgement to client
     */
    private void acknowledge(String ack) {
    }
    
}
