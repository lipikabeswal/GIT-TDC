/**
 * ItemResponseRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ctb.cat.web.data.xsd;

public class ItemResponseRequest  extends com.ctb.cat.web.data.xsd.CATServiceRequest  implements java.io.Serializable {
    private java.lang.Boolean exitTest;

    private java.lang.String exitTestReason;

    private java.lang.String itemID;

    private java.lang.Integer itemPosition;

    private java.lang.Integer rawScore;

    private java.lang.String response;

    private java.lang.Integer timeElapsed;

    public ItemResponseRequest() {
    }

    public ItemResponseRequest(
           java.lang.String sessionID,
           java.lang.Boolean exitTest,
           java.lang.String exitTestReason,
           java.lang.String itemID,
           java.lang.Integer itemPosition,
           java.lang.Integer rawScore,
           java.lang.String response,
           java.lang.Integer timeElapsed) {
        super(
            sessionID);
        this.exitTest = exitTest;
        this.exitTestReason = exitTestReason;
        this.itemID = itemID;
        this.itemPosition = itemPosition;
        this.rawScore = rawScore;
        this.response = response;
        this.timeElapsed = timeElapsed;
    }


    /**
     * Gets the exitTest value for this ItemResponseRequest.
     * 
     * @return exitTest
     */
    public java.lang.Boolean getExitTest() {
        return exitTest;
    }


    /**
     * Sets the exitTest value for this ItemResponseRequest.
     * 
     * @param exitTest
     */
    public void setExitTest(java.lang.Boolean exitTest) {
        this.exitTest = exitTest;
    }


    /**
     * Gets the exitTestReason value for this ItemResponseRequest.
     * 
     * @return exitTestReason
     */
    public java.lang.String getExitTestReason() {
        return exitTestReason;
    }


    /**
     * Sets the exitTestReason value for this ItemResponseRequest.
     * 
     * @param exitTestReason
     */
    public void setExitTestReason(java.lang.String exitTestReason) {
        this.exitTestReason = exitTestReason;
    }


    /**
     * Gets the itemID value for this ItemResponseRequest.
     * 
     * @return itemID
     */
    public java.lang.String getItemID() {
        return itemID;
    }


    /**
     * Sets the itemID value for this ItemResponseRequest.
     * 
     * @param itemID
     */
    public void setItemID(java.lang.String itemID) {
        this.itemID = itemID;
    }


    /**
     * Gets the itemPosition value for this ItemResponseRequest.
     * 
     * @return itemPosition
     */
    public java.lang.Integer getItemPosition() {
        return itemPosition;
    }


    /**
     * Sets the itemPosition value for this ItemResponseRequest.
     * 
     * @param itemPosition
     */
    public void setItemPosition(java.lang.Integer itemPosition) {
        this.itemPosition = itemPosition;
    }


    /**
     * Gets the rawScore value for this ItemResponseRequest.
     * 
     * @return rawScore
     */
    public java.lang.Integer getRawScore() {
        return rawScore;
    }


    /**
     * Sets the rawScore value for this ItemResponseRequest.
     * 
     * @param rawScore
     */
    public void setRawScore(java.lang.Integer rawScore) {
        this.rawScore = rawScore;
    }


    /**
     * Gets the response value for this ItemResponseRequest.
     * 
     * @return response
     */
    public java.lang.String getResponse() {
        return response;
    }


    /**
     * Sets the response value for this ItemResponseRequest.
     * 
     * @param response
     */
    public void setResponse(java.lang.String response) {
        this.response = response;
    }


    /**
     * Gets the timeElapsed value for this ItemResponseRequest.
     * 
     * @return timeElapsed
     */
    public java.lang.Integer getTimeElapsed() {
        return timeElapsed;
    }


    /**
     * Sets the timeElapsed value for this ItemResponseRequest.
     * 
     * @param timeElapsed
     */
    public void setTimeElapsed(java.lang.Integer timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ItemResponseRequest)) return false;
        ItemResponseRequest other = (ItemResponseRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.exitTest==null && other.getExitTest()==null) || 
             (this.exitTest!=null &&
              this.exitTest.equals(other.getExitTest()))) &&
            ((this.exitTestReason==null && other.getExitTestReason()==null) || 
             (this.exitTestReason!=null &&
              this.exitTestReason.equals(other.getExitTestReason()))) &&
            ((this.itemID==null && other.getItemID()==null) || 
             (this.itemID!=null &&
              this.itemID.equals(other.getItemID()))) &&
            ((this.itemPosition==null && other.getItemPosition()==null) || 
             (this.itemPosition!=null &&
              this.itemPosition.equals(other.getItemPosition()))) &&
            ((this.rawScore==null && other.getRawScore()==null) || 
             (this.rawScore!=null &&
              this.rawScore.equals(other.getRawScore()))) &&
            ((this.response==null && other.getResponse()==null) || 
             (this.response!=null &&
              this.response.equals(other.getResponse()))) &&
            ((this.timeElapsed==null && other.getTimeElapsed()==null) || 
             (this.timeElapsed!=null &&
              this.timeElapsed.equals(other.getTimeElapsed())));
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
        if (getExitTest() != null) {
            _hashCode += getExitTest().hashCode();
        }
        if (getExitTestReason() != null) {
            _hashCode += getExitTestReason().hashCode();
        }
        if (getItemID() != null) {
            _hashCode += getItemID().hashCode();
        }
        if (getItemPosition() != null) {
            _hashCode += getItemPosition().hashCode();
        }
        if (getRawScore() != null) {
            _hashCode += getRawScore().hashCode();
        }
        if (getResponse() != null) {
            _hashCode += getResponse().hashCode();
        }
        if (getTimeElapsed() != null) {
            _hashCode += getTimeElapsed().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ItemResponseRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "ItemResponseRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exitTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "exitTest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exitTestReason");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "exitTestReason"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("itemID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "itemID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("itemPosition");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "itemPosition"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rawScore");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "rawScore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("response");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "response"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeElapsed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "timeElapsed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
