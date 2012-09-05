// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 7/31/2012 12:35:15 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 

package com.ctb.tdc.web.TTServlet.dto;

import java.io.Serializable;

public class StateVO
    implements Serializable
{

    public StateVO(int i, String s)
    {
        mseq = i;
        state = s;
    }

    public int getMseq()
    {
        return mseq;
    }

    public void setMseq(int i)
    {
        mseq = i;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String s)
    {
        state = s;
    }

    static final long serialVersionUID = 1L;
    public static final String PENDING_STATE = "pending";
    public static final String ACTKNOWLEDGE_STATE = "ackknowledge";
    private int mseq;
    private String state;
}