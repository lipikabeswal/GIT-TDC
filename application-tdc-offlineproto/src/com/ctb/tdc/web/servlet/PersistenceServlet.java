package com.ctb.tdc.web.servlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctb.tdc.web.utils.ServletUtils;

/**
 * @author Tai_Truong
 */
public class PersistenceServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    // temporary for now
    private static final String AUDIT_FOLDER = "C:\\POC_Servlet\\audit\\";
    
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
        boolean result = true; 
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println(ServletUtils.OK);                        
        out.flush();
        out.close();        
        return result;
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
        boolean result = true; 
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println(ServletUtils.OK);                        
        out.flush();
        out.close();        
        return result;
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
        boolean result = true; 
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String event = ServletUtils.parseEvent(xml);
        if (event.equals(ServletUtils.RESPONSE_EVENT))
            responseEvent(xml);
        if (event.equals(ServletUtils.START_EVENT))
            startEvent();
        if (event.equals(ServletUtils.FINISH_EVENT))
            finishEvent();
        if (event.equals(ServletUtils.PAUSE_EVENT))
            pauseEvent();
        if (event.equals(ServletUtils.HEARTBEAT_EVENT))
            heartbeatEvent();
                
        out.println(ServletUtils.OK);                
        out.flush();
        out.close();        
        return result;
    }

    private void responseEvent(String xml) throws IOException {
        String itemResponse = ServletUtils.parseItemResponse(xml);
        String mseq = ServletUtils.parseMseq(xml);
        String type = ServletUtils.parseType(xml);
        String lsid = ServletUtils.parseLsid(xml);
        String date = ServletUtils.formatDateToDateString(new Date());
                
        writeToAuditFile(mseq, type, date, lsid, itemResponse);        
    }

    private void startEvent() {
        // to do
    }

    private void finishEvent() {
        // to do
    }

    private void pauseEvent() {
        // to do
    }

    private void heartbeatEvent() {
        // to do
    }

    // Mseq Type    Datetime    lsid    encrypted request/response
    private void writeToAuditFile(String mseq, String type, String date, String lsid, String itemResponse) throws IOException {
        
        String fileName = AUDIT_FOLDER + "audit.txt";
        
        File file = new File(fileName);
        
        boolean exist = file.exists();
        
        FileWriter fileWriter = new FileWriter(file, exist);
        
        String text = mseq + " - " + type + " - " + date + " - " + lsid + " - " + itemResponse + "\n";
        
        
        fileWriter.write(text);
        
        fileWriter.flush();
        fileWriter.close();
        
    }
    

}
