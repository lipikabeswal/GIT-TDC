package com.ctb.tdc.tsutility.analyzer;

public class AnalyzerException extends Exception {

	private static final long serialVersionUID = 1L;
	private String displayMessage;
	
	
	public AnalyzerException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AnalyzerException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AnalyzerException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AnalyzerException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}
	
	public String getDisplayMessage() {
		return this.displayMessage != null ? this.displayMessage : this.getMessage();
	}
}
