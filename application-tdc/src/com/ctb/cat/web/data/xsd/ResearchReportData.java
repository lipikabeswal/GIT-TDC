/**
 * ResearchReportData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ctb.cat.web.data.xsd;

public class ResearchReportData  implements java.io.Serializable {
    private java.lang.Integer correctionNum;

    private java.lang.String performanceLevel;

    private java.lang.Double rawScore;

    private java.lang.Double scaleScore;

    private java.lang.Double sem;

    private com.ctb.cat.web.data.xsd.ReportSubscoreData[] subscoreList;

    private java.lang.Double theta;

    public ResearchReportData() {
    }

    public ResearchReportData(
           java.lang.Integer correctionNum,
           java.lang.String performanceLevel,
           java.lang.Double rawScore,
           java.lang.Double scaleScore,
           java.lang.Double sem,
           com.ctb.cat.web.data.xsd.ReportSubscoreData[] subscoreList,
           java.lang.Double theta) {
           this.correctionNum = correctionNum;
           this.performanceLevel = performanceLevel;
           this.rawScore = rawScore;
           this.scaleScore = scaleScore;
           this.sem = sem;
           this.subscoreList = subscoreList;
           this.theta = theta;
    }


    /**
     * Gets the correctionNum value for this ResearchReportData.
     * 
     * @return correctionNum
     */
    public java.lang.Integer getCorrectionNum() {
        return correctionNum;
    }


    /**
     * Sets the correctionNum value for this ResearchReportData.
     * 
     * @param correctionNum
     */
    public void setCorrectionNum(java.lang.Integer correctionNum) {
        this.correctionNum = correctionNum;
    }


    /**
     * Gets the performanceLevel value for this ResearchReportData.
     * 
     * @return performanceLevel
     */
    public java.lang.String getPerformanceLevel() {
        return performanceLevel;
    }


    /**
     * Sets the performanceLevel value for this ResearchReportData.
     * 
     * @param performanceLevel
     */
    public void setPerformanceLevel(java.lang.String performanceLevel) {
        this.performanceLevel = performanceLevel;
    }


    /**
     * Gets the rawScore value for this ResearchReportData.
     * 
     * @return rawScore
     */
    public java.lang.Double getRawScore() {
        return rawScore;
    }


    /**
     * Sets the rawScore value for this ResearchReportData.
     * 
     * @param rawScore
     */
    public void setRawScore(java.lang.Double rawScore) {
        this.rawScore = rawScore;
    }


    /**
     * Gets the scaleScore value for this ResearchReportData.
     * 
     * @return scaleScore
     */
    public java.lang.Double getScaleScore() {
        return scaleScore;
    }


    /**
     * Sets the scaleScore value for this ResearchReportData.
     * 
     * @param scaleScore
     */
    public void setScaleScore(java.lang.Double scaleScore) {
        this.scaleScore = scaleScore;
    }


    /**
     * Gets the sem value for this ResearchReportData.
     * 
     * @return sem
     */
    public java.lang.Double getSem() {
        return sem;
    }


    /**
     * Sets the sem value for this ResearchReportData.
     * 
     * @param sem
     */
    public void setSem(java.lang.Double sem) {
        this.sem = sem;
    }


    /**
     * Gets the subscoreList value for this ResearchReportData.
     * 
     * @return subscoreList
     */
    public com.ctb.cat.web.data.xsd.ReportSubscoreData[] getSubscoreList() {
        return subscoreList;
    }


    /**
     * Sets the subscoreList value for this ResearchReportData.
     * 
     * @param subscoreList
     */
    public void setSubscoreList(com.ctb.cat.web.data.xsd.ReportSubscoreData[] subscoreList) {
        this.subscoreList = subscoreList;
    }

    public com.ctb.cat.web.data.xsd.ReportSubscoreData getSubscoreList(int i) {
        return this.subscoreList[i];
    }

    public void setSubscoreList(int i, com.ctb.cat.web.data.xsd.ReportSubscoreData _value) {
        this.subscoreList[i] = _value;
    }


    /**
     * Gets the theta value for this ResearchReportData.
     * 
     * @return theta
     */
    public java.lang.Double getTheta() {
        return theta;
    }


    /**
     * Sets the theta value for this ResearchReportData.
     * 
     * @param theta
     */
    public void setTheta(java.lang.Double theta) {
        this.theta = theta;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResearchReportData)) return false;
        ResearchReportData other = (ResearchReportData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.correctionNum==null && other.getCorrectionNum()==null) || 
             (this.correctionNum!=null &&
              this.correctionNum.equals(other.getCorrectionNum()))) &&
            ((this.performanceLevel==null && other.getPerformanceLevel()==null) || 
             (this.performanceLevel!=null &&
              this.performanceLevel.equals(other.getPerformanceLevel()))) &&
            ((this.rawScore==null && other.getRawScore()==null) || 
             (this.rawScore!=null &&
              this.rawScore.equals(other.getRawScore()))) &&
            ((this.scaleScore==null && other.getScaleScore()==null) || 
             (this.scaleScore!=null &&
              this.scaleScore.equals(other.getScaleScore()))) &&
            ((this.sem==null && other.getSem()==null) || 
             (this.sem!=null &&
              this.sem.equals(other.getSem()))) &&
            ((this.subscoreList==null && other.getSubscoreList()==null) || 
             (this.subscoreList!=null &&
              java.util.Arrays.equals(this.subscoreList, other.getSubscoreList()))) &&
            ((this.theta==null && other.getTheta()==null) || 
             (this.theta!=null &&
              this.theta.equals(other.getTheta())));
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
        if (getCorrectionNum() != null) {
            _hashCode += getCorrectionNum().hashCode();
        }
        if (getPerformanceLevel() != null) {
            _hashCode += getPerformanceLevel().hashCode();
        }
        if (getRawScore() != null) {
            _hashCode += getRawScore().hashCode();
        }
        if (getScaleScore() != null) {
            _hashCode += getScaleScore().hashCode();
        }
        if (getSem() != null) {
            _hashCode += getSem().hashCode();
        }
        if (getSubscoreList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSubscoreList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSubscoreList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTheta() != null) {
            _hashCode += getTheta().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResearchReportData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "ResearchReportData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correctionNum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "correctionNum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("performanceLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "performanceLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rawScore");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "rawScore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scaleScore");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "scaleScore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sem");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "sem"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subscoreList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "subscoreList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "ReportSubscoreData"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("theta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://data.web.cat.ctb.com/xsd", "theta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
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
