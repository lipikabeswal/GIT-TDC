package com.ctb.tdc.web.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

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
    public static final String AUDIT_FOLDER = "\\data\\audit\\";
    public static final String XML_FOLDER = "\\data\\xmls\\";
    public static final String IMAGE_FOLDER = "\\data\\images\\";

    // logger
    public static HashMap loggerMap = new HashMap();

    private AuditFile() 
    {
        super();
    }

    public static synchronized org.apache.log4j.Category getLogger( String filePath_ ) throws Exception
    {
        org.apache.log4j.Category logger = null;
        if ( !loggerMap.containsKey( filePath_ ) )
        {
	        logger = org.apache.log4j.Category.getInstance( AuditFile.class );
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
	        loggerMap.put( filePath_, logger );
        }
        else
            logger = ( org.apache.log4j.Category )loggerMap.get( filePath_ );
        return logger;
    }
    
    public static void log( AuditVO aAuditVO ) throws Exception
    {
        org.apache.log4j.Category logger = getLogger( aAuditVO.getFileName() );
        logger.log( Priority.INFO, aAuditVO.toString() );
    }
    
    public static String buildFileName(String lsid) {
        lsid = lsid.replace(':', '_');        
        String tdcHome = System.getProperty(TDC_HOME);
        String fileName = tdcHome + AUDIT_FOLDER + lsid + AUDIT_EXTENSION;
        return fileName;
    }    
    
    public static boolean exists(String fileName) {
        File file = new File(fileName);        
        return file.exists();
    }    
}
