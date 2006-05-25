package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Tai_Truong
 *
 */
public class FileUtils {

    // data files
    public static final String TDC_HOME = "tdc.home";
    public static final String AUDIT_EXTENSION = ".log";
    public static final String AUDIT_FOLDER = "\\data\\audit\\";
    public static final String XML_FOLDER = "\\data\\xmls\\";
    public static final String IMAGE_FOLDER = "\\data\\images\\";
    
     /**
     * write file to out 
     * 
     */
    public static boolean printFileToOutput(String fileName, PrintWriter out) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = null;
        while((line = reader.readLine()) != null) {
            out.println(line);
        }
        return true; 
    }
    
     /**
     * get TDC home 
     * 
     */
    public static String getHome() {
        String tdcHome = System.getProperty(TDC_HOME);
        return tdcHome;
    }

     /**
     * build file name from lsid 
     * 
     */
    public static String buildFileName(String lsid) {
        lsid = lsid.replace(':', '_');        
        String tdcHome = System.getProperty(TDC_HOME);
        String fileName = tdcHome + AUDIT_FOLDER + lsid + AUDIT_EXTENSION;
        return fileName;
    }    
}
