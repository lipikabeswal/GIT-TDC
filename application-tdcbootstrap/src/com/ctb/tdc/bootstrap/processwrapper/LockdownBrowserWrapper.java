package com.ctb.tdc.bootstrap.processwrapper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

	private String tdcHome;
	private String ldbHome;
	
	private SplashWindow splashWindow;
	
	private boolean islinux = false;
	
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

			File ldbHomeDir = new File(this.tdcHome + "/lockdownbrowser/mac");
			this.ldbHome = ldbHomeDir.getAbsolutePath();
			this.ldbCommand = new String[1];
//            this.ldbCommand[0] = "/Applications/Safari.app/Contents/MacOS/Safari"; 
			
			/*Solution for Deferred Defect No: 50573 */
			/* call the delete method of ObjectBankUtils class*/
			objectBankUtils = new ObjectBankUtils();
			objectBankUtils.deleteContents();
			
            this.ldbCommand[0] = this.ldbHome + "/OASTDC.app/Contents/MacOS/OASTDC";            
            this.ldbCommand[0] = this.ldbCommand[0].replaceAll(" ", "\\ ");
            
		} else if ( linux ) {
			this.islinux = true;
			
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
				Runtime.getRuntime().exec("xmodmap -e \"pointer = 1 9 8 7 6 5 4 3 2\"");
				Thread.sleep(5000);
				String wmctrl = "./wmctrl -r \"Online Assessment System\" -b \"toggle, fullscreen\"";
				Runtime.getRuntime().exec(wmctrl, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
				while(true) {
					LockdownBrowserWrapper.kill_printscreen_snapshot();
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
			this.splashWindow.hide();
		
			unpackLock();
		
			if (islinux) {
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
					Process ldb = Runtime.getRuntime().exec(this.ldbCommand, null, new File(this.ldbHome));
					ldb.waitFor();
					ConsoleUtils.messageOut("AIR app ended at " + System.currentTimeMillis());
					LockdownBrowserWrapper.Hot_Keys_Enable_Disable(true);
					Runtime.getRuntime().exec("xmodmap -e \"pointer = 1 2 3 4 5 6 7 8 9\"");
					ConsoleUtils.messageOut("Desktop unlocked ...");
				}
				
				this.isAvailable = false;	
			} else {
				// Windows native lib
				System.load(this.tdcHome + "/lock.dll");
				LockdownWin lockdown = new LockdownWin(this.tdcHome);
				
				//boolean flag = false;
				if (LockdownBrowserWrapper.Process_Block()) {
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
					String taskmgr = "taskbarhide.exe";
					Runtime.getRuntime().exec(taskmgr, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
					ConsoleUtils.messageOut("AIR app started at " + System.currentTimeMillis());
					Process ldb = Runtime.getRuntime().exec(this.ldbCommand, null, new File(this.ldbHome));
					//Process ldb = Runtime.getRuntime().exec("C:\\Program Files\\Internet Explorer\\iexplore.exe -k");
					ldb.waitFor();
					ConsoleUtils.messageOut("AIR app ended at " + System.currentTimeMillis());
					LockdownBrowserWrapper.CtrlAltDel_Enable_Disable(true);
					LockdownBrowserWrapper.TaskSwitching_Enable_Disable(true);
					taskmgr = "taskbarshow.exe";
					Runtime.getRuntime().exec(taskmgr, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
					ConsoleUtils.messageOut("Desktop unlocked ...");	
					Thread.sleep(1500);
				}
				
				this.isAvailable = false;
			}	
			cleanupLock();
			this.splashWindow.show();
		} catch (Exception e) {
			ConsoleUtils.messageErr("An error has occured within " + this.getClass().getName(), e);
		}
	}
	
	private ArrayList lockFiles = new ArrayList();
	
	public void cleanupLock() {
		Iterator it = lockFiles.iterator();
		while (it.hasNext()) {
			File file = new File((String)it.next());
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
						if(islinux) {
							Runtime.getRuntime().exec("chmod u+x " + zipEntryFilePath);
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
	
}

