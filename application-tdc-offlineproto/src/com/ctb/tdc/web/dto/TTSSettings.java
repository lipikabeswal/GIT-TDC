package com.ctb.tdc.web.dto;

import java.util.ResourceBundle;

public class TTSSettings {
	private String url;
	private String voiceName;
	private String speedValue;
	
	public TTSSettings (ResourceBundle rb) {
		this.url = rb.getString("texthelp.url");
		this.voiceName = rb.getString("texthelp.voicename");
		this.speedValue = rb.getString("texthelp.speedvalue");
	}
	
	/**
	 * @return Returns the speedValue.
	 */
	public String getSpeedValue() {
		return speedValue;
	}
	/**
	 * @param speedValue The speedValue to set.
	 */
	public void setSpeedValue(String speedValue) {
		this.speedValue = speedValue;
	}
	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return Returns the voiceName.
	 */
	public String getVoiceName() {
		return voiceName;
	}
	/**
	 * @param voiceName The voiceName to set.
	 */
	public void setVoiceName(String voiceName) {
		this.voiceName = voiceName;
	}
	
	
}
