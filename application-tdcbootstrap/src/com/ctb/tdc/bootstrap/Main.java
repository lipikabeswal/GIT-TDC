package com.ctb.tdc.bootstrap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.ctb.tdc.bootstrap.disablers.MacScreenSaverDisabler;
import com.ctb.tdc.bootstrap.exception.BootstrapException;
import com.ctb.tdc.bootstrap.processwrapper.JettyProcessWrapper;
import com.ctb.tdc.bootstrap.processwrapper.LockdownBrowserWrapper;
import com.ctb.tdc.bootstrap.processwrapper.exception.ProcessWrapperException;
import com.ctb.tdc.bootstrap.ui.SimpleMessageDialog;
import com.ctb.tdc.bootstrap.ui.SplashWindow;
import com.ctb.tdc.bootstrap.util.ConsoleUtils;
import com.ctb.tdc.bootstrap.util.ResourceBundleUtils;
import com.ctb.tdc.bootstrap.util.ServletUtils;
import com.ctb.tdc.bootstrap.util.SwfFilenameFilter;
import com.ctb.tdc.bootstrap.util.TdcConfigEncryption;

/**
 * The class containing the main method used in launching, or bootstrapping, 
 * the necessary components utilized in the Test Delivery Client.
 * 
 * @author Giuseppe_Gennaro, Nate_Cohen
 *
 */
public class Main {
	
	public static String baseurl = System.getProperty("tdc.baseurl");
	
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
	
	public static final String TDC_CONFIG_FILENAME = "tdcConfig.enc";
	public static final String UPGRADE_TXT_FILENAME = "upgrade.txt";
	
	public static final String MINOR_UPGRADE = "MINOR";
	public static final String MAJOR_UPGRADE = "MAJOR";
	
	/**
	 * Constructor.
	 */
	public Main() {
		super();
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
	 * Purge SWFs cached in the hidden folder used by Internet Explorer.
	 */
	private static void purgeSwfs(boolean macOS) {

        String tmpdir = System.getProperty("java.io.tmpdir");
		if( tmpdir != null ) {
			File hiddenContentDir = null;            
            if ( macOS ) {
                hiddenContentDir = new File( tmpdir );
            }
            else {
                hiddenContentDir = new File( tmpdir + "../Temporary Internet Files/Content.IE5" );                
            }
            
			if( !hiddenContentDir.canRead() ) {
				ConsoleUtils.messageErr("Can not read! " + hiddenContentDir.getAbsolutePath() );
			} else if( !hiddenContentDir.canWrite() ) {
				ConsoleUtils.messageErr("Can not write! " + hiddenContentDir.getAbsolutePath() );
			} else if ( !hiddenContentDir.isDirectory() ) {
				ConsoleUtils.messageErr("Not a directory! " + hiddenContentDir.getAbsolutePath() );
			} else  {
				File directories[] = hiddenContentDir.listFiles();
				SwfFilenameFilter sff = new SwfFilenameFilter();
				
				for( int i=0; i < directories.length; i++ ) {
					if( directories[i].isDirectory() ) {
						File swfs[] = directories[i].listFiles(sff);
						
						for( int j=0; j < swfs.length; j++ ) {
							try {
								swfs[j].delete();
							} catch( SecurityException se ) {
								ConsoleUtils.messageErr(se.getMessage());
							}
						}
					}
				}
			}
			
		}
		
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
	 * @param tdcHome The location of the test delivery client's home folder. 
	 * @throws BootstrapException If the client configuration file could not be retrieved.
	 * 
	 */
	private static void downloadAndConfigureClient(String tdcHome, boolean macOS) throws BootstrapException {
        
		String tdcConfigUrl = Main.getTdcConfigUrl();
        
		try {
            ConsoleUtils.messageOut("Retrieving client configuration: " + tdcConfigUrl);
			byte[] inBuff = ServletUtils.httpClientSendRequest(tdcConfigUrl).getBytes();
        	byte[] outBuff = TdcConfigEncryption.decrypt(inBuff);
            ByteArrayInputStream decrypted = new ByteArrayInputStream( outBuff );

			ZipInputStream zis = new ZipInputStream( decrypted );
			ZipEntry zipEntry;
			
			ConsoleUtils.messageOut("Zip entries...");
			while( (zipEntry = zis.getNextEntry()) != null ) {
				if( !zipEntry.isDirectory() ) {
                    String zipEntryFilePath = null;
                    String zipEntryFileName = zipEntry.getName();
                    if ( zipEntryFileName.indexOf("LockdownBrowser.ini") >= 0 ) {
                        if ( ! macOS ) {
                            zipEntryFilePath = tdcHome + "/" + zipEntryFileName;
                        }
                    }
                    else
                    if ( zipEntryFileName.indexOf("LDB Settings.plst") >= 0 ) {
                        if ( macOS ) {
                            zipEntryFilePath = "/Library/Application Support/LockdownBrowser/" + zipEntryFileName;
                        }
                    }
                    else {
                        zipEntryFilePath = tdcHome + "/" + zipEntryFileName;
                    }
                    
                    if ( zipEntryFilePath != null ) {
						ConsoleUtils.messageOut( " - writing " + zipEntryFilePath );
						File file = new File(zipEntryFilePath);
						FileOutputStream fos = new FileOutputStream(file, false);
						int read = 0;
						while( read >= 0 && read < zipEntry.getSize() ) {
							byte[] bytes = new byte[2048];
							// fix typo
							read = zis.read(bytes);
							if( read != -1 ) {
								fos.write(bytes, 0, read);
							}
						}
						fos.close();
                    }
                    
				} else {
					new File(zipEntry.getName()).mkdir();
				}
			}
			zis.close();
            ConsoleUtils.messageOut("Done with zip entries.");
		} catch( IOException ioe ) {
			ioe.printStackTrace();
			throw new BootstrapException( ResourceBundleUtils.getString("bootstrap.main.error.clientConfigIOException") );
		}
	}
	
	/**
	 * @param tdcHome The location of the test delivery client's home folder. 
	 * @throws BootstrapException If the client configuration file could not be retrieved.
	 * 
	 */
	private static String getUpgrade() throws BootstrapException {
        String result = null;
		String upgradeUrl = Main.getUpgradeTxtUrl();
        
		try {
            ConsoleUtils.messageOut("Retrieving upgrade: " + upgradeUrl);
            
            result = ServletUtils.httpClientSendRequest(upgradeUrl);
            if(result != null) {
        		ConsoleUtils.messageOut("Got upgrade.");
			} else {
				System.out.println("Error getting upgrade from " + upgradeUrl);
				throw new BootstrapException( ResourceBundleUtils.getString("bootstrap.main.error.clientConfigIOException") );    				
			}
		} catch( Exception ioe ) {
			ioe.printStackTrace();
			throw new BootstrapException( ResourceBundleUtils.getString("bootstrap.main.error.clientConfigIOException") );
		}
		return result;
	}
	
	private static String getUpgradeDirectoryUrl(){
		String productType = System.getProperty("product.type");
		String base = "";
		baseurl = System.getProperty("tdc.baseurl");
		if(baseurl != null && !"".equals(baseurl.trim())) {
			base = baseurl + "/tdcupdate";
		} else {
			if(productType.equals("ISTEP"))
				base = ResourceBundleUtils.getString("bootstrap.main.istep.url");
			else if(productType.equals("GEORGIA"))
				base = ResourceBundleUtils.getString("bootstrap.main.ga.url");
			else if(productType.equals("TABE"))
				base = ResourceBundleUtils.getString("bootstrap.main.tabe.url");
			else if(productType.equals("TERRANOVA"))
				base = ResourceBundleUtils.getString("bootstrap.main.tn.url");
			else if(productType.equals("LASLINKS"))
				base = ResourceBundleUtils.getString("bootstrap.main.llo.url");
			else 
				base = ResourceBundleUtils.getString("bootstrap.main.base.url");
			
			baseurl = base.substring(0, base.indexOf("/tdcupdate"));
		}		
		String version = ResourceBundleUtils.getVersionString("tdc.version");
		version = "v" + version.substring(0, version.lastIndexOf("."));
		String result = base + "/" + version + "/";
		return result;
	}
	
	private static String getTdcConfigUrl(){
		return getUpgradeDirectoryUrl() + TDC_CONFIG_FILENAME;
	}
	private static String getUpgradeTxtUrl(){
		return getUpgradeDirectoryUrl() + UPGRADE_TXT_FILENAME;
	}
	/**
	 * Main method to run the bootstrap application.  Responsible for launching
	 * components in desired sequence with dependencies in mind.
	 * 
	 * @param args	Provided based on Java specifications, but not used.
	 */
	public static void main(String[] args) {
		
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
		
		boolean allowMulti = !"false".equalsIgnoreCase(ResourceBundleUtils.getString("bootstrap.main.allowmulti"));
		if(allowMulti && (jettyPort == 0 || stopPort == 0)) {
			int i = 0;
			while (i < 25) {
				try {
					jettyPort = 12345 + ((int) ((Math.random() + 1.0) * 250));
					startsocket = new ServerSocket(jettyPort, 1);
					//startsocket.close();
					System.out.println("Using jetty port " + jettyPort);
					i = 25;
				} catch( Exception e ) {
					jettyPort = 0;
				}
			}
			i = 0;
			while (i < 25) {
				try {
					stopPort = 12345 + ((int) ((Math.random() + 1.0) * 250));
					stopsocket = new ServerSocket(stopPort, 1);
					//stopsocket.close();
					System.out.println("Using stop port " + stopPort);
					i = 25;
				} catch( Exception e ) {
					stopPort = 0;
				}
			}
		}
		if(jettyPort == 0 || stopPort == 0) {
			String message = ResourceBundleUtils.getString("bootstrap.main.error.applicationAlreadyRunning");
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

		
		// check upgrade, download and config if required, show upgrade message if required.
		try {
			splashWindow.setStatus(ResourceBundleUtils.getString("bootstrap.main.splashWindow.status.default"), -1);
            Main.copyPropertyFiles(tdcHome);   
            ServletUtils.validateServletSettings();
            String upgrade = Main.getUpgrade();
            if(upgrade != null){
	            if(upgrade.contains(MINOR_UPGRADE)){
	    			splashWindow.setStatus(ResourceBundleUtils.getString("bootstrap.main.splashWindow.status.upgrading"), -1);
	    			Main.downloadAndConfigureClient(tdcHome, macOS);
	            }
	            else if (upgrade.contains(MAJOR_UPGRADE)){
	    			ConsoleUtils.messageErr("Requires major upgrade.");
	    			if (linux) splashWindow.hide();
	    			SimpleMessageDialog.showErrorDialog(ResourceBundleUtils.getString("bootstrap.main.error.upgrade"));
	    			ConsoleUtils.messageOut("Done.");
					Main.deletePropertyFiles(tdcHome);        
	    			System.exit(exitCode);
	            }
            }
		} catch( BootstrapException be ) {
			ConsoleUtils.messageErr("An exception has occurred.", be);
			if (linux) splashWindow.hide();
			SimpleMessageDialog.showErrorDialog(be.getMessage());
			ConsoleUtils.messageOut("Done.");
			Main.deletePropertyFiles(tdcHome);        
			System.exit(exitCode);
		}
		
		 
		// The two (loosely) managed processes.
        ConsoleUtils.messageErr("Begin starting processes...");
		LockdownBrowserWrapper ldb = new LockdownBrowserWrapper(tdcHome, macOS, linux, splashWindow, jettyPort);
		JettyProcessWrapper jetty = null;
		try {
			jetty = new JettyProcessWrapper(tdcHome, macOS, jettyPort, stopPort, startsocket, stopsocket, baseurl);
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
        MacScreenSaverDisabler macScreenSaverDisabler = new MacScreenSaverDisabler();
        macScreenSaverDisabler.activate();
        
		
		try {
			// Start Jetty, wait for it to be up, and then launch LDB...
			splashWindow.setStatus(ResourceBundleUtils.getString("bootstrap.main.splashWindow.status.starting"), -1);
			jetty.start();
			boolean ldbLaunched = false;
			while( jetty.isAlive() ) {

				if( jetty.isAvailable() && !ldbLaunched ) {
					splashWindow.setStatus(" ", 100);
					ldb.start();
					ldbLaunched = true;
				}
				
				if( jetty.isAvailable() && ldbLaunched && !ldb.isAlive() ) {
					// Stopping the client...
					splashWindow.setStatus(ResourceBundleUtils.getString("bootstrap.main.splashWindow.status.stopping"), -1);

					// Stop jetty...
					jetty.shutdown();
					
					// Purge content in Internet Explorer's secret caching folder...
					purgeSwfs( macOS );
                    
                    // delete proxy.properties
					Main.deletePropertyFiles(tdcHome);        
				}
				
				Thread.sleep( Main.SLEEP_INTERVAL * 1000 );
			}

			// Check the exit codes of the wrapper processes.
			if( jetty.getExitCode() != 0 ) {
    			if (linux) splashWindow.hide();
				SimpleMessageDialog.showErrorDialog(jetty.getExitMessage());
				exitCode = -1;
			} else {
				exitCode = 0;
			}
			
		} catch( InterruptedException ie ) {
			if (linux) splashWindow.hide();
			SimpleMessageDialog.showErrorDialog(ie.getMessage());
			ConsoleUtils.messageErr("An exception has occurred.", ie);
			exitCode = -1;
			
			if( ldb != null && ldb.isAlive() ) {
				ldb.interrupt();
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
	                setFilePermission(tdcHome);
	            }
	            
	            // Deactivate the disablers
	            macScreenSaverDisabler.deactivate();
	            
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

    private static void setPermission(String fileName) {
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
    }
    
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
