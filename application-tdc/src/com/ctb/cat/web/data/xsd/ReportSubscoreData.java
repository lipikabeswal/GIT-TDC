/**
 * ReportSubscoreData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ctb.cat.web.data.xsd;

public class ReportSubscoreData  implements java.io.Serializable {
    private java.lang.Integer numOfItems;

    private java.lang.Double rawSubscore;

    private java.lang.Double subscore;

    private java.lang.String subscoreCategory;

    private java.lang.String subscorePerfLevel;

    public ReportSubscoreData() {
    }

    public ReportSubscoreData(
           java.lang.Integer numOfItems,
           java.lang.Double rawSubscore,
           java.lang.Double subscore,
           java.lang.String subscoreCategory,
           java.lang.String subscorePerfLevel) {
           this.numOfItems = numOfItems;
           this.rawSubscore = rawSubscore;
           this.subscore = subscore;
           this.subscoreCategory = subscoreCategory;
           this.subscorePerfLevel = subscorePerfLevel;
    }


    /**
     * Gets the numOfItems value for this ReportSubscoreData.
     * 
     * @return numOfItems
     */
    public java.lang.Integer getNumOfItems() {
        return numOfItems;
    }


    /**
     * Sets the numOfItems value for this ReportSubscoreData.
     * 
     * @param numOfItems
     */
    public void setNumOfItems(java.lang.Integer numOfItems) {
        this.numOfItems = numOfItems;
    }


    /**
     * Gets the rawSubscore value for this ReportSubscoreData.
     * 
     * @return rawSubscore
     */
    public java.lang.Double getRawSubscore() {
        return rawSubscore;
    }


    /**
     * Sets the rawSubscore value for this ReportSubscoreData.
     * 
     * @param rawSubscore
     */
    public void setRawSubscore(java.lang.Double rawSubscore) {
        this.rawSubscore = rawSubscore;
    }


    /**
     * Gets the subscore value for this ReportSubscoreData.
     * 
     * @return subscore
     */
    public java.lang.Double getSubscore() {
        return subscore;
    }


    /**
     * Sets the subscore value for this ReportSubscoreData.
     * 
     * @param subscore
     */
    public void setSubscore(java.lang.Double subscore) {
        this.subscore = subscore;
    }


    /**
     * Gets the subscoreCategory value for this ReportSubscoreData.
     * 
     * @return subscoreCategory
     */
    public java.lang.String getSubscoreCategory() {
        return subscoreCategory;
    }


    /**
     * Sets the subscoreCategory value for this ReportSubscoreData.
     * 
     * @param subscoreCategory
     */
    public void setSubscoreCategory(java.lang.String subscoreCategory) {
        this.subscoreCategory = subscoreCategory;
    }


    /**
     * Gets the subscorePerfLevel value for this ReportSubscoreData.
     * 
     * @return subscorePerfLevel
     */
    public java.lang.String getSubscorePerfLevel() {
        return subscorePerfLevel;
    }


    /**
     * Sets the subscorePerfLevel value for this ReportSubscoreData.
     * 
     * @param subscorePerfLevel
     */
    public void setSubscorePerfLevel(java.lang.String subscorePerfLevel) {
        this.subscorePerfLevel = subscorePerfLevel;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReportSubscoreData)) return false;
        ReportSubscoreData other = (ReportSubscoreData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.numOfItems==null && other.getNumOfItems()==null) || 
             (this.numOfItems!=null &&
              this.numOfItems.equals(other.getNumOfItems()))) &&
            ((this.rawSubscore==null && other.getRawSubscore()==null) || 
             (this.rawSubscore!=null &&
              this.rawSubscore.equals(other.getRawSubscore()))) &&
            ((this.subscore==null && other.getSubscore()==null) || 
             (this.subscore!=null &&
              this.subscore.equals(other.getSubscore()))) &&
            ((this.subscoreCategory==null && other.getSubscoreCategory()==null) || 
             (this.subscoreCategory!=null &&
              this.subscoreCategory.equals(other.getSubscoreCategory()))) &&
            ((this.subscorePerfLevel==null && other.getSubscorePerfLevel()==null) || 
             (this.subscorePerfLevel!=null &&
              this.subscorePerfLevel.equals(other.getSubscorePerfLevel())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getNumOfItems() != null) {
            _hashCode += getNumOfItems().hashCode();
        }
        if (getRawSubscore() != null) {
            _hashCode += getRawSubscore().hashCode();
        }
        if (getSubscore() != null) {
            _hashCode += getSubscore().hashCode();
        }
        if (getSubscoreCategory() != null) {
            _hashCode += getSubscoreCategory().hashCode();
        }
        if (getSubscorePerfLevel() != null) {
            _hashCode += getSubscorePerfLevel().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReportSubscoreData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "ReportSubscoreData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numOfItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "numOfItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rawSubscore");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "rawSubscore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subscore");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "subscore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subscoreCategory");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "subscoreCategory"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subscorePerfLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "subscorePerfLevel"));
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
