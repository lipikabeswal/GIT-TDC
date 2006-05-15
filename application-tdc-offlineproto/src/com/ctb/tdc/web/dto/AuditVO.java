package com.ctb.tdc.web.dto;

public class AuditVO implements java.io.Serializable {
    static final long serialVersionUID = 1L;

    private String fileName = null;
    private String mseq = null;
    private String type = null;
    private String lsid = null;
    private String date = null;
    private String response = null;
    
    public AuditVO(String fileName, String mseq, String type, String date, String lsid, String response) {
        this.fileName = fileName;
        this.mseq = mseq;
        this.type = type;
        this.date = date;
        this.lsid = lsid;
        this.response = response;
    }
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
