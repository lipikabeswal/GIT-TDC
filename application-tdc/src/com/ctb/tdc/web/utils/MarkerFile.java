package com.ctb.tdc.web.utils;
import java.io.File;
import java.io.IOException;

public class MarkerFile {

	public static final String MARKER_FILE = "markerfile";
	public static final String LOAD_TEST_FOLDER = "/data/loadtest/";
	
	public static void create(){
		
		String tdcHome = FileUtils.getHome();
		File markerFile = new File(tdcHome + LOAD_TEST_FOLDER + MARKER_FILE);
		try{
			markerFile.createNewFile();
		}catch(IOException ioe){
			//do nothing
		}
	}

	public static boolean fileExists(){
		String tdcHome = FileUtils.getHome();
		boolean fileExists = false;
		
		File f = new File(tdcHome + LOAD_TEST_FOLDER + MARKER_FILE);
		if(f.exists()){
			fileExists = true;			
		}		
		return fileExists;
	}
}
