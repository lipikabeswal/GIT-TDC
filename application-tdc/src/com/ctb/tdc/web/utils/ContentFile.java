package com.ctb.tdc.web.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

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
        BufferedInputStream aFileInputStream = new BufferedInputStream(new FileInputStream( filePath ));
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
    
    public static void writeToFile( byte[] data, String filePath ) throws IOException
    {
        File psFile = new File( filePath );
        BufferedOutputStream psfile = new BufferedOutputStream(new FileOutputStream( psFile ));
        psfile.write( data );
        psfile.flush();
        psfile.close(); 
    }
    
    public static boolean validateHash( String filePath, String hash) throws Exception
    {
        if (!exists(filePath))
        	return false;
    	byte[] buffer = readFromFile( filePath );
    	
        if ( Crypto.checkHash( hash, buffer )) {
        	//Fix for 60 days content deletion 
        	File file = new File(filePath);            
            file.setLastModified(new Date().getTime());
        	return true;
        }
        	
        else {
        	//System.out.println("check: " + hash + "  actual: " + Crypto.generateHash(buffer));
        	return false;
        } 	
    }
    
    
    public static byte[] decryptFile( String filePath, String hash, String key ) throws DecryptionException, HashMismatchException 
    {
        byte[] buffer = null; 
        byte[] result = null;
        
        try {
        	buffer = readFromFile( filePath );
        
	        //boolean hashChecked = Crypto.checkHash( hash, buffer );
	        
	        //if (!hashChecked)
	        //	throw new HashMismatchException("Hash doesn't match.");
        	
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
    
    public static void main(String[] argv) {
    	try {
    		System.out.println(argv[0] + " " + argv[1]);
    		byte[] infile = readFromFile(argv[0]);
    		//String hash = AeSimpleMD5.MD5(new String(infile));
    		String hash = Crypto.generateHash(infile);
    		System.out.println(hash);
    		byte[] bytes = decryptFile(argv[0], hash, argv[1]);
    		FileOutputStream fos = new FileOutputStream("c:\\itemout.txt");
    		fos.write(bytes);
    		System.out.println(new String(bytes));
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static class AeSimpleMD5 {
    	 
    	private static String convertToHex(byte[] data) {
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < data.length; i++) {
            	int halfbyte = (data[i] >>> 4) & 0x0F;
            	int two_halfs = 0;
            	do {
    	        	if ((0 <= halfbyte) && (halfbyte <= 9))
    	                buf.append((char) ('0' + halfbyte));
    	            else
    	            	buf.append((char) ('a' + (halfbyte - 10)));
    	        	halfbyte = data[i] & 0x0F;
            	} while(two_halfs++ < 1);
            }
            return buf.toString();
        }
     
    	public static String MD5(String text) 
    	throws NoSuchAlgorithmException, UnsupportedEncodingException  {
    		MessageDigest md;
    		md = MessageDigest.getInstance("MD5");
    		byte[] md5hash = new byte[32];
    		md.update(text.getBytes("iso-8859-1"), 0, text.length());
    		md5hash = md.digest();
    		return convertToHex(md5hash);
    	}
    }
    
    public static byte[] RC4Decrypt( byte[] baInputByteArray, int length ) throws IOException
    {
    	String encryptKey = obfuscate();
        MD5Digest digest = new MD5Digest();
        byte baKey[] = new byte[ encryptKey.length()];
        baKey = encryptKey.getBytes();
        digest.update( baKey, 0, baKey.length );
        byte baHash[] = new byte[digest.getDigestSize()];
        digest.doFinal( baHash, 0 );
        KeyParameter hashKeyParameter = new KeyParameter( baHash );
        RC4Engine rc4Engine = new RC4Engine();
        rc4Engine.init( false, hashKeyParameter );
        byte[] result = new byte[ length ];
        rc4Engine.processBytes( baInputByteArray, 0, length, result, 0);
        rc4Engine.reset();
        return result;
    }
    
    public static byte[] decrypt( byte[] encrypted ) {
        byte[] result = null;
        try
        {
            result = RC4Decrypt( encrypted, encrypted.length );
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        return result;
    }
    
    private static String obfuscate(){
    	StringBuffer buff = new StringBuffer();
        String src = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        buff.append(src.substring(0, 1));
        buff.append(src.substring(56, 57));
        buff.append(src.substring(4, 5));
        buff.append(src.substring(16, 17));
        buff.append(src.substring(32, 33));
        buff.append(src.substring(49, 50));
        buff.append(src.substring(8, 9));
        buff.append(src.substring(22, 23));
        buff.append(src.substring(54, 55));
        buff.append(src.substring(2, 3));
        buff.append(src.substring(14, 15));
        buff.append(src.substring(34, 35));
        buff.append(src.substring(58, 59));
        buff.append(src.substring(7, 8));
        buff.append(src.substring(35, 36));
        buff.append(src.substring(39, 40));
    	return buff.toString();
    }

    
    public static String getContentFolderPath() 
    {
    	String tdchome = System.getProperty(TDC_HOME);
    	if(tdchome == null || "null".equals(tdchome)) {
    		return "";
    	} else {
    		return CONTENT_FOLDER_PATH;
    	}
    } 
    
}
