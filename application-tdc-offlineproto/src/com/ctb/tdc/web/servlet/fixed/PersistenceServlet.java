package com.ctb.tdc.web.servlet.fixed;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

import com.ctb.tdc.web.dto.StateVO;
import com.ctb.tdc.web.utils.AuditFile;
import com.ctb.tdc.web.utils.MemoryCache;
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

	public static final String OK = "<OK />";
	public static final String ERROR = "<ERROR />";

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(PersistenceServlet.class);

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
//		do nothing
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
//		String method = request.getParameter("method"); // this line use with test.html
		String method = null; // this line is for release
		if ((method != null) && (! method.equals(ServletUtils.NONE_METHOD))) {
			String xml = ServletUtils.buildPersistenceParameters(request, method);
			handleEvent(request, response, method, xml);
		}
		else {
			doGet(request, response);
		}
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
		handleEvent(request, response, method, xml);
	}

	/**
	 * The handleEvent method of the servlet. <br>
	 *
	 * call the method based on each event, return result response xml to client
	 *
	 * @param HttpServletResponse response
	 * @param String method
	 * @param String xml
	 * @throws IOException
	 */
	private void handleEvent(HttpServletRequest request, HttpServletResponse response, String method, String xml) throws IOException {
		String result = this.OK;
		String inxml = request.getParameter("requestXML");

		if (method.equals("login"))
			result = getLoginXML(request);
		else if (method.equals("save") || method.equals("feedback") || method.equals("uploadAuditFile") || method.equals("writeToAuditFile"))
			result = this.OK;
		else
			result = this.ERROR;

		response.setContentType("text/xml");
		PrintWriter out = response.getWriter();
		out.println(result);
		out.flush();
		out.close();
	}

	public String getLoginXML(HttpServletRequest request)
	{
		String user = ServletUtils.getUser(request);
		if (user != null && user.equals("accommodated")) {
			return "<tmssvc_response method=\"login_response\">" +
			"<login_response restart_number=\"0\" restart_flag=\"false\" lsid=\"33696:numerate73\">" +
			"<status status_code=\"200\"/>" +
			"<testing_session_data>" +
			"<cmi.core student_name=\"Student, Accommodated Twenty-One\" student_id=\"84792\"/>" +
			"<lms.student.accommodations rest_break=\"true\" untimed=\"false\" screen_reader=\"true\" magnifier=\"false\" calculator=\"true\">" +
			"<stereotype_style font_magnification=\"1.5\" font_color=\"0x000000\" bgcolor=\"0xFFCCCC\" stereotype=\"directions\"/>" +
			"<stereotype_style font_magnification=\"1.5\" font_color=\"0x000000\" bgcolor=\"0xFFCCCC\" stereotype=\"stimulus\"/>" +
			"<stereotype_style font_magnification=\"1.5\" font_color=\"0x000000\" bgcolor=\"0xFFCCCC\" stereotype=\"stem\"/>" +
			"<stereotype_style font_magnification=\"1.5\" font_color=\"0xFFFFFF\" bgcolor=\"0x000080\" stereotype=\"answerArea\"/>" +
			"</lms.student.accommodations>" +
			"</testing_session_data>" +
			"<manifest title=\"Regression Test\"><sco cmi.core.total_time=\"0:0:0\" adsid=\"23102\" item_encryption_key=\"n7673nBJ2n27bB4oAfme7Ugl5VV42g8\" asmt_encryption_key=\"326\" asmt_hash=\"CC9E7044B9834A6F9942B0F02430318D\" title=\"Regression Test\" sco_unit_type=\"SUBTEST \" sco_unit_question_number_offset=\"0\" sco_duration_minutes=\"0\" id=\"24224\" cmi.core.entry=\"ab-initio\" force_logout=\"false\"/>" +
			"<feedback id=\"STUDENT_FEEDBACK\"/>" +
			"<terminator id=\"SEE_YOU_LATER\"/>" +
			"</manifest>" +
			"</login_response>" +
			"</tmssvc_response>";
		}
		else if (user != null) {
			return "<tmssvc_response method=\"login_response\"><login_response restart_number=\"0\" restart_flag=\"false\" lsid=\"1:swirl50346\"><status status_code=\"200\"/>" +
			"<testing_session_data><cmi.core student_name=\"" + user + "\" student_id=\"84378\" /></testing_session_data>" +
			"<manifest title=\"Mathematics 101\">" +
			"<sco cmi.core.total_time=\"0:0:0\" adsid=\"10\" item_encryption_key=\"n7673nBJ2n27bB4oAfme7Ugl5VV42g8\" asmt_encryption_key=\"1\" asmt_hash=\"1003A05C5AFDD27F24A5F05B627C52E9\"" +
			" title=\"Mathematics 101\" sco_unit_type=\"SUBTEST\" sco_unit_question_number_offset=\"0\" sco_duration_minutes=\"100\" id=\"24105\" cmi.core.entry=\"ab-initio\" force_logout=\"false\" />" +
			"<terminator id=\"SEE_YOU_LATER\" /> </manifest> </login_response> </tmssvc_response>";		
		}
		else {
			return "<tmssvc_response method=\"login_response\"><login_response restart_number=\"0\" restart_flag=\"false\" lsid=\"1:swirl50346\"><status status_code=\"200\"/>" +
			"<testing_session_data><cmi.core student_name=\"Test Student\" student_id=\"84378\" /></testing_session_data>" +
			"<manifest title=\"Mathematics 101\">" +
			"<sco cmi.core.total_time=\"0:0:0\" adsid=\"10\" item_encryption_key=\"n7673nBJ2n27bB4oAfme7Ugl5VV42g8\" asmt_encryption_key=\"1\" asmt_hash=\"1003A05C5AFDD27F24A5F05B627C52E9\"" +
			" title=\"Mathematics 101\" sco_unit_type=\"SUBTEST\" sco_unit_question_number_offset=\"0\" sco_duration_minutes=\"100\" id=\"24105\" cmi.core.entry=\"ab-initio\" force_logout=\"false\" />" +
			"<terminator id=\"SEE_YOU_LATER\" /> </manifest> </login_response> </tmssvc_response>";
		}
	}



	/**
	 * The verifyServletSettings method of the servlet. <br>
	 *
	 * verify if values in tdc.properties are valid
	 */
	private String verifyServletSettings() {
		String errorMessage = ServletUtils.OK;
		if (! ServletUtils.validateServletSettings()) {
//			return error message if values in properties file are invalid
			errorMessage = ServletUtils.getServletSettingsErrorMessage();
		}
		else {
//			properties file are valid, now check for TMS connection
			errorMessage = ServletUtils.httpClientGetStatus();
		}
		return errorMessage;
	}

	/**
	 * The login method of the servlet. <br>
	 *
	 * handle login request from client
	 * request login response xml from TMS
	 * process encryptionKey to memory cache
	 * parse login response xml to determine roster id and restart data (if present)
	 * return login response xml from TMS to client
	 *
	 * @param String xml
	 */
	private String login(String xml) {
		String result = ServletUtils.ERROR;
		try {
//			sent login request to TMS
			result = ServletUtils.httpClientSendRequest(ServletUtils.LOGIN_METHOD, xml);

			if (ServletUtils.isLoginStatusOK(result)) {
//				process encryptionKey to memory cache
				ServletUtils.processContentKeys(result);

//				if file exist handle restart
				String fileName = ServletUtils.buildFileName(xml);
				if (AuditFile.exists(fileName)) {
//					handle restart here in phase 2
				}
				logger.info("Login successfully.");
			}
			else {
				logger.error("TMS returns error in login() : " + result);
			}
		}
		catch (Exception e) {
			logger.error("Exception occured in login() : " + ServletUtils.printStackTrace(e));
			result = ServletUtils.buildXmlErrorMessage("", e.getMessage(), "");
		}
		return result;
	}

	/**
	 * The feedback method of the servlet. <br>
	 *
	 * handle feedback request from client
	 * return feedback response xml from TMS to client
	 *
	 * @param String xml
	 */
	private String feedback(String xml) {
		String result = ServletUtils.ERROR;
		try {
//			sent feedback request to TMS
			result = ServletUtils.httpClientSendRequest(ServletUtils.FEEDBACK_METHOD, xml);
			if (ServletUtils.isStatusOK(result)) {
				logger.info("Get feedback successfully.");
			}
			else {
				logger.error("TMS returns error in feedback() : " + result);
			}
		}
		catch (Exception e) {
			logger.error("Exception occured in feedback() : " + ServletUtils.printStackTrace(e));
			result = ServletUtils.buildXmlErrorMessage("", e.getMessage(), "");
		}
		return result;
	}

	/**
	 * The save method of the servlet. <br>
	 *
	 * verify the acknowledge from TMS, checking based on values settings in tdc.properties
	 * if acknowledge checking failed, return error to client '<ERROR />'
	 * if acknowledge checking passed, write response to audit file (if save response),
	 * return ack to client, send request to TMS, on response from TMS, change ack in memory cache.
	 *
	 * @param HttpServletResponse response
	 * @param String xml
	 * @throws IOException
	 */
	private String save(HttpServletResponse response, String xml) throws IOException {
		response.setContentType("text/xml");
		PrintWriter out = response.getWriter();
		out.println("<OK/>");
		out.flush();
		out.close();
		return ServletUtils.OK;
	}

	/**
	 * The writeToAuditFile method of the servlet. <br>
	 *
	 * write xml model content to audit file
	 *
	 * @param String xml
	 */
	private String writeToAuditFile(String xml) {
		String result = ServletUtils.ERROR;
		String errorMessage = null;

		try {
//			truncate the file if it is bigger than 200 KB before write model content
			String fileName = ServletUtils.buildFileName(xml);
			if (ServletUtils.isFileSizeTooBig(fileName)) {
				logger.info("Audit file is too big (> 200KB), file will be truncated before writing model data.");
				AuditFile.deleteLogger(fileName);
			}

//			write model content to audit file
			AuditFile.log(ServletUtils.createAuditVO(xml, false));

//			sent writeToAuditFile request to TMS
			ServletUtils.httpClientSendRequest(ServletUtils.WRITE_TO_AUDIT_FILE_METHOD, xml);
			result = ServletUtils.OK; // nothing return from TMS
		}
		catch (Exception e) {
			logger.error("Exception occured in writeToAuditFile() : " + ServletUtils.printStackTrace(e));
			errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.writeToAuditFileFailed");
			result = ServletUtils.buildXmlErrorMessage("", errorMessage, "");
		}
		return result;
	}

	/**
	 * The uploadAuditFile method of the servlet. <br>
	 *
	 * upload the audit file to TMS and delete the file from local storage
	 *
	 * @param String xml
	 */
	private String uploadAuditFile(String xml) {
		String result = ServletUtils.ERROR;
		String errorMessage = null;
		int responseCode = HttpStatus.SC_OK;

//		make sure the queue is empty before upload audit file
		String checkQueueClear = waitForQueueToBeClear();
		if (checkQueueClear != null) {
			return checkQueueClear;
		}

		MemoryCache memoryCache = MemoryCache.getInstance();
		if (memoryCache.getSrvSettings().isTmsAuditUpload()) {
//			get the audit file to upload
			String fileName = ServletUtils.buildFileName(xml);
			File file = new File(fileName);

//			get checksum value
			long sumValue = ServletUtils.getChecksum(file);
			if (sumValue == -1L) {
				logger.error("Checksum error.");
				return ServletUtils.ERROR;
			}

//			setup URL parameters
			String tmsURL = ServletUtils.getTmsURLString(ServletUtils.UPLOAD_AUDIT_FILE_METHOD);
			tmsURL += "?" + ServletUtils.XML_PARAM + "=" + xml;
			tmsURL += "&" + ServletUtils.CHECKSUM_PARAM + "=" + sumValue;

			PostMethod filePost = new PostMethod(tmsURL);
			try {
//				set multipart request
				Part[] parts = { new FilePart(ServletUtils.AUDIT_FILE_PARAM, file) };
				filePost.setRequestEntity( new MultipartRequestEntity(parts, filePost.getParams()) );

//				upload the file to TMS
				HttpClientParams clientParams = new HttpClientParams();
				clientParams.setConnectionManagerTimeout(30 * ServletUtils.SECOND); // timeout in 30 seconds
				HttpClient client = new HttpClient(clientParams);

				String proxyHost = ServletUtils.getProxyHost();

				if ((proxyHost != null) && (proxyHost.length() > 0)) {
//					execute with proxy settings
					HostConfiguration hostConfiguration = client.getHostConfiguration();
					int proxyPort = ServletUtils.getProxyPort();
					System.out.println("proxyHost = " + proxyHost + "\nproxyPort = " + proxyPort);
					hostConfiguration.setProxy(proxyHost, proxyPort);
					String username = ServletUtils.getProxyUserName();
					String password = ServletUtils.getProxyPassword();
					UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
					HttpState state = client.getState();
					state.setProxyCredentials(null, proxyHost, credentials);
					responseCode = client.executeMethod(hostConfiguration, filePost);
				}
				else {
//					execute without proxy
					responseCode = client.executeMethod(filePost);
				}

//				delete local file when upload successfully
				if (responseCode == HttpStatus.SC_OK) {
					InputStream isPost = filePost.getResponseBodyAsStream();
					BufferedReader in = new BufferedReader(new InputStreamReader(isPost));
					String inputLine = null;
					String tmsResponse = "";
					while ((inputLine = in.readLine()) != null) {
						tmsResponse += inputLine;
					}
					in.close();
//					if OK return from TMS, delete local file
					if (ServletUtils.isStatusOK(tmsResponse)) {
						logger.info("Upload audit file successfully");
						if (AuditFile.deleteLogger(fileName)) {
							logger.info("Delete audit file successfully");
							result = ServletUtils.OK;
						}
						else {
							logger.error("Failed to delete audit file");
							errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.deleteAuditFileFailed");
							result = ServletUtils.buildXmlErrorMessage("", errorMessage, "");
						}
					}
					else {
						logger.error("Failed to upload audit file, tmsResponse=" + tmsResponse);
						errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.uploadFailed");
						result = ServletUtils.buildXmlErrorMessage("", errorMessage, "");
					}
				}
				else {
					logger.error("Failed to upload audit file, responseCode=" + HttpStatus.getStatusText(responseCode));
					result = ServletUtils.buildXmlErrorMessage("", HttpStatus.getStatusText(responseCode), "");
				}
			}
			catch (Exception e) {
				logger.error("Exception occured in uploadAuditFile() : " + ServletUtils.printStackTrace(e));
				errorMessage = ServletUtils.getErrorMessage("tdc.servlet.error.uploadFailed");
				result = ServletUtils.buildXmlErrorMessage("", errorMessage, "");
			}
			finally {
				filePost.releaseConnection();
			}
		}
		else {
			result = ServletUtils.OK;
		}
		return result;
	}

	/**
	 * The verifyAcknowledge method of the servlet. <br>
	 *
	 * verify if there is an acknowledge message returned from TMS to continue
	 * if there is no ack then try again (tms.ack.messageRetry), each time delay 1 second
	 *
	 * @param MemoryCache memoryCache
	 * @param String lsid
	 * @param String mseq
	 */
	private boolean verifyAcknowledge(MemoryCache memoryCache, String lsid, String mseq) throws InterruptedException {
		int messageRetry = memoryCache.getSrvSettings().getTmsAckMessageRetry();
		boolean pendingState = inPendingState(memoryCache, lsid, mseq);
		while (pendingState && (messageRetry > 0)) {
			logger.info("TmsAckMessageRetry=" + messageRetry);
			messageRetry--;
			Thread.sleep(ServletUtils.SECOND); // delay 1 second and try again
			pendingState = inPendingState(memoryCache, lsid, mseq);
		}
		return (! pendingState);
	}

	/**
	 * The inPendingState method of the servlet. <br>
	 *
	 * verify if there is a pending state entry in the list
	 * the index is computed from 'tms.ack.maxLostMessage' in properties file
	 * mseqIndex = mseqCurrent - tmsAckMaxLostMessage;
	 *
	 * @param MemoryCache memoryCache
	 * @param String lsid
	 * @param String mseq
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
					if (state.getMseq() <= mseqIndex) {
						if (state.getState().equals(StateVO.PENDING_STATE)) {
							pendingState = true;
							break;
						}
					}
				}
			}
		}
		return pendingState;
	}

	public String waitForQueueToBeClear()
	{
		String errorMsg = null;
		MemoryCache memoryCache = MemoryCache.getInstance();
		HashMap stateMap = ( HashMap )memoryCache.getStateMap();
		if ( !stateMap.isEmpty() )
		{
			Set lsids = stateMap.keySet();
			Iterator it = lsids.iterator();
			if ( it.hasNext() )
			{
				String key = ( String )it.next();
				ArrayList states = ( ArrayList )stateMap.get( key );
				int messageRetry = memoryCache.getSrvSettings().getTmsAckMessageRetry();
				try
				{
					while( anyMessageInPending( states ) && ( messageRetry > 0) )
					{
						messageRetry--;
						Thread.sleep( ServletUtils.SECOND );
					}
					if ( anyMessageInPending( states ) )
					{
						errorMsg = ServletUtils.getErrorMessage( "tdc.servlet.error.noAck" );
						logger.error( errorMsg );
						errorMsg = ServletUtils.buildXmlErrorMessage( "", errorMsg, "" );
					}
				}
				catch( Exception e )
				{
					errorMsg = ServletUtils.getErrorMessage( "tdc.servlet.error.noAck" );
					logger.error( errorMsg );
					errorMsg = ServletUtils.buildXmlErrorMessage( "", errorMsg, "" );
				}
			}
		}
		return errorMsg;
	}

	public boolean anyMessageInPending( ArrayList states )
	{
		boolean somethingPending = false;
		if ( states != null )
		{
			for ( int i = 0; i < states.size() && !somethingPending; i++ )
			{
				StateVO state = ( StateVO )states.get( i );
				if ( state.getState().equals( StateVO.PENDING_STATE ) )
					somethingPending = true;
			}
		}
		return somethingPending;
	}

}