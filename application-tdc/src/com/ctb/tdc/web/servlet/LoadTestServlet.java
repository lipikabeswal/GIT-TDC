package com.ctb.tdc.web.servlet;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Date;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

import com.ctb.dex.utility.DExCrypto;
import com.ctb.tdc.web.dto.StateVO;
import com.ctb.tdc.web.utils.AuditFile;
import com.ctb.tdc.web.utils.MemoryCache;
import com.ctb.tdc.web.utils.ServletUtils;
import com.ctb.tdc.web.utils.LoadTestUtils;
import com.ctb.tdc.web.utils.LoadTestCtrlFile;
import com.ctb.tdc.web.utils.FileUtils;
import com.ctb.tdc.web.utils.LoadTestRosterFile;
import com.ctb.tdc.web.utils.LoadTestResultFile;
import com.ctb.tdc.web.utils.SystemInfoFile;
import com.ctb.tdc.web.utils.SystemIdFile;
import com.ctb.tdc.web.dto.LoadTestSettings;
import com.ctb.tdc.web.utils.MarkerFile;

/** 
 * @author Ayan_Bandopadhyay
 * 
 * This servlet is used to detect the load test configuration on the server and also to 
 * run the load test as scheduled 
 */
public class LoadTestServlet extends HttpServlet{
	
	static Logger logger = Logger.getLogger(LoadTestServlet.class);
	public static final String LOAD_TEST_PROPERTIES = "loadtest";
	private LoadTestSettings loadTestSettings;
	//Changes for defect 65267
	private String networkServiceUtility = "No";
	/**
	 * Constructor of the object.
	 */
	public LoadTestServlet() {
		super();
	}

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occure
     */
    public void init() throws ServletException {
        //do nothing
    }
        
	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    
		 
		 doGet(request, response);            
		
	 }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {    
		
		handleEvent(request,response);    // Changes for defect 65267
	}
	
	private void initLoadTestSettings(){
		ResourceBundle rbLoadTest;       
	    try{
	        rbLoadTest = ResourceBundle.getBundle(LOAD_TEST_PROPERTIES);
	        loadTestSettings = new LoadTestSettings(rbLoadTest);
	    }catch(MissingResourceException mre){
	    	logger.error("Exception occured in initializing load test settings : " + mre.getMessage());
	        loadTestSettings = new LoadTestSettings();
	    }
	}
	private void handleEvent(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String result = "<OK />";
		String systemInfoXML = "";
		 //START Changes for defect 65267
		
		this.networkServiceUtility = request.getParameter("NetworkUitlity") != null ? request.getParameter("NetworkUitlity") : "No";
		System.out.println("networkServiceUtility..........." + this.networkServiceUtility);
		//END
		if(!MarkerFile.fileExists()){
			
			boolean systemInfoError = false;
			if(!SystemInfoFile.fileExists()){
				systemInfoError = SystemInfoFile.captureSystemInfo();
			}			
			if (!systemInfoError){
				systemInfoXML = SystemInfoFile.parseSystemInfoFile();
			}
			if(systemInfoXML != null){
				String responseXML = uploadSystemInfoRequest(systemInfoXML);
			}
		}
		
		initLoadTestSettings();
		
		boolean runLoad = getLoadTestConfig();
		
		if(!runLoad){
			logger.info("***** load test no run");
			
		}else{
			result = runLoadTest();
		}
		
		if (result != null) {
            ServletUtils.writeResponse(response, result);
        }
	}
		
	private String uploadSystemInfoRequest(String requestXML){
		
		String responseXML = "";
		boolean validSettings = ServletUtils.validateServletSettings();                
        
        // call method to perform an action only if servlet settings is valid
        if (! validSettings){        	
        	responseXML = ServletUtils.getServletSettingsErrorMessage();
        }else{        	
        	try {
                // sent load test request to TMS
            	logger.info("***** upload system info request");
            	
            	responseXML = ServletUtils.httpClientSendRequest(ServletUtils.UPLOAD_SYSTEM_INFO_METHOD, requestXML);
            	
            	if (!responseXML.contains("OK")){
            		//SystemInfoFile.deleteSystemInfoFile(); //delete the file so that the upload request is made next time this servlet is called
            		
            	}
            	else{
            		MarkerFile.create(); //create a marker file to indicate that the system info data was successfully uploaded
            	}
            } 
            catch (Exception e) {
                logger.error("Exception occured in load test config request : " + ServletUtils.printStackTrace(e));
                responseXML = ServletUtils.buildXmlErrorMessage("", e.getMessage(), "");
                SystemInfoFile.deleteSystemInfoFile(); //delete the file so that the upload request is made next time this servlet is called
               
            }
        }    	            
        return responseXML;
	}
	private boolean getLoadTestConfig(){
	
		boolean runLoad = false;
		
		Date loadTestRunTime = LoadTestCtrlFile.getLoadTestRunTime(); 
		Date currentTime = new Date();
		
		if (currentTime.compareTo(loadTestRunTime) > 0){
			runLoad = true;
			LoadTestCtrlFile.resetLoadTestTime();
			
		}else{
			
			Date lastConfigRequestTime = LoadTestCtrlFile.getLastConfigRequestTime();
			
			Calendar cConfigRequestTime = Calendar.getInstance();
			cConfigRequestTime.setTime(lastConfigRequestTime);
			
			Calendar calCurrTime = Calendar.getInstance();
			
			long diff = (calCurrTime.getTimeInMillis() -  cConfigRequestTime.getTimeInMillis())/1000;
			
			if (diff >= loadTestSettings.getTmsRequestInterval() ){
				LoadTestCtrlFile.setConfigRequestTime(currentTime);
				String loadTestConfigXML = sendLoadTestConfigRequest();
				
				String runFlag = LoadTestUtils.getAttributeValue("status",loadTestConfigXML);
				
				if (runFlag != null){
					
					if (runFlag.equals("R")){
						
						try{
							String runDate = LoadTestUtils.getAttributeValue("run_date", loadTestConfigXML);
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date loadTestRunDate = df.parse(runDate);
							LoadTestCtrlFile.setLoadTestTime(loadTestRunDate);
							
							String rosterId = LoadTestUtils.getAttributeValue("roster_id", loadTestConfigXML);
							String loginId = LoadTestUtils.getAttributeValue("login_id", loadTestConfigXML);
							String accessCode = LoadTestUtils.getAttributeValue("access_code", loadTestConfigXML);
							String password = LoadTestUtils.getAttributeValue("password", loadTestConfigXML);
							String rosterRecord = rosterId + "," + accessCode + "," + loginId + "," + password;
							
							LoadTestRosterFile.setRosterRecord(rosterRecord);
							
							
						}catch(Exception e){
							logger.error("Exception occured in parsing load test config response xml : " + ServletUtils.printStackTrace(e));
						}
					}else{
						
						LoadTestRosterFile.clear();
					}
				}else{
					
					LoadTestRosterFile.clear();
				}
			}
		}					
		return runLoad;
	}
		
	private String sendLoadTestConfigRequest(){
		
		int responseCode = HttpStatus.SC_OK;
		String responseXML = ServletUtils.ERROR;
		//Changes for Defect 65267
		String requestXML = "<tmssvc_request><runLoad_request system_id=\"xx\" system_time=\"xx\" network_service=\"xx\"/></tmssvc_request>";
		//END
		String systemId = SystemIdFile.getSystemId();
		
		String method = "get_load_test_config";
				
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String systemDate = dateFormat.format(date);
		
        boolean validSettings = ServletUtils.validateServletSettings();
        
        requestXML = LoadTestUtils.setAttributeValue("system_id",systemId,requestXML);
        requestXML = LoadTestUtils.setAttributeValue("system_time",systemDate,requestXML);
         //START Changes for defect 65267
        requestXML = LoadTestUtils.setAttributeValue("network_service", this.networkServiceUtility, requestXML);
        //END
        // call method to perform an action only if servlet settings is valid
        if (! validSettings){
        	
        	responseXML = ServletUtils.getServletSettingsErrorMessage();
        }else{
        	
        	try {
                // sent load test request to TMS
            	logger.info("***** load test request");
            	
            	responseXML = ServletUtils.httpClientSendRequest(ServletUtils.LOAD_TEST_METHOD, requestXML);
            	
            } 
            catch (Exception e) {
                logger.error("Exception occured in load test config request : " + ServletUtils.printStackTrace(e));
                responseXML = ServletUtils.buildXmlErrorMessage("", e.getMessage(), ""); 
            }
        }    	            
        return responseXML;
	}
	
	private String runLoadTest(){
		
		String persistenceServlet = "http://127.0.0.1:12347/servlet/PersistenceServlet.do";
		String contentServlet = "http://127.0.0.1:12347/servlet/ContentServlet.do";
		
		String result =  ServletUtils.OK;
		String testHarnessFolder = "/data/loadtest/";
		String tdcHome = FileUtils.getHome();
		String xml = "";
		String rosterRecord = "";
		boolean success = false;
		String method = "";
		String servletUrl = "";
						
		try{
			FileReader rosterFile = new FileReader(tdcHome + testHarnessFolder + "rosterfile");
	        BufferedReader rosterBr = new BufferedReader(rosterFile);

	        while ((rosterRecord = rosterBr.readLine()) != null){
	        	
	        	Integer avgResponseTime = 0;
	    		Integer maxResponseTime = 0;
	    		Integer minResponseTime = 999999;
	    		Integer successCount = 0;
	    		Integer failureCount = 0;
	    		Integer totalResponseTime = 0;
	    		Long requestStartTime = new Long(0);
	    		Long requestEndTime = new Long(0);
	    			    		
		  		StringTokenizer st = new StringTokenizer(rosterRecord, ",");
		  		
		  		String testRosterId = st.nextToken();
		  		String accessCode = st.nextToken();
		  		String loginId = st.nextToken();
		  		String password = st.nextToken();
		  		Integer requestCounter = 0;
		  		
	        	//FileReader requestFile = new FileReader(tdcHome + testHarnessFolder + "requestfile");
	            //BufferedReader requestBr = new BufferedReader(requestFile);
	    			        
		        //while((xml = requestBr.readLine()) != null){
		  		File file = new File(tdcHome + testHarnessFolder + "requestfile");
    			int ch;
    			StringBuffer strContent = new StringBuffer("");
    			FileInputStream fin = null; 
    			fin = new FileInputStream(file);
      			while( (ch = fin.read()) != -1){
    			
        			if (ch != 42){
        				strContent.append((char)ch);
        				
        			}else{
        			    
        			    String encryptedRecord = strContent.toString();
        			    try{
        			    	xml = DExCrypto.decryptUsingPassPhrase(encryptedRecord);
        			    }catch(Exception e){
        			    	logger.error("Exception occured in decrypting request xml " + ServletUtils.printStackTrace(e));
        			    	break;
        			    }
        			    
        			    strContent = new StringBuffer("");
			        	
        			    Integer retryCount	= 0;
			        	System.out.println("Request counter = " + requestCounter.toString());
			        	//create post method with url based on method		    		
			    		if ((method = LoadTestUtils.getAttributeValue("method",xml)) == null)
			    			method = ServletUtils.LOGIN_METHOD;
			    		else if (method.equals("save_testing_session_data"))
			    			method = ServletUtils.SAVE_METHOD;
			    		else if (method.equals("write_to_audit_file"))
			    			method = ServletUtils.WRITE_TO_AUDIT_FILE_METHOD;
			    		else if (method.equals("upload_audit_file"))
			    			method = ServletUtils.UPLOAD_AUDIT_FILE_METHOD;	
			    		
			    		if (method.equals("login")){
			    			xml = LoadTestUtils.setAttributeValue("user_name",loginId,xml);
			    			xml = LoadTestUtils.setAttributeValue("password",password,xml);
			    			xml = LoadTestUtils.setAttributeValue("access_code",accessCode,xml);
			    		}
			    		else if (method.equals("save")|| method.equals("writeToAuditFile") 
			    				|| method.equals("uploadAuditFile")){
			    			String lsid = testRosterId + ":" + accessCode;
			    			xml = LoadTestUtils.setAttributeValue("lsid",lsid,xml);
			    		}
			    				    		
			    		if (method.equals("getSubtest") || method.equals("downloadItem") || 
			    				method.equals("getItem") || method.equals("getImage")){
			    			
			    			servletUrl = contentServlet;
			    			try{
			    			Thread.sleep(loadTestSettings.getContentRequestInterval() * 1000);
			    			}
			    			catch(InterruptedException e){
			    				logger.error("Exception occured in thread sleep: " + ServletUtils.printStackTrace(e));
			    			}
			    		}		    			
			    		else{
			    			
			    			servletUrl = persistenceServlet;
			    			try{
			    			Thread.sleep(loadTestSettings.getPersistenceRequestInterval() * 1000);
			    			}
			    			catch(InterruptedException e){
			    				logger.error("Exception occured in thread sleep: " + ServletUtils.printStackTrace(e));
			    			}		    			
			    		}		    				    		 		    				    		
			    		while(!success){
			    			retryCount +=1;
			    			requestCounter +=1;
				    		requestStartTime = System.currentTimeMillis();
				    		success = sendRequest(servletUrl,xml,method);
				    		requestEndTime = System.currentTimeMillis();
				    		
				    		Long longResponseTime = (requestEndTime - requestStartTime);
							Integer responseTime = Integer.valueOf(longResponseTime.intValue());
							totalResponseTime += responseTime;
							if(responseTime > maxResponseTime)
								maxResponseTime = responseTime;
							if(responseTime < minResponseTime)
								minResponseTime = responseTime;
							avgResponseTime = totalResponseTime/requestCounter;
							if (success){
								successCount +=1;						
							}else{
								failureCount +=1;							
								try{
									Thread.sleep(10 * 1000);
								}catch(InterruptedException e){
				    				logger.error("Exception occured in thread sleep: " + ServletUtils.printStackTrace(e));
				    			}
							}
				    		if (retryCount > 2){
				    			break;			    			
				    		}			    					    		
			    		}		    		
			    		if (!success){
			    			result =  ServletUtils.ERROR;
			    			break;
			    		}	
			    		
			    		//re-initialize retry variables
			    		retryCount =0;
			    		success = false;
      				}
		        } //end request file loop
      			fin.close();
		        
		        uploadStatistics(testRosterId, maxResponseTime, minResponseTime, avgResponseTime, failureCount, successCount);
		        
	        }//end roster file loop
	        rosterBr.close();
	        rosterFile.close();
		}catch(IOException ioe){
			logger.error("Request or Roster File IOException occured in load test request : " + ServletUtils.printStackTrace(ioe));
		}		
		return result;
	}
	
	
	private void uploadStatistics(String testRosterId, Integer maxResponseTime, Integer minResponseTime, Integer avgResponseTime, Integer failureCount, Integer successCount ){
		
		
		int responseCode = HttpStatus.SC_OK;
		String systemId = SystemIdFile.getSystemId();
		
		String resultRecord = "Maximum Response Time = " + maxResponseTime.toString() + " Minimum Response Time = " + minResponseTime.toString() + "  Average Response Time = " + avgResponseTime.toString();
		LoadTestResultFile.setResultRecord(resultRecord);
		
		String responseXML = ServletUtils.OK;				
		String requestXML ="<tmssvc_request><upload_statistics_request system_id=\"xx\" roster_id=\"xx\" max_response_time=\"xx\" min_response_time=\"xx\" avg_response_time=\"xx\" failure_count=\"xx\" success_count=\"xx\"/></tmssvc_request>"; 

		requestXML = LoadTestUtils.setAttributeValue("system_id",systemId,requestXML);
		requestXML = LoadTestUtils.setAttributeValue("roster_id",testRosterId,requestXML);
		requestXML = LoadTestUtils.setAttributeValue("max_response_time",maxResponseTime.toString(), requestXML);
		requestXML = LoadTestUtils.setAttributeValue("min_response_time",minResponseTime.toString(), requestXML);
		requestXML = LoadTestUtils.setAttributeValue("avg_response_time",avgResponseTime.toString(), requestXML);
		requestXML = LoadTestUtils.setAttributeValue("failure_count",failureCount.toString(), requestXML);
		requestXML = LoadTestUtils.setAttributeValue("success_count",successCount.toString(), requestXML);		
		
		boolean validSettings = ServletUtils.validateServletSettings();
		if (! validSettings)
        	responseXML = ServletUtils.getServletSettingsErrorMessage();
        else{
        	try {
                // sent load test request to TMS
            	
            	responseXML = ServletUtils.httpClientSendRequest(ServletUtils.UPLOAD_STATISTICS_METHOD, requestXML);
                          
            }catch(Exception e){
            	logger.error("exception in seding upload statistics request : " + ServletUtils.printStackTrace(e));
            	responseXML = ServletUtils.buildXmlErrorMessage("", e.getMessage(), ""); 
            }
        }
	}
	
	private boolean sendRequest(String servletUrl, String xml, String method){
		
		String result = "";
		int responseCode = HttpStatus.SC_OK;
		boolean success = false;
		PostMethod post = new PostMethod(servletUrl);
		//	setup parameters
		NameValuePair[] params = { new NameValuePair("requestXML", xml),
				new NameValuePair("method", method)};
		post.setRequestBody(params);
	
		//send request to local servlet
		try {
			HttpClientParams clientParams = new HttpClientParams();
			clientParams.setConnectionManagerTimeout(5 * 1000); // timeout in 5 seconds
			HttpClient client = new HttpClient(clientParams);					
				
			responseCode = client.executeMethod(post);	
			
			if (responseCode == HttpStatus.SC_OK) {
				success = true;
				InputStream isPost = post.getResponseBodyAsStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(isPost));
				String inputLine = null;
				result = "";
				while ((inputLine = in.readLine()) != null) {
					result += inputLine;
				}
				in.close();
				if(result.contains("ERROR"))
				{
					success = false;
				}					
			}
			else {
				result = "Error " +  HttpStatus.getStatusText(responseCode);
				success = false;
				logger.info("***** load test request failed **" + xml);
			}
		}
		catch (Exception e) {
			result = "Error " + e.getMessage();
			success = false;
			logger.error("Exception occured in sending load test request : " + ServletUtils.printStackTrace(e));
		}
		finally {
			post.releaseConnection();				
		}	
		return success;
	}

}