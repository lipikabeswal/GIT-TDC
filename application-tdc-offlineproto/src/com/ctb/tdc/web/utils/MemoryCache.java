/*
 * Created on May 18, 2006
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
public class MemoryCache 
{
    protected static MemoryCache memoryCache;

    protected MemoryCache() 
    {
        super();
    }
    
    public static synchronized MemoryCache getMemoryCache()
    {
        if ( MemoryCache.memoryCache == null )
        {
            MemoryCache.memoryCache = new MemoryCache();
        }
        return MemoryCache.memoryCache;
    }

}
