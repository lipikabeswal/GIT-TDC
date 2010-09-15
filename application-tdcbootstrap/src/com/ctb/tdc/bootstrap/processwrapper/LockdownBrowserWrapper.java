package com.ctb.tdc.bootstrap.processwrapper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

import com.ctb.tdc.bootstrap.processwrapper.exception.ProcessWrapperException;
import com.ctb.tdc.bootstrap.ui.SplashWindow;
import com.ctb.tdc.bootstrap.util.ConsoleUtils;
import com.ctb.tdc.bootstrap.util.ObjectBankUtils;
import com.ctb.tdc.bootstrap.util.TdcConfigEncryption;

/**
 * Wrapper class for starting the LockdownBrowser process.  The Lockdown Browser
 * executable is environment specific but will be installed in known locations
 * to be able to be executed successfully from Runtime.exec() method.
 * 
 * @author Giuseppe_Gennaro
 */
public class LockdownBrowserWrapper extends Thread {

	static volatile String processName = "";
	
	private String tdcHome;
	private String ldbHome;
	
	private SplashWindow splashWindow;
	
	private static boolean islinux = false;
	private static boolean ismac = false;
	
	private String[] ldbCommand;
	private boolean isAvailable = false;
			
	ObjectBankUtils objectBankUtils; /*Solution for Deferred defect No: 50573 */
	
	// JNI lockdown functions
	public static native void TaskSwitching_Enable_Disable(boolean flag);
	public static native void Process_Desktop(String windowName, String command);
	public static native boolean Process_Block();
	public static native void CtrlAltDel_Enable_Disable (boolean flag);
	public static native void Hot_Keys_Enable_Disable(boolean flag);
	public static native void kill_printscreen_snapshot();
	public static native boolean Kill_Task_Mgr();
	public static native boolean Process_Check();
	public static native int Get_Blacklist_Process_No();
	
	static volatile boolean flag = false;
	
	/**
	 * Creates a new Lockdown Browser process wrapper with the given tdcHome value.
	 * @param tdcHome  The location of the Test Delivery Client's home folder containing the home folder of Jetty as well as configuration files and additional java libraries. 
	 * @throws ProcessWrapperException If problems within initialization such as determing the default URL or if the port is already in use.
	 */
	public LockdownBrowserWrapper(String tdcHome, boolean macOS, boolean linux, SplashWindow splashWindow) {
		super();
		
		this.tdcHome = tdcHome;
		this.splashWindow = splashWindow;
            
		if ( macOS ) {
			LockdownBrowserWrapper.ismac = true;
			
			File ldbHomeDir = new File(this.tdcHome + "/lockdownbrowser/mac");
			this.ldbHome = ldbHomeDir.getAbsolutePath();
			this.ldbCommand = new String[1];

			objectBankUtils = new ObjectBankUtils();
			objectBankUtils.deleteContents();
			
            this.ldbCommand[0] = this.ldbHome + "/LockDownBrowser.app/Contents/MacOS/LockDownBrowser";            
            this.ldbCommand[0] = this.ldbCommand[0].replaceAll(" ", "\\ ");
            
		} else if ( linux ) {
			LockdownBrowserWrapper.islinux = true;
			
			File ldbHomeDir = new File(this.tdcHome + "/lockdownbrowser/linux");
			this.ldbHome = ldbHomeDir.getAbsolutePath();
			this.ldbCommand = new String[1];
			
			/*Solution for Deferred Defect No: 50573 */
			/* call the delete method of ObjectBankUtils class*/
			objectBankUtils = new ObjectBankUtils();
			objectBankUtils.deleteContents();
			
            this.ldbCommand[0] = this.ldbHome + "/OASTDC/bin/OASTDC";          
            this.ldbCommand[0] = this.ldbCommand[0].replaceAll(" ", "\\ ");
		} else {
			
			File ldbHomeDir = new File(tdcHome + "/lockdownbrowser/pc");
			this.ldbHome = ldbHomeDir.getAbsolutePath();
			
			/*Solution for Deferred Defect No: 50573 */
			objectBankUtils = new ObjectBankUtils();
			objectBankUtils.deleteContents();
			
			this.ldbCommand = new String[1];
			this.ldbCommand[0] = this.ldbHome + "/LockdownBrowser.exe";
		}
		
	}

	/**
	 * Returns whether the LockdownBrowsesr process is currently available.  The 
	 * internal flag is set right after system execution of the executable and 
	 * set to false immediately after the process has been terminated.
	 * 
	 * @return true after the LDB has been launched, false before it has been launched or after it has been terminated.
	 */
	public boolean isAvailable() {
		return this.isAvailable ;
	}
	
	private static class LockdownWin extends Thread {
		
		private String tdcHome;
		
		public LockdownWin(String tdcHome){
			this.tdcHome = tdcHome;
		}
		
		public void run() {
			try {
				LockdownBrowserWrapper.CtrlAltDel_Enable_Disable(false);
				LockdownBrowserWrapper.TaskSwitching_Enable_Disable(false);
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			}
		}
	}
	
	private static class LockdownWinB extends Thread {
		
		private String tdcHome;
		
		public LockdownWinB(String tdcHome){
			this.tdcHome = tdcHome;
		}
		
		public void run() {
			try {
				LockdownBrowserWrapper.Kill_Task_Mgr();
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			}
		}
	}

	private static class LockdownLinux extends Thread {
		
		private String tdcHome;
		
		public LockdownLinux(String tdcHome){
			this.tdcHome = tdcHome;
		}
		
		public void run() {
			try {
				LockdownBrowserWrapper.Hot_Keys_Enable_Disable(false);
				boolean forcedFullscreen = false;
				int retryCount = 0;
				Thread.sleep(5000);
				while (!forcedFullscreen && retryCount < 3) {
					try {
						String wmctrl = "./wmctrl -r \"Online Assessment System\" -b \"toggle, fullscreen\"";
						Runtime.getRuntime().exec(wmctrl, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						Runtime.getRuntime().exec("metacity --replace", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						forcedFullscreen = true;
					} catch (Exception e) {
						// do nothing
					}
					Thread.sleep(2500);
					retryCount++;
				}
				while(true) {
					try{
						LockdownBrowserWrapper.kill_printscreen_snapshot();
						//Runtime.getRuntime().exec("./wmctrl -s \"Desk 1\"", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						//Runtime.getRuntime().exec("./wmctrl -r \"Online Assessment System\" -t \"Desk 1\"", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						//Thread.sleep(100);
						//Runtime.getRuntime().exec("./wmctrl -n 1", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						//Runtime.getRuntime().exec("./wmctrl -s 0", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						//Runtime.getRuntime().exec("./wmctrl -r \"Online Assessment System\" -t 0", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						//Runtime.getRuntime().exec("./wmctrl -R \"Online Assessment System\"", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						Runtime.getRuntime().exec("./wmctrl -n 1", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						Runtime.getRuntime().exec("./wmctrl -R \"Online Assessment System\"", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));	
						Runtime.getRuntime().exec("gconftool-2 -s -t int /apps/compiz/general/screen0/options/number_of_desktops 1", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
					} catch (Exception e) {
						// do nothing
					}
					Thread.sleep(500);
				}
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Starts the LockdownBrowser.  This thread will block and wait for the LDB to finish
	 * execution before this thread itself will die.
	 */
	public void run() {
		try {
			this.splashWindow.hide();
		
			unpackLock();
		
			if (ismac) {
				System.loadLibrary("lock");
				
				ProcessBlock processBlock = new ProcessBlock ();
				processBlock.start();
				processBlock.join();
				
				if (flag) {
					// Run the LDB...
					ConsoleUtils.messageOut(" Using ldbHome = " + this.ldbHome);
					ConsoleUtils.messageOut(" Executing " + this.ldbCommand[0]);
					Process ldb = Runtime.getRuntime().exec(this.ldbCommand, null, new File(this.ldbHome) );
					this.isAvailable = true;
					ldb.waitFor();
	        		Runtime.getRuntime().exec("sh clear_clipboard.sh");
	    			Runtime.getRuntime().exec("sh enable_screen_capture.sh");
	        		System.out.println("enable print screen called");
					this.isAvailable = false;	
				} else {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {
								
								JFrame frame = new JFrame("LockDown Browser Forbidden Process ");
								frame.setLayout(new BorderLayout());
								
								String labelDisplay = "";
								
								if (processName.contains(",")) {
								
									labelDisplay = "          Forbidden process "+processName+" are detected";
									
								} else {
								
									labelDisplay ="          Forbidden process "+processName+" is detected";
								
								}


								final JLabel label = new JLabel(labelDisplay);
								JPanel p = new JPanel(new BorderLayout());
								p.add(label, BorderLayout.CENTER);

								label.setPreferredSize(new Dimension(310, 180));

								frame.add(label, BorderLayout.CENTER);

								
								Dimension screenSize = new Dimension (Toolkit.getDefaultToolkit().getScreenSize());
								String processArr[] = processName.split(",");
								if (processArr.length == 3) {
								
									frame.setPreferredSize(new Dimension(500,200));
								
								} else if (processArr.length >3 && processArr.length <=6) {
								
									frame.setPreferredSize(new Dimension(600,200));
								
								} else {
								
									frame.setPreferredSize(new Dimension(400,200));
								
								}
								
								Dimension windowSize = new Dimension (frame.getPreferredSize());
								int wdwLeft = screenSize.width /2 - windowSize.width /2;
								int wdwTop = screenSize.height /2 -windowSize.height /2;
								frame.pack();
								frame.setLocation (wdwLeft,wdwTop);
								frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
								//frame.pack();
								frame.setVisible(true);
								
							}
						});
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
					
							e.printStackTrace();
					
					}
				}
			} else if (islinux) {
				// Linux native lib
				System.load(this.tdcHome + "/liblock.so");
				LockdownLinux lockdown = new LockdownLinux(this.tdcHome);
				if (LockdownBrowserWrapper.Process_Check()) {
					flag = false;
					ConsoleUtils.messageOut("Process block failed.");
				} else {
					flag = true;
					
					lockdown.start();
					
					ConsoleUtils.messageOut("Desktop Locked..........");
				}
				// Run the LDB...
				ConsoleUtils.messageOut(" Using ldbHome = " + this.ldbHome);
				ConsoleUtils.messageOut(" Executing " + this.ldbCommand[0]);
				
				this.isAvailable = true;
	
				if (flag) {
					ConsoleUtils.messageOut("AIR app started at " + System.currentTimeMillis());
					
					Map<String,String> env = System.getenv();
					Iterator it = env.keySet().iterator();
					String envp[] = new String[env.keySet().size()];
					int i=0;
					while(it.hasNext()) {
						String key = (String) it.next();
						if(!(key.startsWith("http") && key.endsWith("_proxy"))) {
							envp[i] = key + "=" + (String) env.get(key);
						} else {
							envp[i] = i + "=" + i;
						}
						i++;
					}
					Process ldb = Runtime.getRuntime().exec(this.ldbCommand, envp, new File(this.ldbHome));
					ldb.waitFor();
					ConsoleUtils.messageOut("AIR app ended at " + System.currentTimeMillis());	
				}
				LockdownBrowserWrapper.Hot_Keys_Enable_Disable(true);
				Runtime.getRuntime().exec("./wmctrl -n 2", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
				ConsoleUtils.messageOut("Desktop unlocked ...");
				
				this.isAvailable = false;	
			} else {
				// Windows native lib
				System.load(this.tdcHome + "/lock.dll");
				LockdownWin lockdown = new LockdownWin(this.tdcHome);
				LockdownWinB lockdownB = new LockdownWinB(this.tdcHome);
				
				//boolean flag = false;
				if (LockdownBrowserWrapper.Process_Block()) {
					flag = false;
					ConsoleUtils.messageOut("Process block failed.");
				} else {
					flag = true;
					
					lockdown.start();
					lockdownB.start();
					
					ConsoleUtils.messageOut("Desktop Locked..........");
				}
				// Run the LDB...
				ConsoleUtils.messageOut(" Using ldbHome = " + this.ldbHome);
				ConsoleUtils.messageOut(" Executing " + this.ldbCommand[0]);
				
				this.isAvailable = true;
	
				if (flag) {
					String taskmgr = "taskbarhide.exe";
					Runtime.getRuntime().exec(taskmgr, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
					ConsoleUtils.messageOut("AIR app started at " + System.currentTimeMillis());
					Process ldb = Runtime.getRuntime().exec(this.ldbCommand, null, new File(this.ldbHome));
					//Process ldb = Runtime.getRuntime().exec("C:\\Program Files\\Internet Explorer\\iexplore.exe -k");
					ldb.waitFor();
					ConsoleUtils.messageOut("AIR app ended at " + System.currentTimeMillis());
				}
				LockdownBrowserWrapper.CtrlAltDel_Enable_Disable(true);
				LockdownBrowserWrapper.TaskSwitching_Enable_Disable(true);
				String taskmgr = "taskbarshow.exe";
				Runtime.getRuntime().exec(taskmgr, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
				ConsoleUtils.messageOut("Desktop unlocked ...");	
				Thread.sleep(1500);
				
				this.isAvailable = false;
			}	
			
			this.splashWindow.show();
			
			cleanupLock();
			
		} catch (Exception e) {
			ConsoleUtils.messageErr("An error has occured within " + this.getClass().getName(), e);
		} finally {
			exit();
		}
	}
	
	private ArrayList lockFiles = new ArrayList();
	
	public void cleanupLock() {
		Iterator it = lockFiles.iterator();
		while (it.hasNext()) {
			File file = new File((String)it.next());
			file.delete();
		}
		if(islinux) {
			String pcheckFile = this.tdcHome + "/processcheck";
			File file = new File(pcheckFile);
			file.delete();
			pcheckFile = this.tdcHome + "/xmodmap_modified";
			file = new File(pcheckFile);
			file.delete();
			pcheckFile = this.tdcHome + "/xmodmap_original";
			file = new File(pcheckFile);
			file.delete();
		}
	}
	
	public void unpackLock() throws Exception
    {
        try
        {
        	String encFile = this.tdcHome + "/lock.enc";
        	
        	FileInputStream input = new FileInputStream( encFile );
            int size = input.available();
            byte[] src = new byte[ size ];
            input.read( src );
            input.close();
            
            byte[] outBuff = TdcConfigEncryption.decrypt(src);
            ByteArrayInputStream decrypted = new ByteArrayInputStream( outBuff );

			ZipInputStream zis = new ZipInputStream( decrypted );
			ZipEntry zipEntry;
			
			ConsoleUtils.messageOut("Zip entries...");
			while( (zipEntry = zis.getNextEntry()) != null ) {
				if( !zipEntry.isDirectory() ) {
                    String zipEntryFilePath = null;
                    String zipEntryFileName = zipEntry.getName();
                    zipEntryFilePath = tdcHome + "/" + zipEntryFileName;
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
						lockFiles.add(zipEntryFilePath);
						if(islinux || ismac) {
							Runtime.getRuntime().exec("chmod a+rwx " + zipEntryFilePath, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
							if(zipEntryFilePath.endsWith(".sh") || zipEntryFilePath.endsWith(".c")) {
								Runtime.getRuntime().exec("tr -d '\015\032' < " + zipEntryFilePath + " > " + zipEntryFilePath + ".tmp", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
								Runtime.getRuntime().exec("mv " + zipEntryFilePath + ".tmp " + zipEntryFilePath, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
							}
						}
                    }
                    
				} else {
					new File(zipEntry.getName()).mkdir();
				}
			}
			zis.close();
            ConsoleUtils.messageOut("Done with zip entries.");
        } catch ( Exception e ) {
            System.err.println("Exception : "  + e.getMessage());
            e.printStackTrace();
        }
    }
	
	class ProcessBlock extends Thread {

		public void run () {

			try {
			
				LockdownBrowserWrapper.Get_Blacklist_Process_No();
				BufferedReader in = new BufferedReader (new FileReader("temp_forbidden"));
				String str = in.readLine();
				int value = str.indexOf('|');
				if (value != -1) {
			
					str = str.substring(1);
					String []strArray = str.split("\\|");
					for (String tempString : strArray) {
		
						System.out.println(tempString);
						processName = processName +","+getProcessName(Integer.valueOf(tempString).intValue());
		
					}
				
					if ((processName.indexOf(',')) != -1) {
						processName = processName.substring(1);
					}

					flag = false;
				
				} else {

					flag = true;

				}
				
				}catch (Exception e) {
				
				System.out.println("Exception Thrown");
					e.printStackTrace();
				
				}

		}

	}
	
	private String getProcessName (int value) {

		Properties prop = new Properties ();
		String str = null;
		try {

			prop.load(new FileInputStream ("BlacklistProcess.properties"));
			str = prop.getProperty(String.valueOf(value));

		} catch (Exception e) {

			e.printStackTrace();

		}
		return str;

	}
	
	public static synchronized void exit() {
		try {
			if(islinux) {
        		Runtime.getRuntime().exec("killall OASTDC");
        		Thread.sleep(250);
        		Runtime.getRuntime().exec("killall OASTDC");
        	} else if(ismac) {
        		Runtime.getRuntime().exec("killall LockDownBrowser");
        		Thread.sleep(250);
        		Runtime.getRuntime().exec("killall LockDownBrowser");
        	} else {
        		Runtime.getRuntime().exec("taskkill /IM \"LockdownBrowser.exe\"");
        		Thread.sleep(250);
        		Runtime.getRuntime().exec("taskkill /IM \"LockdownBrowser.exe\"");
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

