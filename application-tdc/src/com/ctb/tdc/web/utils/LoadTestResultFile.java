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

public class LoadTestResultFile {
	
	static Logger logger = Logger.getLogger(LoadTestResultFile.class);
	
	public static final String RESULT_FILE_NAME = "resultfile";
	public static final String LOAD_TEST_FOLDER = "/data/loadtest/";
	
	
	public static void setResultRecord(String resultRecord){
		
		String tdcHome = FileUtils.getHome();
		
		
		try{
			FileWriter resultFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + RESULT_FILE_NAME);
			BufferedWriter resultBr = new BufferedWriter(resultFile);
			
			resultBr.write(resultRecord);
			resultBr.close();
			resultFile.close();
			
		}catch(Exception e){
			logger.error("Exception occured in writing load test roster file in method setRosterRecord: " + e.getMessage());
		}
		
	}
	
	public static void clear(){
		
		String tdcHome = FileUtils.getHome();
		
		
		try{
			FileWriter resultFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + RESULT_FILE_NAME);
			BufferedWriter resultBr = new BufferedWriter(resultFile);
			
			resultBr.write("");
			resultBr.close();
			resultFile.close();
			
		}catch(Exception e){
			logger.error("Exception occured in clearing load test result file: " + e.getMessage());
		}
		
	}
}

