/**
 * TestInitializationRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ctb.cat.web.data.xsd;

public class TestInitializationRequest  extends com.ctb.cat.web.data.xsd.CATServiceRequest  implements java.io.Serializable {
    private java.lang.Double abilityScore;

    private java.lang.String[] retakeIneligibleItems;

    public TestInitializationRequest() {
    }

    public TestInitializationRequest(
           java.lang.String configID,
           java.lang.String sessionID,
           java.lang.String studentID,
           java.lang.Double abilityScore,
           java.lang.String[] retakeIneligibleItems) {
        super(
            configID,
            sessionID,
            studentID);
        this.abilityScore = abilityScore;
        this.retakeIneligibleItems = retakeIneligibleItems;
    }


    /**
     * Gets the abilityScore value for this TestInitializationRequest.
     * 
     * @return abilityScore
     */
    public java.lang.Double getAbilityScore() {
        return abilityScore;
    }


    /**
     * Sets the abilityScore value for this TestInitializationRequest.
     * 
     * @param abilityScore
     */
    public void setAbilityScore(java.lang.Double abilityScore) {
        this.abilityScore = abilityScore;
    }


    /**
     * Gets the retakeIneligibleItems value for this TestInitializationRequest.
     * 
     * @return retakeIneligibleItems
     */
    public java.lang.String[] getRetakeIneligibleItems() {
        return retakeIneligibleItems;
    }


    /**
     * Sets the retakeIneligibleItems value for this TestInitializationRequest.
     * 
     * @param retakeIneligibleItems
     */
    public void setRetakeIneligibleItems(java.lang.String[] retakeIneligibleItems) {
        this.retakeIneligibleItems = retakeIneligibleItems;
    }

    public java.lang.String getRetakeIneligibleItems(int i) {
        return this.retakeIneligibleItems[i];
    }

    public void setRetakeIneligibleItems(int i, java.lang.String _value) {
        this.retakeIneligibleItems[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TestInitializationRequest)) return false;
        TestInitializationRequest other = (TestInitializationRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.abilityScore==null && other.getAbilityScore()==null) || 
             (this.abilityScore!=null &&
              this.abilityScore.equals(other.getAbilityScore()))) &&
            ((this.retakeIneligibleItems==null && other.getRetakeIneligibleItems()==null) || 
             (this.retakeIneligibleItems!=null &&
              java.util.Arrays.equals(this.retakeIneligibleItems, other.getRetakeIneligibleItems())));
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
        if (getAbilityScore() != null) {
            _hashCode += getAbilityScore().hashCode();
        }
        if (getRetakeIneligibleItems() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRetakeIneligibleItems());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRetakeIneligibleItems(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TestInitializationRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "TestInitializationRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("abilityScore");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "abilityScore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retakeIneligibleItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "retakeIneligibleItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
