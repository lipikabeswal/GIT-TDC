package com.ctb.cat.web.service;

public class CATServicePortTypeProxy implements com.ctb.cat.web.service.CATServicePortType {
  private String _endpoint = null;
  private com.ctb.cat.web.service.CATServicePortType cATServicePortType = null;
  
  public CATServicePortTypeProxy() {
    _initCATServicePortTypeProxy();
  }
  
  public CATServicePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initCATServicePortTypeProxy();
  }
  
  private void _initCATServicePortTypeProxy() {
    try {
      cATServicePortType = (new com.ctb.cat.web.service.CATServiceLocator()).getCATServiceHttpSoap11Endpoint();
      if (cATServicePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)cATServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)cATServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (cATServicePortType != null)
      ((javax.xml.rpc.Stub)cATServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.ctb.cat.web.service.CATServicePortType getCATServicePortType() {
    if (cATServicePortType == null)
      _initCATServicePortTypeProxy();
    return cATServicePortType;
  }
  
  public com.ctb.cat.web.data.xsd.ItemResponseResponse processItemResponse(com.ctb.cat.web.data.xsd.ItemResponseRequest request) throws java.rmi.RemoteException{
    if (cATServicePortType == null)
      _initCATServicePortTypeProxy();
    return cATServicePortType.processItemResponse(request);
  }
  
  public com.ctb.cat.web.data.xsd.TestInitializationResponse initializeTest(com.ctb.cat.web.data.xsd.TestInitializationRequest request) throws java.rmi.RemoteException{
    if (cATServicePortType == null)
      _initCATServicePortTypeProxy();
    return cATServicePortType.initializeTest(request);
  }
  
  
}