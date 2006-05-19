package com.ctb.tdc.web.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.ctb.tdc.web.dto.StateVO;

/**
 * @author Tai_Truong
 */
public class MemoryCache {
    private HashMap stateHashMap;
    
    private boolean tmsPersist;
    private boolean tmsAckRequired;
    private int tmsAckInflight;
    private boolean tmsAuditUpload;

    private String proxyHost;
    private String proxyPort;
    private String proxyUserName;
    private String proxyPassword;
    
    private MemoryCache() {
        this.stateHashMap = new HashMap();
        this.tmsPersist = true;
        this.tmsAckRequired = true;
        this.tmsAckInflight = 1;
        this.tmsAuditUpload = true;
        this.proxyHost = null;
        this.proxyPort = null;
        this.proxyUserName = null;
        this.proxyPassword = null;
    }

    public static MemoryCache getInstance() {
        return MemoryCacheHolder.instance;
    }

    private static class MemoryCacheHolder {
        private static MemoryCache instance = new MemoryCache();
    }
    
    public StateVO putWaitState(String lsid) {        
        ArrayList states = (ArrayList)this.stateHashMap.get(lsid);
        if (states == null) 
            states = new ArrayList();
        int index = states.size() + 1;
        StateVO state = new StateVO(index, StateVO.WAIT_STATE);            
        states.add(state);
        this.stateHashMap.put(lsid, states);
        return state;
    }

    public void emptyStates(String lsid) {        
        ArrayList states = new ArrayList();
        this.stateHashMap.put(lsid, states);        
    }
    
    public boolean hasAcknowledge(String lsid) {  
        boolean hasAck = true;
        ArrayList states = (ArrayList)this.stateHashMap.get(lsid);
        if (states != null) {
            StateVO state = (StateVO)states.get(0);
            hasAck = state.getState().equals(StateVO.ACTKNOWLEDGE_STATE);
        }        
        return hasAck;
    }
    
}
