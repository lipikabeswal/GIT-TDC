package com.ctb.tdc.web.dto;

import java.io.Serializable;

public class TrackerData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long sequenceNo;
	private String value;
	private String hashValue;
	private Long next;

	/**
	 * @return the hashValue
	 */
	public String getHashValue() {
		return hashValue;
	}

	/**
	 * @param hashValue the hashValue to set
	 */
	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the sequenceNo
	 */
	public long getSequenceNo() {
		return sequenceNo;
	}

	/**
	 * @param sequenceNo
	 *            the sequenceNo to set
	 */
	public void setSequenceNo(long sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	/**
	 * @return the next
	 */
	public Long getNext() {
		return next;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(Long next) {
		this.next = next;
	}

}
