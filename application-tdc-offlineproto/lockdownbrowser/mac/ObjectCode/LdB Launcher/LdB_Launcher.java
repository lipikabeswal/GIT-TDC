//
//  LdB_Launcher.java
//  LdB Launcher
//
//


import java.util.*;

import java.lang.*;


public class LdB_Launcher {

    public static void main (String args[]) {
        
		String osName = System.getProperty("os.name");
		
		String[] pathToLockdownBrowser = null;
		if (osName == null)
		{
			System.out.println("Unable to determine operating system; throw exception?");
		}else
		{
			if (osName.startsWith("Mac OS X")) {
				
				pathToLockdownBrowser = new String[] {"/Applications/Lockdown Browser.app/Contents/MacOS/Lockdown Browser"};
				
			}else if (osName.startsWith("Windows")) {
				
				pathToLockdownBrowser = new String[] {"Path to LockdownBrowser on Windows"};
				
			}else if (osName.startsWith("Linux")) {
				
				pathToLockdownBrowser = new String[] {"Path to LockdownBrowser on Linux"};
				
			}else {
				
				System.out.println("Unknown operating system; throw exception?");
			}
		}
	//	assert(pathToLockdownBrowser != null);
		
		try
		{
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(pathToLockdownBrowser);
			
		}catch(java.io.IOException ioe)
		{
			// The Lockdown Browser has been moved.
			// Do something intelligent about the exception (like suggesting reinstallation).
			System.out.println("Caught java.io.IOException.");
			System.out.println(ioe.getMessage());
		
		}
		
        System.out.println("LdB Launcher should have launched the Lockdown Browser.");
    }
}
