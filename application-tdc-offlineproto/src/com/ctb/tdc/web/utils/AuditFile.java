/*
 * Created on May 17, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.tdc.web.utils;

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
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AuditFile 
{
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
	        String filePath = filePath_;
	        java.io.File f = new java.io.File( filePath_ );
	        if ( !f.exists() )
	            f.createNewFile();
	        FileAppender aFileAppender = new FileAppender();
	        aFileAppender.setLayout( new PatternLayout("[%d{DATE}] %p %m%n") );
	        
	        aFileAppender.setFile( filePath_ );
	        aFileAppender.setImmediateFlush( true );
	        aFileAppender.setAppend( true );
	        aFileAppender.setWriter( new OutputStreamWriter( new FileOutputStream( filePath_, true )) );
	        logger.addAppender( aFileAppender );
	        logger.setPriority( Priority.INFO );
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
}
