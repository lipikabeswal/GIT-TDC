package com.ctb.tdc.web.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.cordova.DroidGap;

import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import android.app.Application;
import android.os.Environment;
import android.util.Base64;

import com.ctb.tdc.web.exception.DecryptionException;
import com.ctb.tdc.web.exception.HashMismatchException;
import com.ctb.tdc.web.plugin.delegateActivity.OasAndroidApp;
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
   // public static final String CONTENT_FOLDER_PATH = System.getProperty(TDC_HOME)+ CONTENT_FOLDER;
    public static final String CONTENT_FOLDER_PATH = CONTENT_FOLDER;
    public static final String DATA_FOLDER = System.getProperty(TDC_HOME)+ "/data/databank/"; 	//"C:\\Program Files\\CTB\\Online Assessment\\data\\databank\\";
    public static final String DATA_FOLDER_DECRYPTED = System.getProperty(TDC_HOME)+ "/data/"; 	//"C:\\Program Files\\CTB\\Online Assessment\\data\\";
    
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
            File file = new File(Environment.getExternalStorageDirectory()+fileName);            
            exists = file.exists();
        }
        return exists;
    } 
    
    public static byte[] readFromFile( String filePath ) throws IOException
    {
    	 BufferedInputStream aFileInputStream = new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory()
                 .getAbsolutePath() +  filePath ));
         int size = aFileInputStream.available();
         byte[] buffer = new byte[ size ];
         aFileInputStream.read( buffer );
         aFileInputStream.close();
         return buffer;
    	/*File f = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() +  filePath);
    	FileInputStream fIn = new FileInputStream(f);
    	BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));*/
    	
        /*BufferedInputStream aFileInputStream = new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory()+ filePath ));
        int size = aFileInputStream.available();
        byte[] buffer = new byte[ size ];*/
    	/*String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}*/
		//myReader.close();
       /* aFileInputStream.read( buffer );
        aFileInputStream.close();*/
        //return aBuffer.getBytes("UTF-8");
    }
    
    public static void writeToFileWithNewName( byte[] data, String originalFilePath )throws Exception
    {
        String newFilePath = originalFilePath + ".xml";
        writeToFile( data, newFilePath );
    }
    
    public static void writeToFileContent( byte[] data, String fileName,String folderName ) throws IOException
    {
    	
       
        File psFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),folderName);
        if(!psFile.exists()) { 
        	System.err.println("It seems like parent directory does not exist...");  
        	if(!psFile.mkdirs())     {         
        		System.err.println("And we cannot create it..."); 
        		// we have to return, throw or something else  
        		} 
        	} 
        File filePs  = new File(psFile, fileName);
        BufferedOutputStream psfile = new BufferedOutputStream(new FileOutputStream( filePs ));
        psfile.write( data );
        psfile.flush();
        psfile.close(); 
	        
    }
    public static void writeToFile( byte[] data, String filePath ) throws IOException
    {
    	
        File psFile = new File( filePath );
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File outputFile = new File(file,filePath);
        if(!outputFile.exists())     { 
			if(outputFile.mkdir()); //directory is created; 
				
		}
        FileOutputStream fileoutputstream = new FileOutputStream(outputFile);
		
		fileoutputstream.write(data);
		
		fileoutputstream.close();
	        
    }
    
    public static boolean validateHash( String filePath, String hash) throws Exception
    {
        if (!exists(filePath))
        	return false;
    	byte[] buffer = readFromFile( filePath );
    	
        if ( Crypto.checkHash( hash, buffer )) {
        	//Fix for 60 days content deletion
        	File file = new File(Environment.getExternalStorageDirectory()
            .getAbsolutePath() +  filePath);
        	//File fileUp = new File(filePath);  
        	
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
        	
        	System.out.println("asa"+buffer.toString());
        	//String byteStr=buffer.toString();
        	//Cipher cipher = Cipher.getInstance("AES");
        	
        	//String str=result.toString();
        	//cipher.init(Cipher.DECRYPT_MODE, key);
        	/* byte baKey[] = new byte[key.length()];
             baKey = key.getBytes("UTF-8");
        	 byte[] decrypted = decrypt( baKey,buffer);
        	 String str=decrypted.toString();*/

         
        	//result=decrypt(key,buffer);
	        //boolean hashChecked = Crypto.checkHash( hash, buffer );
	        
	        //if (!hashChecked)
	        //	throw new HashMismatchException("Hash doesn't match."); 
        	
        	// result = Base64.decode(buffer, Base64.DEFAULT); 

	        Crypto aCrypto = new Crypto();
	        result = aCrypto.checkHashAndDecrypt( key, hash, buffer, true, false );
        	//result=checkHashAndDecrypt( key, hash, buffer, true, false );
	        //writeToFileWithNewName( result, filePath );
	       /* if (result == null)
	        	System.out.println("Decryption result is null.");
	        else*/
	        	//return result;
        	
        	//result=checkHashAndDecrypt( key, hash, buffer, true, false );
        }
       /* catch (HashMismatchException hme) {
        	throw hme;
        }*/
        catch (Exception e) {
        	throw new DecryptionException(e.getMessage());
        }
	    return result;
    }
   
    public static byte[] decrypt(String keyString, byte[] ciphertext) 
    		throws GeneralSecurityException   {
    	// converts the String to a PublicKey instance
    	 byte[] dectyptedText = null;
    	try{
        byte[] keyBytes = Base64.decode(keyString.getBytes("utf-8"),Base64.DEFAULT);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(spec);

        // decrypts the message
       
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        dectyptedText = cipher.doFinal(Base64.decode(ciphertext,Base64.DEFAULT));
        return dectyptedText;
    	}
    	catch(UnsupportedEncodingException e){
    		 return dectyptedText;
    		
    	}
  
    	} 
   
    public static boolean checkHash(String strManifestHash, byte byteArray[])
            throws Exception
        {
            String strHash = generateHash(byteArray);
            return strHash.equals(strManifestHash);
        }
    public static String generateHash(byte baInputData[])
    {
        String strHash = null;
        try
        {
            MD5Digest digest = new MD5Digest();
            digest.update(baInputData, 0, baInputData.length);
            byte baHash[] = new byte[digest.getDigestSize()];
            digest.doFinal(baHash, 0);
            strHash = byteArrayToHexString(baHash);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return strHash;
    }
    public static String byteArrayToHexString(byte in[])
    {
        byte ch = 0;
        int i = 0;
        if(in == null || in.length <= 0)
            return null;
        String hexChar[] = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
            "A", "B", "C", "D", "E", "F"
        };
        StringBuffer out = new StringBuffer(in.length * 2);
        for(; i < in.length; i++)
        {
            ch = (byte)(in[i] & 0xf0);
            ch >>>= 4;
            ch &= 0xf;
            out.append(hexChar[ch]);
            ch = (byte)(in[i] & 0xf);
            out.append(hexChar[ch]);
        }

        String rslt = new String(out);
        return rslt;
    }
    private static int MC_KEY_SIZE = 16;

    private static byte[] createStandardProviderCompatibleHash(byte keyBytes[])
    {
        int i = 0;
        byte newKey[];
        if(keyBytes.length > 4)
        {
            newKey = new byte[MC_KEY_SIZE];
            for(i = 0; i <= 4; i++)
                newKey[i] = keyBytes[i];

            for(i = 5; i < MC_KEY_SIZE; i++)
                newKey[i] = 0;

        } else
        {
            newKey = new byte[MC_KEY_SIZE];
            for(i = 0; i <= keyBytes.length; i++)
                newKey[i] = keyBytes[i];

            for(i = keyBytes.length + 1; i < MC_KEY_SIZE; i++)
                newKey[i] = 0;

        }
        return newKey;
    }


    public static byte[] checkHashAndDecrypt(String strKey, String strManifestHash, byte baInputByteArray[], boolean bUseStrongCipher, boolean bGZIP)
            throws Exception
        {
            byte baOutputByteArrayData[] = (byte[])null;
            try
            {
                if(strManifestHash == null || checkHash(strManifestHash, baInputByteArray))
                {
                    MD5Digest digest = new MD5Digest();
                    byte baKey[] = new byte[strKey.length()];
                    baKey = strKey.getBytes();
                    digest.update(baKey, 0, baKey.length);
                    byte baHash[] = new byte[digest.getDigestSize()];
                    digest.doFinal(baHash, 0);
                    byte baHashKey[] = (byte[])null;
                    if(bUseStrongCipher)
                        baHashKey = baHash;
                    else
                        baHashKey = createStandardProviderCompatibleHash(baHash);
                    KeyParameter hashKeyParameter = new KeyParameter(baHashKey);
                    RC4Engine rc4Engine = new RC4Engine();
                    rc4Engine.init(false, hashKeyParameter);
                    baOutputByteArrayData = new byte[baInputByteArray.length];
                    rc4Engine.processBytes(baInputByteArray, 0, baInputByteArray.length, baOutputByteArrayData, 0);
                    rc4Engine.reset();
                } else
                {
                    System.err.println("Hash mismatch");
                }
            }
            catch(Exception exception)
            {
                baOutputByteArrayData = (byte[])null;
                throw exception;
            }
            return baOutputByteArrayData;
        }


    public static byte[] decryptFileTest( byte[] buffer, String hash, String key ) 
    {
        
        byte[] result = null;
        
        try {
        	
        
	        //boolean hashChecked = Crypto.checkHash( hash, buffer );
	        
	        //if (!hashChecked)
	        //	throw new HashMismatchException("Hash doesn't match.");
        	
	        Crypto aCrypto = new Crypto();
	        result = aCrypto.checkHashAndDecrypt( key, hash, buffer, true, false );
	        //writeToFileWithNewName( result, filePath );
	        if (result == null)
	        	throw new Exception ("Decryption result is null.");
        }
       
        catch (Exception e) {
        	
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
    	try{
    		return CONTENT_FOLDER_PATH;
    //	ClassLoader cl = MainActivity.getAppContext().getClassLoader(); 
    //	Class SystemProperties = cl.loadClass("android.os.SystemProperties");

    	/*String tdchome = System.getProperty(TDC_HOME);
    	if(tdchome == null || "null".equals(tdchome)) {
    		return "";
    	} else {
    		return CONTENT_FOLDER_PATH;
    	}*/
    	}
    	catch (Exception e) {
    		return e.getMessage();
			// TODO: handle exception
		}
    } 
    
    public static void decryptDataFiles(){
    	try {
    		String key =   "n7673nBJ2n27bB4oAfme7Ugl5VV42g8";
    		byte[] infile = null;
    		String hash = null;
    		byte[] outfile = null;
    		FileOutputStream fos = null;
    		Security.insertProviderAt(new BouncyCastleProvider(), 2);
    		File encDataFiles = new File(DATA_FOLDER);
    		
    		FilenameFilter filefilter = new FilenameFilter() {
    			public boolean accept(File dir, String name) {
    		        return name.endsWith(".ecp");
    		      }
    		};    		

    		File[] files = encDataFiles.listFiles(filefilter);
    		if (files.length > 0){
    			System.out.println("Files.length" + files.length);
    			for (int i = 0; i < files.length; i++) {
    				infile = readFromFile(files[i].getAbsolutePath());
					hash = Crypto.generateHash(infile);
					outfile = decryptFile(files[i].getAbsolutePath(), hash, key);
					fos = new FileOutputStream(DATA_FOLDER_DECRYPTED+ File.separator + files[i].getName().substring(0,files[i].getName().length()-4));
					fos.write(outfile);
					fos.close();
				}
    			
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static void deleteDataFiles(){
    	try{
    		File plainDataFiles = new File(DATA_FOLDER_DECRYPTED);
    		FilenameFilter filefilter = new FilenameFilter() {
    			public boolean accept(File dir, String name) {
    		        return name.endsWith(".csv");
    		      }
    		};    		
    		File[] files = plainDataFiles.listFiles(filefilter);
    		if (files.length > 0){
    			for (int i = 0; i < files.length; i++) {
    				deleteFile(files[i].getAbsolutePath());
    			}   				
    		}
    		
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}
