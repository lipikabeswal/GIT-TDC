package com.ctb.tdc.web.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
        else if (method.equals(ServletUtils.SAVE_METHOD))
            save(response, xml);        
        else if (method.equals(ServletUtils.FEEDBACK_METHOD))
            feedback(response, xml);        
	}

    /**
     *  login
     * @param String xml
     *   
     *  request login response xml from TMS   
     *  parse login response xml to determine roster id and restart data (if present)
     *  create audit file if not exists (reconstitute from restart data if restart on different machine??)
     *  write ack to audit file
     *  return login response xml to client
     *  
     */
    private boolean login(HttpServletResponse response, String xml) {
        try {
            String result = sendRequest(xml);
            String lsid = ServletUtils.parseLsid(result);
            String fileName = FileUtils.buildFileName(lsid);            
            FileUtils.createAuditFile(fileName);        
            AuditVO audit = ServletUtils.buildVOFromType(fileName, ServletUtils.LOGIN_EVENT);
            FileUtils.writeToAuditFile(audit);        
            writeResponse(response, result);            
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("******** exception occured in login() ********");
            return false;
        }
        return true;
    }
    
    /**
     *  feedback
     * @param String xml
     * 
     *  write request to audit file
     *  write ack to audit file
     *  request feedback response xml from TMS     
     *  return feedback response xml to client
     *  
     */
    private boolean feedback(HttpServletResponse response, String xml) {
        try {
            AuditVO audit = ServletUtils.buildVOFromXML(xml);
            FileUtils.writeToAuditFile(audit);        
            String result = sendRequestToTMS(xml, audit);
            writeResponse(response, result);
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("******** exception occured in feedback() ********");
            return false;
        }
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
    private boolean save(HttpServletResponse response, String xml) {
        try {
            AuditVO audit = ServletUtils.buildVOFromXML(xml);
            if (! validToProceed(audit)) {
                writeResponse(response, "<err>No Ack</err>");
                return false;
            }
            FileUtils.writeToAuditFile(audit);
            writeResponse(response, xml);            
            //sendRequestToTMS(xml, audit);  comment out for POC      
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("******** exception occured in save() ********");
        }        
        return true;
    }
 
    private String sendRequestToTMS(String xml, AuditVO audit) throws Exception {
        String fileName = audit.getFileName();        
        AuditVO audit_req = ServletUtils.buildVOFromType(fileName, ServletUtils.TMS_REQUEST_EVENT);
        FileUtils.writeToAuditFile(audit_req);        
        String result = sendRequest(xml);
        AuditVO audit_ack = ServletUtils.buildVOFromType(fileName, ServletUtils.TMS_ACK_EVENT);
        FileUtils.writeToAuditFile(audit_ack);
        return result;        
    }    
    
    private String sendRequest(String xml) {
        //String result = "<login_response lsid='28330:oxygenate4' restart_flag='false' restart_number='0' />";
        
        String result = "";
        try {
            URL tmsURL = new URL(ServletUtils.URL_HOST + ServletUtils.URL_WEBAPP);
            URLConnection tmsConnection = tmsURL.openConnection();
            tmsConnection.setDoOutput(true);
            PrintWriter out = new PrintWriter(tmsConnection.getOutputStream());

            out.println("requestXML=" + xml);
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(tmsConnection.getInputStream()));
            String inputLine;            
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                result += inputLine;
            }
            in.close();
        } 
        catch (MalformedURLException e) {
            e.printStackTrace();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    private void writeResponse(HttpServletResponse response, String xml) throws IOException {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        out.println(xml);            
        out.flush();
        out.close();        
    }

    private boolean validToProceed(AuditVO audit) throws IOException {
        boolean valid = true;
        /*
        String fileName = audit.getFileName();
        String line = FileUtils.getLastLineInFile(fileName);        
        AuditVO vo = ServletUtils.buildVOFromString(fileName, line);
        String type = vo.getType();
        if (type.equals(ServletUtils.TMS_REQUEST_EVENT)) {
            valid = false;
        }
        */
        return valid;
    }
    

}
