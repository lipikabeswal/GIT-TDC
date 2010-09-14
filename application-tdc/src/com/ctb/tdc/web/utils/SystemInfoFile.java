package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.io.BufferedWriter;

import org.apache.log4j.Logger;

import com.ctb.tdc.web.utils.FileUtils;
import com.ctb.tdc.web.utils.LoadTestUtils;
import com.ctb.tdc.web.utils.SystemIdFile;


public class SystemInfoFile {

	static Logger logger = Logger.getLogger(SystemInfoFile.class);
	
	public static final String SYSTEM_INFO_FILE = "systeminfo";
	public static final String LOAD_TEST_FOLDER = "/data/loadtest/";
	
	public static boolean fileExists(){
		String tdcHome = FileUtils.getHome();
		boolean fileExists = false;
		
		File f = new File(tdcHome + LOAD_TEST_FOLDER + SYSTEM_INFO_FILE);
		if(f.exists()){
			fileExists = true;
			System.out.println("system info file exists");
		}		
		return fileExists;
	}
	
	public static boolean captureSystemInfo(){
		
		boolean error = false;
		String tdcHome = FileUtils.getHome();
		String errorString = "";
		String resultString = "";
		
		if(LoadTestUtils.isMacOS()){
						
			try{
				Process p1 = Runtime.getRuntime().exec("uname -mn");
				BufferedReader stdInput1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
				
				String hostName = stdInput1.readLine();
				stdInput1.close();
				
				Process p2 = Runtime.getRuntime().exec("system_profiler|grep \"Model Name:\"");
				BufferedReader stdInput2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
				
				String systemModel = stdInput2.readLine();
				stdInput2.close();
				
				Process p3 = Runtime.getRuntime().exec("system_profiler|grep \"Memory:\"");
				BufferedReader stdInput3 = new BufferedReader(new InputStreamReader(p3.getInputStream()));
				
				String physicalMemory = stdInput3.readLine();
				stdInput3.close();
				
				Process p4 = Runtime.getRuntime().exec("system_profiler|grep \"Processor\"");
				BufferedReader stdInput4 = new BufferedReader(new InputStreamReader(p4.getInputStream()));
				
				String processors = "";
				String line = "";
				while ((line = stdInput4.readLine()) != null){
					processors = processors + line;
				}
				stdInput4.close();
				
				Process p5 = Runtime.getRuntime().exec("system_profiler|grep \"VRAM\"");
				BufferedReader stdInput5 = new BufferedReader(new InputStreamReader(p5.getInputStream()));
				
				String virtualMemory = stdInput5.readLine();
				stdInput5.close();
				
				Process p6 = Runtime.getRuntime().exec("sw_vers -productName");
				BufferedReader stdInput6 = new BufferedReader(new InputStreamReader(p6.getInputStream()));
				
				String osVersion = stdInput6.readLine();
				stdInput6.close();
				
				resultString = hostName + "|" + "MacOS" + "|" + osVersion + "|" + systemModel + "|" + physicalMemory + "|" + virtualMemory +  "|" +processors + "|" + "No network info";

				FileWriter systemInfoFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + SYSTEM_INFO_FILE);
				BufferedWriter systemInfoBr = new BufferedWriter(systemInfoFile);
				
				systemInfoBr.write(resultString);
				systemInfoBr.flush();
				systemInfoBr.close();
				systemInfoFile.close();
				
			}catch(IOException e){
				error=true;
			}
			
		}
		
		if(!LoadTestUtils.isMacOS() && !LoadTestUtils.isLinux()){
			
			String sysInfoCommand = "systeminfo /FO CSV  /NH";
			try{
				Process p = Runtime.getRuntime().exec(sysInfoCommand);
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				resultString = stdInput.readLine(); //first line is always blank so skip;
				resultString = stdInput.readLine();
				FileWriter systemInfoFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + SYSTEM_INFO_FILE);
				BufferedWriter systemInfoBr = new BufferedWriter(systemInfoFile);
				
				//remove quotes and change the field delimiter to pipe sign as there can be embedded commas in the values
				resultString = resultString.replace("\",\"", "|");
				resultString = resultString.replace("\"", "");
				
				systemInfoBr.write(resultString);
				systemInfoBr.flush();
				systemInfoBr.close();
				systemInfoFile.close();																						
			}catch(IOException e){
				error = true;
			}						
			if (!SystemInfoFile.fileExists()){
				error = true;
			}				
		}
		
		return error;
	}
	
	public static String parseSystemInfoFile(){
		
		String systemInfoXML = "<tmssvc_request><upload_systemInfo_request system_id=\"xx\" os_name=\"xx\" os_version=\"xx\" system_model=\"xx\" physical_memory=\"xx\" virtual_memory=\"xx\" processors=\"xx\" network_cards=\"xx\"/></tmssvc_request>";
		String systemInfoRecord = "";
		 String tmp = "";
			 
		String tdcHome = FileUtils.getHome();	
		if (!SystemInfoFile.fileExists()){
			systemInfoXML = null;
		}else{
			if(!LoadTestUtils.isMacOS() && !LoadTestUtils.isLinux()){
				try{
					FileReader systemInfoFile = new FileReader(tdcHome + LOAD_TEST_FOLDER + SYSTEM_INFO_FILE);
		            BufferedReader systemInfoBr = new BufferedReader(systemInfoFile);		            
		            systemInfoRecord = systemInfoBr.readLine(); 
		            
		            StringTokenizer st = new StringTokenizer(systemInfoRecord, "|");
		            
		            String hostName = st.nextToken();		
		            //update system id with host name
		            String systemId = SystemIdFile.getSystemId();
		            if(systemId != null){
		            	if(!systemId.contains(":"))
			            	SystemIdFile.setSystemId(systemId + ":" + hostName);
		            }else{
		            	SystemIdFile.setSystemId(":" + hostName);
		            }
		            
		            
		            String osName = st.nextToken();		            		            
		            String osVersion = st.nextToken();
		            
		            //Skip the fields not required.
		            tmp =  st.nextToken() +  st.nextToken() +  st.nextToken() +  st.nextToken() +  st.nextToken() +  st.nextToken() +  st.nextToken() +  st.nextToken(); 		            
		            
		            String systemModel = st.nextToken() + " " +  st.nextToken() + " " +  st.nextToken();
		            String processors =  st.nextToken();
		            
		            //Skip the fields not required.
		            tmp =  st.nextToken() +  st.nextToken() +  st.nextToken() +  st.nextToken() +  st.nextToken() +  st.nextToken() +  st.nextToken(); 		            
		            
		            String physicalMemory = st.nextToken();
		            
		            //Skip the fields not required.
		            tmp = st.nextToken();
		            
		            String virtualMemory = st.nextToken();
		            
		            //Skip the fields not required.
		            tmp =  st.nextToken() + st.nextToken() +  st.nextToken() +  st.nextToken() +  st.nextToken() +  st.nextToken();
		            
		            String networkCards =  st.nextToken();
		            
		            systemInfoXML = LoadTestUtils.setAttributeValue("system_id",SystemIdFile.getSystemId(),systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("os_name",osName,systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("os_version",osVersion,systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("system_model",systemModel,systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("physical_memory",physicalMemory,systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("virtual_memory",virtualMemory,systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("processors",processors,systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("network_cards",networkCards,systemInfoXML);
		            
				}catch(IOException ioe){
					logger.error("Exception in parsing system info file : " + ServletUtils.printStackTrace(ioe));
				}				
			}else if(LoadTestUtils.isMacOS()){
				try{
					FileReader systemInfoFile = new FileReader(tdcHome + LOAD_TEST_FOLDER + SYSTEM_INFO_FILE);
					BufferedReader systemInfoBr = new BufferedReader(systemInfoFile);		            
		            systemInfoRecord = systemInfoBr.readLine(); 
		            
		            StringTokenizer st = new StringTokenizer(systemInfoRecord, "|");
		            
		            String hostName = st.nextToken();		
		            //update system id with host name
		            String systemId = SystemIdFile.getSystemId();
		            if(systemId != null){
		            	if(!systemId.contains(":"))
			            	SystemIdFile.setSystemId(systemId + ":" + hostName);
		            }else{
		            	SystemIdFile.setSystemId(":" + hostName);
		            }
		            
		            String osName = st.nextToken();		            		            
		            String osVersion = st.nextToken();
		            String systemModel = st.nextToken();
		            String physicalMemory = st.nextToken();		            		            
		            String virtualMemory = st.nextToken();
		            String processors = st.nextToken();
		            String networkCards = st.nextToken();
		            
		            
		            systemInfoXML = LoadTestUtils.setAttributeValue("system_id",SystemIdFile.getSystemId(),systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("os_name",osName,systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("os_version",osVersion,systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("system_model",systemModel,systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("physical_memory",physicalMemory,systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("virtual_memory",virtualMemory,systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("processors",processors,systemInfoXML);
		            systemInfoXML = LoadTestUtils.setAttributeValue("network_cards",networkCards,systemInfoXML);
				}catch(IOException ioe){
					logger.error("Exception in parsing system info file : " + ServletUtils.printStackTrace(ioe));
				}			            
			}
		}		
		return systemInfoXML;
	}
	
	public static void deleteSystemInfoFile(){
		
		String tdcHome = FileUtils.getHome();
		File systemInfoFile = new File(tdcHome + LOAD_TEST_FOLDER + SYSTEM_INFO_FILE);
		systemInfoFile.delete();
	}
}
