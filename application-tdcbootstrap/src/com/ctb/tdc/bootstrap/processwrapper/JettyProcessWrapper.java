package com.ctb.tdc.bootstrap.processwrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ctb.tdc.bootstrap.processwrapper.exception.ProcessWrapperException;
import com.ctb.tdc.bootstrap.processwrapper.monitor.ProcessStdErrMonitor;
import com.ctb.tdc.bootstrap.processwrapper.monitor.ProcessStdOutMonitor;
import com.ctb.tdc.bootstrap.util.ConsoleUtils;
import com.ctb.tdc.bootstrap.util.ResourceBundleUtils;
import com.ctb.tdc.web.dto.ServletSettings;
import com.ctb.tdc.web.utils.MemoryCache;
  

/**
 * Wrapper class for starting the Jetty process.  There are two main commands to
 * be executed for controlling Jetty, the start command and the stop command. Normal
 * starting of this thread uses the start command.  Exiting this thread requires
 * the invocation of the shutdown() method which will call the stop command to shutdown
 * the wrapped Jetty process.
 * 
 * @author Giuseppe_Gennaro
 *
 */
public class JettyProcessWrapper extends Thread {

	private static String tdcHome;
	private static String javaHome;
	private static String proxy;
	private String jettyHome;
	private String jettyConfig;
	private int jettyPort;
	private String jettyUrl = null;
	
	private ServerSocket startsocket;
	private ServerSocket stopsocket;
	
	private String[] startCmd;
	private String[] stopCmd;
	private boolean isAvailable = false;
	private int exitCode = 0;
	private String exitMessage = "";
	
	private int retryAttempts = 5;
	private int retryDelay = 5;

    private boolean macOS = false;    
	
    {
    	javaHome = System.getProperty("java.home");
		if(javaHome != null) javaHome = javaHome + "/bin/"; else javaHome = "";
    }
	
	/**
	 * Creates a new Jetty process wrapper with the given tdcHome value.
	 * @param tdcHome  The location of the Test Delivery Client's home folder containing the home folder of Jetty as well as configuration files and additional java libraries. 
	 * @throws ProcessWrapperException If problems within initialization such as determing the default URL or if the port is already in use.
	 */
	public JettyProcessWrapper(String newTdcHome, boolean macOS, int startPort, int stopPort, ServerSocket startsocket, ServerSocket stopsocket, String baseurl) throws ProcessWrapperException {
		super();
		
        this.macOS = macOS;
        
		tdcHome     = newTdcHome;
		this.jettyHome   = tdcHome + "/servletcontainer/jetty-5.1.11RC0";
		
		this.jettyConfig = tdcHome + "/servletcontainer/tdc.xml";
		
		this.jettyPort = startPort;
		
		this.startsocket = startsocket;
		this.stopsocket = stopsocket;
	
		//String proxy = System.getProperty("tdc.proxy");
		ServletSettings settings = MemoryCache.getInstance().getSrvSettings();
		proxy = settings.getProxyDomain() + "\\" 
					 + settings.getProxyUserName() + ":" 
					 + settings.getProxyPassword() + "@"
					 + settings.getProxyHost() + ":"
					 + settings.getProxyPort();
		ConsoleUtils.messageOut("Bootstrap passing proxy info: " + proxy);
		
		this.startCmd = new String[17];
		this.startCmd[0] = javaHome + "java";
		this.startCmd[1] = "-Dtdc.proxy=" + proxy;
		this.startCmd[2] = "-Dtdc.home=" + tdcHome;
		this.startCmd[3] = "-Xms128m";
		this.startCmd[4] = "-Xmx256m";
		if(baseurl == null) baseurl = "";
		this.startCmd[5] = "-Dtdc.baseurl=" + baseurl;
		this.startCmd[6] = "-Dtdc.productType=" + System.getProperty("product.type");
		this.startCmd[7] = "-Djetty.port=" + startPort;
		this.startCmd[8] = "-DSTOP.PORT=" + stopPort;
		this.startCmd[9] = "-Djetty.home=" + jettyHome;
		this.startCmd[10] = "-Dorg.mortbay.log.LogFactory.noDiscovery=false";
		this.startCmd[11] = "-Djetty.class.path=" + jettyHome + "/etc";
		this.startCmd[12] = "-cp";
		this.startCmd[13] = jettyHome + "/lib/org.mortbay.jetty.jar";
		this.startCmd[14] = "-jar";
		this.startCmd[15] = jettyHome + "/start.jar";
		this.startCmd[16] = jettyConfig;
	
	
		this.stopCmd = new String[11];
		this.stopCmd[0] = "java";
		this.stopCmd[1] = "-Dtdc.home=" + tdcHome;
		this.stopCmd[2] = "-DSTOP.PORT=" + stopPort;
		this.stopCmd[3] = "-Djetty.home=" + jettyHome;
		this.stopCmd[4] = "-Dorg.mortbay.log.LogFactory.noDiscovery=false";
		this.stopCmd[5] = "-Djetty.class.path=" + jettyHome + "/etc";
		this.stopCmd[6] = "-cp";
		this.stopCmd[7] = jettyHome + "/lib/org.mortbay.jetty.jar";
		this.stopCmd[8] = "-jar";
		this.stopCmd[9] = jettyHome + "/stop.jar";
		this.stopCmd[10] = jettyConfig;
	
		/*this.stopCmd = new String[10];
		this.stopCmd[0] = "java";
		this.stopCmd[1] = "-Dtdc.home=" + this.tdcHome;
		this.stopCmd[2] = "-DSTOP.PORT=" + stopPort;
		this.stopCmd[3] = "-Djetty.home=" + jettyHome;
		this.stopCmd[4] = "-Dorg.mortbay.log.LogFactory.noDiscovery=false";
		this.stopCmd[5] = "-Djetty.class.path=" + jettyHome + "/etc";
		this.stopCmd[6] = "-cp";
		this.stopCmd[7] = jettyHome + "/lib/org.mortbay.jetty.jar";
		this.stopCmd[8] = "-jar";
		this.stopCmd[9] = jettyHome + "/stop.jar";
		this.stopCmd[10] = jettyConfig; */
		
		// Parse the jetty configuration for port and url.  this.jettyPort and this.jettyUrl
		// get set in that method.
		parseJettyConfig();
		
		// Check if the port is in use.
		//if( ServerSocketUtils.isPortInUse(this.jettyPort) ) {
		//	throw new ProcessWrapperException( ResourceBundleUtils.getString("bootstrap.jetty.error.portInUse") );
		//}
		
	}

	/**
	 * Parse Jetty configuration to obtain the jetty port and finally url.
	 * 
	 * @throws ProcessWrapperException If problems parsing the Jetty configuration.
	 */
	private void parseJettyConfig() throws ProcessWrapperException {
		
		this.jettyUrl = "http://127.0.0.1:" + this.jettyPort + "/servlet/PersistenceServlet.do?method=verifySettings";
		
	}

 

	/**
	 * @return Parsed response from the connection that will have the error message if there is one, empty string otherwise.
	 * @throws IOException 
	 */
	private String connectToJettyUrl() throws IOException {
		String parsedResponse = "";

		URL url = new URL(this.jettyUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		if( connection.getResponseCode() == 200 ) {
			BufferedReader in = new BufferedReader(new InputStreamReader((InputStream)connection.getContent()));
			String line = "";
			String response = "";
			while( (line = in.readLine()) != null ) {
				response += line + "\n";
			}
			in.close();
			
			Pattern p = Pattern.compile("(?sm).*<MESSAGE>(.*?)</MESSAGE>.*");
			Matcher m = p.matcher(response);
			if( m.matches() ) {
				parsedResponse = m.group(1);
			}
		}
		return parsedResponse;
	}	
	
	
	
	
	
	/**
	 * Returns whether the Jetty process is currently available.  The internal flag
	 * is managed by detecting when the process is actually running and capable of
	 * serving HTTP requests.
	 * 
	 * @return true if Jetty is up and able to server HTTP requests, false otherwise.
	 */
	public boolean isAvailable() {
		return this.isAvailable;
	}
	
	
	/**
	 * @return 0 if there were no errors, -1 otherwise.
	 */
	public int getExitCode() {
		return this.exitCode;
	}
	
	/**
	 * @return empty string if there were no messages, error message otherwise.
	 */
	public String getExitMessage() {
		return this.exitMessage;
	}
	
	
	/**
	 * Starts Jetty with an external system process. Will attempt to connect to the 
	 * default page served by Jetty to determine if it is available. If it is available, 
	 * this thread will block and wait for the Jetty process to exit.  The Jetty process 
	 * will exit when this method's shutdown() is called to stop Jetty.
	 */
	public void run() {

		try {
			// Start Jetty...
            if ( this.macOS ) {
                String productType = System.getProperty("product.type");
                if("OKLAHOMA".equals(productType)) {
                	this.startCmd[0] = javaHome + "java";
                	this.startCmd[1] = "-d32";
            		this.startCmd[2] = "-Dtdc.proxy=" + proxy;
            		this.startCmd[3] = "-Dtdc.home=" + tdcHome;
            		System.out.println(this.startCmd[3]);
            		this.startCmd[4] = "-Xmx256m";
            		this.startCmd[2] = this.startCmd[2].replaceAll(" ", "\\ ");
                    this.startCmd[3] = this.startCmd[3].replaceAll(" ", "\\ ");
                } else {
                	this.startCmd[1] = this.startCmd[1].replaceAll(" ", "\\ ");
                    this.startCmd[2] = this.startCmd[2].replaceAll(" ", "\\ ");
                }
                this.startCmd[5] = this.startCmd[5].replaceAll(" ", "\\ ");
                this.startCmd[6] = this.startCmd[6].replaceAll(" ", "\\ ");
                this.startCmd[8] = this.startCmd[8].replaceAll(" ", "\\ ");
                this.startCmd[9] = this.startCmd[9].replaceAll(" ", "\\ ");
            }
        
            ConsoleUtils.messageOut("jettyHome: " + this.jettyHome);
            // close the reserved sockets right before starting jetty
            this.startsocket.close();
            this.stopsocket.close();
			Process jetty = Runtime.getRuntime().exec(this.startCmd, null, new File(this.jettyHome) );
			
			// Start process monitoring threads
			ProcessStdErrMonitor stdErrMonitor = new ProcessStdErrMonitor(jetty, System.err);
			Thread errThread = new Thread(stdErrMonitor);
			errThread.start();
            
			ProcessStdOutMonitor stdOutMonitor = new ProcessStdOutMonitor(jetty, System.out);
			Thread outThread = new Thread(stdOutMonitor);
			outThread.start();
			
			// Check if jetty is up and running.
			String parsedResponse = null;
			boolean connected = false;
			boolean configured = false;
			int attempts;
			for(attempts = 1; attempts <= this.retryAttempts && !connected && errThread.isAlive() && outThread.isAlive(); attempts++) {
				Thread.sleep(this.retryDelay * 1000);
				ConsoleUtils.messageOut("Connecting with attempt " + attempts + " to " + this.jettyUrl);
				
				try {
					parsedResponse = connectToJettyUrl();
					ConsoleUtils.messageOut("Connected.");
					connected = true;
					
                    ConsoleUtils.messageOut("parsedResponse= " + parsedResponse);
					if( parsedResponse.equalsIgnoreCase("" ) ){
						configured = true;
					}
				} catch( IOException ioe ) {
					ConsoleUtils.messageOut("Failed to connect after attempt " + attempts + ".");
				}
			}
			
			if( !connected ) {
				// Could not start successfully, shut it down now.
				this.exitCode = -1;
				this.exitMessage = ResourceBundleUtils.getString("bootstrap.jetty.error.connectFailure");
				ConsoleUtils.messageErr(this.exitMessage);
				shutdown();
				
			} else if( !configured ) {
				// Determined it was not configured correctly.
				this.exitCode = -1;
				this.exitMessage = parsedResponse;
				ConsoleUtils.messageErr("Servlet was not configured correctly.");
				ConsoleUtils.messageErr(parsedResponse);
				shutdown();
				
			} else {
				Thread.sleep(500);
				this.isAvailable = true;
				
				// Jetty is up, wait for the process.
				jetty.waitFor();

				this.isAvailable = false;
				this.exitCode = 0;
				this.exitMessage = "";
			}
			
		} catch( IOException e ) {
			// IOException for Runtime.getRuntime().exec() method.
			String message = ResourceBundleUtils.getString("bootstrap.jetty.error.processFailure");
			ConsoleUtils.messageErr(message, e);
		} catch( InterruptedException e ) {
			// InterruptedException for jetty.waitFor() method or for Thread.sleep() method.
			String message = ResourceBundleUtils.getString("bootstrap.jetty.error.processInterruption");
			ConsoleUtils.messageErr(message, e);
			shutdown();
		}
	}


	/**
	 * Stops Jetty by executing the system specific stop command.
	 *
	 */
	public void shutdown() {
		try {
            if ( this.macOS ) {
                this.stopCmd[1] = this.stopCmd[1].replaceAll(" ", "\\ ");
                this.stopCmd[2] = this.stopCmd[2].replaceAll(" ", "\\ ");
                this.stopCmd[4] = this.stopCmd[4].replaceAll(" ", "\\ ");
                this.stopCmd[6] = this.stopCmd[6].replaceAll(" ", "\\ ");
                this.stopCmd[8] = this.stopCmd[8].replaceAll(" ", "\\ ");
                this.stopCmd[9] = this.stopCmd[9].replaceAll(" ", "\\ ");
            }            
			Runtime.getRuntime().exec(this.stopCmd, null, new File(tdcHome) );
			String teItemsPath = this.tdcHome + File.separator + "webapp" + File.separator + "items";
			deleteTEItems(teItemsPath);
		} catch( IOException e ) {
			ConsoleUtils.messageErr("An error has occured within " + this.getClass().getName(), e);
		}
	}
	/**
	 * Delete TE items from webapp/items folder
	 * after the test is over
	 */
	public void deleteTEItems(String path) {
		File files[] = new File(path).listFiles();
		for (File file : files) {
			if(file.isDirectory()) {
				deleteTEItems(file.getAbsolutePath());
				file.delete();
			} else if(file.isFile() && file.getName().indexOf("txt") == -1) {
				file.delete();
			}
		}
	}
}

