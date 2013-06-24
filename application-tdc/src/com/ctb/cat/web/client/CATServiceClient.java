package com.ctb.cat.web.client;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom.Element;

import com.ctb.cat.web.data.xsd.ItemResponseRequest;
import com.ctb.cat.web.data.xsd.ItemResponseResponse;
import com.ctb.cat.web.data.xsd.ReportSubscoreData;
import com.ctb.cat.web.data.xsd.ResearchReportData;
import com.ctb.cat.web.data.xsd.TestInitializationRequest;
import com.ctb.cat.web.data.xsd.TestInitializationResponse;
import com.ctb.cat.web.service.CATServiceLocator;
import com.ctb.cat.web.service.CATServicePortType;
import com.ctb.tdc.web.servlet.PersistenceServlet;

public class CATServiceClient {
	static Logger logger = Logger.getLogger(CATServiceClient.class);
	
	private static CATServiceLocator serviceLocator;
	private static CATServicePortType service;
	
	private static String testRosterId;
	private static String studentId;
	private static String subtestId;
	private static String subtestName;
	private static String configId;
	private static String [] ineligibleItems;
	private static Double priorAbility;
	
	private static Double rawScore;
	private static Double scaleScore;
	private static Double sem;
	private static String subscoresString = "";
	
	private static String nextItemId;
	
	private static int itemPosition;
	
	private static HashMap<String, CatPriorData> priorMap;
	
	public static HashMap<String, String> PEIDToADSIdMap = new HashMap<String, String>();
	public static HashMap<String, String> ADSToPEIDIdMap = new HashMap<String, String>();
	
	public static boolean isStudentStop = false;
	
	private static String itemId;
	private static Integer itemRawScore;
	private static String itemResponse;
	private static Integer timeElapsed;
	
	private static class CatPriorData {
		public String subtestId;
		public Double ability;
		public String [] priorItems;
	}
	
	static {
		try {
			logger.setLevel(Level.DEBUG);
			serviceLocator = new CATServiceLocator();
			ResourceBundle rb = ResourceBundle.getBundle("tdc");
			String serviceString = rb.getString("cat.service.url");
			URL address = new URL(serviceString);
			service = serviceLocator.getCATServiceHttpSoap11Endpoint(address);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static double getAbilityScore() {
		return scaleScore==null?0:scaleScore.doubleValue();
	}
	
	public static double getSEM() {
		return sem==null?0:sem.doubleValue();
	}
	
	public static String getObjScore() {
		return subscoresString;
	}
	
	public static void processCATLoginElement(org.jdom.Document loginResponseDocument) {
		logger.debug("CATServiceClient: processCATLoginElement: start");
		org.jdom.Element loginElement = loginResponseDocument.getRootElement().getChild("login_response");
		CATServiceClient.priorMap = new HashMap<String, CatPriorData>(10);
		if(loginElement != null) {
			CATServiceClient.testRosterId = loginElement.getAttributeValue("lsid") + ":" + loginElement.getAttributeValue("restart_number");
			CATServiceClient.studentId = loginElement.getChild("testing_session_data").getChild("cmi.core").getAttributeValue("student_id");
			org.jdom.Element catElement = loginElement.getChild("cat_prior_data");
			if(catElement != null) {
				List<Element> subtests = catElement.getChildren();
				if(subtests != null) {
					logger.debug("CATServiceClient: processCATLoginElement: found prior delivery data");
					Iterator<Element> it = subtests.iterator();
					while(it.hasNext()) {
						CatPriorData data = new CatPriorData();
						Element subtest = it.next();
						String subtestId = subtest.getChild("subtest_id").getAttributeValue("value");
						String externalId = subtest.getChild("external_id").getAttributeValue("value");
						String priorAbility = subtest.getChild("prior_ability").getAttributeValue("value");
						logger.debug("CATServiceClient: processCATLoginElement: prior ability for subtest " + subtestId + " is " + priorAbility);
						data.subtestId = subtestId;
						data.ability = Double.parseDouble(priorAbility);
						List<Element> items = subtest.getChild("prior_item_history").getChildren();
						Iterator<Element> iit = items.iterator();
						ArrayList<String> priors = new ArrayList<String>();
						while(iit.hasNext()) {
							Element item = iit.next();
							String piid = item.getAttributeValue("item_id");
							String peid = item.getAttributeValue("external_id");
							priors.add(piid);
						}
						logger.debug("CATServiceClient: processCATLoginElement: prior item list length for subtest " + subtestId + " is " + priors.size());
						data.priorItems = (String[]) priors.toArray(new String[0]);
						priorMap.put(subtestId, data);
					}
				}
			}
		}
		logger.debug("CATServiceClient: processCATLoginElement: end\n\n");
	}
	
	public static String getNextADSItemId() {
		String adsitem = PEIDToADSIdMap.get(nextItemId);
		logger.debug("CATServiceClient: getNextADSItemId: returning ADSID " + adsitem + " for ID value " + nextItemId + "\n\n");
		return adsitem;
	}
	
	public static void start(String subtestId, String subtestName) throws RemoteException {
		logger.debug("CATServiceClient: start: start");
		
		CATServiceClient.subscoresString = "";
		
		CATServiceClient.isStudentStop = false;
		
		CATServiceClient.subtestId = subtestId;
		CATServiceClient.subtestName = subtestName;
		CATServiceClient.configId = getConfigId();
		
		CatPriorData data = priorMap.get(subtestId);
		if(data != null) {
			CATServiceClient.ineligibleItems = data.priorItems;
			if(data.ability != null && data.ability.doubleValue() != 0) {
				CATServiceClient.priorAbility = data.ability;
			} else {
				CATServiceClient.priorAbility = new Double(getDefaultAbility());
			}
		} else {
			CATServiceClient.ineligibleItems = new String[0];
			CATServiceClient.priorAbility = new Double(getDefaultAbility());
		}

		TestInitializationRequest request = new TestInitializationRequest(configId, testRosterId + ":" + subtestId, studentId, priorAbility, ineligibleItems);
		String ineligibleItemList = "";
		for(int i=0;i<ineligibleItems.length;i++) {
			ineligibleItemList = ineligibleItemList + ineligibleItems[i] + ", ";
		}
		logger.debug("CATServiceClient: start: service init request params: " + configId + ", " + testRosterId + ":" + subtestId + ", " + studentId + ", " + priorAbility + ", [" + ineligibleItemList + "]");
		TestInitializationResponse response = service.initializeTest(request);
		logger.debug("CATServiceClient: start: service init response: status code: " + response.getStatusCode());
		logger.debug("CATServiceClient: start: service init response: status message: " + response.getStatusMessage());
		logger.debug("CATServiceClient: start: service init response: session id: " + response.getSessionID());
		logger.debug("CATServiceClient: start: service init response: starting item id: " + response.getNextItemID());
		if("OK".equals(response.getStatusCode())) {
			CATServiceClient.nextItemId = response.getNextItemID();
			logger.debug("CATServiceClient: start: setting starting item id: " + CATServiceClient.nextItemId);
		} else {
			CATServiceClient.nextItemId = null;
			logger.warn("CATServiceClient: start: no starting item id obtained!!!");
		}
		CATServiceClient.itemPosition = 1;
		logger.debug("CATServiceClient: start: end\n\n");
	}
	
	public static void nextItem(String itemId, Integer itemRawScore, String itemResponse, Integer timeElapsed) throws RemoteException {
		logger.debug("CATServiceClient: nextItem: start");
		
		CATServiceClient.itemPosition += 1;
		
		CATServiceClient.itemId = itemId;
		CATServiceClient.itemRawScore = itemRawScore;
		CATServiceClient.itemResponse = itemResponse;
		CATServiceClient.timeElapsed = timeElapsed;
		
		ItemResponseRequest request = new ItemResponseRequest(testRosterId + ":" + subtestId, Boolean.FALSE, "", (String) ADSToPEIDIdMap.get(itemId), CATServiceClient.itemPosition, itemRawScore, itemResponse, timeElapsed);
		logger.debug("CATServiceClient: nextItem: service nextItem request params: " + testRosterId + ":" + subtestId + ", FALSE, , " + ADSToPEIDIdMap.get(itemId) + ", " + CATServiceClient.itemPosition + ", " + itemRawScore + ", " + itemResponse + ", " + timeElapsed);
		ItemResponseResponse response = service.processItemResponse(request);
		logger.debug("CATServiceClient: nextItem: service nextItem response: status code: " + response.getStatusCode());
		logger.debug("CATServiceClient: nextItem: service nextItem response: status message: " + response.getStatusMessage());
		logger.debug("CATServiceClient: nextItem: service nextItem response: session id: " + response.getSessionID());
		logger.debug("CATServiceClient: nextItem: service nextItem response: next item id: " + response.getNextItemID());
		logger.debug("CATServiceClient: nextItem: service nextItem response: next item position: " + response.getNextItemPosition());
		if("OK".equals(response.getStatusCode())) {
			processResultData(response.getResearchReportData());
			if(response.getNextItemID() != null) {
				nextItemId = response.getNextItemID();
				logger.debug("CATServiceClient: nextItem: setting next item id: " + CATServiceClient.nextItemId);
			} else {
				logger.debug("CATServiceClient: nextItem: no next item id obtained");
			}
		} else {
			nextItemId =  null;
			logger.warn("CATServiceClient: nextItem: no next item id obtained!!!");
		}	
		logger.debug("CATServiceClient: nextItem: end\n\n");
	}
	
	public static void stop(String stopReason, String itemId, Integer itemRawScore, String itemResponse, Integer timeElapsed) throws RemoteException {
		logger.debug("CATServiceClient: stop: start");
		
		CATServiceClient.itemPosition += 1;
		
		CATServiceClient.itemId = itemId;
		CATServiceClient.itemRawScore = itemRawScore;
		CATServiceClient.itemResponse = itemResponse;
		CATServiceClient.timeElapsed = timeElapsed;
		CATServiceClient.isStudentStop = true;
		
		ItemResponseRequest request = new ItemResponseRequest(testRosterId + ":" + subtestId, Boolean.TRUE, stopReason, (String) ADSToPEIDIdMap.get(itemId), CATServiceClient.itemPosition, itemRawScore, itemResponse, timeElapsed);
		logger.debug("CATServiceClient: stop: service nextItem request params: " + testRosterId + ":" + subtestId + ", TRUE," + stopReason + ", " + ADSToPEIDIdMap.get(itemId) + ", " + CATServiceClient.itemPosition + ", " + itemRawScore + ", " + itemResponse + ", " + timeElapsed);
		ItemResponseResponse response = service.processItemResponse(request);
		logger.debug("CATServiceClient: stop: service nextItem response: status code: " + response.getStatusCode());
		logger.debug("CATServiceClient: stop: service nextItem response: status message: " + response.getStatusMessage());
		logger.debug("CATServiceClient: stop: service nextItem response: session id: " + response.getSessionID());
		logger.debug("CATServiceClient: stop: service nextItem response: next item id: " + response.getNextItemID());
		logger.debug("CATServiceClient: stop: service nextItem response: next item position: " + response.getNextItemPosition());
		if("OK".equals(response.getStatusCode())) {
			processResultData(response.getResearchReportData());
		}
		logger.debug("CATServiceClient: nextItem: end\n\n");
	}
	
	private static void processResultData(ResearchReportData data) {
		logger.debug("CATServiceClient: processResultData");
		if(data != null) {
			rawScore = data.getRawScore();
			scaleScore = data.getScaleScore();
			sem = data.getSem();
			logger.debug("CATServiceClient: processResultData: raw: " + rawScore + ", scale: " + scaleScore + ", SEM: " + sem);
			ReportSubscoreData [] subscores = data.getSubscoreList();
			if(subscores != null && subscores.length > 0) {
				for(int i=0;i<subscores.length;i++) {
					ReportSubscoreData subscore = subscores[i];
					if(subscore != null) {
						String subscoreString = subscore.getSubscoreCategory() + "," + subscore.getRawSubscore() + "," + subscore.getNumOfItems() + "," + subscore.getSubscore() + ",0," + subscore.getSubscorePerfLevel();
						if(i!=0) subscoreString = "|" + subscoreString;
						subscoresString = subscoresString + subscoreString;
					}
				}
				logger.debug("CATServiceClient: processResultData: subscores: " + subscoresString);
			}
		}
	}
	
	public static String getConfigId() {
		if(subtestName.indexOf("AM") >= 0) {
			return "21";
		} else if(subtestName.indexOf("LA") >= 0) {
			return "22";
		} else if(subtestName.indexOf("MC") >= 0) {
			return "23";
		} else if(subtestName.indexOf("RD") >= 0) {
			return "24";
		} else {
			logger.error("!!! No config ID found for subtest: " + subtestName);
			return null;
		}
	}
		
	public static int getTestLength() {
		if(subtestName.indexOf("AM") >= 0) {
			return 28;
		} else if(subtestName.indexOf("LA") >= 0) {
			return 25;
		} else if(subtestName.indexOf("MC") >= 0) {
			return 20;
		} else if(subtestName.indexOf("RD") >= 0) {
			return 25;
		} else {
			logger.error("!!! No target length found for subtest: " + subtestName);
			return 0;
		}
	}
	
	public static int getDefaultAbility() {
		if(subtestName.indexOf("AM") >= 0) {
			return 502;
		} else if(subtestName.indexOf("LA") >= 0) {
			return 524;
		} else if(subtestName.indexOf("MC") >= 0) {
			return 506;
		} else if(subtestName.indexOf("RD") >= 0) {
			return 515;
		} else {
			logger.error("!!! No default scale score found for subtest: " + subtestName);
			return 0;
		}
	}
	
}
