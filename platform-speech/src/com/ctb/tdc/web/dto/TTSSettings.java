package com.ctb.tdc.web.dto;

import java.util.ResourceBundle;

public class TTSSettings {
	private String url;
	private String host;
	private String voiceName;
	private String speedValue;
	private String method;
	
	public static final String TEXTHELPMETHOD = "texthelp";
	public static final String READSPEAKERMETHOD = "readspeaker";
	
    public TTSSettings() {
        init();
    }
    
    private void init() {
    
    }
	
	public String getMethod() {
		return method;
	}

	public TTSSettings (ResourceBundle rb) {
		this.method = rb.getString("tts.method");
		if(READSPEAKERMETHOD.equals(this.method)) {
			this.url = rb.getString("readspeaker.url");
			this.voiceName = rb.getString("readspeaker.voicename");
			this.speedValue = rb.getString("readspeaker.speedvalue");
			this.host = rb.getString("readspeaker.host");
		} else {
			this.url = rb.getString("texthelp.url");
			this.voiceName = rb.getString("texthelp.voicename");
			if(this.voiceName == null || "".equals(this.voiceName.trim())) {
				this.voiceName = "ScanSoft Samantha_Full_22kHz";
			}
			this.speedValue = rb.getString("texthelp.speedvalue");
			this.host = rb.getString("texthelp.host");
		}
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

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
}
