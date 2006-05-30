package com.ctb.tdc.web.dto;

import java.util.Date;

import com.ctb.tdc.web.utils.AuditFileEncrytor;

/**
 * @author Tai_Truong
 */
public class AuditVO implements java.io.Serializable {
    static final long serialVersionUID = 1L;

    private String fileName = null;
    private String mseq = null;
    private String itemId = null;
    private String response = null;    
    private Date now = null;
    private String encodedData = null;

    public AuditVO(String fileName, String mseq, String itemId, String response) {
        this.fileName = fileName;
        this.mseq = mseq;
        this.itemId = itemId;
        this.response = response;
        this.now = new Date();
        encryptText();
    }
    
    public String getEncodedData() {
        return encodedData;
    }

    public void setEncodedData(String encodedData) {
        this.encodedData = encodedData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMseq() {
        return mseq;
    }


    public void setMseq(String mseq) {
        this.mseq = mseq;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    private void encryptText() {
        byte[] gsBytes = {29 , 0};
        String groupSeparator = new String(gsBytes);        
        String payloads = this.itemId + groupSeparator + this.response;
        this.encodedData = AuditFileEncrytor.encrypt(payloads);
    }
    
    // format:  millis, mseq, <itemId|response>
    public String toString() {
        String str = "\"";
        str += this.now.getTime();
        str += "\",\"";
        str += this.mseq;
        str += "\",\"";
        str += this.encodedData + "\"";
        return str;
    }
}
