package com.ctb.tdc.loadtest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import java.net.HttpURLConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Date;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.ctb.tdc.bootstrap.processwrapper.JettyProcessWrapper;
import com.ctb.tdc.loadtest.utils.LoadTestFileUtils;

public class Main {

	private static final ResourceBundle rb = ResourceBundle.getBundle("loadtestResources");
	private static final String URL_LOAD_TEST = "http://127.0.0.1:12347/servlet/LoadTestServlet.do";
	private static final int SOCKET_FOR_SINGLE_INSTANCE = 12347;
	private static int MAIN_SLEEP_INTERVAL;
	private static int JETTY_SLEEP_INTERVAL;
	private static int JETTY_LAUNCH_INTERVAL;
	
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
	private static void initSettings(){
		if (rb != null){
			try{
				MAIN_SLEEP_INTERVAL = new Integer(rb.getString("loadtest.main.sleepInterval")).intValue();
			}catch(MissingResourceException mre){
				MAIN_SLEEP_INTERVAL = 600;
			}
			try{
				JETTY_SLEEP_INTERVAL = new Integer(rb.getString("loadtest.jetty.sleepInterval")).intValue();
			}catch(MissingResourceException mre){
				JETTY_SLEEP_INTERVAL = 5;
			}
			try{
				JETTY_LAUNCH_INTERVAL = new Integer(rb.getString("loadtest.jetty.launchInterval")).intValue();
			}catch(MissingResourceException mre){
				JETTY_LAUNCH_INTERVAL = 28800;
			}
		}else{
			MAIN_SLEEP_INTERVAL = 600;
			JETTY_SLEEP_INTERVAL = 5;
			JETTY_LAUNCH_INTERVAL = 28800;
		}
	}
	private static boolean isMacOS() {
        String os = System.getProperty("os.name");
        if (os == null) 
            os = "";
        return ( os.toLowerCase().indexOf("mac") != -1 );
    }
	
    public static boolean isLinux() {
        String os = System.getProperty("os.name");
        if (os == null) 
            os = "";
        return ( os.toLowerCase().indexOf("linux") != -1 );
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
	
	public static void main(String args[]){
		initSettings();
		boolean isWindows = true;
		HttpClient client = new HttpClient();
		GetMethod downloadMethod = new GetMethod(URL_LOAD_TEST);
		while (isWindows){
			boolean appFlag = false;
			boolean jettyFlag = false;
			boolean launchFlag = false;
			ServerSocket ss = null; // keep variable in scope of the main method to maintain hold on it. 
			try {
				ss = new ServerSocket(SOCKET_FOR_SINGLE_INSTANCE, 1);
			} catch( Exception e ) {
				System.out.println("App already running !!!");
				appFlag = true;
			}			
			if (!appFlag){
				
				try{
					ss.close(); //release the socket
				}catch(IOException e){
					System.out.println("Error closing app socket!");
				}				
				String tdcHome = Main.getTdcHome();
				Date loadTestRunTime = LoadTestFileUtils.getLoadTestRunTime(tdcHome); 
				Date currentTime = new Date();
				
				if (currentTime.compareTo(loadTestRunTime) > 0){
					launchFlag = true;					
					
				}			
				Calendar calCurrTime = Calendar.getInstance();
				Date lastConfigRequestTime = LoadTestFileUtils.getLastConfigRequestTime(tdcHome);			
				Calendar cConfigRequestTime = Calendar.getInstance();
				cConfigRequestTime.setTime(lastConfigRequestTime);
				long diff = (calCurrTime.getTimeInMillis() -  cConfigRequestTime.getTimeInMillis())/1000;
				
				if (diff >= JETTY_LAUNCH_INTERVAL){
					launchFlag = true;
				}
				
				JettyProcessWrapper jetty = null;
				boolean jettyStarted = false;
				try {
					if (launchFlag){
						
						boolean macOS = isMacOS();
						Main.copyPropertyFiles(tdcHome); 
						
						try {
							jetty = new JettyProcessWrapper(tdcHome, macOS, 12355, 12357);
						} 
				        catch( Exception e ) {
				        	System.out.println("Jetty already running !!!");
				        	jettyFlag = true;
						}
				        
				        if(!jettyFlag){
				        	jetty.start();
				        	jettyStarted = true;
				        	System.out.println("Jetty started.");
				        	LoadTestFileUtils.setConfigRequestTime(tdcHome);
				        	
							try{
								Thread.sleep( JETTY_SLEEP_INTERVAL * 1000 );
							}catch(Exception e){
								System.out.println("Jetty Sleep Exception!");
							}					
							
							try{
								if( client.executeMethod(downloadMethod) == HttpURLConnection.HTTP_OK ) {
									System.out.println("Success!");
								}
								else{
									System.out.println("Failure!");
								}
							}catch(Exception e){
								System.out.println("Exception!");
							}												
							// Stop jetty...
							//jetty.shutdown();
												
			                // delete proxy.properties
							Main.deletePropertyFiles(tdcHome);        						
						}					
					}
				} finally {
					if(jetty != null && jettyStarted) {
						jetty.shutdown();
						try{
							Thread.sleep( JETTY_SLEEP_INTERVAL * 1000 );
						}catch(Exception e){
							System.out.println("Jetty Sleep Exception!");
						}	
						System.out.println("Jetty stopped.");
					}
				}
			}
			if(isMacOS() || isLinux()){
				isWindows = false;
			}else{
				try{
					Thread.sleep(MAIN_SLEEP_INTERVAL * 1000);
				}catch(Exception e){
					System.out.println("Main Sleep Exception!");
				}
			}
			
		}						
	}
}
