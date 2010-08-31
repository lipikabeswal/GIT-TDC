package com.ctb.tdc.bootstrap.processwrapper;

import java.io.File;


/**
 * Wrapper class for starting the LockdownBrowser process.  The Lockdown Browser
 * executable is environment specific but will be installed in known locations
 * to be able to be executed successfully from Runtime.exec() method.
 * 
 * @author TCS
 */
public class LockdownBrowserWrapper  {

	public static native int Get_Blacklist_Process_No(); 
		
	// JNI lockdown library
	static {
        System.loadLibrary("lock");
    }
	
}

