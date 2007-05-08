package com.ctb.tdc.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.ctb.tdc.web.exception.DecryptionException;
import com.ctb.tdc.web.exception.HashMismatchException;
import com.stgglobal.util.CryptoLE.Crypto;

/**
 * @author John_Wang
 *
 */
public class ContentFile 
{
    // data files
    public static final String TDC_HOME = "tdc.home";
    public static final String SUBTEST_FILE_EXTENSION = ".eam";
    public static final String ITEM_FILE_EXTENSION = ".ecp";
    public static final String CONTENT_FOLDER = "/data/objectbank/";
    public static final String CONTENT_FOLDER_PATH = System.getProperty(TDC_HOME)+ CONTENT_FOLDER;
    
    private ContentFile() 
    {
        super();
    }

    public static synchronized boolean deleteFile(String filePath) throws Exception
    {
        java.io.File file = new java.io.File( filePath );
        return file.delete();
    }
    
    public static boolean exists(String fileName) {
        boolean exists = false;
        
//        if (fileName != null && fileName.indexOf(CONTENT_FOLDER)<0)
//        	fileName = CONTENT_FOLDER+fileName;
        if (fileName != null) {
            File file = new File(fileName);            
            exists = file.exists();
        }
        return exists;
    } 
    
    public static byte[] readFromFile( String filePath ) throws IOException
    {
        FileInputStream aFileInputStream = new FileInputStream( filePath );
        int size = aFileInputStream.available();
        byte[] buffer = new byte[ size ];
        aFileInputStream.read( buffer );
        aFileInputStream.close();
        return buffer;
    }
    
    public static void writeToFileWithNewName( byte[] data, String originalFilePath )throws Exception
    {
        String newFilePath = originalFilePath + ".xml";
        writeToFile( data, newFilePath );
    }
    
    public static void writeToFile( byte[] data, String filePath ) throws Exception
    {
        File psFile = new File( filePath );
        FileOutputStream psfile = new FileOutputStream( psFile );
        psfile.write( data );
        psfile.flush();
        psfile.close(); 
    }
    
    public static boolean validateHash( String filePath, String hash) throws Exception
    {
        if (!exists(filePath))
        	return false;
    	byte[] buffer = readFromFile( filePath );
        if ( Crypto.checkHash( hash, buffer ))
        	return true;
        else
        	return false;
    }
    
    
    public static byte[] decryptFile( String filePath, String hash, String key ) throws DecryptionException, HashMismatchException 
    {
        byte[] buffer = null; 
        byte[] result = null;
        
        try {
        	buffer = readFromFile( filePath );
        
	        boolean hashChecked = Crypto.checkHash( hash, buffer );
	        
	        if (!hashChecked)
	        	throw new HashMismatchException("Hash doesn't match.");
        	
	        Crypto aCrypto = new Crypto();
	        result = aCrypto.checkHashAndDecrypt( key, hash, buffer, true, false );
	        //writeToFileWithNewName( result, filePath );
	        if (result == null)
	        	throw new Exception ("Decryption result is null.");
        }
        catch (HashMismatchException hme) {
        	throw hme;
        }
        catch (Exception e) {
        	throw new DecryptionException(e.getMessage());
        }
	    return result;
    }
    
    public static String getContentFolderPath() 
    {
//	    String tdcHome = System.getProperty(ContentFile.TDC_HOME);
//	    String filePath = tdcHome + ContentFile.CONTENT_FOLDER;
//	    return filePath;
    	return CONTENT_FOLDER_PATH;
    } 
    
}
