// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 7/31/2012 12:35:06 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 

package com.ctb.tdc.web.dto;

import java.io.Serializable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ServletSettings
    implements Serializable
{

    public ServletSettings()
    {
        init();
    }

    public ServletSettings(ResourceBundle resourcebundle, ResourceBundle resourcebundle1)
    {
        init();
        if(resourcebundle != null)
        {
            tmsHost = resourceBundleGetString(resourcebundle, "tms.server.host");
            tmsPort = resourceBundleGetInt(resourcebundle, "tms.server.port");
            try
            {
                tmsPersist = resourceBundleGetBoolean(resourcebundle, "tms.server.persist");
            }
            catch(MissingResourceException missingresourceexception)
            {
                tmsPersist = true;
            }
            try
            {
                tmsAckRequired = resourceBundleGetBoolean(resourcebundle, "tms.ack.required");
            }
            catch(MissingResourceException missingresourceexception1)
            {
                tmsAckRequired = true;
            }
            tmsAckMaxLostMessage = resourceBundleGetInt(resourcebundle, "tms.ack.maxLostMessage", 1, 10);
            tmsAckMessageRetry = resourceBundleGetInt(resourcebundle, "tms.ack.messageRetry", 0, 35);
            tmsAuditUpload = resourceBundleGetBoolean(resourcebundle, "tms.audit.upload");
        }
        if(resourcebundle1 != null)
        {
            proxyHost = resourceBundleGetString(resourcebundle1, "proxy.host");
            proxyPort = resourceBundleGetInt(resourcebundle1, "proxy.port");
            proxyUserName = resourceBundleGetString(resourcebundle1, "proxy.username");
            proxyPassword = resourceBundleGetString(resourcebundle1, "proxy.password");
            proxyDomain = resourceBundleGetString(resourcebundle1, "proxy.ntlmdomain");
        }
    }

    private void init()
    {
        tmsHost = null;
        tmsPort = 0;
        tmsPersist = true;
        tmsAckRequired = true;
        tmsAckMaxLostMessage = 0;
        tmsAckMessageRetry = 0;
        tmsAuditUpload = false;
        proxyHost = null;
        proxyPort = 0;
        proxyUserName = null;
        proxyPassword = null;
        proxyDomain = null;
        validSettings = true;
        errorMessage = "";
    }

    public String getProxyDomain()
    {
        return proxyDomain;
    }

    public void setProxyDomain(String s)
    {
        proxyDomain = s;
    }

    public String getProxyHost()
    {
        return proxyHost;
    }

    public void setProxyHost(String s)
    {
        proxyHost = s;
    }

    public String getProxyPassword()
    {
        return proxyPassword;
    }

    public void setProxyPassword(String s)
    {
        proxyPassword = s;
    }

    public int getProxyPort()
    {
        return proxyPort;
    }

    public void setProxyPort(int i)
    {
        proxyPort = i;
    }

    public String getProxyUserName()
    {
        return proxyUserName;
    }

    public void setProxyUserName(String s)
    {
        proxyUserName = s;
    }

    public int getTmsAckMaxLostMessage()
    {
        return tmsAckMaxLostMessage;
    }

    public void setTmsAckMaxLostMessage(int i)
    {
        tmsAckMaxLostMessage = i;
    }

    public int getTmsAckMessageRetry()
    {
        return tmsAckMessageRetry;
    }

    public void setTmsAckMessageRetry(int i)
    {
        tmsAckMessageRetry = i;
    }

    public boolean isTmsAckRequired()
    {
        return tmsAckRequired;
    }

    public void setTmsAckRequired(boolean flag)
    {
        tmsAckRequired = flag;
    }

    public boolean isTmsAuditUpload()
    {
        return tmsAuditUpload;
    }

    public void setTmsAuditUpload(boolean flag)
    {
        tmsAuditUpload = flag;
    }

    public String getTmsHost()
    {
        return tmsHost;
    }

    public void setTmsHost(String s)
    {
        tmsHost = s;
    }

    public boolean isTmsPersist()
    {
        return tmsPersist;
    }

    public void setTmsPersist(boolean flag)
    {
        tmsPersist = flag;
    }

    public int getTmsPort()
    {
        return tmsPort;
    }

    public void setTmsPort(int i)
    {
        tmsPort = i;
    }

    public String getTmsHostPort()
    {
        if(tmsPort > 0)
            return (new StringBuilder()).append(tmsHost).append(":").append(tmsPort).toString();
        else
            return tmsHost;
    }

    public String getProxyHostPort()
    {
        if(proxyPort > 0)
            return (new StringBuilder()).append(proxyHost).append(":").append(proxyPort).toString();
        else
            return proxyHost;
    }

    public boolean isValidSettings()
    {
        return validSettings;
    }

    public void setValidSettings(boolean flag)
    {
        validSettings = flag;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String s)
    {
        errorMessage = s;
    }

    private String resourceBundleGetString(ResourceBundle resourcebundle, String s)
    {
        return resourcebundle.getString(s).trim();
    }

    private int resourceBundleGetInt(ResourceBundle resourcebundle, String s, int i, int j)
    {
        int k = -1;
        String s1 = resourcebundle.getString(s).trim();
        try
        {
            k = (new Integer(s1)).intValue();
            if(k < i || k > j)
            {
                validSettings = false;
                String s2 = buildErrorMessage(s, "tdc.servletSetting.error.outOfRange");
                errorMessage = (new StringBuilder()).append(s2).append(" ").append(String.valueOf(i)).append(" - ").append(String.valueOf(j)).toString();
            }
        }
        catch(Exception exception)
        {
            validSettings = false;
            errorMessage = buildErrorMessage(s, "tdc.servletSetting.error.invalidInteger");
        }
        return k;
    }

    private int resourceBundleGetInt(ResourceBundle resourcebundle, String s)
    {
        int i = -1;
        String s1 = resourcebundle.getString(s).trim();
        try
        {
            if(s1.length() > 0)
                i = (new Integer(s1)).intValue();
        }
        catch(Exception exception)
        {
            validSettings = false;
            errorMessage = buildErrorMessage(s, "tdc.servletSetting.error.invalidInteger");
        }
        return i;
    }

    private boolean resourceBundleGetBoolean(ResourceBundle resourcebundle, String s)
    {
        boolean flag = false;
        String s1 = resourcebundle.getString(s).trim();
        if(s1.length() > 0)
        {
            s1 = s1.toLowerCase();
            if(s1.equals("true"))
                flag = true;
            else
            if(s1.equals("false"))
            {
                flag = false;
            } else
            {
                validSettings = false;
                errorMessage = buildErrorMessage(s, "tdc.servletSetting.error.invalidBoolean");
            }
        } else
        {
            validSettings = false;
            errorMessage = buildErrorMessage(s, "tdc.servletSetting.error.cannotBeBlank");
        }
        return flag;
    }

    private String buildErrorMessage(String s, String s1)
    {
        ResourceBundle resourcebundle = ResourceBundle.getBundle("tdcResources");
        String s2 = resourcebundle.getString("tdc.servletSetting.error.commonText");
        String s3 = resourcebundle.getString(s1);
        return (new StringBuilder()).append(s2).append(s).append(s3).toString();
    }

    static final long serialVersionUID = 1L;
    private String tmsHost;
    private int tmsPort;
    private boolean tmsPersist;
    private boolean tmsAckRequired;
    private int tmsAckMaxLostMessage;
    private int tmsAckMessageRetry;
    private boolean tmsAuditUpload;
    private String proxyHost;
    private int proxyPort;
    private String proxyUserName;
    private String proxyPassword;
    private String proxyDomain;
    private boolean validSettings;
    private String errorMessage;
}