/*
 * Created on May 22, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.tdc.web.utils;

/**
 * @author wen-jin_chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AssetInfo 
{
    public byte[] data;
    public String mimeType;
    
    public AssetInfo()
    {
        super();
    }
    
    public void setExt( String ext )
    {
        mimeType = "image/gif";
        if ( "swf".equals( ext ))
            mimeType = "application/x-shockwave-flash";
        if ( "gif".equals( ext ))
            mimeType = "image/gif";
        if ( "jpg".equals( ext ))
            mimeType = "image/jpg";
    }
    
    public void setData( byte[] data_ )
    {
        data = data_;
    }
    
    public String getMIMEType()
    {
        return mimeType;
    }
    
    public byte[] getData()
    {
        return data;
    }

}
