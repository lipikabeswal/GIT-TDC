package com.ctb.tdc.web.dto;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Tai_Truong
 */
public class ServletSettings implements java.io.Serializable {
    static final long serialVersionUID = 1L;

    private String tmsHost;
    private String backupURL;
    
    private int tmsPort;
    private boolean tmsPersist;
    private boolean tmsAckRequired;
    private boolean tmsAuditUpload;

    private String proxyHost;
    private int proxyPort;
    private String proxyUserName;
    private String proxyPassword;
    private String proxyDomain;

	private boolean validSettings;
    private String errorMessage;

    public ServletSettings() {
        init();
    }
    
    public ServletSettings(ResourceBundle rbTdc, ResourceBundle rbProxy) {
        
        init();
        
        if (rbTdc != null) {
            this.tmsHost = resourceBundleGetString(rbTdc, "tms.server.host");
            /*if(!this.tmsHost.startsWith("https")) {
            	this.tmsHost = null;
            	throw new RuntimeException("ERROR: TMS url is not secure!");
            }*/
            //this.backupURL = resourceBundleGetString(rbTdc, "tms.server.backupURL");
            this.backupURL = resourceBundleGetString(rbTdc, "tms.dr.server.host");
            /*if(!this.backupURL.startsWith("https")) {
            	this.backupURL = null;
            	throw new RuntimeException("ERROR: TMS url is not secure!");
            } */           
            this.tmsPort = resourceBundleGetInt(rbTdc, "tms.server.port");      
            try {
                this.tmsPersist = resourceBundleGetBoolean(rbTdc, "tms.server.persist");
            }
            catch (MissingResourceException mre) {
                this.tmsPersist = true;            
            }
            try {
                this.tmsAckRequired = resourceBundleGetBoolean(rbTdc, "tms.ack.required");
            }
            catch (MissingResourceException mre) {
                this.tmsAckRequired = true;            
            }
            this.tmsAuditUpload = resourceBundleGetBoolean(rbTdc, "tms.audit.upload");
        }
        
        if (rbProxy != null) {
            this.proxyHost = resourceBundleGetString(rbProxy, "proxy.host");
            this.proxyPort = resourceBundleGetInt(rbProxy, "proxy.port");        
            this.proxyUserName = resourceBundleGetString(rbProxy, "proxy.username");
            this.proxyPassword = resourceBundleGetString(rbProxy, "proxy.password");
            this.proxyDomain = resourceBundleGetString(rbProxy, "proxy.ntlmdomain");
        }
    }

    private void init() {
        this.tmsHost = null;
        this.backupURL = null;
        this.tmsPort = 0;
        this.tmsPersist = true;
        this.tmsAckRequired = true;
        this.tmsAuditUpload = false;
        this.proxyHost = null;
        this.proxyPort = 0;
        this.proxyUserName = null;
        this.proxyPassword = null;    
        this.validSettings = true;
        this.errorMessage = "";
    }
         
    

    public String getProxyDomain() {
		return proxyDomain;
	}

	public void setProxyDomain(String proxyDomain) {
		this.proxyDomain = proxyDomain;
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

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUserName() {
        return proxyUserName;
    }

    public void setProxyUserName(String proxyUserName) {
        this.proxyUserName = proxyUserName;
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

    public int getTmsPort() {
        return tmsPort;
    }

    public void setTmsPort(int tmsPort) {
        this.tmsPort = tmsPort;
    }
    
    public String getTmsHostPort() {
        if (tmsPort > 0)
            return (tmsHost + ":" + tmsPort);
        else
            return tmsHost;
    }
    
    public String getBackupURLHostPort() {
        if (tmsPort > 0)
            return (backupURL + ":" + tmsPort);
        else
            return backupURL;
    }
    
    public String getProxyHostPort() {
        if (proxyPort > 0)
            return (proxyHost + ":" + proxyPort);
        else
            return proxyHost;
    }

    public boolean isValidSettings() {
        return validSettings;
    }

    public void setValidSettings(boolean validSettings) {
        this.validSettings = validSettings;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    private String resourceBundleGetString(ResourceBundle rb, String name) {
        return rb.getString(name).trim();
    }
 
    private int resourceBundleGetInt(ResourceBundle rb, String name, int min, int max) {
        int value = -1;
        String str = rb.getString(name).trim();
        try {        
            value = new Integer(str).intValue();
            if ((value < min) || (value > max)) {
                this.validSettings = false;                
                String errStr = buildErrorMessage(name, "tdc.servletSetting.error.outOfRange");
                this.errorMessage = errStr + " " + String.valueOf(min) + " - " + String.valueOf(max); 
            }
        } catch (Exception e) { 
            this.validSettings = false;
            this.errorMessage = buildErrorMessage(name, "tdc.servletSetting.error.invalidInteger");
        }        
        return value;
    }

    private int resourceBundleGetInt(ResourceBundle rb, String name) {
        int value = -1;
        String str = rb.getString(name).trim();
        try {    
            if (str.length() > 0) {
                value = new Integer(str).intValue();
            }
        } catch (Exception e) { 
            this.validSettings = false;
            this.errorMessage = buildErrorMessage(name, "tdc.servletSetting.error.invalidInteger");
        }        
        return value;
    }
    
    private boolean resourceBundleGetBoolean(ResourceBundle rb, String name) {
        boolean value = false;
        String str = rb.getString(name).trim();
        if (str.length() > 0) {
            str = str.toLowerCase();
            if (str.equals("true")) 
                value = true;
            else
            if (str.equals("false")) 
                value = false;
            else {
                this.validSettings = false;
                this.errorMessage = buildErrorMessage(name, "tdc.servletSetting.error.invalidBoolean");
            }
        }
        else {
            this.validSettings = false;
            this.errorMessage = buildErrorMessage(name, "tdc.servletSetting.error.cannotBeBlank");
        }
        return value;
    }
    
    private String buildErrorMessage(String name, String error) {
        ResourceBundle rb = ResourceBundle.getBundle("tdcResources");
        String commonText = rb.getString("tdc.servletSetting.error.commonText");
        String errStr = rb.getString(error);        
        return (commonText + name + errStr);
    }

	/**
	 * @return the altTmsHost
	 */
	public String getBackupURL() {
		return backupURL;
	}

	/**
	 * @param altTmsHost the altTmsHost to set
	 */
	public void setBackupURL(String backupURL) {
		this.backupURL = backupURL;
	}
}
