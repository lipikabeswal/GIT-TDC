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
        ArrayList states = (ArrayList)this.stateHashMap.get(lsid);
        if (states == null) states = new ArrayList();
        int index = states.size() + 1;
        StateVO state = new StateVO(index, StateVO.PENDING_STATE);            
        states.add(state);
        this.stateHashMap.put(lsid, states);
        return state;
    }

    public void setAcknowledgeState(StateVO state) {
        state.setState(StateVO.ACTKNOWLEDGE_STATE);        
    }
    
    public void emptyStates(String lsid) {        
        ArrayList states = new ArrayList();
        this.stateHashMap.put(lsid, states);        
    }
    
    public boolean pendingState(String lsid) {  
        boolean pending = false;
        ArrayList states = (ArrayList)this.stateHashMap.get(lsid);
        if (states != null) {
            StateVO state = (StateVO)states.get(0);
            pending = state.getState().equals(StateVO.PENDING_STATE);
        }        
        return pending;
    }
    
}
