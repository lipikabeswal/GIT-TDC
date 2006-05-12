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

    // temporary for now
    private static final String FILE_PATH = "C:\\xmls\\";

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
        String itemId = ServletUtils.getItemId(request);
        String imageId = ServletUtils.getImageId(request);
        String encryptionKey = ServletUtils.getEncryptionKey(request);
        
        if (method.equals(ServletUtils.LOAD_SUBTEST_METHOD))
            loadSubtest(response, itemSetId, encryptionKey);
        if (method.equals(ServletUtils.LOAD_ITEM_METHOD))
            loadItem(response, itemId, encryptionKey);        
        if (method.equals(ServletUtils.LOAD_IMAGE_METHOD))
            loadImage(response, imageId, encryptionKey);                
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
     * @throws IOException 
     * 
     *  retrieves encrypted subtest xml for this item set id from local directory
     *  decrypts subtest xml
     *  returns decrypted subtest xml
     *   
     */
    private boolean loadSubtest(HttpServletResponse response, String itemSetId, String encryptionKey) throws IOException {
        boolean result = true; 
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        
        String fileName = FILE_PATH + "subtest.xml";
        result = ServletUtils.getFile(fileName, out);     
        
        out.flush();
        out.close();        
        return result;
    }

    /**
     *  Load an item
     * @param String itemSetId
     * @param String encryptionKey
     * @throws IOException 
     * 
     *  retrieves encrypted item xml for this item id from local directory
     *  decrypts item xml
     *  saves assets to local directory
     *  returns decrypted item xml
     *   
     */
    private boolean loadItem(HttpServletResponse response, String itemId, String encryptionKey) throws IOException {
        boolean result = true; 
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        
        String fileName = FILE_PATH + "item" + itemId + ".xml";
        result = ServletUtils.getFile(fileName, out);        

        out.flush();
        out.close();        
        return result;
    }

    /**
     *  Load an image
     * @param String imageId
     * @param String encryptionKey
     * @throws IOException 
     * 
     *  retrieves image from local directory
     *  decrypts image xml
     *  returns decrypted image xml
     *   
     */
    private boolean loadImage(HttpServletResponse response, String imageId, String encryptionKey) throws IOException {
        boolean result = true; 
        response.setContentType(ServletUtils.getMIMEType("gif"));
        PrintWriter out = response.getWriter();
        
        String fileName = FILE_PATH + "ctb_logo.gif";
        result = ServletUtils.getFile(fileName, out);        

        out.flush();
        out.close();        
        return result;
    }
    

    
}
