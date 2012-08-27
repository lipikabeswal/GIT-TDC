import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.ctb.tdc.web.exception.DecryptionException;
import com.ctb.tdc.web.exception.HashMismatchException;
import com.stgglobal.util.CryptoLE.Crypto;

public class CATDataFileEncryptor 
{
    
    public static void writeToFileWithNewName( byte[] data, String originalFilePath )throws Exception
    {
        String newFilePath = originalFilePath + ".ecp";
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
    
    public static byte[] readFromFile( String filePath ) throws IOException
    {
        BufferedInputStream aFileInputStream = new BufferedInputStream(new FileInputStream( filePath ));
        int size = aFileInputStream.available();
        byte[] buffer = new byte[ size ];
        aFileInputStream.read( buffer );
        aFileInputStream.close();
        return buffer;
    }
    
    public static byte[] encryptFile( String filePath, String key ) throws DecryptionException, HashMismatchException 
    {
        byte[] buffer = null; 
        byte[] result = null;
        
        try {
        	buffer = readFromFile( filePath );
        
        	Security.insertProviderAt(new BouncyCastleProvider(), 2);
        	
        	Crypto aCrypto = new Crypto();
	        result = aCrypto.encrypt( key, buffer, true, false );
	        writeToFileWithNewName( result, filePath );
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
    		encryptFile("C:\\tdcupdates\\data\\databank\\RD.csv", "n7673nBJ2n27bB4oAfme7Ugl5VV42g8");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}

