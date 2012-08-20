package com.ctb.tdc.web.utils;


import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.zip.Adler32;


public class LoadTestUtils {

    public static final String TDC_HOME = "tdc.home";
    public static final String LOAD_TEST_EXTENSION = "_ld.log";
    public static final String LOAD_TEST_FOLDER = "/data/LoadTest";
    public static final String LOAD_TEST_LOGIN_FILE = "loginRequests.log";
	
    public static String getLoginFileName() {
    	String fullFileName = null;
    	String tdcHome = System.getProperty(LoadTestUtils.TDC_HOME); 
    	
    	fullFileName = tdcHome + LoadTestUtils.LOAD_TEST_FOLDER + LoadTestUtils.LOAD_TEST_LOGIN_FILE;
    	
    	return fullFileName;
    }
    
    public static boolean isMacOS() {
        String os = System.getProperty("os.name");
        if (os == null) 
            os = "";
        return ( os.toLowerCase().indexOf("mac") != -1 );
    }
    
    public static boolean isLinux() {
        String os = System.getProperty("os.name");
        if (os == null) 
            os = "";
        return ( os.toLowerCase().indexOf("linux") != -1 );
    }
    
    /**
	 * construct a file name for Test Harness log file  from xml
	 *
	 */
	public static String buildLoadTestFileName(String xml) {
		String fullFileName = null;
		String lsid = ServletUtils.parseLsid(xml);
		if ((lsid != null) && (!lsid.equals("-"))) {
			String fileName = lsid.replace(':', '_');
			String tdcHome = System.getProperty(LoadTestUtils.TDC_HOME);
			fullFileName = tdcHome + LoadTestUtils.LOAD_TEST_FOLDER + fileName + LoadTestUtils.LOAD_TEST_EXTENSION;
		}
		return fullFileName;
	}
	
	public static void logLoadTestMetrics(String msg, String fileName) {
		
		try{
			FileWriter fstream = new FileWriter(fileName,true);
			BufferedWriter out = new BufferedWriter(fstream);
        
			out.write(msg);
			out.newLine();
			out.close();
		}
		catch(IOException e){
			System.out.println("Error :" + e);
		}
	}
	
	public static String getAttributeValue(String attributeName, String xml) {
  		String result = null;
  		int startIndex = xml.indexOf(attributeName);
  		if (startIndex>=0) {
  			startIndex += attributeName.length();
  			startIndex = xml.indexOf("\"", startIndex);
  			startIndex +=1;
  			int endIndex = xml.indexOf("\"", startIndex);
  			if (endIndex >startIndex)
  				result = xml.substring(startIndex, endIndex);
  		}
  		return result;
   		
  	}
	
 	public static String setAttributeValue(String attributeName, String attributeValue, String xml){
  		
 		String result = xml;
 		int startIndex = xml.indexOf(attributeName);
 		if (startIndex>=0) {
  			startIndex += attributeName.length();
  			startIndex = xml.indexOf("\"", startIndex);
  			startIndex += 1;
  			int endIndex = xml.indexOf("\"", startIndex);
  			if (endIndex >startIndex)
  				result = xml.substring(0, startIndex) + attributeValue + xml.substring(endIndex, xml.length());
  		}
 		return result;
  	}
}
