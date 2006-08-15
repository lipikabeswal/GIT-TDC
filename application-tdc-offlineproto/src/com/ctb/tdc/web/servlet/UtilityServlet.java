package com.ctb.tdc.web.servlet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ctb.tdc.web.dto.ServletSettings;
import com.ctb.tdc.web.utils.AuditFile;
import com.ctb.tdc.web.utils.MemoryCache;
import com.ctb.tdc.web.utils.ServletUtils;
 
/**
 * @author Tai_Truong
 */
public class UtilityServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    static Logger logger = Logger.getLogger(PersistenceServlet.class);
    
	/**
	 * Constructor of the object.
	 */
	public UtilityServlet() {
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
        if (method.equals("deleteAuditFile")) {
            deleteAuditFile(request);
            ServletUtils.writeResponse(response, ServletUtils.OK);
        }
        else
        if (method.equals("auditFileGetLineCount")) {
            int lineCount = auditFileGetLineCount(request);
            String str = String.valueOf(lineCount);
            ServletUtils.writeResponse(response, str);
        }
        else
        if (method.equals("auditFileExists")) {
            boolean exists = auditFileExists(request);
            String str = String.valueOf(exists);
            ServletUtils.writeResponse(response, str);
        }
        else
        if (method.equals("servletSetting")) {
            servletSetting(request);
            ServletUtils.writeResponse(response, ServletUtils.OK);
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

    private void servletSetting(HttpServletRequest request) {

        MemoryCache memoryCache = MemoryCache.getInstance();
        ResourceBundle rb = ResourceBundle.getBundle(ServletUtils.SERVLET_NAME);
        ServletSettings srvSettings = new ServletSettings(rb);

        String tmsPersist = request.getParameter("tmsPersist");
        if (tmsPersist != null) {
            srvSettings.setTmsPersist(tmsPersist.equals("true"));
        }
        String tmsAckRequired = request.getParameter("tmsAckRequired");
        if (tmsAckRequired != null) {
            srvSettings.setTmsAckRequired(tmsAckRequired.equals("true"));
        }
        String tmsAckMaxLostMessage = request.getParameter("tmsAckMaxLostMessage");
        if (tmsAckMaxLostMessage != null) {
            srvSettings.setTmsAckMaxLostMessage(Integer.parseInt(tmsAckMaxLostMessage));
        }
        String proxyHost = request.getParameter("proxyHost");
        if (proxyHost != null) {
            srvSettings.setProxyHost(proxyHost);
        }
        String proxyPort = request.getParameter("proxyPort");
        if (proxyPort != null) {
            srvSettings.setProxyPort(Integer.parseInt(proxyPort));
        }
        String proxyUserName = request.getParameter("proxyUserName");
        if (proxyUserName != null) {
            srvSettings.setProxyUserName(proxyUserName);
        }
        String proxyPassword = request.getParameter("proxyPassword");
        if (proxyPassword != null) {
            srvSettings.setProxyPassword(proxyPassword);
        }
        
        memoryCache.setSrvSettings(srvSettings);
        memoryCache.setStateMap(new HashMap());        
        memoryCache.setLoaded(true);
    }
    
    private void deleteAuditFile(HttpServletRequest request) {
        String fileName = request.getParameter("fileName");
        String tdcHome = System.getProperty(AuditFile.TDC_HOME);
        fileName = tdcHome + AuditFile.AUDIT_FOLDER + fileName;

        try {
            AuditFile.deleteLogger(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }                      
    }
    
    private boolean auditFileExists(HttpServletRequest request) {
        boolean exists = false;
        String fileName = request.getParameter("fileName");
        String tdcHome = System.getProperty(AuditFile.TDC_HOME);
        fileName = tdcHome + AuditFile.AUDIT_FOLDER + fileName;

        try {
            exists = AuditFile.exists(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }      
        return exists;
    }
    
    private int auditFileGetLineCount(HttpServletRequest request) throws IOException {
        String fileName = request.getParameter("fileName");
        String tdcHome = System.getProperty(AuditFile.TDC_HOME);
        fileName = tdcHome + AuditFile.AUDIT_FOLDER + fileName;
        
        String buff = null;
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        int lineCount = 0;
        while((buff = reader.readLine()) != null) {
            lineCount++;
        }
        reader.close();
        return lineCount;
    }

    
}
