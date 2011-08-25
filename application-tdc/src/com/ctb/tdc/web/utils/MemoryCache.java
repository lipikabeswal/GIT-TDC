package com.ctb.tdc.web.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdom.input.SAXBuilder;

import com.ctb.tdc.web.dto.ServletSettings;
import com.ctb.tdc.web.dto.StateVO;
import com.ctb.tdc.web.dto.TTSSettings;
 
/**
 * @author Tai_Truong
 */
public class MemoryCache {
    static final long serialVersionUID = 1L;

    private HashMap subtestInfoMap;
    private ServletSettings srvSettings;
    private TTSSettings ttsSettings;
    private HashMap itemMap;
    private HashMap assetMap;
    public SAXBuilder saxBuilder;
    private boolean loaded = false;
    private HashMap imageMap;
    
    public TTSSettings getTTSSettings() {
    	synchronized(this) {
    		return this.ttsSettings;
    	}
    }

    public void setTTSSettings(TTSSettings ttsSettings) {
    	synchronized(this) {
    		this.ttsSettings = ttsSettings;
    	}
    }

	private MemoryCache() {
        this.loaded = false;
        this.subtestInfoMap = new HashMap();
        this.srvSettings = new ServletSettings();
        clearContent();
        saxBuilder = new SAXBuilder();
        this.imageMap = new HashMap();
    }

    public static MemoryCache getInstance() {
    	synchronized(MemoryCache.class) {
    		return MemoryCacheHolder.instance;
    	}
    }

    private static final class MemoryCacheHolder {
    	private static final MemoryCache instance = new MemoryCache();	
    }
    
    public ServletSettings getSrvSettings() {
    	synchronized(this) {
    		return this.srvSettings;
    	}
    }

    public void setSrvSettings(ServletSettings srvSettings) {
    	synchronized(this) {
    		this.srvSettings = srvSettings;
    	}
    }
    
    public boolean isLoaded() {
    	synchronized(this) {
    		return loaded;
    	}
    }

    public void setLoaded(boolean loaded) {
    	synchronized(this) {
    		this.loaded = loaded;
    	}
    }

    public HashMap getImageMap() {
    	synchronized(this) {
    		return imageMap;
    	}
    }

    public void setImageMap(HashMap imageMap) {
    	synchronized(this) {
    		this.imageMap = imageMap;
    	}
    }
    
    public void clearContent()
    {
    	synchronized(this) {
    		itemMap = new HashMap();
    		assetMap = new HashMap();
    	}
    }
    
    public HashMap getAssetMap()
    {
    	synchronized(this) {
    		return assetMap;
    	}
    }
    
    public HashMap getItemMap()
    {
    	synchronized(this) {
    		return itemMap;
    	}
    }
    
    public HashMap getSubtestInfoMap()
    {
    	synchronized(this) {
    		return subtestInfoMap;
    	}
    }
   
}
