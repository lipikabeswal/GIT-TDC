/*synchronized*/

package com.ctb.tdc.tsutility.analyzer;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

import com.ctb.tdc.tsutility.ui.BandwidthMainWindow;
import com.ctb.tdc.tsutility.ui.MainWindow;


public class SimulatorList extends Thread {

	public BandwidthMainWindow ui = null;
	public ArrayList simulatorList = null;
	public int numberMachines = 0;
	public String url = null;
	public long duration = 0;	
	private long timeToRun = 0;								
	private static final int CONNECTION_TIMEOUT = 15 * 1000;
	
	/**
	 * 
	 *
	 */
	public SimulatorList(BandwidthMainWindow window, int numberMachines, String url, int secondToRun) {
		this.ui = window;		
		this.numberMachines = numberMachines;
		this.url = url;
		this.timeToRun = secondToRun * 1000;		// mill sec			
	}

	/**
	 * 
	 */
	public void initSimulation() {
		this.simulatorList = new ArrayList();	
		HttpClient client = newClient();
		for (int iteration = 1; iteration <= this.numberMachines ; iteration++) {				
			Simulator simulator = new Simulator(client, url);
			this.simulatorList.add(simulator);			
		}
		
	}
	
	private HttpClient newClient() {
		HttpClient client = new HttpClient();

		client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
		MultiThreadedHttpConnectionManager cm = new MultiThreadedHttpConnectionManager();
		cm.setMaxConnectionsPerHost(this.numberMachines);
		cm.setMaxTotalConnections(this.numberMachines);
		client.setHttpConnectionManager(cm);

		ResourceBundle proxyRB = ResourceBundle.getBundle("proxy");
		String proxyHost = proxyRB.getString("proxy.host");
		String proxyPort = proxyRB.getString("proxy.port");
		String proxyUsername = proxyRB.getString("proxy.username");
		String proxyPassword = proxyRB.getString("proxy.password");
		
		
		if(MainWindow.getProxyHost() != null){
			proxyHost = MainWindow.getProxyHost();
		}
		if(MainWindow.getProxyPort()!= null){
			proxyPort = MainWindow.getProxyPort();
		}
		if(MainWindow.getProxyUsername() != null){
			proxyUsername = MainWindow.getProxyUsername();
		}
		if(MainWindow.getProxyPassword() != null){
			proxyPassword = MainWindow.getProxyPassword();
		}
	
		boolean proxyHostDefined = proxyHost != null && proxyHost.length() > 0;
		boolean proxyPortDefined = proxyPort != null && proxyPort.length() > 0;
		boolean proxyUsernameDefined = proxyUsername != null && proxyUsername.length() > 0;
		
		
		if( proxyHostDefined && proxyPortDefined ) {
			client.getHostConfiguration().setProxy(proxyHost, Integer.valueOf(proxyPort));
		} else if( proxyHostDefined ) {
			client.getHostConfiguration().setProxyHost(new ProxyHost(proxyHost) );
		}
	
		if( proxyHostDefined && proxyUsernameDefined ) {
			AuthScope proxyScope;
			
			if( proxyPortDefined )
				proxyScope = new AuthScope(proxyHost, Integer.valueOf(proxyPort), AuthScope.ANY_REALM);
			else
				proxyScope = new AuthScope(proxyHost, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
			
			UsernamePasswordCredentials upc = new UsernamePasswordCredentials(proxyUsername, proxyPassword);
			client.getParams().setAuthenticationPreemptive(true);
			client.getState().setProxyCredentials(proxyScope, upc);
		}
		return client;
	}
	
	 
	/**
	 * run()
	 */
	public void run() {
		try {
			boolean continueToRun = true;
			for(int i=0 ; ((i<ui.simulationArray.size()) && continueToRun) ; i++) {
				ui.getResultProgressBar().setMaximum((int)this.timeToRun/1000);
				ui.setDownloadProgress(0);
				this.numberMachines = ((Integer) ui.simulationArray.get(i)).intValue();
				this.initSimulation();
				boolean done = false;
				long start = System.currentTimeMillis();
				startSimulators();
				while (!done) {
					duration = System.currentTimeMillis() - start;
					int numberSeconds = (int)(duration / 1000);	// seconds
					ui.setDownloadProgress(numberSeconds);
					if (duration > this.timeToRun) {
						stopSimulators();						
						continueToRun = ui.showSimulationDetails(this);
						done = true;
					}
					Thread.sleep(1000);
				}	
			}
			ui.endProcess(false);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	
	/**
	 * 
	 */
	public synchronized void stopSimulators() {
		
		int i;
		Simulator simulator;
		
		long start = System.currentTimeMillis();
		
		ui.getResultProgressBar().setMaximum(this.simulatorList.size());
		
		if (this.simulatorList != null) {
			for (i=0 ; i<this.simulatorList.size() ; i++) {			
				
				simulator = (Simulator)this.simulatorList.get(i);

				simulator.diedie();
				((MultiThreadedHttpConnectionManager) simulator.client.getHttpConnectionManager()).shutdown();
				ui.setDownloadProgress(i + 1);

				try {
					simulator.join(1000000);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
				//simulator.stop();
			}
		}
		
		long elaspe = System.currentTimeMillis() - start;
		System.out.println("stopSimulators took = " + elaspe);
		
	}
		
	/**
	 * 
	 */
	public synchronized void startSimulators() {
		
		for (int iteration = 0; iteration < this.simulatorList.size() ; iteration++) {				
			Simulator simulator = (Simulator)this.simulatorList.get(iteration);
			simulator.start();
		}		
		
	}


	public double getBandwidth() {		
		double bandwidth = 0;
		for (int i=0 ; i<this.simulatorList.size() ; i++) {
			Simulator simulator = (Simulator)this.simulatorList.get(i);
			bandwidth += simulator.getBandWidth();		
		}
		return bandwidth;
	}
	
	
	public long getTotalBytesRead() {		
		long totalBytesRead = 0;
		for (int i=0 ; i<this.simulatorList.size() ; i++) {
			Simulator simulator = (Simulator)this.simulatorList.get(i);
			totalBytesRead += simulator.getTotalBytesRead();		
		}
		return totalBytesRead;
	}


	public int getNumberMachines() {
		return numberMachines;
	}


	public void setNumberMachines(int numberMachines) {
		this.numberMachines = numberMachines;
	}


	public ArrayList getSimulatorList() {
		return simulatorList;
	}


	public void setSimulatorList(ArrayList simulators) {
		this.simulatorList = simulators;
	}


	public long getTimeToRun() {
		return timeToRun;
	}


	public void setTimeToRun(long timeToRun) {
		this.timeToRun = timeToRun;
	}


	public BandwidthMainWindow getUi() {
		return ui;
	}


	public void setUi(BandwidthMainWindow ui) {
		this.ui = ui;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	
}
