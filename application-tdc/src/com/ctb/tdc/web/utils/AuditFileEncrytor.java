/*
 * Created on May 25, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.tdc.web.utils;

import java.security.MessageDigest;

import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 * @author wen-jin_chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AuditFileEncrytor
{


    public AuditFileEncrytor() 
    {
        super();
    }
    
    public static String encrypt( String data )
    {
        String result = data;
        try {
            result = RC4Encrypt( data );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private static int MC_KEY_SIZE = 16;
    private static String encryptKey = "CTB";
    
    private static String RC4Encrypt( String xml )throws Exception
    {
        byte[] baInputByteArray = xml.getBytes( "UTF-8" );
        String value = null;
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );
            byte baKey[] = encryptKey.getBytes();
            messageDigest.update( baKey );
            byte baHash[] = messageDigest.digest();
            KeyParameter hashKeyParameter = new KeyParameter( baHash );
            RC4Engine rc4Engine = new RC4Engine();
            rc4Engine.init( true, hashKeyParameter );
            byte[] baOutputByteArray = new byte[ baInputByteArray.length ];
            rc4Engine.processBytes( baInputByteArray, 0, baInputByteArray.length, baOutputByteArray, 0);
            rc4Engine.reset();
            value = Base64.encode( baOutputByteArray );
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return value;
    }

}
