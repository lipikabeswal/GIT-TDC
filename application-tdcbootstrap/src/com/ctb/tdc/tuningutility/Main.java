package com.ctb.tdc.tuningutility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ctb.tdc.bootstrap.exception.BootstrapException;
import com.ctb.tdc.bootstrap.processwrapper.JettyProcessWrapper;
import com.ctb.tdc.bootstrap.processwrapper.LockdownBrowserWrapper;
import com.ctb.tdc.bootstrap.processwrapper.exception.ProcessWrapperException;
import com.ctb.tdc.bootstrap.ui.SimpleMessageDialog;
import com.ctb.tdc.bootstrap.ui.SplashWindow;
import com.ctb.tdc.bootstrap.util.ConsoleUtils;
import com.ctb.tdc.bootstrap.util.ResourceBundleUtils;
import com.ctb.tdc.tuningutility.processwrapper.TuningUtilityBrowserWrapper;

/**
 * The class containing the main method used in launching, or bootstrapping, 
 * the necessary components utilized in the Test Delivery Client.
 * 
 * @author Giuseppe_Gennaro, Nate_Cohen
 *
 */
public class Main {
	
	/**
	 * Sleep interval for the polling while loop within the main method.
	 */
	public static final int SLEEP_INTERVAL = 2;
	/**
	 * Port number used to guarantee only one instance of the bootstrap
	 * can be running on the workstation.
	 */
	public static int jettyPort = 0;
	public static int stopPort = 0;
	
/*	public static final String TDC_CONFIG_FILENAME = "tdcConfig.enc";
	public static final String UPGRADE_TXT_FILENAME = "upgrade.txt";
	
	public static final String MINOR_UPGRADE = "MINOR";
	public static final String MAJOR_UPGRADE = "MAJOR";*/

	private static int MAIN_SLEEP_INTERVAL;
	private static int JETTY_SLEEP_INTERVAL;
	private static int JETTY_LAUNCH_INTERVAL;

	private static final ResourceBundle rb = ResourceBundle.getBundle("tuningutilityResources");
	
	/**
	 * Constructor.
	 */
	public Main() {
		super();
	}
	
    private static void copyPropertyFiles(String tdcHome) throws BootstrapException {        
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
            throw new BootstrapException( ResourceBundleUtils.getString("bootstrap.main.error.clientConfigIOException") );
        }
    }

    private static void deletePropertyFiles(String tdcHome) {        
        String proxyFilePath = tdcHome + "/webapp/WEB-INF/classes/proxy.properties";
        File proxyFile = new File(proxyFilePath);
        proxyFile.delete();        
    }
 
	/**
	 * Obtain the tdcHome property from the system and return the 
	 * absolute path.
	 * 
	 * @return The absolute path translation of the tdcHome property specified in the System.
	 * @throws BootstrapException
	 */
	private static String getTdcHome() throws BootstrapException {

		String tdcHomeProperty = System.getProperty("tdc.home");

		if( tdcHomeProperty == null ) {
			throw new BootstrapException(ResourceBundleUtils.getString("bootstrap.main.error.tdcHomeNotSpecified"));
		}
		
		File tdcHome = new File( tdcHomeProperty );
		if( !tdcHome.isDirectory() ) {
			throw new BootstrapException(ResourceBundleUtils.getString("bootstrap.main.error.tdcHomeNotDirectory"));
		}

		return tdcHome.getAbsolutePath();
	}

	private static void initSettings(){
		if (rb != null){
			try{
				MAIN_SLEEP_INTERVAL = new Integer(rb.getString("tuningutility.main.sleepInterval")).intValue();
			}catch(MissingResourceException mre){
				MAIN_SLEEP_INTERVAL = 600;
			}
			try{
				JETTY_SLEEP_INTERVAL = new Integer(rb.getString("tuningutility.jetty.sleepInterval")).intValue();
			}catch(MissingResourceException mre){
				JETTY_SLEEP_INTERVAL = 5;
			}
			try{
				JETTY_LAUNCH_INTERVAL = new Integer(rb.getString("tuningutility.jetty.launchInterval")).intValue();
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
    
    private static boolean isLinux() {
        String os = System.getProperty("os.name");
        if (os == null) 
            os = "";
        return ( os.toLowerCase().indexOf("linux") != -1 );
    }
    
	/**
	 * Verifies the environment by checking the following:
	 * <ul>
	 *   <li>Java VM is at least 1.5 or greater</li>
	 * </ul>
	 * @throws BootstrapException If one of the environment requirements are not met.
	 *
	 */
	private static void verifyEnvironment() throws BootstrapException {
        
		String property = System.getProperty("java.specification.version");
		double minJvmVersion = 1.5;        
        ConsoleUtils.messageOut("java.specification.version: " + property);
        
        String jmvVersion = parseString(property);
        if (Double.parseDouble(jmvVersion) < minJvmVersion ) { 
        	throw new BootstrapException(ResourceBundleUtils.getString("bootstrap.main.error.jvmVersion"));
		}
	}
	
	/**
	 * Main method to run the bootstrap application.  Responsible for launching
	 * components in desired sequence with dependencies in mind.
	 * 
	 * @param args	Provided based on Java specifications, but not used.
	 */
	public static void main(String[] args) {
		
		initSettings();
		
		int exitCode = -1;
		boolean macOS = isMacOS();
		boolean linux = isLinux();
		
		// Use a socket to check if the application is already running or not.
		ConsoleUtils.messageOut("Starting...");
		ServerSocket startsocket = null; // keep variable in scope of the main method to maintain hold on it. 
		ServerSocket stopsocket = null;
		
		try {
			jettyPort = 12345;
			startsocket = new ServerSocket(jettyPort, 1);
			System.out.println("Using jetty port " + jettyPort);
		} catch( Exception e ) {
			jettyPort = 0;
		}
		try {
			stopPort = 12355;
			stopsocket = new ServerSocket(stopPort, 1);
			System.out.println("Using stop port " + stopPort);
		} catch( Exception e ) {
			stopPort = 0;
		}
		
		if(jettyPort == 0 || stopPort == 0) {
			String message = ResourceBundleUtils.getString("tuningutility.main.error.applicationAlreadyRunning");
			ConsoleUtils.messageErr(message);
			SimpleMessageDialog.showErrorDialog(message);
			ConsoleUtils.messageOut("Done.");
			System.exit(exitCode);
		}
		
		// Check if we are running in a good environment and obtain the tdcHome property.
		String tdcHome = null;
		try {
			Main.verifyEnvironment();			
			tdcHome = Main.getTdcHome();
			ConsoleUtils.messageOut(" Using " + tdcHome + " for tdc.home folder.");
		} catch( BootstrapException be ) {
			ConsoleUtils.messageErr("An exception has occurred.", be);
			SimpleMessageDialog.showErrorDialog(be.getMessage());
			ConsoleUtils.messageOut("Done.");
			System.exit(exitCode);
		}
        
        
		// Display the splash screen.
		SplashWindow splashWindow = new SplashWindow();
		splashWindow.setVisible(true);

		splashWindow.setStatus(ResourceBundleUtils.getString("bootstrap.main.splashWindow.status.default"), -1);
		try {
			Main.copyPropertyFiles(tdcHome);
		} catch (BootstrapException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    
		// The two (loosely) managed processes.
        ConsoleUtils.messageErr("Begin starting processes...");
        TuningUtilityBrowserWrapper wtb = new TuningUtilityBrowserWrapper(tdcHome, macOS, linux, splashWindow, jettyPort);
		JettyProcessWrapper jetty = null;
		try {
			jetty = new JettyProcessWrapper(tdcHome, macOS, jettyPort, stopPort, startsocket, stopsocket);
		} 
        catch( ProcessWrapperException pwe ) {
			ConsoleUtils.messageErr("An exception has occurred.", pwe);
			if (linux) splashWindow.hide();
			SimpleMessageDialog.showErrorDialog(pwe.getMessage());
			ConsoleUtils.messageOut("Done.");
			Main.deletePropertyFiles(tdcHome);        
			System.exit(exitCode);
		}

        
        // Activate the disablers...
        //MacScreenSaverDisabler macScreenSaverDisabler = new MacScreenSaverDisabler();
        //macScreenSaverDisabler.activate();
        
		
		try {
			// Start Jetty, wait for it to be up, and then launch LDB...
			splashWindow.setStatus(ResourceBundleUtils.getString("bootstrap.main.splashWindow.status.starting"), -1);
			jetty.start();
			boolean wtbLaunched = false;
			while( jetty.isAlive() ) {

				if( jetty.isAvailable() && !wtbLaunched ) {
					splashWindow.setStatus(" ", 100);
					wtb.start();
					wtbLaunched = true;
					splashWindow.setVisible(false);
				}
				
				if( jetty.isAvailable() && wtbLaunched && !wtb.isAlive() ) {
					
					splashWindow.setVisible(true);
					// Stopping the client...
					splashWindow.setStatus(ResourceBundleUtils.getString("bootstrap.main.splashWindow.status.stopping"), -1);

					// Stop jetty...
					jetty.shutdown();
					
					// Purge content in Internet Explorer's secret caching folder...
					//purgeSwfs( macOS );
                    
                    // delete proxy.properties
					Main.deletePropertyFiles(tdcHome);        
				}
				
				Thread.sleep( Main.SLEEP_INTERVAL * 1000 );
			}

			// Check the exit codes of the wrapper processes.
			if( jetty.getExitCode() != 0 ) {
    			if (linux) splashWindow.setVisible(false);
				SimpleMessageDialog.showErrorDialog(jetty.getExitMessage());
				exitCode = -1;
			} else {
				exitCode = 0;
			}
			
		} catch( InterruptedException ie ) {
			if (linux) splashWindow.setVisible(false);
			SimpleMessageDialog.showErrorDialog(ie.getMessage());
			ConsoleUtils.messageErr("An exception has occurred.", ie);
			exitCode = -1;
			
			if( wtb != null && wtb.isAlive() ) {
				wtb.interrupt();
			}
			if( jetty != null && jetty.isAlive() ) {
				jetty.shutdown();
			}
			
		} finally {
			try {
				if( !startsocket.isClosed() ) {
					try {
						startsocket.close();
					} catch( IOException e ) {
						ConsoleUtils.messageErr("An exception has occurred.", e);
					}
				}
				if( !stopsocket.isClosed() ) {
					try {
						stopsocket.close();
					} catch( IOException e ) {
						ConsoleUtils.messageErr("An exception has occurred.", e);
					}
				}
				        
	            if (macOS) {
	                //setFilePermission(tdcHome);
	            }
	            
	            // Deactivate the disablers
	            //macScreenSaverDisabler.deactivate();
	            
				// Close out the application.
				ConsoleUtils.messageOut("Done.");
			} finally {
				
				// make sure LDB is dead
				LockdownBrowserWrapper.exit();
				
				// make sure jetty is dead
				if( jetty != null && jetty.isAlive() ) {
					jetty.shutdown();
				}

				// make sure bootstrap is dead
				System.exit(exitCode);
			}
		}

	}

 /*   private static void setPermission(String fileName) {
        String[] chmodCmd = new String[3];
        chmodCmd[0] = "chmod";
        chmodCmd[1] = "a+rw";
        chmodCmd[2] = fileName;       
        chmodCmd[2] = chmodCmd[2].replaceAll(" ", "\\ ");
 
        try {
            Process chmod = Runtime.getRuntime().exec( chmodCmd );
            chmod.waitFor();
        }
        catch ( Exception e ) {}
    }
    
    private static void setFilePermission(String tdcHome) {
        String fileName = null;
        
        // set permission for tdc.log
        fileName = tdcHome + "/servletcontainer/jetty-5.1.11RC0/logs/tdc.log";            
        setPermission(fileName);
        
        // set permission for audit files
        fileName = tdcHome + "/data/audit";
        File auditDir = new File( fileName );
        File auditFiles[] = auditDir.listFiles();
               
        for( int i=0; i<auditFiles.length; i++ ) {
            File auditFile = auditFiles[i];
            fileName = tdcHome + "/data/audit/" + auditFile.getName();
            setPermission(fileName);
        }

        // set permission for text reader / flash helper file
    }*/
    
    private static String parseString(String inputStr)
	{
    	if (inputStr == null)
    		inputStr = "0";
    	
    	inputStr.trim();
		StringBuffer strBuff = new StringBuffer( inputStr.length() );
		int countDot = 0;
		for(int i=0; i<inputStr.length(); i++) {
			char ch = inputStr.charAt(i);
			if ((ch >= 48) && (ch <= 57)) {
				strBuff.append(ch);
			}
			else
			if (ch == 46) {
				countDot++;
				if (countDot == 1)
					strBuff.append(ch);
				else
					break;	// stop at second dot
			}
			else
				break;	// not 0-9 or '.' so terminate parsing
		}
		
		return strBuff.toString();
	}
    
}
