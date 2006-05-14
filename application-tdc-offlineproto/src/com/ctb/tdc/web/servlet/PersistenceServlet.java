package com.ctb.tdc.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctb.tdc.web.dto.AuditVO;
import com.ctb.tdc.web.utils.FileUtils;
import com.ctb.tdc.web.utils.ServletUtils;

/**
 * @author Tai_Truong
 */
public class PersistenceServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
	/**
	 * Constructor of the object.
	 */
	public PersistenceServlet() {
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
        String xml = ServletUtils.getXml(request);
        
        if (method.equals(ServletUtils.LOGIN_METHOD))
            login(response, xml);
        if (method.equals(ServletUtils.SAVE_METHOD))
            save(response, xml);        
        if (method.equals(ServletUtils.FEEDBACK_METHOD))
            feedback(response, xml);        
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
     *  login
     * @param String xml
     *   
     *  request login response xml from TMS   
     *  parse login response xml to determine roster id and restart data (if present)
     *  create audit file if not exists (reconstitute from restart data if restart on different machine??)
     *  write response to audit file
     *  return login response xml to client
     *  
     */
    private boolean login(HttpServletResponse response, String xml) throws IOException {
        handleEvent(xml, ServletUtils.LOGIN_EVENT);
        writeResponse(response, xml);
        return true;
    }
    
    /**
     *  feedback
     * @param String xml
     * 
     *  write request to audit file
     *  request feedback response xml from TMS     
     *  write ack to audit file
     *  return feedback response xml to client
     *  
     */
    private boolean feedback(HttpServletResponse response, String xml) throws IOException {
        handleEvent(xml, ServletUtils.FEEDBACK_EVENT);
        writeResponse(response, xml);
        return true;
    }

    /**
     *  save
     * @param String xml
     *   
     *  check that last line of audit file is an ack   
     *  if last line is not an ack, return error to client
     *  if last line is an ack, write request to audit file
     *  return ack to client
     *  send request to TMS
     *  on response from TMS, write ack to audit file.
     *  
     */
    private boolean save(HttpServletResponse response, String xml) throws IOException {                
        String line = FileUtils.getLastLineInFile();
        AuditVO audit = ServletUtils.buildVOFromString(line);
        String type = audit.getType();
        if (type.equals(ServletUtils.TMS_REQUEST_EVENT)) {
            String error = "<error>No Acknowledgement From TMS</error>";
            writeResponse(response, error);
            return false;
        }
        
        String event = ServletUtils.parseEvent(xml);
        audit = ServletUtils.buildVOFromXML(xml, event);
        FileUtils.writeToAuditFile(audit);        
        writeResponse(response, xml);        
        sendRequestToTMS(event);        
        return true;
    }
 
    private void handleEvent(String xml, String event) throws IOException {
        AuditVO audit = ServletUtils.buildVOFromXML(xml, event);
        FileUtils.writeToAuditFile(audit);        
    }

    private void sendRequestToTMS(String event) throws IOException {
        AuditVO audit = ServletUtils.buildVOFromType(ServletUtils.TMS_REQUEST_EVENT);
        FileUtils.writeToAuditFile(audit);        
        
        // call TMS here
        
        // pretend TMS return ack, this code will be removed later
        AuditVO audit_ack = ServletUtils.buildVOFromType(ServletUtils.TMS_ACK_EVENT);
        FileUtils.writeToAuditFile(audit_ack);        
    }    
    
    private void writeResponse(HttpServletResponse response, String xml) throws IOException {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        out.println(xml);            
        out.flush();
        out.close();        
    }
    
        

}
