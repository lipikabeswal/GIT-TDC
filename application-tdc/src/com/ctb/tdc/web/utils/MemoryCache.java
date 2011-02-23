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

    private HashMap stateMap;
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
        this.stateMap = new HashMap();
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

    public HashMap getStateMap() {
    	synchronized(this) {
    		return stateMap;
    	}
    }

    public void setStateMap(HashMap stateMap) {
    	synchronized(this) {
    		this.stateMap = stateMap;
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
    
    public StateVO setPendingState(String lsid, String mseq, String method, String xml) {
    	synchronized(MemoryCache.class) {
    		boolean duplicate = false;
    		StateVO state = null;
	        if (this.srvSettings.isTmsAckRequired()) {
	            ArrayList states = (ArrayList)this.stateMap.get(lsid);
	            if (states == null) 
	                states = new ArrayList();
	            for (int i=0 ; i<states.size() ; i++) {
	                state = (StateVO)states.get(i);
	                if (Integer.parseInt(mseq) == state.getMseq()) {
	                    duplicate = true;
	                }
	            }
	            if(!duplicate) {
	            	state = new StateVO(Integer.parseInt(mseq), StateVO.PENDING_STATE, method, xml);            
	            	states.add(state);
	            	this.stateMap.put(lsid, states);
	            }
	        }
	        return state;
    	}
    }

    public void setAcknowledgeState(StateVO state) {
    	synchronized(this) {
	        if ((state != null) && this.srvSettings.isTmsAckRequired()) {
	            state.setState(StateVO.ACKNOWLEDGED_STATE);
	        }
    	}
    }
    
    public void setAcknowledgeState(String lsid, String mseq) {
    	synchronized(this) {
    		StateVO state = null;
	        ArrayList states = (ArrayList)this.stateMap.get(lsid);
	        for (int i=0 ; i<states.size() ; i++) {
	        	state = (StateVO)states.get(i);
                if (Integer.parseInt(mseq) == state.getMseq()) {
                	state.setState(StateVO.ACKNOWLEDGED_STATE);
                	break;
                }
	        }
    	}
    }
    
    public void removeAcknowledgeStates(String lsid) {
    	synchronized(this) {
	        ArrayList states = (ArrayList)this.stateMap.get(lsid);
	        if (states != null) {
	            for (int i=states.size()-1 ; i>=0 ; i--) { 
	                StateVO state = (StateVO)states.get(i);
	                if (state.getState().equals(StateVO.ACKNOWLEDGED_STATE)) {
	                    states.remove(i);
	                }
	            }
	        }
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
