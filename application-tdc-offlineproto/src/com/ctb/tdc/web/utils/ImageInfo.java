package com.ctb.tdc.web.utils;

import java.util.ArrayList;
 
/**
 * @author Tai_Truong
 */
public class ImageInfo 
{
    private String mimeType;
    private ArrayList data;
    
    public ImageInfo()
    {
        super();
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    
}
