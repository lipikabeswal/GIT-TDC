package com.ctb.cat.web.service;

/**
 * @author john_wang
 *
 */
public interface CATServicePortType extends java.rmi.Remote {
	
    public com.ctb.cat.web.data.xsd.ItemResponseResponse processItemResponse(com.ctb.cat.web.data.xsd.ItemResponseRequest request) throws java.rmi.RemoteException;
    public com.ctb.cat.web.data.xsd.TestInitializationResponse initializeTest(com.ctb.cat.web.data.xsd.TestInitializationRequest request) throws java.rmi.RemoteException;
	

}
