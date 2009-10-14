package com.ctb.tdc.web.dto;

/**
 * @author Tai_Truong
 */
public class StateVO implements java.io.Serializable {
    static final long serialVersionUID = 1L;
    
    public static final String PENDING_STATE = "pending"; 
    public static final String ACKNOWLEDGED_STATE = "acknowledged";
    
    public String method = "";
    public String xml = "";

    private int mseq;
    private String state;
    
    public StateVO(int mseq, String state, String method, String xml) {
        this.mseq = mseq;
        this.state = state;
        this.method = method;
        this.xml = xml;
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
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getXml() {
        return xml;
    }
    public void setXml(String xml) {
        this.xml = xml;
    }
    
}
