package com.ctb.tdc.web.servlet;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

import com.ctb.tdc.web.dto.StateVO;
import com.ctb.tdc.web.utils.MemoryCache;
import com.ctb.tdc.web.utils.AuditFile;
import com.ctb.tdc.web.utils.ServletUtils;

/**
 * @author Tai_Truong
 * 
 * This supports response persistence and lifecycle events. 
 * New events are only accepted and persisted locally if prior events were acknowledged by 
 * upstream partner, otherwise an error occurs after a suitable wait/retry cycle. 
 * When prior events have been acknowledged by the TMS, new events generated by the client 
 * are acknowledged by the local servlet as soon as they are securely persisted locally, allowing 
 * the user to continue. Delay (and ultimately, in severe cases, interruption) of test thus only 
 * occur if upstream response time exceeds user 'think' time. To ensure that no responses are 
 * lost, an error is returned immediately in that case.
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
     * handleEvent
     * @param HttpServletResponse response
     * @param String method
     * @param String xml
     *   
     * call the method based on each event   
     * return response xml to client
     * @throws IOException 
     */
    private void handleEvent(HttpServletResponse response, String method, String xml) throws IOException {
        String result = ServletUtils.OK;
        ServletUtils.initMemoryCache();
        
        // call method
        if (method.equals(ServletUtils.LOGIN_METHOD))
            result = login(xml);
        else if (method.equals(ServletUtils.SAVE_METHOD))
            result = save(response, xml);        
        else if (method.equals(ServletUtils.FEEDBACK_METHOD))
            result = feedback(xml);        
        else if (method.equals(ServletUtils.UPLOAD_AUDIT_FILE_METHOD))
            result = uploadAuditFile(xml);
        else if (method.equals(ServletUtils.WRITE_TO_AUDIT_FILE_METHOD))
            result = writeToAuditFile(xml);
        else
            result = ServletUtils.ERROR;    
        
        // return response to client
        if (result != null) {
            ServletUtils.writeResponse(response, result);
        }
    }
    
    /**
     * login
     * @param HttpServletResponse response
     * @param String xml
     * 
     *  handle login request from client 
     *  request login response xml from TMS   
     *  process encryptionKey to memory cache
     *  parse login response xml to determine roster id and restart data (if present)
     *  return login response xml from TMS to client
     *  
     */
    private String login(String xml) {
        String result = ServletUtils.OK;
        try {
            // sent login request to TMS
            result = ServletUtils.httpConnectionSendRequest(ServletUtils.LOGIN_METHOD, xml);
            
            // process encryptionKey to memory cache
            ServletUtils.processContentKeys( result );
            
            // if file exist handle restart  
            String fileName = ServletUtils.buildFileName(xml);
            if (AuditFile.exists(fileName)) {
                // handle restart here in phase 2
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            result = ServletUtils.ERROR;
        }
        return result;
    }
    
    /**
     * feedback
     * @param String xml
     * 
     *  handle feedback request from client
     *  return feedback response xml from TMS to client
     *  
     */
    private String feedback(String xml) {
        String result = ServletUtils.OK;
        try {
            // sent feedback request to TMS
            result = ServletUtils.httpConnectionSendRequest(ServletUtils.FEEDBACK_METHOD, xml);
        } 
        catch (Exception e) {
            e.printStackTrace();
            result = ServletUtils.ERROR;
        }
        return result;
    }

    /**
     * save
     * @param HttpServletResponse response
     * @param String xml
     *   
     *  check that last line of audit file is an ack   
     *  if last line is not an ack, return error to client
     *  if last line is an ack, write response to audit file, return ack to client
     *  send request to TMS, on response from TMS, change ack in memory cache.
     *  
     */
    private String save(HttpServletResponse response, String xml) {
        String result = null;
        try {
            // parse request xml for information
            String lsid = ServletUtils.parseLsid(xml);    
            String mseq = ServletUtils.parseMseq(xml); 
            MemoryCache memoryCache = MemoryCache.getInstance();

            // check if TMS sent acknowledge before continue
            if (hasAcknowledge(memoryCache, lsid, mseq)) {
                // ok to continue, so first remove all ACK entries
                memoryCache.removeAcknowledgeStates(lsid);
                
                // log an entry into audit file if save response
                if (ServletUtils.hasResponse(xml)) {
                    AuditFile.log(ServletUtils.createAuditVO(xml, true));
                }

                // return response to client
                ServletUtils.writeResponse(response, ServletUtils.OK);
                
                // send request to TMS
                if (memoryCache.getSrvSettings().isTmsPersist()) {
                    // set pending state before send request to TMS
                    StateVO state = memoryCache.setPendingState(lsid, mseq);
                    // send save request to TMS
                    String tmsResponse = ServletUtils.httpConnectionSendRequest(ServletUtils.SAVE_METHOD, xml);                    
                    // if OK return from TMS, set acknowledge state
                    if (ServletUtils.isStatusOK(tmsResponse)) {
                        memoryCache.setAcknowledgeState(state);
                    }
                }
            }
            else {                
                result = ServletUtils.ERROR; // failed to continue
            }            
        } 
        catch (Exception e) {
            e.printStackTrace();
            result = ServletUtils.ERROR;
        }       
        return result;
    }

    /**
     * writeToAuditFile
     * @param HttpServletResponse response
     * @param String xml
     * 
     * write xml model content to audit file
     * 
     */
    private String writeToAuditFile(String xml) {
        String result = ServletUtils.OK;
        try {
            // write content to audit file
            AuditFile.log(ServletUtils.createAuditVO(xml, false));
            
            // sent writeToAuditFile request to TMS
            ServletUtils.httpConnectionSendRequest(ServletUtils.WRITE_TO_AUDIT_FILE_METHOD, xml);
        } 
        catch (Exception e) {
            e.printStackTrace();
            result = ServletUtils.ERROR;
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
    private String uploadAuditFile(String xml) {
        String result = ServletUtils.OK;
        MemoryCache memoryCache = MemoryCache.getInstance();
        if (memoryCache.getSrvSettings().isTmsAuditUpload()) {
            // parse request for information to build URL
            String testRosterId = ServletUtils.parseTestRosterId(xml);
            String itemSetId = ServletUtils.parseItemSetId(xml);
            String tmsURL = ServletUtils.getTmsURLString(ServletUtils.UPLOAD_AUDIT_FILE_METHOD);
            tmsURL += "?";
            tmsURL += ServletUtils.TEST_ROSTER_ID_PARAM + "=" + testRosterId;
            tmsURL += "&";
            tmsURL += ServletUtils.ITEM_SET_ID_PARAM + "=" + itemSetId;            
            PostMethod filePost = new PostMethod(tmsURL);             
                
            try {
                // get the file
                String fileName = ServletUtils.buildFileName(xml);            
                File file = new File(fileName);
                Part[] parts = { new FilePart(ServletUtils.AUDIT_FILE_PARAM, file) }; 
                filePost.setRequestEntity( new MultipartRequestEntity(parts, filePost.getParams()) );
                
                // upload the file to TMS
                HttpClient client = new HttpClient(); 
                int status = client.executeMethod(filePost);             
                if (status == HttpStatus.SC_OK) {
                    // delete local file when upload successfully
                    AuditFile.deleteLogger(fileName);                    
                }
                else {
                    result = ServletUtils.ERROR;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                result = ServletUtils.ERROR;
            }
            finally {
                filePost.releaseConnection();
            }
        }
        return result;
    }
        
    /**
     * hasAcknowledge
     * @param MemoryCache memoryCache
     * @param String lsid
     * @param String mseq
     * 
     * verify if there is an acknowledge message returned from TMS to continue
     * if there is no ack then try again (tms.ack.retry), each time delay 1 second 
     * 
     */
    private boolean hasAcknowledge(MemoryCache memoryCache, String lsid, String mseq) throws InterruptedException {
        int messageRetry = memoryCache.getSrvSettings().getTmsAckMessageRetry();
        boolean pendingState = inPendingState(memoryCache, lsid, mseq);
        while (pendingState && (messageRetry > 0)) {
            messageRetry--;
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
        int tmsAckMaxLostMessage = memoryCache.getSrvSettings().getTmsAckMaxLostMessage();
        
        if (isTmsAckRequired) {
            ArrayList states = (ArrayList)memoryCache.getStateMap().get(lsid);
            if (states != null) {
                int mseqCurrent = Integer.parseInt(mseq);
                int mseqIndex = mseqCurrent - tmsAckMaxLostMessage;
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
