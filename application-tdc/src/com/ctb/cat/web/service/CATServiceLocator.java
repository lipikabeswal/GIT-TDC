/**
 * CATServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ctb.cat.web.service;

public class CATServiceLocator extends org.apache.axis.client.Service implements com.ctb.cat.web.service.CATService {

    public CATServiceLocator() {
    }


    public CATServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CATServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CATServiceHttpSoap11Endpoint
    private java.lang.String CATServiceHttpSoap11Endpoint_address = "http://localhost:8080/CATWebservice/services/CATService.CATServiceHttpSoap11Endpoint/";

    public java.lang.String getCATServiceHttpSoap11EndpointAddress() {
        return CATServiceHttpSoap11Endpoint_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CATServiceHttpSoap11EndpointWSDDServiceName = "CATServiceHttpSoap11Endpoint";

    public java.lang.String getCATServiceHttpSoap11EndpointWSDDServiceName() {
        return CATServiceHttpSoap11EndpointWSDDServiceName;
    }

    public void setCATServiceHttpSoap11EndpointWSDDServiceName(java.lang.String name) {
        CATServiceHttpSoap11EndpointWSDDServiceName = name;
    }

    public com.ctb.cat.web.service.CATServicePortType getCATServiceHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CATServiceHttpSoap11Endpoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCATServiceHttpSoap11Endpoint(endpoint);
    }

    public com.ctb.cat.web.service.CATServicePortType getCATServiceHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.ctb.cat.web.service.CATServiceSoap11BindingStub _stub = new com.ctb.cat.web.service.CATServiceSoap11BindingStub(portAddress, this);
            _stub.setPortName(getCATServiceHttpSoap11EndpointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCATServiceHttpSoap11EndpointEndpointAddress(java.lang.String address) {
        CATServiceHttpSoap11Endpoint_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.ctb.cat.web.service.CATServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.ctb.cat.web.service.CATServiceSoap11BindingStub _stub = new com.ctb.cat.web.service.CATServiceSoap11BindingStub(new java.net.URL(CATServiceHttpSoap11Endpoint_address), this);
                _stub.setPortName(getCATServiceHttpSoap11EndpointWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CATServiceHttpSoap11Endpoint".equals(inputPortName)) {
            return getCATServiceHttpSoap11Endpoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.web.cat.ctb.com", "CATService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.web.cat.ctb.com", "CATServiceHttpSoap11Endpoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CATServiceHttpSoap11Endpoint".equals(portName)) {
            setCATServiceHttpSoap11EndpointEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
