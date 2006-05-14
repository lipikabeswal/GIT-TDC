package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.ctb.tdc.web.dto.AuditVO;

public class FileUtils {

    // data folders
    public static final String AUDIT_FOLDER = "../../data/audit/";
    public static final String XML_FOLDER = "../../data/xmls/";
    public static final String IMAGE_FOLDER = "../../data/images/";
    
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
    public static boolean createAuditFile(String xml) throws IOException {
        String fileName = AUDIT_FOLDER + "audit.txt"; // for now        
        File file = new File(fileName);        
        boolean exist = file.exists();
        if (exist) {
            // handle restart data???
        }
        return true;
    }
    
    // format = mseq     type    datetime    lsid    response
    public static boolean writeToAuditFile(AuditVO audit) throws IOException {
        String fileName = AUDIT_FOLDER + "audit.txt"; // for now       
        File file = new File(fileName);        
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
    public static String getLastLineInFile() throws IOException {
        String fileName = AUDIT_FOLDER + "audit.txt";        
        String line = null;
        String buff = null;
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        while((buff = reader.readLine()) != null) {
            line = buff;
        }
        return line;
    }
    
}
