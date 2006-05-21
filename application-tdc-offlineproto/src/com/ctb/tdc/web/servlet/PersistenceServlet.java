package com.ctb.tdc.web.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctb.tdc.web.dto.AuditVO;
import com.ctb.tdc.web.dto.StateVO;
import com.ctb.tdc.web.utils.FileUtils;
import com.ctb.tdc.web.utils.MemoryCache;
import com.ctb.tdc.web.utils.AuditFile;
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
        else if (method.equals(ServletUtils.UPLOAD_AUDIT_FILE_METHOD))
            uploadAuditFile(response, xml);        
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
            ServletUtils.initMemoryCache();
            String result = sendRequest(xml, ServletUtils.LOGIN_METHOD);
            String lsid = ServletUtils.parseLsid(result);
            String fileName = AuditFile.buildFileName(lsid);
            if (AuditFile.exists(fileName)) {
                // handle restart here
            }
            AuditVO audit = ServletUtils.createAuditVO(fileName, lsid, ServletUtils.NONE, ServletUtils.RECEIVE_EVENT, xml);            
            AuditFile.log(audit);        
            writeResponse(response, result);            
        } 
        catch (Exception e) {
            e.printStackTrace();
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
            String lsid = ServletUtils.parseLsid(xml);
            String fileName = AuditFile.buildFileName(lsid);
            AuditVO audit = ServletUtils.createAuditVO(fileName, lsid, ServletUtils.NONE, ServletUtils.RECEIVE_EVENT, xml);            
            AuditFile.log(audit);        
            String result = sendRequest(xml, ServletUtils.FEEDBACK_METHOD);
            writeResponse(response, result);
        } 
        catch (Exception e) {
            e.printStackTrace();
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
            AuditVO audit = ServletUtils.createAuditVO(xml, ServletUtils.RECEIVE_EVENT);

            MemoryCache memoryCache = MemoryCache.getInstance();
            boolean pending = memoryCache.pendingState(audit.getLsid());
            if (pending) {
                writeResponse(response, "<error>Wait for TMS to response</error>");
                return false;
            }
            else {
                memoryCache.removeAcknowledgeStates(audit.getLsid());
            }
            
            AuditFile.log(audit);        
            writeResponse(response, xml);       

            StateVO state = memoryCache.setPendingState(audit.getLsid());                       
            String result = null;
            if (memoryCache.getSrvSettings().isTmsPersist())
                result = sendRequest(xml, ServletUtils.SAVE_METHOD);        
            memoryCache.setAcknowledgeState(state);                       
            
            if (result != null) {
                String fileName = audit.getFileName();  
                String lsid = audit.getLsid();        
                audit = ServletUtils.createAuditVO(fileName, lsid, ServletUtils.NONE, ServletUtils.ACTKNOWLEDGE_EVENT, result);            
                AuditFile.log(audit);
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }        
        return true;
    }

    private void writeResponse(HttpServletResponse response, String xml) throws IOException {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        out.println(xml);            
        out.flush();
        out.close();        
    }

    private String sendRequest(String xml, String method) {
        String result = "";
        try {
            URL tmsURL = ServletUtils.getTmsURL(method, xml);
            URLConnection tmsConnection = tmsURL.openConnection();
            tmsConnection.setDoOutput(true);
            PrintWriter out = new PrintWriter(tmsConnection.getOutputStream());

            out.println(ServletUtils.XML_PARAM + "=" + xml);
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(tmsConnection.getInputStream()));
            String inputLine = "";            
            while ((inputLine = in.readLine()) != null) {
                result += inputLine;
            }
            in.close();          
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    private void uploadAuditFile(HttpServletResponse response, String xml) {
        try {
            MemoryCache memoryCache = MemoryCache.getInstance();
            if (! memoryCache.getSrvSettings().isTmsAuditUpload())
                return; 
            
            URL tmsURL = ServletUtils.getTmsURL(ServletUtils.URL_WEBAPP_UPLOAD_AUDIT_FILE, xml);
            
            URLConnection tmsConnection = tmsURL.openConnection();
            tmsConnection.setDoOutput(true);
            PrintWriter out = new PrintWriter(tmsConnection.getOutputStream());            

            String lsid = ServletUtils.parseLsid(xml);
            String fileName = AuditFile.buildFileName(lsid);
            FileUtils.printFileToOutput(fileName, out);             
            out.flush();
            out.close();        
            /*
            BufferedReader in = new BufferedReader(new InputStreamReader(tmsConnection.getInputStream()));
            String inputLine = "";            
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();   
            */
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
