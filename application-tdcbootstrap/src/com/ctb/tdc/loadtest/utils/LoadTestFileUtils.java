package com.ctb.tdc.loadtest.utils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.io.BufferedWriter;


public class LoadTestFileUtils {
	
	public static final String CONTROL_FILE_NAME = "controlfile";
	public static final String RESULT_FILE_NAME = "resultfile";
	public static final String LOAD_TEST_FOLDER = "/data/loadtest/";
	
	
	public static Date getLastConfigRequestTime(String tdcHome){
		
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date configRequestTime = new Date();
        try{
        	configRequestTime = df.parse("2050-01-01 00:00:00");
        	FileReader ctrlFile = new FileReader(tdcHome + LOAD_TEST_FOLDER + CONTROL_FILE_NAME);
	        BufferedReader ctrlBr = new BufferedReader(ctrlFile);
	        
	        String ctrlRecord = ctrlBr.readLine();
	        StringTokenizer st = new StringTokenizer(ctrlRecord, ",");
			String strConfigRequestTime = st.nextToken();
	        
			if (strConfigRequestTime != null)
	        	configRequestTime = df.parse(strConfigRequestTime);
	        
	        ctrlFile.close();
	        ctrlBr.close();
	        
		}catch(Exception e){
			System.out.println("Exception occured in reading load test control file in method getLastConfigRequestTime: " + e.getMessage());
		}finally{
			return configRequestTime;
		}
   
	}
	
	public static Date getLoadTestRunTime(String tdcHome){
				
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date loadTestRunTime = new Date();
		try{
			loadTestRunTime = df.parse("2050-01-01 00:00:00");
			FileReader ctrlFile = new FileReader(tdcHome + LOAD_TEST_FOLDER + CONTROL_FILE_NAME);
	        BufferedReader ctrlBr = new BufferedReader(ctrlFile);
	        
	        String ctrlRecord = ctrlBr.readLine();
	        StringTokenizer st = new StringTokenizer(ctrlRecord, ",");
			String strConfigRequestTime = st.nextToken();
			String strLoadTestRunTime = st.nextToken();
			
			if (strLoadTestRunTime != null)
				loadTestRunTime = df.parse(strLoadTestRunTime);
	        
	        ctrlFile.close();
	        ctrlBr.close();
	        
		}catch(Exception e){
			System.out.println("Exception occured in reading load test control file in method getLoadTestRunTime: " + e.getMessage());
		}finally{
			return loadTestRunTime;
		}
   
	}
	
	public static void setConfigRequestTime(String tdcHome){
		
		Date configRequestTime = new Date();
		Date loadTestRunTime = getLoadTestRunTime(tdcHome);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		String strloadTestRunTime = df.format(loadTestRunTime);
		String strConfigRequestTime = df.format(configRequestTime);
		
		String ctrlFileRecord = strConfigRequestTime + "," +  strloadTestRunTime;
		
		try{
			FileWriter ctrlFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + CONTROL_FILE_NAME);
			BufferedWriter ctrlBr = new BufferedWriter(ctrlFile);
			
			ctrlBr.write(ctrlFileRecord);
			ctrlBr.close();
			ctrlFile.close();
			
		}catch(Exception e){
			System.out.println("Exception occured in writing load test control file in method setConfigRequestTime: " + e.getMessage());
		}		
	}
			
}
