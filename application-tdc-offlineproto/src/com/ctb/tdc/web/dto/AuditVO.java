package com.ctb.tdc.web.dto;

/**
 * @author Tai_Truong
 */
public class AuditVO implements java.io.Serializable {
    static final long serialVersionUID = 1L;

    private String fileName = null;
    private String lsid = null;
    private String mseq = null;
    private String event = null;
    private String xml = null;

    public AuditVO(String fileName, String lsid, String mseq, String event, String xml) {
        this.fileName = fileName;
        this.lsid = lsid;
        this.mseq = mseq;
        this.event = event;
        this.xml = xml;
    }
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getLsid() {
        return lsid;
    }

    public void setLsid(String lsid) {
        this.lsid = lsid;
    }

    public String getMseq() {
        return mseq;
    }

    public void setMseq(String mseq) {
        this.mseq = mseq;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
    
    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    // format as : "time stamp", "lsid", "mseq", "event", "xml"
    public String toString() {
        String str = "";
        str += "\"" + this.lsid + "\", ";
        str += "\"" + this.mseq + "\", ";
        str += "\"" + this.event + "\", ";
        str += "\"" + this.xml + "\"";
        return str;
    }
}
