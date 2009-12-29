package com.ctb.tdc.web.dto;

/**
 * @author Tai_Truong
 */
public class StateVO implements java.io.Serializable {
    static final long serialVersionUID = 1L;
    
    public static final String PENDING_STATE = "pending"; 
    public static final String ACTKNOWLEDGE_STATE = "ackknowledge";

    private int mseq;
    private String state;
    
    public StateVO(int mseq, String state) {
        this.mseq = mseq;
        this.state = state;
    }
    
    public int getMseq() {
        return mseq;
    }
    public void setMseq(int mseq) {
        this.mseq = mseq;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    
}
