package com.ctb.tdc.web.utils;

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

import org.apache.log4j.Logger;

import com.ctb.tdc.web.utils.FileUtils;

public class LoadTestCtrlFile {
	
	static Logger logger = Logger.getLogger(LoadTestCtrlFile.class);
	
	public static final String CONTROL_FILE_NAME = "controlfile";
	public static final String LOAD_TEST_FOLDER = "/data/loadtest/";
	
	public static Date getLastConfigRequestTime(){
		
		String tdcHome = FileUtils.getHome();
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
			logger.error("Exception occured in reading load test control file in method getLastConfigRequestTime: " + e.getMessage());
		}finally{
			return configRequestTime;
		}
   
	}
	
	public static Date getLoadTestRunTime(){
		
		String tdcHome = FileUtils.getHome();
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
			logger.error("Exception occured in reading load test control file in method getLoadTestRunTime: " + e.getMessage());
		}finally{
			return loadTestRunTime;
		}
   
	}
	
	public static void setConfigRequestTime(Date configRequestTime){
		
		String tdcHome = FileUtils.getHome();
		Date loadTestRunTime = getLoadTestRunTime();
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
			logger.error("Exception occured in writing load test control file in method setConfigRequestTime: " + e.getMessage());
		}		
	}
	
	public static void setLoadTestTime(Date loadTestTime){
		
		String tdcHome = FileUtils.getHome();
		Date configRequestTime = getLastConfigRequestTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		String strloadTestRunTime = df.format(loadTestTime);
		String strConfigRequestTime = df.format(configRequestTime);
		
		String ctrlFileRecord = strConfigRequestTime + "," +  strloadTestRunTime;
		
		try{
			FileWriter ctrlFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + CONTROL_FILE_NAME);
			BufferedWriter ctrlBr = new BufferedWriter(ctrlFile);
			
			ctrlBr.write(ctrlFileRecord);
			ctrlBr.close();
			ctrlFile.close();
			
		}catch(Exception e){
			logger.error("Exception occured in writing load test control file in method setLoadTestTime: " + e.getMessage());
		}		
	}
	
	public static void resetLoadTestTime(){
		
		String tdcHome = FileUtils.getHome();
		Date configRequestTime = getLastConfigRequestTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String defaultTime = "2050-01-01 00:00:00";
		
		String strConfigRequestTime = df.format(configRequestTime);
		
		String ctrlFileRecord = strConfigRequestTime + "," +  defaultTime;
		
		try{
			FileWriter ctrlFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + CONTROL_FILE_NAME);
			BufferedWriter ctrlBr = new BufferedWriter(ctrlFile);
			
			ctrlBr.write(ctrlFileRecord);
			ctrlBr.close();
			ctrlFile.close();
			
		}catch(Exception e){
			logger.error("Exception occured in writing load test control file in method resetLoadTestTime: " + e.getMessage());
		}
		
	}
}
