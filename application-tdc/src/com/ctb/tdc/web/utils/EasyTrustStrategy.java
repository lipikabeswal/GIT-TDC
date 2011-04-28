package com.ctb.tdc.web.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ssl.TrustStrategy;

public class EasyTrustStrategy implements TrustStrategy {

	public boolean isTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		return true;
	}

}
