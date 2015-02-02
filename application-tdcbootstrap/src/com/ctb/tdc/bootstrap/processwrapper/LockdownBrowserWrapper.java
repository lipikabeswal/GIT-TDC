package com.ctb.tdc.bootstrap.processwrapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import com.ctb.tdc.bootstrap.util.ConsoleUtils;

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
	private String productType;
	private String form;
	private SplashWindow splashWindow;
	
	private static boolean islinux = false;
	private static boolean ismac = false;
	
	private String[] ldbCommand;
	private ArrayList<String> command;
	private ProcessBuilder processBuilder;
	private boolean isAvailable = false;
	private static boolean isProcessExit = false;
	
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
	static Process ldbProcess;
	static volatile boolean flag = false;
	static volatile boolean ready = false;
	
	/**
	 * Creates a new Lockdown Browser process wrapper with the given tdcHome value.
	 * @param tdcHome  The location of the Test Delivery Client's home folder containing the home folder of Jetty as well as configuration files and additional java libraries. 
	 * @throws ProcessWrapperException If problems within initialization such as determing the default URL or if the port is already in use.
	 */
	public LockdownBrowserWrapper(String tdcHome, boolean macOS, boolean linux, SplashWindow splashWindow, int jettyPort, String formName, String productType) {
		super();
		//ConsoleUtils.consoleOut("Product 1******"+productType);
	//	ConsoleUtils.consoleOut("Formname 1*****"+formName);
		this.tdcHome = tdcHome;
		this.splashWindow = splashWindow;
		this.productType=productType;
		this.form=formName;
		command=new ArrayList<String>();
            
		if ( macOS ) {
			LockdownBrowserWrapper.ismac = true;
			File ldbHomeDir = new File(this.tdcHome + "/lockdownbrowser/mac");
			this.ldbHome = ldbHomeDir.getAbsolutePath();
			// Code added to launch LockDownBrowser.app for Laslinks and Tabe.
			if(productType.equals("LASLINKS") || productType.equals("TABE")){
				if(formName.equals("Form A / Form B / Español A") || formName.equals("TABE Online")){
					writeForm("http://127.0.0.1:" + jettyPort + "/login_swf.html");
				}else if(formName.equals("Form C / Form D / Español B") || formName.equals("TABE Testlets")){
					writeForm("http://127.0.0.1:" + jettyPort + "/login.html");
				}
				this.ldbCommand = new String[1];
				this.ldbCommand[0] = this.ldbHome + "/LockDownBrowser.app/Contents/MacOS/LockDownBrowser";
	            this.ldbCommand[0] = this.ldbCommand[0].replaceAll(" ", "\\ ");
	            command.add(ldbCommand[0]);
			}else{
				// Code added to launch cefclient.app for other products like TASC
				this.ldbCommand = new String[4];
				this.ldbCommand[0] =this.ldbHome +"/cefclient.app/Contents/MacOS/cefclient";
				this.ldbCommand[1] = "--url=http://127.0.0.1:" + jettyPort + "/login.html";
				this.ldbCommand[2] = "--off-screen-rendering-enabled"; //added to improve rendering of client in browser
				this.ldbCommand[3] = "--transparent-painting-enabled";
				command.add(this.ldbHome +"/cefclient.app/Contents/MacOS/cefclient");
				command.add("--url=http://127.0.0.1:" + jettyPort + "/login.html");
				command.add("--off-screen-rendering-enabled");
				command.add("--transparent-painting-enabled");
			}
            
		} else if ( linux ) {
					/*LockdownBrowserWrapper.islinux = true;
					
					File ldbHomeDir = new File(this.tdcHome + "/lockdownbrowser/linux");
					this.ldbHome = ldbHomeDir.getAbsolutePath();
					this.ldbCommand = new String[2];
					
					this.ldbCommand[0] = this.ldbHome + "/OASTDC/bin/OASTDC";          
					this.ldbCommand[0] = this.ldbCommand[0].replaceAll(" ", "\\ ");
					this.ldbCommand[1] = "http://127.0.0.1:" + jettyPort + "/login.html";*/
					LockdownBrowserWrapper.islinux = true;
					//File tdcHomeDir= new File(this.tdcHome);
					
					File ldbHomeDir = new File(this.tdcHome + "/lockdownbrowser/linux");
					this.ldbHome = ldbHomeDir.getAbsolutePath();
					
					try {
						Runtime.getRuntime().exec("chmod +x libcheck.sh", null, new File(this.ldbHome.replaceAll(" ", "\\ ")));
						Runtime.getRuntime().exec("chmod +x ChromiumLDB/cefclient", null, new File(this.ldbHome.replaceAll(" ", "\\ ")));
						Runtime.getRuntime().exec("./libcheck.sh", null, new File(this.ldbHome.replaceAll(" ", "\\ ")));
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
					String loginHtml = null;
					
					if(productType.equals("LASLINKS") || productType.equals("TABE")){
						//consoleOut("Inside Laslinks:::");
						if(formName.equals("Form A / Form B / Español A") || formName.equals("TABE Online")){
							//ConsoleUtils.messageOut("Launching Form A&B");
							this.ldbCommand = new String[2];
							this.ldbCommand[0] = this.ldbHome + "/OASTDC/bin/OASTDC";          
							this.ldbCommand[0] = this.ldbCommand[0].replaceAll(" ", "\\ ");
							this.ldbCommand[1] = "http://127.0.0.1:" + jettyPort + "/login_swf.html";
							command.add((this.ldbHome + "/OASTDC/bin/OASTDC").replaceAll(" ", "\\ "));
							command.add("http://127.0.0.1:" + jettyPort + "/login_swf.html");
							
						}else if(formName.equals("Form C / Form D / Español B") || formName.equals("TABE Testlets")){
							//ConsoleUtils.messageOut("Launching Form C&D");
							/*this.ldbCommand = new String[3];
							this.ldbCommand[0] ="java";
							this.ldbCommand[1] = "-jar";
							this.ldbCommand[2] = "LockdownBrowser.jar";*/
							this.ldbCommand = new String[2];
							this.ldbCommand[0] =this.ldbHome +"/ChromiumLDB/cefclient";
							this.ldbCommand[1] = "--url=http://127.0.0.1:" + jettyPort + "/login.html";
							command.add(this.ldbHome +"/ChromiumLDB/cefclient");
							command.add("--url=http://127.0.0.1:" + jettyPort + "/login.html");
						}
						//this.ldbCommand[1] = "http://127.0.0.1:" + jettyPort + loginHtml;
					}else{
						//ConsoleUtils.messageOut("Launching DEFAULT");
						//this.ldbCommand[1] = "http://127.0.0.1:" + jettyPort + "/login.html";
						/*this.ldbCommand = new String[3];
						this.ldbCommand[0] ="java";
						this.ldbCommand[1] = "-jar";
						this.ldbCommand[2] = "LockdownBrowser.jar";*/
						this.ldbCommand = new String[2];
						this.ldbCommand[0] =this.ldbHome +"/ChromiumLDB/cefclient";
						this.ldbCommand[1] = "--url=http://127.0.0.1:" + jettyPort + "/login.html";
						command.add(this.ldbHome +"/ChromiumLDB/cefclient");
						command.add("--url=http://127.0.0.1:" + jettyPort + "/login.html");
					}
		} else {
			//ConsoleUtils.consoleOut("WINDOWS****");
			/*File ldbHomeDir = new File(tdcHome + "/lockdownbrowser/pc");
			this.ldbHome = ldbHomeDir.getAbsolutePath();
			
			this.ldbCommand = new String[2];
			this.ldbCommand[0] = this.ldbHome + "/LockdownBrowser.exe";
			this.ldbCommand[1] = "http://127.0.0.1:" + jettyPort + "/login.html";*/
			
			File ldbHomeDir = new File(tdcHome + "/lockdownbrowser/pc");
			this.ldbHome = ldbHomeDir.getAbsolutePath();
			String loginHtml = null;
			if(productType.equals("LASLINKS") || productType.equals("TABE")){
			//	ConsoleUtils.consoleOut("Product Laslinks/TABE");
				if(formName.equals("Form A / Form B / Español A") || formName.equals("TABE Online")){
					//ConsoleUtils.consoleOut("Product TABE 9/10*****");
					//consoleOut("Inside Form A/Form B/Espanol");
					this.ldbCommand = new String[2];
					this.ldbCommand[0] = this.ldbHome + "/LockdownBrowser.exe";
					loginHtml="/login_swf.html";
					this.ldbCommand[1] = "http://127.0.0.1:" + jettyPort + loginHtml;
					//command.add(this.ldbHome +"/LockdownBrowser.exe");
					//command.add("--url=http://127.0.0.1:" + jettyPort + loginHtml);
					
				}else if(formName.equals("Form C / Form D / Español B")  || formName.equals("TABE Testlets")){
				//	ConsoleUtils.messageOut("Inside TABE Adaptive");
					/*this.ldbCommand = new String[3];
					this.ldbCommand[0] ="java";
					this.ldbCommand[1] = "-jar";
					this.ldbCommand[2] = "LockdownBrowser.jar";*/
					//this.ldbCommand = new String[2];
					//this.ldbCommand[0] =this.ldbHome +"/ChromiumLDB/cefclient";
					//this.ldbCommand[1] = "--url=http://127.0.0.1:" + jettyPort + "/login.html";
					command.add(this.ldbHome +"/ChromiumLDB/cefclient");
					command.add("--disable-accelerated-2d-canvas");					
					command.add("--url=http://127.0.0.1:" + jettyPort + "/login.html");
					command.add("--cache-path="); //cache directory specified as null to prevent caching
					
				}
				
			}else{
				//consoleOut("Not Laslinks");
				//this.ldbCommand[1] = "http://127.0.0.1:" + jettyPort + "/login.html";
				/*this.ldbCommand = new String[3];
				this.ldbCommand[0] ="java";
				this.ldbCommand[1] = "-jar";
				this.ldbCommand[2] = "LockdownBrowser.jar";*/
				//this.ldbCommand = new String[2];
				//this.ldbCommand[0] =this.ldbHome +"/ChromiumLDB/cefclient";
				//this.ldbCommand[1] = "--url=http://127.0.0.1:" + jettyPort + "/login.html";
				command.add(this.ldbHome +"/ChromiumLDB/cefclient");
				command.add("--disable-accelerated-2d-canvas");
				command.add("--url=http://127.0.0.1:" + jettyPort + "/login.html");
			}
			
			
			
		}
		
	}

	/*Function to write the URL to be launched in Mac
	 * Used only in old Mac LDB
	 * 
	 */
	private void writeForm(String output) {
		try {
			
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(this.tdcHome+"/lockdownbrowser/mac/form.txt", true)));
		    out.println(output);
		    out.close();
		} catch (IOException e) {
		   	ConsoleUtils.messageOut("Failed to writeform, default will be launched");
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
private static class LockdownWinMain extends Thread {
		
		private static String allProcessNameStr = "";
		
		private String tdcHome;
		
		public LockdownWinMain(String tdcHome){
			this.tdcHome = tdcHome;
		}
		public void run() {
			try {
				while(true){
					//ConsoleUtils.messageOut("Started Running LockdownWinMain @"+System.currentTimeMillis());
					isProcessRunning(allProcessNameStr);
					//ConsoleUtils.messageOut("Finished Running LockdownWinMain @"+System.currentTimeMillis());
					Thread.sleep(2000);
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public  static void getAllProcessName () {
			try {
				Enumeration<String> em=ResourceBundleUtils.getAllBlistProcessKeys();
				while(em.hasMoreElements()){
				  String keyStr = (String)em.nextElement();
				  allProcessNameStr = allProcessNameStr + ResourceBundleUtils.getBlistProcessString(keyStr).toLowerCase() + ",";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static void isProcessRunning(String serviceNames) throws Exception {
			//ConsoleUtils.messageOut("Started isProcessRunning @"+System.currentTimeMillis()); 
			Process processList = Runtime.getRuntime().exec("tasklist");
			 BufferedReader reader = new BufferedReader(new InputStreamReader(
			 processList.getInputStream()));
			 String line;
			 while ((line = reader.readLine()) != null) {
				 if(line.equals("") || line.length() == 0) {
					  continue;
				  }
				  String lineProcess= line.substring(0, line.indexOf(" ")).toLowerCase();
				  try{	
					  if (serviceNames.contains(lineProcess))
					  	{
						  killProcess(line.substring(0, line.indexOf(" ")));
					  	}
				   } catch (Exception e) {
						e.printStackTrace();
					}
				  
			}
			// ConsoleUtils.messageOut("Finished isProcessRunning @"+System.currentTimeMillis()); 
				
		} 	
		
		public static void killProcess(String serviceName) {
			try{
//				ConsoleUtils.messageOut("kill "+ serviceName.split("\\.")[0]);
				Runtime.getRuntime().exec("tskill "+ serviceName.split("\\.")[0]);
			 } catch (Exception e) {
				//	ConsoleUtils.messageOut("tskill "+ serviceName.split("\\.")[0] +" failed");
			}
			 try {
			//	ConsoleUtils.messageOut("taskkill "+serviceName);
				Runtime.getRuntime().exec("taskkill /F /IM "+ serviceName);  
//				
			} catch (Exception e) {
				//	ConsoleUtils.messageOut("taskkill /F /IM "+ serviceName+" failed");
			}				
			
		 }		
		
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
		private boolean keepRunning;
		public LockdownWinB(String tdcHome){
			this.tdcHome = tdcHome;
		}
		
		public void run() {
			try {
				//LockdownBrowserWrapper.Kill_Task_Mgr();
				keepRunning=true;
				while(keepRunning){
					Runtime.getRuntime().exec("taskkill /F /IM taskmgr.exe /T");
					Runtime.getRuntime().exec("taskbarhide.exe", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
					Thread.sleep(1000);
				}
					
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			}
		}
		
		public void stopLock() {
			keepRunning=false;
		}
	}

	private static class LockdownLinux extends Thread {
		
		private String tdcHome;
		
		public LockdownLinux(String tdcHome){
			this.tdcHome = tdcHome;
		}
		
		public boolean forceFullScreen() {
			try {
				//Runtime.getRuntime().exec("metacity --replace", null, new File(this.tdcHome.replaceAll(" ", "\\ "))); //causes ubuntu and suse to hang after exiting
				String wmctrl = "./wmctrl -r \"Online Assessment System\" -b \"toggle, fullscreen\"";
				Runtime.getRuntime().exec(wmctrl, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
				
				
			} catch (Exception e) {

				ConsoleUtils.messageOut("Force fullscreen : returned false -"+e.getMessage());
				return false;
			}
			return true;
		}
		
		public void run() {
			try {
				boolean forcedFullScreen = false;
				ConsoleUtils.messageOut("Blocking hotkeys at " + System.currentTimeMillis());
				LockdownBrowserWrapper.Hot_Keys_Enable_Disable(false);
				Runtime.getRuntime().exec("sh lockdown.sh &", null, new File(tdcHome.replaceAll(" ", "\\ "))); //Prevent right click from keyboard
				ConsoleUtils.messageOut("Starting lock loop at " + System.currentTimeMillis());
				while(!isProcessExit) {
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
							Runtime.getRuntime().exec("./wmctrl -a \"Presentation Canvas\"", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));	
							Runtime.getRuntime().exec("sh lockdown.sh &", null, new File(tdcHome.replaceAll(" ", "\\ "))); //Prevent right click from keyboard
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
				
				//Check whether forbidden apps are running
				ProcessBlock processBlock = new ProcessBlock (this.tdcHome);
				processBlock.start();
				processBlock.join();
				
				if (flag) {
					// Run the LDB...
					//Runtime.getRuntime().exec("sh clear_clipboard.sh", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
					//Runtime.getRuntime().exec("rm -rf ~/Library/Caches/*", null, new File(this.tdcHome.replaceAll(" ", "\\ "))); //does not work
					ConsoleUtils.messageOut(" Using ldbHome = " + this.ldbHome);
					ConsoleUtils.messageOut(" Executing " + this.ldbCommand[0]);
					
					//Process ldb = Runtime.getRuntime().exec(this.ldbCommand, null, new File(this.ldbHome) );
					processBuilder= new ProcessBuilder(command);
					processBuilder.directory(new File(this.ldbHome));		
					processBuilder.redirectErrorStream(true);
					
					Process ldb=processBuilder.start();
					ConsoleUtils.messageOut("LDB app started at " + System.currentTimeMillis());
					
					//code to consume all console output produced by the LDB
					final BufferedReader consoleBufferReader = new BufferedReader(new InputStreamReader(ldb.getInputStream()));
					new Thread(new Runnable() {
					        public void run() {
					            while(true)
					            {
					        	try {
									consoleBufferReader.readLine();
								} catch (IOException e) {
									ConsoleUtils.messageErr("Error while reading ldb output", e);
								}
					            }
					        }
					    }).start();
					this.isAvailable = true;
					ldbProcess=ldb; // Capturing ldb instance.
					ldb.waitFor();
					isProcessExit=true;
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
						e.printStackTrace();
					} catch (InvocationTargetException e) {
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
//					ConsoleUtils.messageOut("Desktop Locked at "+System.currentTimeMillis());
				}
				// Run the LDB...
				ConsoleUtils.messageOut(" Using ldbHome = " + this.ldbHome);
				
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
					ConsoleUtils.messageOut("LDB started at " + startTime);
					
					//Process ldb = Runtime.getRuntime().exec(this.ldbCommand, envp, new File(this.ldbHome+"/ChromiumLDB/"));
					//Process pBuilder= new ProcessBuilder(ldbCommand[0], ldbCommand[1]).start();
					processBuilder= new ProcessBuilder(command);
					processBuilder.directory(new File(this.ldbHome+"/ChromiumLDB/"));		
					processBuilder.redirectErrorStream(true);
					//processBuilder.redirectOutput(new File(this.ldbHome+"/ChromiumLDB/ldbDebug.log"));
					//ldb=null;
					//Thread.sleep(5000);
					
					
		//			ConsoleUtils.messageOut("Lockdown Started at " + System.currentTimeMillis());
					Process ldb=processBuilder.start();
					lockdown.start();
		//			ConsoleUtils.messageOut("LDB app started at " + System.currentTimeMillis());
					final BufferedReader consoleBufferReader = new BufferedReader(new InputStreamReader(ldb.getInputStream()));
					new Thread(new Runnable() {
					        public void run() {
					            while(true)
					            {
					        	try {
									consoleBufferReader.readLine();
								} catch (IOException e) {
									ConsoleUtils.messageErr("Error while reading ldb output", e);
								}
					            }
					        }
					    }).start();
					 ready = true;
					this.isAvailable = true;
					ldb.waitFor();
					isProcessExit=true;
					this.isAvailable = false;
					ConsoleUtils.messageOut("LDB app ended at " + System.currentTimeMillis());	
					//ConsoleUtils.messageOut("Enabled hot keys at " + System.currentTimeMillis());	
					Runtime.getRuntime().exec("./wmctrl -n 2", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
					
					LockdownBrowserWrapper.Hot_Keys_Enable_Disable(true);
					ConsoleUtils.messageOut("Hotkeys enabled at " + System.currentTimeMillis());	
					
					
					//ConsoleUtils.messageOut("Desktop unlocked ...");
					
					/*while(true) {
						Thread.sleep(3000);
					//	ConsoleUtils.messageOut("Inside LDB Loop - isProcessExit:"+isProcessExit);
						if(isProcessExit) {
							this.isAvailable = false;
							ConsoleUtils.messageOut("LDB app ended at " + System.currentTimeMillis());	
							LockdownBrowserWrapper.Hot_Keys_Enable_Disable(true);
							//ConsoleUtils.messageOut("Enabled hot keys at " + System.currentTimeMillis());	
							Runtime.getRuntime().exec("./wmctrl -n 2", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
							//ConsoleUtils.messageOut("Desktop unlocked ...");
							break;
						}
					}*/
					
					/*while(!isProcessExit)
					{
					Thread.sleep(3000);	
					}
					ConsoleUtils.messageOut("LDB ended at " + System.currentTimeMillis());	
					this.isAvailable = false;*/
					
				}
//				LockdownBrowserWrapper.Hot_Keys_Enable_Disable(true);
//				Runtime.getRuntime().exec("./wmctrl -n 2", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
					
			} else {
				// Windows native lib
				/**
				 * Get JVM bitness and load proper lock.dll.
				 */
				try {
					String jvmVersion = System.getProperty("os.arch");
					if(jvmVersion.indexOf("64") >= 0){
						System.load(this.tdcHome + "/lock_64.dll");
					}else{
						System.load(this.tdcHome + "/lock.dll");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				LockdownWin lockdown = new LockdownWin(this.tdcHome);
				LockdownWinB lockdownB = new LockdownWinB(this.tdcHome);
				//Moved From Main.java
				LockdownWinMain lockdownOK = new LockdownWinMain(tdcHome);
				LockdownWinMain.getAllProcessName();
				LockdownWinMain.allProcessNameStr = LockdownWinMain.allProcessNameStr.substring(0, LockdownWinMain.allProcessNameStr.length() - 1);
				lockdownOK.setPriority(Thread.MAX_PRIORITY);
				lockdownOK.start();
				// flag = false;
				//Code for process running popup
				/*if (LockdownBrowserWrapper.Process_Block()) {
					flag = false;
					ConsoleUtils.messageOut("Process block failed.");
				} else {*/
					flag = true;
			
					lockdown.start();
					lockdownB.start();
					
					ConsoleUtils.messageOut("Desktop Locked..........");
				//}
				// Run the LDB...
				ConsoleUtils.messageOut(" Using ldbHome = " + this.ldbHome);
				//ConsoleUtils.messageOut(" Executing " + this.ldbCommand[0]);
				
				
	
				if (flag) {
					String taskmgr = "taskbarhide.exe";
					try {
						Runtime.getRuntime().exec(taskmgr, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						ConsoleUtils.messageOut("AIR app started at " + System.currentTimeMillis());
						
						//Process ldb = new ProcessBuilder(ldbCommand[0], ldbCommand[1]).start();
						
						Process ldb;
						this.isAvailable = true;
						
						//Different launching mechanisms for different LDBs cause One is not working for both
						if(productType.equals("LASLINKS") || productType.equals("TABE")){
							if(form.equals("Form A / Form B / Español A") || form.equals("TABE Online")){
								ConsoleUtils.messageOut("Laslinks for A/B will be launched");
								ldb = Runtime.getRuntime().exec(this.ldbCommand, null, new File(this.ldbHome));
							}
							else{

								ConsoleUtils.messageOut("Laslinks for C/D will be launched");
								processBuilder= new ProcessBuilder(command);
								processBuilder.directory(new File(this.ldbHome+"/ChromiumLDB/"));
								processBuilder.redirectErrorStream(true);
								ldb=processBuilder.start();
								// Commenting code block as this is now handled from native code.
								/*try {
									Runtime.getRuntime().exec("tskill explorer");
									} catch (IOException e) {
									e.printStackTrace();
								}*/
							}
							
						}
						else{

							ConsoleUtils.messageOut("Normal client will be launched");
							processBuilder= new ProcessBuilder(command);
							processBuilder.directory(new File(this.ldbHome+"/ChromiumLDB/"));
							processBuilder.redirectErrorStream(true);
							ldb=processBuilder.start();
						}
						final BufferedReader consoleBufferReader = new BufferedReader(new InputStreamReader(ldb.getInputStream()));
						//final BufferedReader errorBufferReader = new BufferedReader(new InputStreamReader(ldb.getErrorStream()));
						new Thread(new Runnable() {
						        public void run() {
						            while(true)
						            {
						        	try {
						        		//ConsoleUtils.messageOut("Inside Output Consume Loop *******: "+consoleBufferReader.readLine());
						        		consoleBufferReader.readLine();
									} catch (IOException e) {
										e.printStackTrace();
									}
						            }
						        }
						    }).start();
						ldbProcess=ldb;
						ldb.waitFor();
						ConsoleUtils.messageOut("AIR app finished at"+System.currentTimeMillis());
						this.isAvailable=false;
						isProcessExit=true;
						taskmgr = "taskbarshow.exe";
						ConsoleUtils.messageOut("Exiting...");
						lockdownB.stopLock();
						lockdownB.join();
						Runtime.getRuntime().exec(taskmgr, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						Thread.sleep(500);	
						Runtime.getRuntime().exec(taskmgr, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						ConsoleUtils.messageOut("Unlocking");
						LockdownBrowserWrapper.CtrlAltDel_Enable_Disable(true);
						LockdownBrowserWrapper.TaskSwitching_Enable_Disable(true);
						
						cleanupLock();	// call here to fix issue in 64 bit Windows 7 
						ConsoleUtils.messageOut("Desktop unlocked ...");	
						//ldb = null;
						//int extval=ldb.waitFor();
						//Thread.sleep(300000);
						//ConsoleUtils.messageOut("Exited with " + extval);
					} catch (Throwable te) {
						ConsoleUtils.messageOut("Exited with " + te.getMessage());
						te.printStackTrace();
					}
					ConsoleUtils.messageOut("AIR app ended at " + System.currentTimeMillis());
				}
				
				
				
				/*while(true) {
					Thread.sleep(3000);
					if(isProcessExit) {
						ConsoleUtils.messageOut("Unlocking");
						LockdownBrowserWrapper.CtrlAltDel_Enable_Disable(true);
						LockdownBrowserWrapper.TaskSwitching_Enable_Disable(true);
						this.isAvailable = false;
						String taskmgr = "taskbarshow.exe";
						ConsoleUtils.messageOut("Exiting...");
						Runtime.getRuntime().exec(taskmgr, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						Thread.sleep(500);	
						Runtime.getRuntime().exec(taskmgr, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
						
						cleanupLock();	// call here to fix issue in 64 bit Windows 7 
						ConsoleUtils.messageOut("Desktop unlocked ...");	
						//Thread.sleep(1500);
						break;
					}
				}*/
			}	
			
			this.splashWindow.show();

			cleanupLock();
			//ConsoleUtils.messageOut("cleanupLock called");
			
		} catch (Exception e) {
			ConsoleUtils.messageErr("An error has occured within " + this.getClass().getName(), e);
		} finally {
		//	ConsoleUtils.messageOut("Finally...");
			cleanupLock();
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
//            ConsoleUtils.messageOut("Done with zip entries at "+System.currentTimeMillis());
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
			isProcessExit = true;
			
			ConsoleUtils.messageOut("exit called****************");
			if(islinux) {
        		Runtime.getRuntime().exec("killall OASTDC");
        		Thread.sleep(250);
        		Runtime.getRuntime().exec("killall OASTDC");
        		
        		Runtime.getRuntime().exec("killall cefclient");
        		Thread.sleep(250);
        		Runtime.getRuntime().exec("killall cefclient");
        		
        	} else if(ismac) {
        		Runtime.getRuntime().exec("killall -KILL LockDownBrowser");
        		Thread.sleep(250);
        		Runtime.getRuntime().exec("killall -KILL LockDownBrowser");
        		
        		// Code added to destroy cefclient process.
        		Runtime.getRuntime().exec("killall -KILL cefclient");
        		Thread.sleep(250);
        		Runtime.getRuntime().exec("killall -KILL cefclient");
        		
        		// Code added to destroy instance of cefclient.
        		try{
        			ldbProcess.destroy();
        		}catch (Exception e){
        			e.printStackTrace();
        		}
        	} else {
        		try {/*
        			Runtime.getRuntime().exec("taskkill /f /fi \"WINDOWTITLE eq Lockdown Browser\"");
	        		Thread.sleep(250);
	        		Runtime.getRuntime().exec("taskkill /f /fi \"WINDOWTITLE eq Lockdown Browser\"");*/

        			/*Runtime.getRuntime().exec("taskkill /f -im cefclient.exe");
	        		Thread.sleep(350);
	        		Runtime.getRuntime().exec("taskkill /f -im cefclient.exe");*/
        			// Killing ldb processes.
        			ldbProcess.destroy();
        			
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        		try {
        			Runtime.getRuntime().exec("tskill \"LockdownBrowser.exe\"");
	        		Thread.sleep(250);
	        		Runtime.getRuntime().exec("tskill \"LockdownBrowser.exe\"");
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
        		try {
        			Runtime.getRuntime().exec("tskill \"cefclient\"");
	        		Thread.sleep(250);
	        		Runtime.getRuntime().exec("tskill \"cefclient\"");
        		} catch (Exception e) {
        			e.printStackTrace();
        		}	
        	}
			Thread.sleep(3000);
			//ConsoleUtils.messageOut("In exit isProcessExit"+isProcessExit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

