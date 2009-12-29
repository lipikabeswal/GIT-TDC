package com.ctb.tdc.bootstrap.processwrapper;

import java.io.File;


/**
 * Wrapper class for starting the LockdownBrowser process.  The Lockdown Browser
 * executable is environment specific but will be installed in known locations
 * to be able to be executed successfully from Runtime.exec() method.
 * 
 * @author Giuseppe_Gennaro
 */
public class LockdownBrowserWrapper  {

	
			
	/*Solution for Deferred defect No: 50573 */
	
	// JNI lockdown functions
	public static native void CtrlAltDel_Enable_Disable(boolean toggle);
	public static native void TaskSwitching_Enable_Disable(boolean toggle);
	public static native void Process_Desktop(String windowName, String command);
	public static native boolean Process_Block();
	public static native boolean Kill_Task_Mgr();
	public static native void Hot_Keys_Enable_Disable( boolean flag);
	public static native void kill_printscreen_snapshot();
	public static native boolean Process_Check();
        public static native int Get_Blacklist_Process_No(); 
	public static native void Add_shortcuts(boolean flag);
        public static native void Rightclick_Enable_Disable(boolean flag);
	
	// JNI lockdown library
	static {
        System.loadLibrary("lock");
    }
	
	/*public static void main (String [] argv) {
		try {
			//GeneralKeys_Enable_Disable(false);
			CtrlAltDel_Enable_Disable(false);
	        TaskSwitching_Enable_Disable(false);
	        
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}*/
	
	
	/**
	 * Returns whether the LockdownBrowsesr process is currently available.  The 
	 * internal flag is set right after system execution of the executable and 
	 * set to false immediately after the process has been terminated.
	 * 
	 * @return true after the LDB has been launched, false before it has been launched or after it has been terminated.
	 */
	/*public boolean isAvailable() {
		return this.isAvailable ;
	}*/
	
	/**
	 * Starts the LockdownBrowser.  This thread will block and wait for the LDB to finish
	 * execution before this thread itself will die.
	 */
	
	

}

