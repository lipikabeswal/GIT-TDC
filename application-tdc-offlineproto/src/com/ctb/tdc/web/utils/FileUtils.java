package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtils {

    // data folders
    public static final String AUDIT_FOLDER = "../../data/audit/";
    public static final String XML_FOLDER = "../../data/xmls/";
    public static final String IMAGE_FOLDER = "../../data/images/";
    
    // print file to output
    public static boolean printFileToOutput(String fileName, PrintWriter out) {
        boolean result = true;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while((line = reader.readLine()) != null) {
                out.println(line);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    
    // format = mseq     type    datetime    lsid    response
    public static void writeToAuditFile(String mseq, String type, String date, String lsid, String itemResponse) throws IOException {

        String fileName = AUDIT_FOLDER + "audit.txt";        
        File file = new File(fileName);        
        boolean exist = file.exists();
        
        FileWriter fileWriter = new FileWriter(file, exist);

        String text = null;
        if (! exist) {
            //text = "MSEQ         TYPE            DATE                LSID                   RESPONSE \n";
            text = "MSEQ \t TYPE \t\t DATE \t\t\t LSID \t\t\t RESPONSE \n";
            fileWriter.write(text);            
            text = "--------------------------------------------------------------------------------- \n";
            fileWriter.write(text);            
        }
//        text = mseq + "          " + type + "    " + date + "   " + lsid + "           " + itemResponse + "\n";
        text = mseq + " \t " + type + " \t " + date + " \t " + lsid + " \t " + itemResponse + "\n";
                
        fileWriter.write(text);
        
        fileWriter.flush();
        fileWriter.close();  
    }

}
