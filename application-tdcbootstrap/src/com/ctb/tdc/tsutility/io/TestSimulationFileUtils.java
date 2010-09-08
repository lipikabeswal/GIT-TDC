package com.ctb.tdc.tsutility.io;

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

public class TestSimulationFileUtils {
		
	public static final String CONTROL_FILE_NAME = "controlfile";
	public static final String RESULT_FILE_NAME = "resultfile";
	public static final String LOAD_TEST_FOLDER = "/data/loadtest/";
	
	
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
			
			ctrlBr.close();
			ctrlFile.close();	        
	        
		}catch(Exception e){
			loadTestRunTime = null;
		}finally{
			return loadTestRunTime;
		}   
	}
	
	public static void resetConfigRequestTime(String tdcHome) throws IOException{
		
		
		Date loadTestRunTime = getLoadTestRunTime(tdcHome);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String defaultTime = "2000-01-01 00:00:00";
		
		String strLoadTestRunTime = df.format(loadTestRunTime);
		
		String ctrlFileRecord = defaultTime + "," +  strLoadTestRunTime;
		
		try{
			FileWriter ctrlFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + CONTROL_FILE_NAME);
			BufferedWriter ctrlBr = new BufferedWriter(ctrlFile);
			
			ctrlBr.write(ctrlFileRecord);
			ctrlBr.close();
			ctrlFile.close();
			
		}catch(IOException e){
			IOException ioe = new IOException();
			throw ioe;
		}
		
	}
	
	public static String getResult(String tdcHome){
		String resultRecord = "";
		try{			
			FileReader resultFile = new FileReader(tdcHome + LOAD_TEST_FOLDER + RESULT_FILE_NAME);
	        BufferedReader resultBr = new BufferedReader(resultFile);
	        
	        resultRecord = resultBr.readLine();
	       		
	        
	        resultBr.close();
	        resultFile.close();	        
	        
		}catch(Exception e){
			resultRecord = "";
		}finally{
			return resultRecord;
		}   
	}
}

