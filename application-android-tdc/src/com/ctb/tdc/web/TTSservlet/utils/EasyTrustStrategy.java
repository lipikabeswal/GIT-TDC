// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 7/31/2012 12:35:48 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 

package com.ctb.tdc.web.TTSservlet.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ssl.TrustStrategy;



public class EasyTrustStrategy implements TrustStrategy
{

    public EasyTrustStrategy()
    {
    }

    public boolean isTrusted(X509Certificate ax509certificate[], String s)
        throws CertificateException
    {
        return true;
    }
}