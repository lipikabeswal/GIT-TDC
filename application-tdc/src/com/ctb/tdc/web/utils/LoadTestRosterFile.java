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

public class LoadTestRosterFile {
	
	static Logger logger = Logger.getLogger(LoadTestRosterFile.class);
	
	public static final String ROSTER_FILE_NAME = "rosterfile";
	public static final String LOAD_TEST_FOLDER = "/data/loadtest/";
	
	
	public static void setRosterRecord(String rosterRecord){
		
		String tdcHome = FileUtils.getHome();
		
		
		try{
			FileWriter rosterFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + ROSTER_FILE_NAME);
			BufferedWriter rosterBr = new BufferedWriter(rosterFile);
			
			rosterBr.write(rosterRecord);
			rosterBr.close();
			rosterFile.close();
			
		}catch(Exception e){
			logger.error("Exception occured in writing load test roster file in method setRosterRecord: " + e.getMessage());
		}
		
	}
	
	public static void clear(){
		
		String tdcHome = FileUtils.getHome();
		
		
		try{
			FileWriter rosterFile = new FileWriter(tdcHome + LOAD_TEST_FOLDER + ROSTER_FILE_NAME);
			BufferedWriter rosterBr = new BufferedWriter(rosterFile);
			
			rosterBr.write("");
			rosterBr.close();
			rosterFile.close();
			
		}catch(Exception e){
			logger.error("Exception occured in clearing load test roster file: " + e.getMessage());
		}
		
	}
}
