package com.ctb.tdc.web.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.log4j.Category;
import org.apache.log4j.FileAppender;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;

import com.ctb.tdc.web.dto.AuditVO;

/**
 * @author wen-jin_chang
 *
 */
public class AuditFile 
{
    // data files
    public static final String TDC_HOME = "tdc.home";
    public static final String AUDIT_EXTENSION = ".log";
    public static final String AUDIT_FOLDER = "/data/audit/";
    public static final String XML_FOLDER = "/data/xmls/";
    public static final String IMAGE_FOLDER = "/data/images/";
    
    // format:  millis, mseq, <itemId|response>
    private AuditFile() 
    {
        super();
    }

    public static synchronized boolean deleteLogger( String filePath_ ) throws Exception
    {
        Category logger = Category.getInstance( AuditFile.class );
        logger.removeAllAppenders();
        java.io.File file = new java.io.File( filePath_ );
        return file.delete();
    }
    
    public static synchronized org.apache.log4j.Category getLogger( String filePath_ ) throws Exception
    {
        Category logger = Category.getInstance( AuditFile.class );
        logger.removeAllAppenders();
        logger.setAdditivity( false );
        java.io.File f = new java.io.File( filePath_ );
        if ( !f.exists() )
            f.createNewFile();
        FileAppender aFileAppender = new FileAppender();
        aFileAppender.setLayout( new PatternLayout("%m%n") );
        
        aFileAppender.setFile( filePath_ );
        aFileAppender.setImmediateFlush( true );
        aFileAppender.setAppend( true );
        aFileAppender.setWriter( new OutputStreamWriter( new FileOutputStream( filePath_, true )) );
        
        logger.addAppender( aFileAppender );
        return logger;
    }
    
    public static void log( AuditVO audit ) throws Exception
    {
        String fileName = audit.getFileName();
        if (fileName != null) {
            org.apache.log4j.Category logger = getLogger(fileName);
            logger.log( Priority.INFO, audit.toString() );            
        }
    }
    
    public static boolean exists(String fileName) {
        boolean exists = false;
        if (fileName != null) {
            File file = new File(fileName);            
            exists = file.exists();
        }
        return exists;
    }  
      
}
