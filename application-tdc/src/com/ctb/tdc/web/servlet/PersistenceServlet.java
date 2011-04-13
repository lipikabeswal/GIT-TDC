package com.ctb.tdc.web.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.log4j.Logger;

import com.ctb.tdc.web.dto.StateVO;
import com.ctb.tdc.web.utils.AuditFile;
import com.ctb.tdc.web.utils.Base64;
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

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(PersistenceServlet.class);
	private static final String osName = System.getProperty("os.name")
			.toLowerCase();
	private static final String windowsPath = "//Application Data//Macromedia//Flash Player//macromedia.com//support//flashplayer//sys//#127.0.0.1";
	private static final String macPath = "//library//preferences//macromedia//Flash Player//macromedia.com//support//flashplayer//sys//#127.0.0.1";
	private static final String unixPath = "//.macromedia//Flash_Player//macromedia.com//support//flashplayer//sys";

	private static HashMap<String, String> audioResponseHash = new HashMap<String, String>();

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
		//String method = request.getParameter("method");   // this line use with test.html
		String method = null; // this line is for release   
		if ((method != null) && (!method.equals(ServletUtils.NONE_METHOD))) {
			String xml = ServletUtils.buildPersistenceParameters(request,
					method);
			//handleEvent(response, method, xml, audioResponseString);
			handleEvent(response, method, xml, request);
		} else {
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
		long startTime = System.currentTimeMillis();
		String xml = ServletUtils.getXml(request);
		handleEvent(response, method, xml, request);
		logger.info("PersistenceServlet: " + method + " took "
				+ (System.currentTimeMillis() - startTime) + "\n");
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
	private void handleEvent(HttpServletResponse response, String method,
			String xml, HttpServletRequest request) throws IOException {
		String result = ServletUtils.OK;
		boolean validSettings = ServletUtils.validateServletSettings();

		// call method to perform an action only if servlet settings is valid
		if (!validSettings)
			result = ServletUtils.getServletSettingsErrorMessage();
		else if (method != null
				&& method.equals(ServletUtils.VERIFY_SETTINGS_METHOD)) {
			result = waitForQueueToBeClear();
			if (result == null)
				result = verifyServletSettings();
		} else if (method != null && method.equals(ServletUtils.LOGIN_METHOD))
			result = login(xml);

		else if (method != null && method.equals(ServletUtils.SAVE_METHOD))
			result = save(response, xml, request);
		else if (method != null && method.equals(ServletUtils.FEEDBACK_METHOD))
			result = feedback(xml);
		else if (method != null
				&& method.equals(ServletUtils.UPLOAD_AUDIT_FILE_METHOD))
			result = uploadAuditFile(xml);
		else if (method != null
				&& method.equals(ServletUtils.WRITE_TO_AUDIT_FILE_METHOD))
			result = writeToAuditFile(xml);
		else
			result = ServletUtils.ERROR;

		// return response to client
		if (result != null) {
			//System.out.println(result);
			String mseq = ServletUtils.parseMseq(xml);
			ServletUtils.writeResponse(response, result, mseq);
		}

	}

	/**
	 * The verifyServletSettings method of the servlet. <br>
	 * 
	 *  verify if values in tdc.properties are valid 
	 */
	private String verifyServletSettings() {
		String errorMessage = ServletUtils.OK;
		if (!ServletUtils.validateServletSettings()) {
			// return error message if values in properties file are invalid
			errorMessage = ServletUtils.getServletSettingsErrorMessage();
		} else {
			// properties file are valid, now check for TMS connection
			errorMessage = ServletUtils.httpClientGetStatus();
		}
		return errorMessage;
	}

	/**
	 * The login method of the servlet. <br>
	 * 
	 *  handle login request from client 
	 *  request login response xml from TMS   
	 *  process encryptionKey to memory cache
	 *  parse login response xml to determine roster id and restart data (if present)
	 *  return login response xml from TMS to client
	 * 
	 * @param String xml
	 */
	private String login(String xml) {
		String result = ServletUtils.ERROR;

		try {
			// sent login request to TMS
			//logger.info("***** login request");
			result = ServletUtils.httpClientSendRequest(
					ServletUtils.LOGIN_METHOD, xml);
			//System.out.println("loginresponse xml "+ result);
			String userHome = System.getProperty("user.home");
			//Performing the FlashCookie Copy operation to avoid the flash security pop-up

			File flashCookieSource = new File(getServletContext().getRealPath(
					"/")
					+ "//settings.sol");
			//System.out.println("flash Cookie Source exists" + flashCookieSource.exists());
			InputStream in = null;
			try {
				in = new FileInputStream(flashCookieSource);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
				e.printStackTrace();
			}

			File flashCookieTarget = null;
			if (osName.indexOf("win") >= 0) {
				System.out.println("Inside If");
				boolean successWindows = new File(userHome + windowsPath)
						.mkdirs();
				flashCookieTarget = new File(userHome + windowsPath
						+ "//settings.sol");
			} else if (osName.indexOf("mac") >= 0) {
				boolean successMac = new File(userHome + macPath).mkdirs();
				flashCookieTarget = new File(userHome + macPath
						+ "//settings.sol");
				if (!flashCookieTarget.exists())
					flashCookieTarget.createNewFile();
			} else {
				boolean successUnix = new File(userHome + unixPath).mkdirs();
				flashCookieTarget = new File(userHome + unixPath
						+ "//settings.sol");
				if (!flashCookieTarget.exists())
					flashCookieTarget.createNewFile();
			}

			OutputStream outFile = null;

			try {
				outFile = new FileOutputStream(flashCookieTarget);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					outFile.write(buf, 0, len);
				}
				in.close();
				outFile.close();
				//	System.out.println("FileCopied");
			} catch (Exception e) {
				e.printStackTrace();
			}
			//System.out.println("*******user Home********" + userHome);

			if (ServletUtils.isLoginStatusOK(result)) {
				// process encryptionKey to memory cache
				ServletUtils.processContentKeys(result);

				// if file exist handle restart  
				String fileName = ServletUtils.buildFileName(xml);
				if (AuditFile.exists(fileName)) {
					// handle restart here in phase 2
				}
				//logger.info("Login successfully.");                
			} else {
				//logger.error("TMS returns error in login() : " + result);   
			}
			
			File speexFile = new File(getServletContext().getRealPath("/")
					+ "//streams//");
				File[] files = speexFile.listFiles();
				System.out.println("files length" + files.length);
				if(files.length>0){
					for (int i = 0; i < files.length; i++) {
						files[i].delete();
					}
				}
		}

		catch (Exception e) {
			logger.error("Exception occured in login() : "
					+ ServletUtils.printStackTrace(e));
			result = ServletUtils.buildXmlErrorMessage("", e.getMessage(), "");
		}
		return result;
	}

	/**
	 * The feedback method of the servlet. <br>
	 * 
	 *  handle feedback request from client
	 *  return feedback response xml from TMS to client
	 *  
	 * @param String xml
	 */
	private String feedback(String xml) {
		String result = ServletUtils.ERROR;
		try {
			MemoryCache memoryCache = MemoryCache.getInstance();
			int TMSRetryCount = memoryCache.getSrvSettings()
					.getTmsMessageRetryCount();
			int TMSRetryInterval = memoryCache.getSrvSettings()
					.getTmsMessageRetryInterval();
			int expansion = memoryCache.getSrvSettings()
					.getTmsMessageRetryExpansionFactor();
			int i = 1;
			while (TMSRetryCount > 0) {
				// sent feedback request to TMS
				//logger.info("***** studentFeedback request");
				result = ServletUtils.httpClientSendRequest(
						ServletUtils.FEEDBACK_METHOD, xml);
				if (ServletUtils.isStatusOK(result)) {
					//logger.info("Get feedback successful.");
					TMSRetryCount = 0;
				} else {
					//logger.error("TMS returns error in feedback() : " + result);    
					if (TMSRetryCount > 1) {
						//logger.error("Retrying message: " + xml);
						Thread
								.sleep(TMSRetryInterval * ServletUtils.SECOND
										* i);
					}
					TMSRetryCount--;
				}
				i = i * expansion;
			}
		} catch (Exception e) {
			logger.error("Exception occured in feedback() : "
					+ ServletUtils.printStackTrace(e));
			result = ServletUtils.buildXmlErrorMessage("", e.getMessage(), "");
		}
		return result;
	}

	/**
	 * The save method of the servlet. <br>
	 *   
	 *  verify the acknowledge from TMS, checking based on values settings in tdc.properties   
	 *  if acknowledge checking failed, return error to client '<ERROR />'
	 *  if acknowledge checking passed, write response to audit file (if save response), 
	 *  return ack to client, send request to TMS, on response from TMS, change ack in memory cache.
	 *  
	 * @param HttpServletResponse response
	 * @param String xml
	 */
	private String save(HttpServletResponse response, String xml,
			HttpServletRequest request) {

		String result = null; // must set to null to prevent sending response twice
		String errorMessage = null;
		// parse request xml for information
		String lsid = ServletUtils.parseLsid(xml);
		String mseq = ServletUtils.parseMseq(xml);
		String rosterId = lsid.split(":")[0];
		String itemId = ServletUtils.parseItemId(xml);
		String itemResponse = ServletUtils.parseResponse(xml);
		String fileName = rosterId + "_" + itemId;
		String decodedXml = null;
		String startAnswerTag = null;
		String endAnswerTag = null;
		String decodedItemResponse = null;
		String replacedItemResponse = null;
		String paramXml = xml;
		boolean containsAudioResponse = false;
		String audioResponseString = null;
		String base64String = null;
		/*
		 //check only for existing session but don't create
		 if(request.getSession(false) != null && request.getSession(false).getAttribute("encodedString") != null){
		 //get the encodedString of audio file generated by SoundRecorder servlet
		 audioResponseString = request.getSession(false).getAttribute("encodedString").toString();
		 if(audioResponseString != null ){
		 System.out.println("audioResponseString 1..");
		 audioResponseHash.put(fileName, audioResponseString);
		 }
		 request.getSession(false).removeAttribute("encodedString");
		 }
		 else if(audioResponseHash.get(fileName) != null && audioResponseString == null){
		 System.out.println("audioResponseHash 2..");
		 audioResponseString = audioResponseHash.get(fileName).toString();
		 }
		 //Audio Response XML Preparation
		 //System.out.println("audio Response String 3" + audioResponseString);
		 */
		try {

			//			if(audioResponseString != null ){
			//System.out.println("inside if of xml replace");
			decodedXml = URLDecoder.decode(xml);
			decodedItemResponse = URLDecoder.decode(itemResponse);
			
			//System.out.println("decodedItemResponse : " + decodedItemResponse);
			
			String[] answersTags = decodedItemResponse.split(fileName);
			//System.out.println("answersTags length : " + answersTags.length);
			//System.out.println("file name : " + fileName);
			if (answersTags.length > 1) {
				//System.out.println("inside if answers tag loop");

				//prepare answer tags
				startAnswerTag = URLEncoder.encode(answersTags[0]);
				endAnswerTag = URLEncoder.encode(answersTags[1]);

				
				base64String = generateBase64String(fileName);

				//System.out.println("base64String : "+base64String);
				
				if(base64String != "") {	
					//prepare answer string with actual audio data
					replacedItemResponse = startAnswerTag + "" + base64String + ""
							+ endAnswerTag;
				}
				else {
					replacedItemResponse = base64String;
				}
				
				//replace the audio file name with actual audio data
				xml = decodedXml.replaceAll(decodedItemResponse,
						replacedItemResponse);

				containsAudioResponse = true;
			}

			boolean isEndSubtest = ServletUtils.isEndSubtest(xml);

			if (isEndSubtest) {
				// make sure the queue is empty before finishing subtest
				String checkQueueClear = waitForQueueToBeClear();
				//delete the audio file list.
				

				//System.out.println("length of file is " + files.length);

				if (checkQueueClear != null) {
					result = ServletUtils.buildXmlErrorMessage("",
							checkQueueClear, "");
					return result;
				}
			}

			MemoryCache memoryCache = MemoryCache.getInstance();

			// check if TMS sent acknowledge before continue
			if (verifyAcknowledge(memoryCache, lsid, mseq, response)) {
				// ncohen: I don't think we need this removeAcks bit,
				// and I need all acks to check for dupe mseqs . . .
				// so I'm commenting it out.
				//memoryCache.removeAcknowledgeStates(lsid);

				// log an entry into audit file
				boolean hasResponse = ServletUtils.hasResponse(xml);
				if (hasResponse) {
					if (containsAudioResponse) {
						//	System.out.println("containsaudioresponse");
						AuditFile.log(ServletUtils.createAuditVO(paramXml,
								hasResponse));
					} else {

						AuditFile.log(ServletUtils.createAuditVO(xml,
								hasResponse));
					}
				}

				StateVO state = null;
				if (!isAcknowledged(memoryCache, lsid, mseq)) {
					state = memoryCache.setPendingState(lsid, mseq,
							ServletUtils.SAVE_METHOD, xml);
					if (!isEndSubtest) {
						new retrier(ServletUtils.SAVE_METHOD, xml, mseq, state)
								.start();
						result = ServletUtils.OK;
					} else {
						System.out.println("inside else of save method");
						result = save(xml, state);
					}
				}
			} else {
				// failed on checking acknowledge from TMS, return error to client
				errorMessage = ServletUtils
						.getErrorMessage("tdc.servlet.error.noAck");
				//logger.error("mseq " + mseq + ": " + errorMessage);
				result = ServletUtils.ERROR; //ServletUtils.buildXmlErrorMessage("", errorMessage, ""); 
			}
		} catch (Exception e) {
			logger.error("mseq " + mseq + ": Exception occured in save() : "
					+ e.getMessage());
			e.printStackTrace();
			errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.noAck");
			result = ServletUtils.buildXmlErrorMessage("", errorMessage, "");
		}
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^result^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ "+  result);
		
		if (fileName != null) {
			if (result == ServletUtils.OK) {
				if (base64String != null) {
					File speexFile = new File(getServletContext().getRealPath(
							"/")
							+ "//streams//" + fileName + ".spx");

					if (speexFile.exists()) {
						speexFile.delete();
					}
				}
			}
		}
		return result;
	}

	private static String save(String xml, StateVO state) throws Exception {
		String result = null;
		MemoryCache memoryCache = MemoryCache.getInstance();
		String lsid = ServletUtils.parseLsid(xml);
		String mseq = ServletUtils.parseMseq(xml);
		boolean isEndSubtest = ServletUtils.isEndSubtest(xml);
		// send request to TMS
		if (memoryCache.getSrvSettings().isTmsPersist()) {
			// message was added to pending list in cache,
			// send save request to TMS
			int TMSRetryCount = memoryCache.getSrvSettings()
					.getTmsMessageRetryCount();
			int TMSRetryInterval = memoryCache.getSrvSettings()
					.getTmsMessageRetryInterval();
			int expansion = memoryCache.getSrvSettings()
					.getTmsMessageRetryExpansionFactor();
			String tmsResponse = "";
			int i = 1;
			while (TMSRetryCount > 0) {
				//logger.info("mseq " + mseq + ": persistence request");
				tmsResponse = ServletUtils.httpClientSendRequest(
						ServletUtils.SAVE_METHOD, xml);

				// if OK return from TMS, set acknowledge state
				if (ServletUtils.isStatusOK(tmsResponse)) {
					memoryCache.setAcknowledgeState(state);
					TMSRetryCount = 0;
				} else {
					//logger.error("mseq " + mseq + ": TMS returns error in save() : " + tmsResponse);
					if (TMSRetryCount > 1) {
						//logger.error("mseq " + mseq + ": Retrying message: " + xml);
						Thread
								.sleep(TMSRetryInterval * ServletUtils.SECOND
										* i);
					}
					TMSRetryCount--;
				}
				i = i * expansion;
			}
			if (isEndSubtest) {
				result = tmsResponse;
			}
		}
		return result;
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
		System.out.println("writeToAuditFile....");
		try {
			// truncate the file if it is bigger than 200 KB before write model content
			String fileName = ServletUtils.buildFileName(xml);
			if (ServletUtils.isFileSizeTooBig(fileName)) {
				//logger.info("Audit file is too big (> 200KB), file will be truncated before writing model data.");
				AuditFile.deleteLogger(fileName);
			}

			// write model content to audit file
			AuditFile.log(ServletUtils.createAuditVO(xml, false));

			// sent writeToAuditFile request to TMS
			//logger.info("***** uploadAuditFile request");
			ServletUtils.httpClientSendRequest(
					ServletUtils.WRITE_TO_AUDIT_FILE_METHOD, xml);
			result = ServletUtils.OK; // nothing return from TMS
		} catch (Exception e) {
			logger.error("Exception occured in writeToAuditFile() : "
					+ ServletUtils.printStackTrace(e));
			errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.writeToAuditFileFailed");
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
		synchronized (ServletUtils.client) {
			String result = ServletUtils.ERROR;
			String errorMessage = null;
			int responseCode = HttpStatus.SC_OK;

			// make sure the queue is empty before upload audit file
			String checkQueueClear = waitForQueueToBeClear();
			if (checkQueueClear != null) {
				return checkQueueClear;
			}

			MemoryCache memoryCache = MemoryCache.getInstance();
			if (memoryCache.getSrvSettings().isTmsAuditUpload()) {
				// get the audit file to upload
				String fileName = ServletUtils.buildFileName(xml);
				File file = new File(fileName);

				if (file.exists()) {
					// get checksum value
					long sumValue = ServletUtils.getChecksum(file);
					if (sumValue == -1L) {
						//logger.error("Checksum error.");
						return ServletUtils.ERROR;
					}

					// setup URL parameters
					String tmsURL = ServletUtils
							.getTmsURLString(ServletUtils.UPLOAD_AUDIT_FILE_METHOD);
					tmsURL += "?" + ServletUtils.XML_PARAM + "=" + xml;
					tmsURL += "&" + ServletUtils.CHECKSUM_PARAM + "="
							+ sumValue;

					PostMethod filePost = new PostMethod(tmsURL);
					try {
						// set multipart request
						Part[] parts = { new FilePart(
								ServletUtils.AUDIT_FILE_PARAM, file) };
						filePost.setRequestEntity(new MultipartRequestEntity(
								parts, filePost.getParams()));

						responseCode = ServletUtils.client
								.executeMethod(filePost);

						// delete local file when upload successfully 
						if (responseCode == HttpStatus.SC_OK) {
							InputStream isPost = filePost
									.getResponseBodyAsStream();
							BufferedReader in = new BufferedReader(
									new InputStreamReader(isPost));
							String inputLine = null;
							String tmsResponse = "";
							while ((inputLine = in.readLine()) != null) {
								tmsResponse += inputLine;
							}
							in.close();
							// if OK return from TMS, delete local file
							if (ServletUtils.isStatusOK(tmsResponse)) {
								//logger.info("Upload audit file successfully");
								if (AuditFile.deleteLogger(fileName)) {
									//logger.info("Delete audit file successfully");
									result = ServletUtils.OK;
								} else {
									//logger.error("Failed to delete audit file");
									errorMessage = ServletUtils
											.getErrorMessage("tdc.servlet.error.deleteAuditFileFailed");
									result = ServletUtils.buildXmlErrorMessage(
											"", errorMessage, "");
								}
							} else {
								//logger.error("Failed to upload audit file, tmsResponse=" + tmsResponse);
								errorMessage = ServletUtils
										.getErrorMessage("tdc.servlet.error.uploadFailed");
								result = ServletUtils.buildXmlErrorMessage("",
										errorMessage, "");
							}
						} else {
							//logger.error("Failed to upload audit file, responseCode=" + HttpStatus.getStatusText(responseCode));
							result = ServletUtils.buildXmlErrorMessage("",
									HttpStatus.getStatusText(responseCode), "");
						}
					} catch (Exception e) {
						logger
								.error("Exception occured in uploadAuditFile() : "
										+ ServletUtils.printStackTrace(e));
						errorMessage = ServletUtils
								.getErrorMessage("tdc.servlet.error.uploadFailed");
						result = ServletUtils.buildXmlErrorMessage("",
								errorMessage, "");
					} finally {
						filePost.releaseConnection();
					}
				} else {
					result = ServletUtils.OK;
				}
			} else {
				// file was already uploaded by another thread
				result = ServletUtils.OK;
			}

			return result;
		}
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
	 * @throws IOException 
	 */
	private boolean verifyAcknowledge(MemoryCache memoryCache, String lsid,
			String mseq, HttpServletResponse response)
			throws InterruptedException {
		boolean pendingState = inPendingState(memoryCache, lsid, mseq, response);
		return (!pendingState);
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
	 * @throws IOException 
	 */
	public boolean inPendingState(MemoryCache memoryCache, String lsid,
			String mseq, HttpServletResponse response) {
		boolean pendingState = false;
		boolean isTmsAckRequired = memoryCache.getSrvSettings()
				.isTmsAckRequired();
		int tmsAckMaxLostMessage = memoryCache.getSrvSettings()
				.getTmsAckMaxLostMessage();

		if (isTmsAckRequired) {
			ArrayList states = (ArrayList) memoryCache.getStateMap().get(lsid);
			if (states != null) {
				int mseqCurrent = Integer.parseInt(mseq);
				int mseqIndex = mseqCurrent - tmsAckMaxLostMessage;
				for (int i = 0; i < states.size(); i++) {
					StateVO state = (StateVO) states.get(i);
					if (state.getMseq() <= mseqIndex) {
						if (state.getState().equals(StateVO.PENDING_STATE)) {
							//logger.warn("Found outstanding unacknowledged message, max in flight threshold exceeded. mseq " + state.getMseq() + ": " + state.getXml());
							pendingState = true;
							new retrier(state.getMethod(), state.getXml(),
									mseq, state).start();
							break;
						}
					}
				}
			}
		}
		return pendingState;
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
	public static boolean isAcknowledged(MemoryCache memoryCache, String lsid,
			String mseq) {
		boolean acknowledged = false;
		boolean isTmsAckRequired = memoryCache.getSrvSettings()
				.isTmsAckRequired();

		if (isTmsAckRequired) {
			ArrayList states = (ArrayList) memoryCache.getStateMap().get(lsid);
			if (states != null) {
				int mseqCurrent = Integer.parseInt(mseq);
				for (int i = 0; i < states.size(); i++) {
					StateVO state = (StateVO) states.get(i);
					if (state.getMseq() == mseqCurrent) {
						if (state.getState().equals(StateVO.ACKNOWLEDGED_STATE)) {
							acknowledged = true;
							break;
						}
					}
				}
			}
		}
		return acknowledged;
	}

	private static class retrier extends Thread {
		private String method;
		private String xml;
		private String mseq;
		private StateVO state;

		public retrier(String method, String xml, String mseq, StateVO state) {
			this.method = method;
			this.xml = xml;
			this.mseq = mseq;
			this.state = state;
		}

		public void run() {
			MemoryCache memoryCache = MemoryCache.getInstance();

			int retryInterval = memoryCache.getSrvSettings()
					.getTmsMessageRetryInterval();
			int waitTime = memoryCache.getSrvSettings()
					.getTmsAckMessageWaitTime();
			int expansion = memoryCache.getSrvSettings()
					.getTmsMessageRetryExpansionFactor();
			long startTime = System.currentTimeMillis();
			long currentTime = startTime;
			boolean timeout = false;

			String tmsResponse = "";
			//
			String lsid = ServletUtils.parseLsid(xml);

			try {
				int i = 1;
				while (!timeout) {
					if (!isAcknowledged(memoryCache, lsid, mseq)) {
						if (i > 1) {
							//logger.info("mseq " + mseq + ": Waited " + ((currentTime - startTime)/ServletUtils.SECOND) + " for TMS response.");
							//logger.info("mseq " + mseq + ": Retrying message: " + xml);
						}
						tmsResponse = ServletUtils.httpClientSendRequest(
								method, xml);
						// if OK return from TMS, set acknowledge state
						if (ServletUtils.isStatusOK(tmsResponse)) {
							memoryCache.setAcknowledgeState(state);
							timeout = true;
						} else {
							Thread.sleep(retryInterval * ServletUtils.SECOND
									* i); // delay 1 second and try again
							currentTime = System.currentTimeMillis();
							timeout = (currentTime - startTime) > (waitTime * ServletUtils.SECOND);
							i = i * expansion;
						}
					} else {
						timeout = true;
					}
				}
			} catch (InterruptedException ie) {
				// do nothing
			}
		}
	}

	public String waitForQueueToBeClear() {
		String errorMsg = null;
		MemoryCache memoryCache = MemoryCache.getInstance();
		HashMap stateMap = (HashMap) memoryCache.getStateMap();

		if (!stateMap.isEmpty()) {
			Set lsids = stateMap.keySet();
			Iterator it = lsids.iterator();
			if (it.hasNext()) {
				String key = (String) it.next();
				ArrayList states = (ArrayList) stateMap.get(key);
				int messageWait = memoryCache.getSrvSettings()
						.getTmsAckMessageWaitTime();
				try {
					while (anyMessageInPending(states) && (messageWait > 0)) {
						messageWait--;
						Thread.sleep(ServletUtils.SECOND);
					}
					if (anyMessageInPending(states)) {
						errorMsg = ServletUtils
								.getErrorMessage("tdc.servlet.error.noAck");
						//logger.error( errorMsg );
						errorMsg = ServletUtils.buildXmlErrorMessage("",
								errorMsg, "");
					}
				} catch (Exception e) {
					errorMsg = ServletUtils
							.getErrorMessage("tdc.servlet.error.noAck");
					logger.error(errorMsg);
					errorMsg = ServletUtils.buildXmlErrorMessage("", errorMsg,
							"");
				}
			}
		}
		return errorMsg;
	}

	public boolean anyMessageInPending(ArrayList states) {
		boolean somethingPending = false;
		if (states != null) {
			for (int i = 0; i < states.size() && !somethingPending; i++) {
				StateVO state = (StateVO) states.get(i);
				if (state.getState().equals(StateVO.PENDING_STATE))
					somethingPending = true;
			}
		}
		return somethingPending;
	}

	/***
	 * generateBase64String() helps in generating the base64 encoded String from 
	 * the Audio File
	 * 
	 * 
	 * @return
	 */
	private String generateBase64String(String fileName) {
	
	//System.out.println("file passed in generateBase64String : " +  fileName);
		String base64EncodedString = "";
		try {
			File file = new File(getServletContext().getRealPath("/")
					+ "//streams//" + fileName + ".spx");
			
			if (!file.exists()) {
				
				return base64EncodedString;//file does not exist so return empty string
			}

			FileInputStream fis = new FileInputStream(file);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			try {
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum); //Initial run so no doubt here it's 0
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			bos.close();
			fis.close();

			byte[] bytes = bos.toByteArray();
			base64EncodedString = Base64.encode(bytes);

		} catch (Exception e) {
			logger.error("Base64String Not Generated");
			e.printStackTrace();
		}
		return base64EncodedString;

	}

}
