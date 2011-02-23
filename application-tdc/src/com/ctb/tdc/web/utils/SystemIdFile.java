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

public class SystemIdFile {
static Logger logger = Logger.getLogger(LoadTestRosterFile.class);
	
	public static final String SYSTEMID_FILE_NAME = "systemid";
	public static final String LOAD_TEST_FOLDER = "/data/loadtest/";
	
	public static void setSystemId(String systemId){
		
		String tdcHome = FileUtils.getHome();
		
		
		try{
			FileWriter systemIdFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + SYSTEMID_FILE_NAME);
			BufferedWriter systemIdBr = new BufferedWriter(systemIdFile);
			
			systemIdBr.write(systemId);
			systemIdBr.flush();
			systemIdBr.close();
			systemIdBr.close();
			
		}catch(Exception e){
			logger.error("Exception occured in writing system id file in method setSystemId: " + e.getMessage());
		}
		
	}
	
	public static String getSystemId(){
		
		String tdcHome = FileUtils.getHome();
		String systemId = "";
		
		try{
			FileReader systemIdFile = new FileReader(tdcHome + LOAD_TEST_FOLDER + SYSTEMID_FILE_NAME);
			BufferedReader systemIdBr = new BufferedReader(systemIdFile);
			
			systemId = systemIdBr.readLine();			
			systemIdBr.close();
			systemIdBr.close();
			
		}catch(Exception e){
			logger.error("Exception occured in writing system id file in method setSystemId: " + e.getMessage());
		}
		return systemId;
	}
	public static void clear(){
		
		String tdcHome = FileUtils.getHome();
		
		
		try{
			FileWriter systemIdFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + SYSTEMID_FILE_NAME);
			BufferedWriter systemIdBr = new BufferedWriter(systemIdFile);
			
			systemIdBr.write("");
			systemIdBr.flush();
			systemIdBr.close();
			systemIdBr.close();
			
		}catch(Exception e){
			logger.error("Exception occured in clearing system id file: " + e.getMessage());
		}
		
	}
	
	public static void reset(){
		
		String tdcHome = FileUtils.getHome();
		
		String systemId = getSystemId();
		if (systemId.contains(":")){
			systemId = systemId.substring(0,systemId.indexOf(":"));
		}
		try{
			FileWriter systemIdFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + SYSTEMID_FILE_NAME);
			BufferedWriter systemIdBr = new BufferedWriter(systemIdFile);
			
			systemIdBr.write(systemId);
			systemIdBr.flush();
			systemIdBr.close();
			systemIdBr.close();
			
		}catch(Exception e){
			logger.error("Exception occured in clearing system id file: " + e.getMessage());
		}
		
	}
}
