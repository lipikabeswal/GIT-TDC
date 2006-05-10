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
public class LoadContentServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public LoadContentServlet() {
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
        String imageId = ServletUtils.getImageId(request);
        String encryptionKey = ServletUtils.getEncryptionKey(request);
        
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("<HEAD><TITLE>LoadContentServlet</TITLE></HEAD>");
        out.println("<BODY>");
        
        String result = "";
        if (method.equals(ServletUtils.LOAD_SUBTEST_METHOD))
            result = loadSubtest(itemSetId, encryptionKey);
        if (method.equals(ServletUtils.LOAD_ITEM_METHOD))
            result = loadItem(itemSetId, encryptionKey);        
        if (method.equals(ServletUtils.LOAD_IMAGE_METHOD))
            result = loadImage(imageId, encryptionKey);        
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
     *  Load a subtest
     * @param String itemSetId
     * @param String encryptionKey
     * 
     *  retrieves encrypted subtest xml for this item set id from local directory
     *  decrypts subtest xml
     *  returns decrypted subtest xml
     *   
     */
    private String loadSubtest(String itemSetId, String encryptionKey) {
        String result = "loadSubtest invoked with parameters:<br/>";
        result += "itemSetId=" + itemSetId + "<br/>" + "encryptionKey=" + encryptionKey;
        return result;
    }

    /**
     *  Load an item
     * @param String itemSetId
     * @param String encryptionKey
     * 
     *  retrieves encrypted item xml for this item id from local directory
     *  decrypts item xml
     *  saves assets to local directory
     *  returns decrypted item xml
     *   
     */
    private String loadItem(String itemSetId, String encryptionKey) {
        String result = "loadItem invoked with parameters:<br/>";
        result += "itemSetId=" + itemSetId + "<br/>" + "encryptionKey=" + encryptionKey;
        return result;
    }

    /**
     *  Load an image
     * @param String imageId
     * @param String encryptionKey
     * 
     *  retrieves image from local directory
     *  decrypts image xml
     *  returns decrypted image xml
     *   
     */
    private String loadImage(String imageId, String encryptionKey) {
        String result = "loadImage invoked with parameters:<br/>";
        result += "imageId=" + imageId + "<br/>" + "encryptionKey=" + encryptionKey;
        return result;
    }
    
}
