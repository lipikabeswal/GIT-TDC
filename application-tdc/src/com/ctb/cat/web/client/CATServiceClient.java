package com.ctb.cat.web.client;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.ctb.cat.web.data.xsd.ItemResponseRequest;
import com.ctb.cat.web.data.xsd.ItemResponseResponse;
import com.ctb.cat.web.data.xsd.ReportSubscoreData;
import com.ctb.cat.web.data.xsd.ResearchReportData;
import com.ctb.cat.web.data.xsd.TestInitializationRequest;
import com.ctb.cat.web.data.xsd.TestInitializationResponse;
import com.ctb.cat.web.service.CATServiceLocator;
import com.ctb.cat.web.service.CATServicePortType;

public class CATServiceClient {

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
	private static String subscoresString;
	
	private static String nextItemId;
	
	private static HashMap<String, CatPriorData> priorMap;
	
	public static HashMap itemIdMap = new HashMap();
	
	public static boolean isStudentStop = false;
	
	private static class CatPriorData {
		public String subtestId;
		public Double ability;
		public String [] priorItems;
	}
	
	static {
		try {
			serviceLocator = new CATServiceLocator();
			URL address = new URL("http://ca17dmhe0008.mhe.mhc:22101/CATWebservice/services/CATService.CATServiceHttpSoap11Endpoint/");
			service = serviceLocator.getCATServiceHttpSoap11Endpoint(address);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static double getAbilityScore() {
		return scaleScore.doubleValue();
	}
	
	public static double getSEM() {
		return sem.doubleValue();
	}
	
	public static String getObjScore() {
		return subscoresString;
	}
	
	public static void processCATLoginElement(org.jdom.Element loginElement) {
		CATServiceClient.priorMap = new HashMap<String, CatPriorData>(10);
		if(loginElement != null) {
			CATServiceClient.testRosterId = loginElement.getAttributeValue("lsid");
			CATServiceClient.studentId = loginElement.getChild("testing_session_data").getChild("cmi.core").getAttributeValue("student_id");
			
			org.jdom.Element catElement = loginElement.getChild("cat_prior_data");
			
			List<Element> subtests = catElement.getChildren();
			Iterator<Element> it = subtests.iterator();
			while(it.hasNext()) {
				CatPriorData data = new CatPriorData();
				
				Element subtest = it.next();
				String subtestId = subtest.getChild("subtest_id").getAttributeValue("value");
				String externalId = subtest.getChild("external_id").getAttributeValue("value");
				String priorAbility = subtest.getChild("prior_ability").getAttributeValue("value");
				
				data.subtestId = subtestId;
				data.ability = Double.parseDouble(priorAbility);
				
				List<Element> items = subtest.getChild("prior_item_history").getChildren();
				Iterator<Element> iit = items.iterator();
				ArrayList<String> priors = new ArrayList<String>();
				while(iit.hasNext()) {
					Element item = iit.next();
					String piid = item.getAttributeValue("item_id");
					String peid = item.getAttributeValue("external_id");
					priors.add(peid);
				}
				
				data.priorItems = (String[]) priors.toArray();
				
				priorMap.put(subtestId, data);
			}
		}
	}
	
	public static String getNextItem() {
		Integer adsitem = (Integer)itemIdMap.get(nextItemId);
		return String.valueOf(adsitem.intValue());
	}
	
	public static void start(String subtestId, String subtestName) throws RemoteException {

		CATServiceClient.isStudentStop = false;
		
		CATServiceClient.subtestId = subtestId;
		CATServiceClient.subtestName = subtestName;
		CATServiceClient.configId = getConfigId();
		
		CatPriorData data = priorMap.get(subtestId);
		
		CATServiceClient.ineligibleItems = data.priorItems;
		CATServiceClient.priorAbility = data.ability;

		TestInitializationRequest request = new TestInitializationRequest(configId, testRosterId + subtestId, studentId, priorAbility, ineligibleItems);
		System.out.println("CAT Service Request: " + request.toString() + "\n\n");
		TestInitializationResponse response = service.initializeTest(request);
		System.out.println("CAT Service Response: " + response.toString() + "\n\n");
		if("OK".equals(response.getStatusCode())) {
			nextItemId = response.getNextItemID();
		} else {
			nextItemId = null;
		}
	}
	
	public static void nextItem(String itemId, String stimulusId, Boolean isFT, Integer itemPosition, Integer rawScore, String itemResponse, Integer timeElapsed) throws RemoteException {
		ItemResponseRequest request = new ItemResponseRequest(configId, testRosterId + subtestId, studentId, Boolean.FALSE, "", isFT, ineligibleItems, itemId, itemPosition, rawScore, itemResponse, stimulusId, timeElapsed);
		System.out.println("CAT Service Request: " + request.toString() + "\n\n");
		ItemResponseResponse response = service.processItemResponse(request);
		System.out.println("CAT Service Response: " + response.toString() + "\n\n");
		if("OK".equals(response.getStatusCode())) {
			processResultData(response.getResearchReportData());
			nextItemId = response.getNextItemID();
		} else {
			nextItemId =  null;
		}
	}
	
	public static void stop(String itemId, String stimulusId, Boolean isFT, Integer itemPosition, Integer rawScore, String itemResponse, Integer timeElapsed, String stopReason) throws RemoteException {
		CATServiceClient.isStudentStop = true;
		
		ItemResponseRequest request = new ItemResponseRequest(configId, testRosterId + subtestId, studentId, Boolean.TRUE, stopReason, isFT, ineligibleItems, itemId, itemPosition, rawScore, itemResponse, stimulusId, timeElapsed);
		System.out.println("CAT Service Request: " + request.toString() + "\n\n");
		ItemResponseResponse response = service.processItemResponse(request);
		System.out.println("CAT Service Response: " + response.toString() + "\n\n");
		if("OK".equals(response.getStatusCode())) {
			processResultData(response.getResearchReportData());
		}
	}
	
	private static void processResultData(ResearchReportData data) {
		if(data != null) {
			rawScore = data.getRawScore();
			scaleScore = data.getScaleScore();
			sem = data.getSem();
			if(data.getSubscoreList() != null) {
				ReportSubscoreData [] subscores =data.getSubscoreList();
				for(int i=0;i<subscores.length;i++) {
					ReportSubscoreData subscore = subscores[i];
					String subscoreString = subscore.getSubscoreCategory() + "," + subscore.getRawSubscore() + "," + subscore.getNumOfItems() + "," + subscore.getSubscore() + ",0," + subscore.getSubscorePerfLevel();
					if(i!=0) subscoreString = "|" + subscoreString;
					subscoresString = subscoresString + subscoreString;
				}
			}
		}
	}
	
	public static String getConfigId() {
		if(subtestName.indexOf("Applied Mathematics") >= 0) {
			return "21";
		} else if(subtestName.indexOf("Language Arts") >= 0) {
			return "22";
		} else if(subtestName.indexOf("Mathematics Computation") >= 0) {
			return "23";
		} else if(subtestName.indexOf("Reading") >= 0) {
			return "24";
		} else {
			System.out.println("!!! No config ID found for subtest: " + subtestName);
			return null;
		}
	}
		
	public static int getTestLength() {
		if(subtestName.indexOf("Applied Mathematics") >= 0) {
			return 25;
		} else if(subtestName.indexOf("Language Arts") >= 0) {
			return 25;
		} else if(subtestName.indexOf("Mathematics Computation") >= 0) {
			return 25;
		} else if(subtestName.indexOf("Reading") >= 0) {
			return 25;
		} else {
			System.out.println("!!! No target length found for subtest: " + subtestName);
			return 0;
		}
	}
	
}
