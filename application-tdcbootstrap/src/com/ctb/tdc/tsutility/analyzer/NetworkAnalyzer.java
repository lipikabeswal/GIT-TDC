package com.ctb.tdc.tsutility.analyzer;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import com.ctb.tdc.tsutility.AppResourceBundleUtil;
import com.ctb.tdc.tsutility.AppConstants.AnalysisState;
import com.ctb.tdc.tsutility.ui.MainWindow;


public class NetworkAnalyzer extends Thread {

	/* URLs to use from resource bundle. */
	private static final String URL_OAS_HTTP          = AppResourceBundleUtil.getString("tsutility.networkAnalyzer.oas.http");
	private static final String URL_OAS_HTTPS         = AppResourceBundleUtil.getString("tsutility.networkAnalyzer.oas.https");
	private static final String URL_OASDELIVERY_HTTP  = AppResourceBundleUtil.getString("tsutility.networkAnalyzer.oasdelivery.http");
	private static final String URL_OASDELIVERY_HTTPS = AppResourceBundleUtil.getString("tsutility.networkAnalyzer.oasdelivery.https");
	private static final String URL_DYNAMIC_PAGES     = AppResourceBundleUtil.getString("tsutility.networkAnalyzer.dynamicPages");
	private static final String URL_CONTENT_DOWNLOAD  = AppResourceBundleUtil.getString("tsutility.networkAnalyzer.contentDownload");
	private static final int CONNECTION_TIMEOUT = 15 * 1000;
	
	
	private MainWindow ui = null;
	private HttpClient client = null;
	private boolean userInterrupted = false;
	
	
	/**
	 * 
	 *
	 */
	public NetworkAnalyzer(MainWindow window) {
		
		this.ui = window;
		this.client = new HttpClient();
		this.client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
	}

	
	/**
	 * 
	 *
	 */
	public void interruptByUser() {
		this.userInterrupted = true;
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
