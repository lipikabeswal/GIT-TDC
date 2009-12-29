package com.ctb.tdc.bootstrap.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;


/*
 * Created on May 17, 2007
 *
 */

/**
 * @author Jon Becker
 *
 */
public class TdcConfigEncryption 
{
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
    
    public static byte[] encrypt( byte[] in )
    {
        byte[] result = null;
        try
        {
           	String encryptKey = obfuscate();
            MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );
            byte baKey[] = encryptKey.getBytes();
            messageDigest.update( baKey );
            byte baHash[] = messageDigest.digest();
            KeyParameter hashKeyParameter = new KeyParameter( baHash );
            RC4Engine rc4Engine = new RC4Engine();
            rc4Engine.init( true, hashKeyParameter );
            result = new byte[ in.length ];
            rc4Engine.processBytes( in, 0, in.length, result, 0);
            rc4Engine.reset();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return result;
    }
    
    public static void main( String[] args ) throws Exception
    {
        try
        {
        	String operation = "encrypt";
        	String inputFile = "C:\\tdcupdates\\lock.zip";
        	String outputFile = "C:\\tdcupdates\\lock.enc";
        	
        	/*if(args.length > 1) {
	        	inputFile = args[0];
	        	if(inputFile == null || inputFile.length() < 1) {
	        		inputFile = "C:\\tdcupdates\\tdcConfig.zip";
	        	}
	        	
	        	outputFile = args[1];
	        	if(outputFile == null || outputFile.length() < 1) {
	        		outputFile = "C:\\tdcupdates\\tdcConfig.enc";
	        	}
        	}*/
        	if("encrypt".equals(operation)) {
	            FileInputStream input = new FileInputStream( inputFile );
	            int size = input.available();
	            byte[] src = new byte[ size ];
	            input.read( src );
	            input.close();
	            
	            byte[] encrypted = encrypt(src);
	            //byte[] decrypted = decrypt(encrypted);
	            FileOutputStream enc = new FileOutputStream(outputFile);
	            enc.write( encrypted );
	            enc.close();
	            //FileOutputStream output = new FileOutputStream(inputFile + ".new");
	            //output.write( decrypted );
	            //output.close();
        	} else {
        		FileInputStream input = new FileInputStream( outputFile );
	            int size = input.available();
	            byte[] src = new byte[ size ];
	            input.read( src );
	            input.close();
	            
	            byte[] decrypted = decrypt(src);
	            FileOutputStream output = new FileOutputStream(inputFile);
	            output.write( decrypted );
	            output.close();
        	}
          }   
        catch ( Exception e )
        {
            System.err.println("Exception : "  + e.getMessage());
            e.printStackTrace();
        }
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
}