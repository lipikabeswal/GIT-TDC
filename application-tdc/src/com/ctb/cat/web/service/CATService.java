/**
 * CATService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ctb.cat.web.service;

public interface CATService extends javax.xml.rpc.Service {
    public java.lang.String getCATServiceHttpSoap11EndpointAddress();

    public com.ctb.cat.web.service.CATServicePortType getCATServiceHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException;

    public com.ctb.cat.web.service.CATServicePortType getCATServiceHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
