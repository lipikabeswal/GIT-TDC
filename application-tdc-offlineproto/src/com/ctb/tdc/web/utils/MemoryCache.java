package com.ctb.tdc.web.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdom.input.SAXBuilder;

import com.ctb.tdc.web.dto.ServletSettings;
import com.ctb.tdc.web.dto.StateVO;
import com.stgglobal.util.CryptoLE.Crypto;

/**
 * @author Tai_Truong
 */
public class MemoryCache {
    static final long serialVersionUID = 1L;

    private HashMap stateMap;
    private ServletSettings srvSettings;
    private HashMap itemMap;
    private HashMap assetMap;
    public Crypto aCrypto;
    public SAXBuilder saxBuilder;
    private boolean loaded = false;

    
    private MemoryCache() {
        this.loaded = false;
        this.stateMap = new HashMap();
        this.srvSettings = new ServletSettings();
        clearContent();
        aCrypto = new Crypto();
        saxBuilder = new SAXBuilder();
    }

    public static MemoryCache getInstance() {
        return MemoryCacheHolder.instance;
    }

    private static class MemoryCacheHolder {
        private static MemoryCache instance = new MemoryCache();
    }
    
    public ServletSettings getSrvSettings() {
        return this.srvSettings;
    }

    public void setSrvSettings(ServletSettings srvSettings) {
        this.srvSettings = srvSettings;
    }

    public HashMap getStateMap() {
        return stateMap;
    }

    public void setStateMap(HashMap stateMap) {
        this.stateMap = stateMap;
    }
    
    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public StateVO setPendingState(String lsid) {
        StateVO state = null;
        if (this.srvSettings.isTmsAckRequired()) {
            ArrayList states = (ArrayList)this.stateMap.get(lsid);
            if (states == null) 
                states = new ArrayList();
            int index = states.size() + 1;
            state = new StateVO(index, StateVO.PENDING_STATE);            
            states.add(state);
            this.stateMap.put(lsid, states);
        }
        return state;
    }

    public void setAcknowledgeState(StateVO state) {
        if (this.srvSettings.isTmsAckRequired() && (state != null))
            state.setState(StateVO.ACTKNOWLEDGE_STATE);        
    }
    
    public void removeAcknowledgeStates(String lsid) {        
        ArrayList states = (ArrayList)this.stateMap.get(lsid);
        if (states != null) {
            for (int i=0 ; i<states.size() ; i++) {
                StateVO state = (StateVO)states.get(i);
                if (state.getState().equals(StateVO.ACTKNOWLEDGE_STATE))
                    states.remove(i);                   
            }
        }
    }
    
    public boolean pendingState(String lsid) {  
        boolean pending = false;
        if (this.srvSettings.isTmsAckRequired()) {
            ArrayList states = (ArrayList)this.stateMap.get(lsid);
            if (states != null) {
                int pendingCount = 0;
                for (int i=0 ; i<states.size() ; i++) {
                    StateVO state = (StateVO)states.get(i);
                    if (state.getState().equals(StateVO.PENDING_STATE))
                        pendingCount++;                   
                }
                pending = (pendingCount >= this.srvSettings.getTmsAckInflight());
            }
        }
        return pending;
    }
    
    public void clearContent()
    {
        itemMap = new HashMap();
        assetMap = new HashMap();
    }
    
    public HashMap getAssetMap()
    {
        return assetMap;
    }
    
    public HashMap getItemMap()
    {
        return itemMap;
    }
    
}
