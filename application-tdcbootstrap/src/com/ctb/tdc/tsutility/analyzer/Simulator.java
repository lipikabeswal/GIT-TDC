/*synchronized*/

package com.ctb.tdc.tsutility.analyzer;

import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;


public class Simulator extends Thread {
	
	private int threadId = 0;
	private String url_download_file = null;
	private double bandwidth = 0;			// Mbps
	long totalBytesRead = 0;	
	public HttpClient client;
	
	/**
	 * 
	 *
	 */
	public Simulator(HttpClient client, String url) {
		this.totalBytesRead = 0;
		this.bandwidth = 0;
		this.totalBytesRead = 0;
		this.client = client;
		this.url_download_file = url;
	}

	private volatile String flag = "live";
	
    public void diedie() {
        flag = "die";
    }

	
	/**
	 * run()
	 */
	public void run() {
		try {
			while("live".equals(this.flag)) {
				Thread.sleep(100);
				downloadFile(this.client);
			}
		} catch(InterruptedException ie) {
		 // do nothing
		} 
	}

	/**
	 * downloadFile()
	 */
	private void downloadFile(HttpClient client) {
		
		GetMethod downloadMethod = new GetMethod(this.url_download_file);		
		
		try {
			
			int responseCode = client.executeMethod(downloadMethod);
						
			if( responseCode == HttpURLConnection.HTTP_OK ) {
			
				InputStream is = downloadMethod.getResponseBodyAsStream();
				byte[] bytes = new byte[102400];
				int bytesRead = 0;
				
				while( (bytesRead = is.read(bytes)) > 0 && "live".equals(this.flag)) {
					
					this.totalBytesRead += (long)bytesRead;
				}
				
			}
			else {
				System.out.println("Error in client.executeMethod - responseCode = " + responseCode);				
			}
		} 
		catch (Exception e) {
			//e.printStackTrace();
		} finally {
			//downloadMethod.releaseConnection();
		}
	}

	public double getBandWidth() {
		return this.bandwidth;
	}

	public int getThreadId() {
		return this.threadId;
	}
	
	public long getTotalBytesRead() {
		return totalBytesRead;
	}

	public void setTotalBytesRead(long totalBytesRead) {
		this.totalBytesRead = totalBytesRead;
	}

 

}
