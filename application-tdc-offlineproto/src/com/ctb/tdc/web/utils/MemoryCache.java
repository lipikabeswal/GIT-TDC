package com.ctb.tdc.web.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.ctb.tdc.web.dto.ServletSettings;
import com.ctb.tdc.web.dto.StateVO;

/**
 * @author Tai_Truong
 */
public class MemoryCache {
    private HashMap stateHashMap;
    private ServletSettings srvSettings;
    
    private MemoryCache() {
        this.stateHashMap = new HashMap();
        this.srvSettings = new ServletSettings();
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

    public HashMap getStateHashMap() {
        return stateHashMap;
    }

    public void setStateHashMap(HashMap stateHashMap) {
        this.stateHashMap = stateHashMap;
    }

    public StateVO setPendingState(String lsid) {
        StateVO state = null;
        if (this.srvSettings.isTmsAckRequired()) {
            ArrayList states = (ArrayList)this.stateHashMap.get(lsid);
            if (states == null) 
                states = new ArrayList();
            int index = states.size() + 1;
            state = new StateVO(index, StateVO.PENDING_STATE);            
            states.add(state);
            this.stateHashMap.put(lsid, states);
        }
        return state;
    }

    public void setAcknowledgeState(StateVO state) {
        if (this.srvSettings.isTmsAckRequired() && (state != null))
            state.setState(StateVO.ACTKNOWLEDGE_STATE);        
    }
    
    public void removeAcknowledgeStates(String lsid) {        
        ArrayList states = (ArrayList)this.stateHashMap.get(lsid);
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
            ArrayList states = (ArrayList)this.stateHashMap.get(lsid);
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
    
}
