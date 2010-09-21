package com.ctb.tdc.tsutility.analyzer;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.httpclient.methods.GetMethod;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;



import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import com.ctb.tdc.tsutility.AppResourceBundleUtil;
import com.ctb.tdc.tsutility.AppConstants.AnalysisState;
import com.ctb.tdc.tsutility.ui.MainWindow;

import com.ctb.tdc.bootstrap.processwrapper.JettyProcessWrapper;
import com.ctb.tdc.tsutility.io.TestSimulationFileUtils;

public class NetworkAnalyzer extends Thread {

	/* URLs to use from resource bundle. */
	private static final String URL_OAS_HTTP          		= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.oas.http");
	private static final String URL_OAS_HTTPS         		= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.oas.https");
	private static final String URL_OASDELIVERY_HTTP  		= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.oasdelivery.http");
	private static final String URL_OASDELIVERY_HTTPS 		= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.oasdelivery.https");
	private static final String URL_DYNAMIC_PAGES     		= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.dynamicPages");
	private static final String URL_CONTENT_DOWNLOAD  		= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.contentDownload");
	private static final String SIM_COMPLETE		  		= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.simulation.Complete");
	private static final String SIM_ERR_GENERAL  	  		= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.simulation.error.general");
	private static final String SIM_ERR_TDC_HOME 	  		= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.simulation.error.tdcHomeNotFound");
	private static final String SIM_ERR_CLIENT_RUNNING 		= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.simulation.error.testClientRunning");
	private static final String SIM_ERR_STARTING_SERVLET	= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.simulation.error.startingLocalServlet");
	private static final String SIM_ERR_READ_CTRL_FILE		= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.simulation.error.readControlFile");
	private static final String SIM_ERR_WRTIRE_CTRL_FILE	= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.simulation.error.writeControlFile");
	private static final String SIM_ERR_LOCAL_SERVLET		= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.simulation.error.localServlet");
	private static final String SIM_ERR_CONNECTION_FAILED	= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.simulation.error.connectionFailed");
	private static final String SIM_ERR_NOT_ALLOWED			= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.simulation.error.simulationNotAllowed");
	private static final String USER_INTERRUPT				= AppResourceBundleUtil.getString("tsutility.networkAnalyzer.error.interruptedByUser");
	private static final int CONNECTION_TIMEOUT = 15 * 1000;
	private static final String URL_LOAD_TEST = "http://127.0.0.1:12345/servlet/LoadTestServlet.do";
	public static final int SOCKET_FOR_SINGLE_INSTANCE = 12344;
	public static final int JETTY_SLEEP_INTERVAL = 5;
	public static final String RESPONSE_OK = "<OK />";
	
	private MainWindow ui = null;
	private HttpClient client = null;
	private HttpClient ldclient = null;
	private boolean userInterrupted = false;
	private JettyProcessWrapper jetty = null;
	private GetMethod downloadMethod = null;
	private boolean loadTestUserInterrupt = false;
	
	
	/**
	 * 
	 *
	 */
	public NetworkAnalyzer(MainWindow window) {
		
		this.ui = window;
		this.client = new HttpClient();
		this.client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
		this.ldclient = new HttpClient();
		this.ldclient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
	}

	
	/**
	 * 
	 *
	 */
	public void interruptByUser() {
		this.userInterrupted = true;
		loadTestUserInterrupt = true;
		if (jetty != null){
			if (jetty.isAlive()){			
				if (downloadMethod != null)
					downloadMethod.releaseConnection();
				jetty.shutdown();									
			}	
		}
		
	}
	

	private static String getTdcHome()   {

		String tdcHomeProperty = System.getProperty("tdc.home");

		if( tdcHomeProperty == null ) {
			System.out.println("tdc home not defined");
		}
		
		File tdcHome = new File( tdcHomeProperty );
		if( !tdcHome.isDirectory() ) {
			System.out.println("tdc home directory does not exist");
		}

		return tdcHome.getAbsolutePath();
	}
	
	private static boolean isMacOS() {
        String os = System.getProperty("os.name");
        if (os == null) 
            os = "";
        return ( os.toLowerCase().indexOf("mac") != -1 );
    }
	
	private static void copyPropertyFiles(String tdcHome)  {        
        try {
            String proxySrcFilePath = tdcHome + "/etc/proxy.properties";
            File proxySrcFile = new File(proxySrcFilePath);        
            FileInputStream proxySrcFos = new FileInputStream(proxySrcFile);
            
            String proxyDesFilePath = tdcHome + "/webapp/WEB-INF/classes/proxy.properties";
            File proxyDesFile = new File(proxyDesFilePath);        
            FileOutputStream proxyDesFos = new FileOutputStream(proxyDesFile, false);
            
            byte[] bytes = new byte[1024];
            int read = proxySrcFos.read(bytes);
            proxyDesFos.write(bytes, 0, read);
            proxyDesFos.close();
            proxySrcFos.close(); 
        }
        catch( IOException ioe ) {
        	ioe.printStackTrace();
        }
    }
	
	private static void deletePropertyFiles(String tdcHome) {        
	        String proxyFilePath = tdcHome + "/webapp/WEB-INF/classes/proxy.properties";
	        File proxyFile = new File(proxyFilePath);
	        proxyFile.delete();        
	}
	   
	
	/**
	 * TODO: Break this big method up please.
	 */
	public void run() {

		/* Configure the client */
		try {
			ResourceBundle proxyRB = ResourceBundle.getBundle("proxy");
			String proxyHost = proxyRB.getString("proxy.host");
			String proxyPort = proxyRB.getString("proxy.port");
			String proxyUsername = proxyRB.getString("proxy.username");
			String proxyPassword = proxyRB.getString("proxy.password");

			boolean proxyHostDefined = proxyHost != null && proxyHost.length() > 0;
			boolean proxyPortDefined = proxyPort != null && proxyPort.length() > 0;
			boolean proxyUsernameDefined = proxyUsername != null && proxyUsername.length() > 0;
			
			if( proxyHostDefined && proxyPortDefined ) {
				this.client.getHostConfiguration().setProxy(proxyHost, Integer.valueOf(proxyPort));
			} else if( proxyHostDefined ) {
				this.client.getHostConfiguration().setProxyHost(new ProxyHost(proxyHost) );
			}

			if( proxyHostDefined && proxyUsernameDefined ) {
				AuthScope proxyScope;
				
				if( proxyPortDefined )
					proxyScope = new AuthScope(proxyHost, Integer.valueOf(proxyPort), AuthScope.ANY_REALM);
				else
					proxyScope = new AuthScope(proxyHost, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
				
				UsernamePasswordCredentials upc = new UsernamePasswordCredentials(proxyUsername, proxyPassword);
				this.client.getParams().setAuthenticationPreemptive(true);
				this.client.getState().setProxyCredentials(proxyScope, upc);
			}

			
		} catch( NumberFormatException nfe ) {
			ui.setAnalysisInterrupted();
			ui.setResultForCheckingOas( AnalysisState.FAIL, AppResourceBundleUtil.getString("tsutility.networkAnalyzer.error.proxyPortMisconfigured") );
			ui.setResultForCheckingOasDelivery( AnalysisState.UNATTEMPTED, "" );
			ui.setResultForCheckingDynamicPages( AnalysisState.UNATTEMPTED, "" );
			ui.setResultForDownloadContent( AnalysisState.UNATTEMPTED, "", 0, 0);
			return;
		}
		
		try {
			checkUrl(URL_OAS_HTTP);
			checkUrl(URL_OAS_HTTPS);
			ui.setResultForCheckingOas( AnalysisState.PASS, "" );
		} catch (AnalyzerException ae) {
			ui.setAnalysisInterrupted();
			ui.setResultForCheckingOas( AnalysisState.FAIL, ae.getDisplayMessage() );
			ui.setResultForCheckingOasDelivery( AnalysisState.UNATTEMPTED, "" );
			ui.setResultForCheckingDynamicPages( AnalysisState.UNATTEMPTED, "" );
			ui.setResultForDownloadContent( AnalysisState.UNATTEMPTED, "", 0, 0);
			return;
		}
		
		try {
			checkUrl(URL_OASDELIVERY_HTTP);
			checkUrl(URL_OASDELIVERY_HTTPS);
			ui.setResultForCheckingOasDelivery( AnalysisState.PASS, "" );
		} catch (AnalyzerException ae) {
			ui.setAnalysisInterrupted();
			ui.setResultForCheckingOasDelivery( AnalysisState.FAIL, ae.getDisplayMessage() );
			ui.setResultForCheckingDynamicPages( AnalysisState.UNATTEMPTED, "" );
			ui.setResultForDownloadContent( AnalysisState.UNATTEMPTED, "", 0, 0);
			return;
		}
		
		try {
			String firstCheck = checkUrl(URL_DYNAMIC_PAGES);
			String secondCheck = checkUrl(URL_DYNAMIC_PAGES);
			if( firstCheck.equals(secondCheck) ) {
				AnalyzerException ae = new AnalyzerException();
				ae.setDisplayMessage( AppResourceBundleUtil.getString("tsutility.networkAnalyzer.error.dynamicPageCached") );
				throw ae;
			}
			ui.setResultForCheckingDynamicPages( AnalysisState.PASS, "" );
		} catch (AnalyzerException ae) {
			ui.setAnalysisInterrupted();
			ui.setResultForCheckingDynamicPages( AnalysisState.FAIL, ae.getDisplayMessage() );
			ui.setResultForDownloadContent( AnalysisState.UNATTEMPTED, "", 0, 0);
			return;
		}
		
		try {
			GetMethod downloadMethod = new GetMethod(URL_CONTENT_DOWNLOAD);
			Date startDate = new Date();
			if( this.client.executeMethod(downloadMethod) == HttpURLConnection.HTTP_OK ) {
			
				String contentLength = downloadMethod.getResponseHeader("Content-length").getValue();
				int fileSize = Integer.valueOf(contentLength);
				InputStream is = downloadMethod.getResponseBodyAsStream();
				byte[] bytes = new byte[10240];
				int bytesRead = 0;
				int totalBytesRead = 0;
				while( (bytesRead = is.read(bytes)) > 0 ) {
					if( this.userInterrupted ) {
						ui.setAnalysisInterrupted();
						AnalyzerException ae = new AnalyzerException();
						ae.setDisplayMessage( AppResourceBundleUtil.getString("tsutility.networkAnalyzer.error.interruptedByUser") );
						throw ae;
					}
					totalBytesRead += bytesRead;
					double percentDownloaded = ((double)(totalBytesRead) / (double)(fileSize)) * 100;
					ui.setResultForDownloadContent( AnalysisState.PASS, "", (int)percentDownloaded, 1000);
				}
				//System.out.println("totalBytesRead=" + totalBytesRead);			

			} else {
				AnalyzerException ae = new AnalyzerException();
				ae.setDisplayMessage( "download: " + downloadMethod.getStatusLine().toString() );
				throw ae;
			}
			Date endDate = new Date();
			long duration = endDate.getTime() - startDate.getTime();
			//System.out.println("duration=" + duration);
			
			ui.setResultForDownloadContent( AnalysisState.PASS, "", 100, duration);
			
		} catch (AnalyzerException e) {
			ui.setAnalysisInterrupted();
			ui.setResultForDownloadContent( AnalysisState.FAIL, e.getDisplayMessage(), 0, 0);
			return;
		} catch (IOException e) {
			String errorMessage = AppResourceBundleUtil.getString("tsutility.networkAnalyzer.error.ioException").replace("$x", URL_CONTENT_DOWNLOAD);
			ui.setAnalysisInterrupted();
			ui.setResultForDownloadContent( AnalysisState.FAIL, errorMessage, 0, 0);
			return;
		}
		
		/********************************************************************************/
		/*       This section launches the test simulation task                         */
		/*		        Added by ayan as part of OAS8.5                                 */
		/********************************************************************************/
		
		ui.setResultForSimulateTest( AnalysisState.PASS, "", 0); //set task status to In progress		
		String details = "";
		boolean macOS = isMacOS();
		boolean getConfig = false;
		
		
		if (loadTestUserInterrupt){
			ui.setAnalysisInterrupted();
			ui.setResultForSimulateTest( AnalysisState.FAIL, USER_INTERRUPT , 0);
			return;
		}
		String tdcHome = getTdcHome();
		if (tdcHome == null){
			ui.setAnalysisInterrupted();
			ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_TDC_HOME, 0);
			return;
		}
		
		ServerSocket ss = null; // check if TDC is running on port 
		try {
			ss = new ServerSocket(SOCKET_FOR_SINGLE_INSTANCE, 1);
		} catch( Exception e ) {
			ui.setAnalysisInterrupted();
			ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_CLIENT_RUNNING , 0);
			return;
		}

		try{
			ss.close(); //release the socket
		}catch(IOException e){
			ui.setAnalysisInterrupted();
			ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_GENERAL , 0);
			return;
		}
		
		if (loadTestUserInterrupt){
			ui.setAnalysisInterrupted();
			ui.setResultForSimulateTest( AnalysisState.FAIL, USER_INTERRUPT , 0);
			return;
		}
		
		copyPropertyFiles(tdcHome); 
		//JettyProcessWrapper jetty = null;
		try {
			jetty = new JettyProcessWrapper(tdcHome, macOS);
		}catch( Exception e ) {
			ui.setAnalysisInterrupted();
			ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_STARTING_SERVLET , 0);
			return;
		}
		
		if (loadTestUserInterrupt){
			ui.setAnalysisInterrupted();
			ui.setResultForSimulateTest( AnalysisState.FAIL, USER_INTERRUPT , 0);
			return;
		}
		
		//check if load test run time has already been set, if not then send get load test config request
		Date currentTime = new Date();
		Date loadTestRunTime = TestSimulationFileUtils.getLoadTestRunTime(tdcHome);
		
		if (loadTestRunTime == null){
			ui.setAnalysisInterrupted();
			ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_READ_CTRL_FILE , 0);
			return;
		}
		if (currentTime.compareTo(loadTestRunTime) < 0){
			getConfig = true;
			try{
				//reset the last config request time forcing the load test servlet to always make a config request
				TestSimulationFileUtils.resetConfigRequestTime(tdcHome); 
			}catch(IOException e){
				ui.setAnalysisInterrupted();
				ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_WRTIRE_CTRL_FILE , 0);
				return;
			}
		}				
		jetty.start();
		while( jetty.isAlive() ) {
			try{
				Thread.sleep( JETTY_SLEEP_INTERVAL * 1000 );
			}catch(Exception e){
				ui.setAnalysisInterrupted();
				ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_LOCAL_SERVLET , 0);
				return;
			}								
			
			downloadMethod = new GetMethod(URL_LOAD_TEST);					
			if(getConfig){ //send get load test config request
				try{
					if( this.ldclient.executeMethod(downloadMethod) != HttpURLConnection.HTTP_OK ) {
						ui.setAnalysisInterrupted();
						
						if (loadTestUserInterrupt){
							ui.setResultForSimulateTest( AnalysisState.FAIL, USER_INTERRUPT , 0);
						}else{
							ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_CONNECTION_FAILED , 0);
						}
						downloadMethod.releaseConnection();
						jetty.shutdown();
						return;					
					}
					if( !downloadMethod.getResponseBodyAsString().contains(RESPONSE_OK) ) {
						ui.setAnalysisInterrupted();
						if (loadTestUserInterrupt){
							ui.setResultForSimulateTest( AnalysisState.FAIL, USER_INTERRUPT , 0);
						}else{
							ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_CONNECTION_FAILED , 0);
						}						
						downloadMethod.releaseConnection();
						jetty.shutdown();
						return;					
					}
					
					if( downloadMethod.getResponseBodyAsString() == null  ) {
						ui.setAnalysisInterrupted();
						if (loadTestUserInterrupt){
							ui.setResultForSimulateTest( AnalysisState.FAIL, USER_INTERRUPT , 0);
						}else{
							ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_CONNECTION_FAILED , 0);
						}
						downloadMethod.releaseConnection();
						jetty.shutdown();
						return;					
					}
					
				}catch(Exception e){
					ui.setAnalysisInterrupted();
					if (loadTestUserInterrupt){
						ui.setResultForSimulateTest( AnalysisState.FAIL, USER_INTERRUPT , 0);
					}else{
						ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_CONNECTION_FAILED , 0);
					}
					downloadMethod.releaseConnection();
					jetty.shutdown();
					return;

				}				
				//check for latest load test runtime 
				loadTestRunTime = TestSimulationFileUtils.getLoadTestRunTime(tdcHome);
				
				if (loadTestRunTime == null){
					ui.setAnalysisInterrupted();
					ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_READ_CTRL_FILE , 0);
					downloadMethod.releaseConnection();
					jetty.shutdown();
					return;
				}
				if (currentTime.compareTo(loadTestRunTime) < 0){
					ui.setAnalysisInterrupted();
					ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_NOT_ALLOWED , 0);
					downloadMethod.releaseConnection();
					jetty.shutdown();
					return;
				}	
			}
			
			//Ok to simulate load test
			try{
				if( this.ldclient.executeMethod(downloadMethod) != HttpURLConnection.HTTP_OK ) { //this request runs the load test
					ui.setAnalysisInterrupted();
					if (loadTestUserInterrupt){
						ui.setResultForSimulateTest( AnalysisState.FAIL, USER_INTERRUPT , 0);
					}else{
						ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_CONNECTION_FAILED , 0);
					}
					downloadMethod.releaseConnection();
					jetty.shutdown();
					return;					
				}
				if( !downloadMethod.getResponseBodyAsString().contains(RESPONSE_OK)) {
					ui.setAnalysisInterrupted();
					if (loadTestUserInterrupt){
						ui.setResultForSimulateTest( AnalysisState.FAIL, USER_INTERRUPT , 0);
					}else{
						ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_CONNECTION_FAILED , 0);
					}
					downloadMethod.releaseConnection();
					jetty.shutdown();					
					return;					
				}
			}catch(Exception e){
				ui.setAnalysisInterrupted();
				if (loadTestUserInterrupt){
					ui.setResultForSimulateTest( AnalysisState.FAIL, USER_INTERRUPT , 0);
				}else{
					ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_CONNECTION_FAILED , 0);
				}
				downloadMethod.releaseConnection();
				jetty.shutdown();
				return;
			}			
			// Stop jetty...
			jetty.shutdown();
			try{
				Thread.sleep( JETTY_SLEEP_INTERVAL * 1000 );
			}catch(Exception e){
				ui.setAnalysisInterrupted();
				ui.setResultForSimulateTest( AnalysisState.FAIL, SIM_ERR_LOCAL_SERVLET , 0);
				downloadMethod.releaseConnection();
				jetty.shutdown();
				return;
			}			
            // delete proxy.properties
			deletePropertyFiles(tdcHome); 
			downloadMethod.releaseConnection();
		}												
		
		if (!loadTestUserInterrupt){
			ui.setResultForSimulateTest( AnalysisState.PASS, SIM_COMPLETE , 100);	
		}else{
			ui.setAnalysisInterrupted();
			ui.setResultForSimulateTest( AnalysisState.FAIL, USER_INTERRUPT , 0);	
		}
		
		ui.setAnalysisComplete();
	}
	

	
	
	//--------------------------------------------------------------------------
	

	/**
	 * @throws AnalyzerException 
	 */
	private String checkUrl(String url) throws AnalyzerException {
		
		if( this.userInterrupted ) {
			AnalyzerException ae = new AnalyzerException();
			ae.setDisplayMessage( AppResourceBundleUtil.getString("tsutility.networkAnalyzer.error.interruptedByUser") );
			throw ae;
		}
		
		String aeMessage;
		String responseBody = null;
		GetMethod method = new GetMethod(url);
		try {
			int statusCode = this.client.executeMethod(method);

			switch( statusCode ) {
			case HttpURLConnection.HTTP_OK:
				break;
			case HttpURLConnection.HTTP_PROXY_AUTH:
				aeMessage = AppResourceBundleUtil.getString("tsutility.networkAnalyzer.error.proxyAuthRequired").replace("$x", url);
				AnalyzerException aeProxy = new AnalyzerException();
				aeProxy.setDisplayMessage( aeMessage );
				throw aeProxy;
			default:
				aeMessage = AppResourceBundleUtil.getString("tsutility.networkAnalyzer.error.unknownHostException").replace("$x", url);
				AnalyzerException aeDefault = new AnalyzerException();
				aeDefault.setDisplayMessage( aeMessage  + " (" + method.getStatusLine().toString() + ")" );
				throw aeDefault;
			}
			
			responseBody = method.getResponseBodyAsString();
			
		} catch (UnknownHostException e) {
			aeMessage = AppResourceBundleUtil.getString("tsutility.networkAnalyzer.error.unknownHostException").replace("$x", url);
			AnalyzerException ae = new AnalyzerException();
			ae.setDisplayMessage( aeMessage );
			throw ae;
		} catch (ConnectException e) {
			aeMessage = AppResourceBundleUtil.getString("tsutility.networkAnalyzer.error.connectException").replace("$x", url);
			AnalyzerException ae = new AnalyzerException();
			ae.setDisplayMessage( aeMessage );
			throw ae;
		} catch (IOException e) {
			aeMessage = AppResourceBundleUtil.getString("tsutility.networkAnalyzer.error.ioException").replace("$x", url);
			AnalyzerException ae = new AnalyzerException();
			ae.setDisplayMessage( aeMessage );
			throw ae;
		} finally {
			method.releaseConnection();
		}
		
		return responseBody;
	}

}
