package com.ctb.tdc.web.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctb.tdc.web.dto.AuditVO;
import com.ctb.tdc.web.dto.StateVO;
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
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occure
     */
    public void init() throws ServletException {
        // do nothing
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
        handleEvent(response, method, xml);         
	}

    /**
     *  handleEvent
     * @param HttpServletResponse response
     * @param String method
     * @param String xml
     *   
     *  handle event   
     * @throws IOException 
     */
    private void handleEvent(HttpServletResponse response, String method, String xml) throws IOException {
        String result = ServletUtils.OK;
        ServletUtils.initMemoryCache();
        
        // call method
        if (method.equals(ServletUtils.LOGIN_METHOD))
            result = login(response, xml);
        else if (method.equals(ServletUtils.SAVE_METHOD))
            result = save(response, xml);        
        else if (method.equals(ServletUtils.FEEDBACK_METHOD))
            result = feedback(response, xml);        
        else if (method.equals(ServletUtils.UPLOAD_AUDIT_FILE_METHOD))
            result = uploadAuditFile(response, xml);
        else if (method.equals(ServletUtils.WRITE_TO_AUDIT_FILE_METHOD))
            result = writeToAuditFile(response, xml);
        else
            result = ServletUtils.UNKNOWN_METHOD_ERROR;    
        
        // return response to client
        ServletUtils.writeResponse(response, result);                        
    }
    
    /**
     *  login
     * @param HttpServletResponse response
     * @param String xml
     *   
     *  request login response xml from TMS   
     *  parse login response xml to determine roster id and restart data (if present)
     *  create audit file if not exists (reconstitute from restart data if restart on different machine??)
     *  write ack to audit file
     *  return login response xml to client
     *  
     */
    private String login(HttpServletResponse response, String xml) {
        String result = ServletUtils.OK;
        try {
            // sent login request to TMS
            result = sendRequest(xml, ServletUtils.LOGIN_METHOD);
            
            // parse response xml for information
            String encryptionKey = ServletUtils.parseEncryptionKey(result);
            String lsid = ServletUtils.parseLsid(result);
            String fileName = AuditFile.buildFileName(lsid);
            
            // save encryptionKey to memory cache
            MemoryCache memoryCache = MemoryCache.getInstance();
            memoryCache.setEncryptionKey(encryptionKey);
            
            // if file exist handle restart  
            if (AuditFile.exists(fileName)) {
                // handle restart here in phase 2
            }
            
            // log RECEIVE_EVENT in audit file
            AuditVO audit = ServletUtils.createAuditVO(fileName, lsid, ServletUtils.NONE, ServletUtils.RECEIVE_EVENT, xml);            
            AuditFile.log(audit);        
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
    private String feedback(HttpServletResponse response, String xml) {
        String result = ServletUtils.OK;
        try {
            // parse request xml for information
            String lsid = ServletUtils.parseLsid(xml);
            String fileName = AuditFile.buildFileName(lsid);
            
            // log RECEIVE_EVENT in audit file
            AuditVO audit = ServletUtils.createAuditVO(fileName, lsid, ServletUtils.NONE, ServletUtils.RECEIVE_EVENT, xml);            
            AuditFile.log(audit);
            
            // sent feedback request to TMS
            result = sendRequest(xml, ServletUtils.FEEDBACK_METHOD);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
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
    private String save(HttpServletResponse response, String xml) {
        String result = ServletUtils.OK;
        try {
            // log RECEIVE_EVENT in audit file
            AuditVO audit = ServletUtils.createAuditVO(xml, ServletUtils.RECEIVE_EVENT);
            String fileName = audit.getFileName();  
            String lsid = audit.getLsid();        
            String mseq = audit.getMseq();    
            MemoryCache memoryCache = MemoryCache.getInstance();

            // check if TMS sent acknowledge before continue
            if (hasAcknowledge(memoryCache, lsid, mseq)) {
                // ok to continue, so first remove all ACK entries
                memoryCache.removeAcknowledgeStates(lsid);
                // send response to client
                AuditFile.log(audit);        
                ServletUtils.writeResponse(response, ServletUtils.OK);       
                // send request to TMS
                if (memoryCache.getSrvSettings().isTmsPersist()) {
                    // set pending state before send request to TMS
                    StateVO state = memoryCache.setPendingState(lsid, mseq);
                    // send request to TMS
                    result = sendRequest(xml, ServletUtils.SAVE_METHOD);
                    // set acknowledge state after return from TMS                    
                    memoryCache.setAcknowledgeState(state);                       
                    // log ACTKNOWLEDGE_EVENT in audit file when TMS return
                    audit = ServletUtils.createAuditVO(fileName, lsid, ServletUtils.NONE, ServletUtils.ACTKNOWLEDGE_EVENT, result);            
                    AuditFile.log(audit);
                }
            }
            else {                
                result = ServletUtils.ACK_ERROR; // fail to continue
            }            
        } 
        catch (Exception e) {
            e.printStackTrace();
        }       
        return result;
    }

    /**
     * uploadAuditFile
     * @param HttpServletResponse response
     * @param String xml
     * 
     * upload the audit file to TMS and delete the file from local storage
     * 
     */
    private String uploadAuditFile(HttpServletResponse response, String xml) {
        String result = ServletUtils.OK;
        try {
            MemoryCache memoryCache = MemoryCache.getInstance();
            if (memoryCache.getSrvSettings().isTmsAuditUpload()) {            
                // upload audit file to TMS
                result = ServletUtils.uploadAuditFile(xml);            
                if (result.equals(ServletUtils.OK)) {
                    // delete local file when upload successfully
                    String lsid = ServletUtils.parseLsid(xml);
                    String fileName = AuditFile.buildFileName(lsid);            
                    AuditFile.deleteLogger(fileName);
                }                 
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * writeToAuditFile
     * @param HttpServletResponse response
     * @param String xml
     * 
     * write xml content to audit file
     * 
     */
    private String writeToAuditFile(HttpServletResponse response, String xml) {
        String result = ServletUtils.OK;
        try {
            // log RECEIVE_EVENT in audit file
            AuditVO audit = ServletUtils.createAuditVO(xml, ServletUtils.RECEIVE_EVENT);
            AuditFile.log(audit);
            
            // write stuff to audit file
            result = sendRequest(xml, ServletUtils.WRITE_TO_AUDIT_FILE_METHOD);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * sendRequest
     * @param String xml
     * @param String method
     * 
     * Connect to TMS and send request contain a xml
     * 
     */
    private String sendRequest(String xml, String method) {
        String result = "";
        try {
            // get TMS url to make connection
            URL tmsURL = ServletUtils.getTmsURL(method);
            URLConnection tmsConnection = tmsURL.openConnection();
            tmsConnection.setDoOutput(true);
            PrintWriter out = new PrintWriter(tmsConnection.getOutputStream());

            // send to TMS
            out.println(ServletUtils.METHOD_PARAM + "=" + method + "&" + ServletUtils.XML_PARAM + "=" + xml);
            out.close();

            // get response from TMS
            BufferedReader in = new BufferedReader(new InputStreamReader(tmsConnection.getInputStream()));
            String inputLine = null;       
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
    
    /**
     * hasAcknowledge
     * @param MemoryCache memoryCache
     * @param String lsid
     * 
     * verify if there is an acknowledge message returned from TMS to continue
     * if there is no ack then try again (tms.ack.retry), each time delay 1 second 
     * 
     */
    private boolean hasAcknowledge(MemoryCache memoryCache, String lsid, String mseq) throws InterruptedException {
        int retry = memoryCache.getSrvSettings().getTmsAckRetry();
        boolean pendingState = inPendingState(memoryCache, lsid, mseq);
        while (pendingState && (retry > 0)) {
            retry--;
            Thread.sleep(1000); // delay 1 second and try again
            pendingState = inPendingState(memoryCache, lsid, mseq);
        }            
        return (! pendingState);
    }
    
    /**
     * inPendingState
     * @param MemoryCache memoryCache
     * @param String lsid
     * 
     * verify if there is a pending state entry in the list
     * the index is computed from 'tms.ack.inflight' in properties file
     * mseqIndex = mseqCurrent - tmsAckInflight;
     * 
     */
    public boolean inPendingState(MemoryCache memoryCache, String lsid, String mseq) {  
        boolean pendingState = false;
        boolean isTmsAckRequired = memoryCache.getSrvSettings().isTmsAckRequired();
        int tmsAckInflight = memoryCache.getSrvSettings().getTmsAckInflight();
        
        if (isTmsAckRequired) {
            ArrayList states = (ArrayList)memoryCache.getStateMap().get(lsid);
            if (states != null) {
                int mseqCurrent = Integer.parseInt(mseq);
                int mseqIndex = mseqCurrent - tmsAckInflight;
                for (int i=0 ; i<states.size() ; i++) {
                    StateVO state = (StateVO)states.get(i);
                    if (state.getMseq() == mseqIndex) {
                        pendingState = state.getState().equals(StateVO.PENDING_STATE);
                    }
                }
            }
        }
        return pendingState;
    }
        
}
