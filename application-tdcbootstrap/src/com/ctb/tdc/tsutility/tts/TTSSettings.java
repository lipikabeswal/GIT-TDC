package com.ctb.tdc.tsutility.tts;
 
import java.util.ResourceBundle;

public class TTSSettings {
	private String url;
	private String host;
	private String voiceName;
	private String speedValue;
	private String userName;
	private String password;
	
    public TTSSettings() {
        init();
    }
    
    private void init() {
		/*this.url = "https://oastts.ctb.com/SpeechServer/";
		this.voiceName = "ScanSoft Jill_Full_22kHz";
		this.speedValue = "-2";
		this.userName = "5ACmaE0j245f";
		this.password = "5ACmf1U5";
		this.host = "oastts.ctb.com";*/
		//Making changes for new TTS
    	this.url = "http://app.readspeaker.com/cgi-bin/rsent";
		this.voiceName = "Kate";
		this.speedValue = "-2";
		this.userName = "5ACmaE0j245f";
		this.password = "5ACmf1U5";
		this.host = "app.readspeaker.com";
    	
    }
	
	public TTSSettings (ResourceBundle rb) {
		
		this.url = rb.getString("texthelp.url");
		this.voiceName = rb.getString("texthelp.voicename");
		this.speedValue = rb.getString("texthelp.speedvalue");
		this.userName = rb.getString("texthelp.username");
		this.password = rb.getString("texthelp.password");
		this.host = rb.getString("texthelp.host");
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
