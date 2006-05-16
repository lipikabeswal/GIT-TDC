package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.ctb.tdc.web.dto.AuditVO;

public class FileUtils {

    // data files
    public static final String TDC_HOME = "tdc.home";
    public static final String AUDIT_DEFAULT_FILENAME = "audit.log";
    public static final String AUDIT_EXTENSION = ".log";
    public static final String AUDIT_FOLDER = "/data/audit/";
    public static final String XML_FOLDER = "/data/xmls/";
    public static final String IMAGE_FOLDER = "/data/images/";
    
    // print file to output
    public static boolean printFileToOutput(String fileName, PrintWriter out) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = null;
        while((line = reader.readLine()) != null) {
            out.println(line);
        }
        return true;
    }
    
    // create audit file
    public static boolean createAuditFile(String fileName) throws IOException {
        File file = new File(fileName);        
        boolean exist = file.exists();
        if (exist) {
            // handle restart data???
        }
        return true;
    }
    
    // format = mseq     type    datetime    lsid    response
    public static boolean writeToAuditFile(AuditVO audit) throws IOException {
        String fileFullName = audit.getFileName();
        File file = new File(fileFullName);        
        boolean exist = file.exists();
        
        FileWriter fileWriter = new FileWriter(file, exist);

        String text = null;
        if (! exist) {
            text = "MSEQ \t TYPE \t\t DATE \t\t\t LSID \t\t\t RESPONSE \n";
            fileWriter.write(text);            
            text = "--------------------------------------------------------------------------------- \n";
            fileWriter.write(text);            
        }
        text = audit.getMseq() + " \t " + audit.getType() + " \t " + audit.getDate() + " \t " + audit.getLsid() + " \t " + audit.getResponse() + "\n";               
        fileWriter.write(text);
        
        fileWriter.flush();
        fileWriter.close();  
        return true;
    }

    // get last line
    public static String getLastLineInFile(String fileName) throws IOException {
        String line = null;
        String buff = null;
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        while((buff = reader.readLine()) != null) {
            line = buff;
        }
        return line;
    }

    // get audit file
    public static String getAuditFilePath() {
        String tdcHome = System.getProperty(TDC_HOME) + "/";
        String fileName = tdcHome + AUDIT_FOLDER;
        return fileName;
    }

    public static String buildFileName(String lsid) {
        lsid = lsid.replace(':', '_');        
        String tdcHome = System.getProperty(TDC_HOME);
        String fileName = tdcHome + AUDIT_FOLDER + lsid + AUDIT_EXTENSION;
        return fileName;
    }
    
}
