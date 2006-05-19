package com.ctb.tdc.web.dto;

/**
 * @author Tai_Truong
 */
public class StateVO implements java.io.Serializable {
    static final long serialVersionUID = 1L;
    
    public static final String PENDING_STATE = "pending"; 
    public static final String ACTKNOWLEDGE_STATE = "ackknowledge";

    private int index;
    private String state;
    
    public StateVO(int index, String state) {
        this.index = index;
        this.state = state;
    }
    
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    
}
