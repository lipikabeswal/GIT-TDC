// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 7/31/2012 12:35:39 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 

package com.ctb.tdc.web.TTServlet.dto;

import java.util.ResourceBundle;

public class TTSSettings
{

    public TTSSettings()
    {
        init();
    }

    private void init()
    {
    }

    public TTSSettings(ResourceBundle resourcebundle)
    {
        url = resourcebundle.getString("texthelp.url");
        voiceName = resourcebundle.getString("texthelp.voicename");
        speedValue = resourcebundle.getString("texthelp.speedvalue");
        userName = resourcebundle.getString("texthelp.username");
        password = resourcebundle.getString("texthelp.password");
        host = resourcebundle.getString("texthelp.host");
    }

    public String getSpeedValue()
    {
        return speedValue;
    }

    public void setSpeedValue(String s)
    {
        speedValue = s;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String s)
    {
        url = s;
    }

    public String getVoiceName()
    {
        return voiceName;
    }

    public void setVoiceName(String s)
    {
        voiceName = s;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String s)
    {
        password = s;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String s)
    {
        userName = s;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String s)
    {
        host = s;
    }

    private String url;
    private String host;
    private String voiceName;
    private String speedValue;
    private String userName;
    private String password;
}