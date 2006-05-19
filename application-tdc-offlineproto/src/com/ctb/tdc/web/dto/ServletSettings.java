package com.ctb.tdc.web.dto;

import java.util.ResourceBundle;

/**
 * @author Tai_Truong
 */
public class ServletSettings implements java.io.Serializable {
    static final long serialVersionUID = 1L;

    private String tmsHost;
    private String tmsPort;
    private boolean tmsPersist;
    private boolean tmsAckRequired;
    private boolean tmsAuditUpload;
    private int tmsAckInflight;

    private String proxyHost;
    private String proxyPort;
    private String proxyUserName;
    private String proxyPassword;

    public ServletSettings() {
        this.tmsHost = null;
        this.tmsPort = null;
        this.tmsPersist = true;
        this.tmsAckRequired = true;
        this.tmsAuditUpload = true;
        this.tmsAckInflight = 1;
        this.proxyHost = null;
        this.proxyPort = null;
        this.proxyUserName = null;
        this.proxyPassword = null;        
    }
    
    public ServletSettings(ResourceBundle rb) {
        this.tmsHost = rb.getString("tms.server.host");
        this.tmsPort = rb.getString("tms.server.port");
        this.tmsPersist = new Boolean(rb.getString("tms.server.persist")).booleanValue();
        this.tmsAckRequired = new Boolean(rb.getString("tms.ack.required")).booleanValue();
        this.tmsAuditUpload = new Boolean(rb.getString("tms.audit.upload")).booleanValue();
        this.tmsAckInflight = new Integer(rb.getString("tms.ack.inflight")).intValue();
        this.proxyHost = rb.getString("proxy.host");
        this.proxyPort = rb.getString("proxy.port");
        this.proxyUserName = rb.getString("proxy.username");
        this.proxyPassword = rb.getString("proxy.password");       
        
        // make sure settings make sense
        if (this.tmsAckRequired && (this.tmsAckInflight <= 0)) {
            this.tmsAckInflight = 1;
        }
        if (! this.tmsPersist) {
            this.tmsAckRequired = false;
            this.tmsAuditUpload = true;
        }
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUserName() {
        return proxyUserName;
    }

    public void setProxyUserName(String proxyUserName) {
        this.proxyUserName = proxyUserName;
    }

    public int getTmsAckInflight() {
        return tmsAckInflight;
    }

    public void setTmsAckInflight(int tmsAckInflight) {
        this.tmsAckInflight = tmsAckInflight;
    }

    public boolean isTmsAckRequired() {
        return tmsAckRequired;
    }

    public void setTmsAckRequired(boolean tmsAckRequired) {
        this.tmsAckRequired = tmsAckRequired;
    }

    public boolean isTmsAuditUpload() {
        return tmsAuditUpload;
    }

    public void setTmsAuditUpload(boolean tmsAuditUpload) {
        this.tmsAuditUpload = tmsAuditUpload;
    }

    public String getTmsHost() {
        return tmsHost;
    }

    public void setTmsHost(String tmsHost) {
        this.tmsHost = tmsHost;
    }

    public boolean isTmsPersist() {
        return tmsPersist;
    }

    public void setTmsPersist(boolean tmsPersist) {
        this.tmsPersist = tmsPersist;
    }

    public String getTmsPort() {
        return tmsPort;
    }

    public void setTmsPort(String tmsPort) {
        this.tmsPort = tmsPort;
    }
    
    
}
