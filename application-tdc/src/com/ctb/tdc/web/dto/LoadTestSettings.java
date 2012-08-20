package com.ctb.tdc.web.dto;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LoadTestSettings {
	
	private int contentRequestInterval;
	private int persistenceRequestInterval;
	private int tmsRequestInterval;
	
	public LoadTestSettings() {
        init();
    }
	
	private void init(){
		this.contentRequestInterval = 1;
		this.persistenceRequestInterval = 10;
		this.tmsRequestInterval = 3600;
	}
	
	public LoadTestSettings(ResourceBundle rbLoadTest){
		init();
		
		if (rbLoadTest != null){
			try{
				this.contentRequestInterval = new Integer(rbLoadTest.getString("loadtest.content.requestInterval")).intValue();
			}catch(MissingResourceException mre){
				this.contentRequestInterval = 1;
			}
			try{
				this.persistenceRequestInterval = new Integer(rbLoadTest.getString("loadtest.persistence.requestInterval")).intValue();
			}catch(MissingResourceException mre){
				this.persistenceRequestInterval = 10;
			}
			try{
				this.tmsRequestInterval = new Integer(rbLoadTest.getString("loadtest.tms.requestInterval")).intValue();
			}catch(MissingResourceException mre){
				this.tmsRequestInterval = 3600;
			}
		}
	}
	
	public void setContentRequestInterval(int contentRequestInterval){
		this.contentRequestInterval=contentRequestInterval;
	}
	
	public int getContentRequestInterval(){
		return this.contentRequestInterval;
	}
	
	public void setPersistenceRequestInterval(int persistenceRequestInterval){
		this.persistenceRequestInterval=persistenceRequestInterval;
	}
	
	public int getPersistenceRequestInterval(){
		return this.persistenceRequestInterval;
	}
	
	public void setTmsRequestInterval(int tmsRequestInterval){
		this.tmsRequestInterval=tmsRequestInterval;
	}
	
	public int getTmsRequestInterval(){
		return this.tmsRequestInterval;
	}
}
