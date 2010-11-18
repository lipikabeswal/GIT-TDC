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
			String systemModel = "";
			String physicalMemory = "";
			String processors = "";
			String virtualMemory = "";
			String osVersion = "";
			String line = "";
			
			try{
				Process p1 = Runtime.getRuntime().exec("uname -mn");
				BufferedReader stdInput1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
				
				String hostName = stdInput1.readLine();
				
				stdInput1.close();
				
				Process p2 = Runtime.getRuntime().exec("/usr/sbin/system_profiler");
				BufferedReader stdInput2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
			
				while ((line = stdInput2.readLine()) != null){
					if (line.contains("Model Name:"))
						systemModel = systemModel + line;
					if (line.contains("Memory:"))
						physicalMemory = physicalMemory + line;
					if (line.contains("Processor"))
						processors = processors + line;
					if (line.contains("VRAM"))
						virtualMemory = virtualMemory + line;
					
				}
				
				stdInput2.close();
				
				Process p6 = Runtime.getRuntime().exec("sw_vers");
				BufferedReader stdInput6 = new BufferedReader(new InputStreamReader(p6.getInputStream()));
				
				
				while ((line = stdInput6.readLine()) != null){
					osVersion = osVersion + line;
				}
			
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
				logger.error("Mac Exception captturing system info : " + ServletUtils.printStackTrace(e));
			}
			
		}
		
		if(LoadTestUtils.isLinux()){
			String systemModel = "";
			String physicalMemory = "";
			String processors = "";
			String virtualMemory = " No Information Available";
			String osVersion = "";
			String osName = "";
			String networkCards = "";
			String line = "";
			
			try{
				
				Process p1 = Runtime.getRuntime().exec("uname -n");
				BufferedReader stdInput1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
				
				String hostName = stdInput1.readLine();
				
				stdInput1.close();
				
				
				Process p2 = Runtime.getRuntime().exec("uname -s");
				BufferedReader stdInput2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
			
				while ((line = stdInput2.readLine()) != null){
					osName = osName + line;					
				}				
				stdInput2.close();
				
				Process p3 = Runtime.getRuntime().exec("grep MemTotal /proc/meminfo");
				BufferedReader stdInput3 = new BufferedReader(new InputStreamReader(p3.getInputStream()));
			
				while ((line = stdInput3.readLine()) != null){
					physicalMemory = physicalMemory + line;					
				}				
				stdInput3.close();
				
				Process p4 = Runtime.getRuntime().exec("uname -a");
				BufferedReader stdInput4 = new BufferedReader(new InputStreamReader(p4.getInputStream()));
			
				while ((line = stdInput4.readLine()) != null){
					systemModel = systemModel + line;					
				}				
				stdInput4.close();
				
				Process p5 = Runtime.getRuntime().exec("cat /proc/cpuinfo");
				BufferedReader stdInput5 = new BufferedReader(new InputStreamReader(p5.getInputStream()));
			
				while ((line = stdInput5.readLine()) != null){
					if (line.contains("model name"))
							processors = processors + line;					
				}				
				stdInput5.close();
				
				Process p6 = Runtime.getRuntime().exec("head -n1 /etc/issue");
				BufferedReader stdInput6 = new BufferedReader(new InputStreamReader(p6.getInputStream()));
								
				while ((line = stdInput6.readLine()) != null){
					osVersion = osVersion + line;
				}			
				stdInput6.close();
				
				Process p7 = Runtime.getRuntime().exec("/sbin/ip link show");
				BufferedReader stdInput7 = new BufferedReader(new InputStreamReader(p7.getInputStream()));
								
				while ((line = stdInput7.readLine()) != null){
					networkCards = networkCards + line;
				}			
				stdInput7.close();
				
				resultString = hostName + "|" + osName + "|" + osVersion + "|" + systemModel + "|" + physicalMemory + "|" + virtualMemory +  "|" +processors + "|" + networkCards;
				logger.info("resultString=" + resultString );
				
				resultString = resultString.replace("<","*");
				resultString = resultString.replace(">","*");
				
				FileWriter systemInfoFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + SYSTEM_INFO_FILE);
				BufferedWriter systemInfoBr = new BufferedWriter(systemInfoFile);
				
				systemInfoBr.write(resultString);
				systemInfoBr.flush();
				systemInfoBr.close();
				systemInfoFile.close();
				
			}catch(IOException e){
				error=true;
				logger.error("Linux Exception capturing system info : " + ServletUtils.printStackTrace(e));
			}

		}
		if(!LoadTestUtils.isMacOS() && !LoadTestUtils.isLinux()){
			
			String sysInfoCommand = "systeminfo /FO CSV  /NH";
			try{
				Process p = Runtime.getRuntime().exec(sysInfoCommand);
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				resultString = stdInput.readLine(); //first line is always blank in XP but not in Vista;
				if (resultString != null ){					
					if(!resultString.contains("Windows")){						
						resultString = stdInput.readLine();						
					}
				}else{
					resultString = stdInput.readLine();
				}
				logger.info("final  result String  = " +  resultString);
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
		            
		            //fix to handle null fields in systeminfo file
		            while(systemInfoRecord.contains("||")){
		            	systemInfoRecord = systemInfoRecord.replace("||", "|null|");
		            }
		            		            
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
			}else if(LoadTestUtils.isMacOS() || LoadTestUtils.isLinux()){
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
