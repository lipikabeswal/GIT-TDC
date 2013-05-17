/**
 * TestInitializationResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ctb.cat.web.data.xsd;

public class TestInitializationResponse  extends com.ctb.cat.web.data.xsd.CATServiceResponse  implements java.io.Serializable {
    private java.lang.String nextItemID;

    public TestInitializationResponse() {
    }

    public TestInitializationResponse(
           com.ctb.cat.web.data.xsd.ResearchDebugData researchDebugData,
           java.lang.String sessionID,
           java.lang.String statusCode,
           java.lang.String statusMessage,
           java.lang.String nextItemID) {
        super(
            researchDebugData,
            sessionID,
            statusCode,
            statusMessage);
        this.nextItemID = nextItemID;
    }


    /**
     * Gets the nextItemID value for this TestInitializationResponse.
     * 
     * @return nextItemID
     */
    public java.lang.String getNextItemID() {
        return nextItemID;
    }


    /**
     * Sets the nextItemID value for this TestInitializationResponse.
     * 
     * @param nextItemID
     */
    public void setNextItemID(java.lang.String nextItemID) {
        this.nextItemID = nextItemID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TestInitializationResponse)) return false;
        TestInitializationResponse other = (TestInitializationResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.nextItemID==null && other.getNextItemID()==null) || 
             (this.nextItemID!=null &&
              this.nextItemID.equals(other.getNextItemID())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getNextItemID() != null) {
            _hashCode += getNextItemID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TestInitializationResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "TestInitializationResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nextItemID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "nextItemID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
