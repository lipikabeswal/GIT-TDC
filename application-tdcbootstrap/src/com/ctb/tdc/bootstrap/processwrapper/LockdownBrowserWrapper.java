package com.ctb.tdc.bootstrap.processwrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.SwingUtilities;

import com.ctb.tdc.bootstrap.processwrapper.exception.ProcessWrapperException;
import com.ctb.tdc.bootstrap.ui.SplashWindow;
import com.ctb.tdc.bootstrap.util.ConsoleUtils;
import com.ctb.tdc.bootstrap.util.ObjectBankUtils;
import com.ctb.tdc.bootstrap.util.ResourceBundleUtils;
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
	
	// JNI lockdown functions
	public static native void TaskSwitching_Enable_Disable(boolean flag);
	public static native void Process_Desktop(String windowName, String command);
	public static native boolean Process_Block();
	public static native void CtrlAltDel_Enable_Disable (boolean flag);
	public static native void Hot_Keys_Enable_Disable(boolean flag);
	public static native void kill_printscreen_snapshot();
	public static native boolean Kill_Task_Mgr();
	public static native boolean Process_Check();
	//public static native int Get_Blacklist_Process_No();
	
	static volatile boolean flag = false;
	static volatile boolean ready = false;
	
	/**
	 * Creates a new Lockdown Browser process wrapper with the given tdcHome value.
	 * @param tdcHome  The location of the Test Delivery Client's home folder containing the home folder of Jetty as well as configuration files and additional java libraries. 
	 * @throws ProcessWrapperException If problems within initialization such as determing the default URL or if the port is already in use.
	 */
	public LockdownBrowserWrapper(String tdcHome, boolean macOS, boolean linux, SplashWindow splashWindow, int jettyPort) {
		super();
		
		this.tdcHome = tdcHome;
		this.splashWindow = splashWindow;
            
		if ( macOS ) {
			LockdownBrowserWrapper.ismac = true;
			
			File ldbHomeDir = new File(this.tdcHome + "/lockdownbrowser/mac");
			this.ldbHome = ldbHomeDir.getAbsolutePath();
			this.ldbCommand = new String[1];
			
            this.ldbCommand[0] = this.ldbHome + "/LockDownBrowser.app/Contents/MacOS/LockDownBrowser";            
            this.ldbCommand[0] = this.ldbCommand[0].replaceAll(" ", "\\ ");
            
		} else if ( linux ) {
			LockdownBrowserWrapper.islinux = true;
			
			File ldbHomeDir = new File(this.tdcHome + "/lockdownbrowser/linux");
			this.ldbHome = ldbHomeDir.getAbsolutePath();
			this.ldbCommand = new String[2];
			
            this.ldbCommand[0] = this.ldbHome + "/OASTDC/bin/OASTDC";          
            this.ldbCommand[0] = this.ldbCommand[0].replaceAll(" ", "\\ ");
            this.ldbCommand[1] = "http://127.0.0.1:" + jettyPort + "/login.html";
		} else {
			
			File ldbHomeDir = new File(tdcHome + "/lockdownbrowser/pc");
			this.ldbHome = ldbHomeDir.getAbsolutePath();
			
			this.ldbCommand = new String[2];
			this.ldbCommand[0] = this.ldbHome + "/LockdownBrowser.exe";
			this.ldbCommand[1] = "http://127.0.0.1:" + jettyPort + "/login.html";
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
				//LockdownBrowserWrapper.Kill_Task_Mgr();
				while(true){
					Runtime.getRuntime().exec("taskkill /F /IM taskmgr.exe /T");
					Thread.sleep(10000);
				}
					
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This Thread has been moved out of this LockBrowserWrapper into Main.java as the ldb.waitFor() call will not allow this thread to run execute any further.
	 * @author jaivardhan_mehrotra
	 *
	 */
	/*private static class LockdownWinOK extends Thread {
	
		private static ArrayList<String> processName = new ArrayList<String>();
		
		private String tdcHome;
		
		public LockdownWinOK(String tdcHome){
			this.tdcHome = tdcHome;
		}
		public void run() {
			System.out.println("LockdownWinOK..................................");
			try {			
				if(processName.isEmpty()){
					String tmpStr = null;
					getAllProcessName();
				}
				System.out.println("processName.size()......"+processName.size());
				while(true){
					for(int i=0;i<processName.size();i++){					
						isProcessRunning(processName.get(i).toLowerCase());
					}
					//Thread.sleep(2000);
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private  void getAllProcessName () {
			//Properties prop = new Properties ();
			
			String str = null;
			try {
				//prop.load(getClass().getResourceAsStream("BlacklistProcessNames.properties"));
				Enumeration<String> em=ResourceBundleUtils.getAllBlistProcessKeys();
				while(em.hasMoreElements()){
					  String keyStr = (String)em.nextElement();
					  str = ResourceBundleUtils.getBlistProcessString(keyStr);
					  if(str!=null)
						  processName.add(str);
					  }
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
		public static void isProcessRunning(String serviceName) throws Exception {
			System.out.println("isProcessRunning 1...............................................");
			 Process processList = Runtime.getRuntime().exec("tasklist"); 
			 System.out.println("isProcessRunning 2..............................................."); 
			 BufferedReader reader = new BufferedReader(new InputStreamReader(
			 processList.getInputStream()));
			 String line;
			 System.out.println("line..............................................."+ reader.readLine());
			 while ((line = reader.readLine()) != null) {
				  String lineProcess=line.toLowerCase();
				  System.out.println("in while 1..............................................."+lineProcess);
				  System.out.println("in while 2..............................................."+serviceName);
				  
				  if (lineProcess.indexOf(serviceName) != -1)
				  {
					  killProcess(line.substring(0, line.indexOf(" ")));
				  }
			}

		} 	
		
		public static void killProcess(String serviceName) throws Exception {
			  Runtime.getRuntime().exec("taskkill /F /IM "+ serviceName);
		 }		
		
	}*/
	
	
	

	private static class LockdownLinux extends Thread {
		
		private String tdcHome;
		
		public LockdownLinux(String tdcHome){
			this.tdcHome = tdcHome;
		}
		
		public boolean forceFullScreen() {
			try {
				String wmctrl = "./wmctrl -r \"Online Assessment System\" -b \"toggle, fullscreen\"";
				Runtime.getRuntime().exec(wmctrl, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
				Runtime.getRuntime().exec("metacity --replace", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
			} catch (Exception e) {
				// do nothing
				return false;
			}
			return true;
		}
		
		public void run() {
			try {
				boolean forcedFullScreen = false;
				ConsoleUtils.messageOut("Blocking hotkeys at " + System.currentTimeMillis());
				LockdownBrowserWrapper.Hot_Keys_Enable_Disable(false);
				ConsoleUtils.messageOut("Starting lock loop at " + System.currentTimeMillis());
				while(true) {
					if(ready) {
						if(!forcedFullScreen) {
							ConsoleUtils.messageOut("trying to force fullscreen at " + System.currentTimeMillis());
							forcedFullScreen = forceFullScreen();
						}
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
							ConsoleUtils.messageOut("completed lock loop at " + System.currentTimeMillis());
						} catch (Exception e) {
							// do nothing
						}
					}
					Thread.sleep(1000);
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
			unpackLock();
		
			this.splashWindow.hide();
			
			if (ismac) {
				// start change for mac 10.4
				//System.loadLibrary("lock");
				// endchange for mac 10.4
				
				ProcessBlock processBlock = new ProcessBlock (this.tdcHome);
				processBlock.start();
				processBlock.join();
				
				if (flag) {
					// Run the LDB...
					//Runtime.getRuntime().exec("sh clear_clipboard.sh", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
					ConsoleUtils.messageOut(" Using ldbHome = " + this.ldbHome);
					ConsoleUtils.messageOut(" Executing " + this.ldbCommand[0]);
					
					Process ldb = Runtime.getRuntime().exec(this.ldbCommand, null, new File(this.ldbHome) );
					this.isAvailable = true;
					ldb.waitFor();
					this.isAvailable = false;
	        		Runtime.getRuntime().exec("sh clear_clipboard.sh", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
	    			Runtime.getRuntime().exec("sh enable_screen_capture.sh", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
	    			String[] appString = {"osascript","-e","set the clipboard to \"\""};
	    			Runtime.getRuntime().exec(appString);
	    			//System.out.println("enable print screen called");	
				} else {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {
								new CustomDialog(processName);
								/*
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
								frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								//frame.pack();
								frame.setVisible(true);
								*/
							}
						});
						long startTime = System.currentTimeMillis();
						while(CustomDialog.dialogOpen && ((System.currentTimeMillis() - startTime) < 60000)) {
							Thread.sleep(1000);
						}
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
				
				if (LockdownBrowserWrapper.Process_Check()) {
					flag = false;
					ConsoleUtils.messageOut("Process block failed.");
				} else {
					flag = true;
					ConsoleUtils.messageOut("Desktop Locked..........");
				}
				// Run the LDB...
				ConsoleUtils.messageOut(" Using ldbHome = " + this.ldbHome);
				ConsoleUtils.messageOut(" Executing " + this.ldbCommand[0]);
	
				if (flag) {
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
					
					long startTime = System.currentTimeMillis();
					LockdownLinux lockdown = new LockdownLinux(this.tdcHome);
					ConsoleUtils.messageOut("AIR app started at " + startTime);
					Process ldb = Runtime.getRuntime().exec(this.ldbCommand, envp, new File(this.ldbHome));
					Thread.sleep(10000);
					lockdown.start();
					ready = true;
					this.isAvailable = true;
					ldb.waitFor();
					this.isAvailable = false;
					ConsoleUtils.messageOut("AIR app ended at " + System.currentTimeMillis());	
				}
				LockdownBrowserWrapper.Hot_Keys_Enable_Disable(true);
				Runtime.getRuntime().exec("./wmctrl -n 2", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
				ConsoleUtils.messageOut("Desktop unlocked ...");	
			} else {
				System.out.println("else........................................................");
				// Windows native lib
				System.load(this.tdcHome + "/lock.dll");
				LockdownWin lockdown = new LockdownWin(this.tdcHome);
				LockdownWinB lockdownB = new LockdownWinB(this.tdcHome);
				//LockdownWinOK lockdownOK = new LockdownWinOK(this.tdcHome);
				//boolean flag = false;
				if (LockdownBrowserWrapper.Process_Block()) {
					flag = false;
					ConsoleUtils.messageOut("Process block failed.");
				} else {
					flag = true;
					
					lockdown.start();
					lockdownB.start();
					
					ConsoleUtils.messageOut("Desktop Locked..........");
					  
		           	//lockdownOK.start();
		           	ConsoleUtils.messageOut("Desktop Locked for oklahoma..........");
		                
				}
				// Run the LDB...
				ConsoleUtils.messageOut(" Using ldbHome = " + this.ldbHome);
				ConsoleUtils.messageOut(" Executing " + this.ldbCommand[0]);
				
				
	
				if (flag) {
					String taskmgr = "taskbarhide.exe";
					Runtime.getRuntime().exec(taskmgr, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
					ConsoleUtils.messageOut("AIR app started at " + System.currentTimeMillis());
					Process ldb = Runtime.getRuntime().exec(this.ldbCommand, null, new File(this.ldbHome));
					//Process ldb = Runtime.getRuntime().exec("C:\\Program Files\\Internet Explorer\\iexplore.exe -k");
					this.isAvailable = true;
					ldb.waitFor();
					this.isAvailable = false;
					ConsoleUtils.messageOut("AIR app ended at " + System.currentTimeMillis());
				}
				
				
				LockdownBrowserWrapper.CtrlAltDel_Enable_Disable(true);
				LockdownBrowserWrapper.TaskSwitching_Enable_Disable(true);
				String taskmgr = "taskbarshow.exe";
				
				Runtime.getRuntime().exec(taskmgr, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
				Thread.sleep(500);	
				Runtime.getRuntime().exec(taskmgr, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
				
				cleanupLock();	// call here to fix issue in 64 bit Windows 7 
				
				ConsoleUtils.messageOut("Desktop unlocked ...");	
				Thread.sleep(1500);
				
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
		
		String msg;
		Iterator it = lockFiles.iterator();
		while (it.hasNext()) {

			try {
				File file = new File((String)it.next());
				boolean ret = file.delete();
				msg = "delete " + file.getName() + " = " + ret + "   ";
				ConsoleUtils.messageOut(msg);	
			} catch (Exception e) {
				msg = "failed to delete file" + e.getMessage();				
				ConsoleUtils.messageOut(msg);		
			}
			
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
						try {
							File file = new File(zipEntryFilePath);
							FileOutputStream fos = new FileOutputStream(file, false);
							int read = 0;
							while( read >= 0 && read < zipEntry.getSize() ) {
								byte[] bytes = new byte[2048];
								// fix typo
								read = zis.read(bytes);
								if( read != -1 ) {
									if(zipEntryFileName.indexOf(".sh") >= 0) {
										// strip out Ctrl-M
										byte[] cleanbytes = new byte[read];
										for(int i=0;i<read;i++){
											if(bytes[i] != 13) {
												cleanbytes[i] = bytes[i];
											} else {
												cleanbytes[i] = ' ';
											}
										}
										fos.write(cleanbytes, 0, read);
									} else {
										fos.write(bytes, 0, read);
									}
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
						} catch (FileNotFoundException e) {
							// couldn't write file as it's in use by another instance
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
		private String tdchome;
		public ProcessBlock(String tdchome){
			this.tdchome = tdchome;
		}
		public void run () {

			try {
				// start change for mac 10.4
				//LockdownBrowserWrapper.Get_Blacklist_Process_No();
				Process lock = Runtime.getRuntime().exec("sh macpcheck.sh", null, new File(this.tdchome.replaceAll(" ", "\\ ")));
				lock.waitFor();
				// end change for mac 10.4
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
        		Runtime.getRuntime().exec("killall -KILL LockDownBrowser");
        		Thread.sleep(250);
        		Runtime.getRuntime().exec("killall -KILL LockDownBrowser");
        	} else {
        		try {
	        		Runtime.getRuntime().exec("taskkill /IM \"LockdownBrowser.exe\"");
	        		Thread.sleep(250);
	        		Runtime.getRuntime().exec("taskkill /IM \"LockdownBrowser.exe\"");
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        		try {
        			Runtime.getRuntime().exec("tskill \"LockdownBrowser\"");
	        		Thread.sleep(250);
	        		Runtime.getRuntime().exec("tskill \"LockdownBrowser\"");
        		} catch (Exception e) {
        			e.printStackTrace();
        		}	
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

