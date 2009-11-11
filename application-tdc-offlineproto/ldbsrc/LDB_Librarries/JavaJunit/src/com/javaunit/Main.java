package com.javaunit;

import java.io.IOException;

//public class Main implements Runnable{
public class Main {
	/**
	 * @param args
	 */
	static boolean flag = false;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
/*		Main m = new Main();
		Thread t = new Thread (m);
		t.start();*/
		if (LockdownBrowserWrapper.Process_Block()) {
			
			flag = false;
			System.out.println("inner block of process block");
		} else {
			
			flag = true;
			LockdownBrowserWrapper.CtrlAltDel_Enable_Disable(false);
			LockdownBrowserWrapper.Hot_Keys_Enable_Disable(false);
			System.out.println("Desktop Locked..........");
		}
		Thread.sleep(1000);
		
		if (flag) {
			
			System.out.println("Main Thread Started..........");
			//Runtime.getRuntime().exec("C:\\Program Files\\CTB\\Online Assessment\\lockdownbrowser\\pc\\LockdownBrowser.exe");
			Runtime.getRuntime().exec("C:\\Program Files\\Internet Explorer\\iexplore.exe -k");
			//LockdownBrowserWrapper.Process_Desktop("OAS", "C:\\Program Files\\Internet Explorer\\iexplore.exe");
			Thread.sleep(40000);
			LockdownBrowserWrapper.CtrlAltDel_Enable_Disable(true);
			LockdownBrowserWrapper.Hot_Keys_Enable_Disable(true);
			System.out.println("WinLock-C.exe process killed");
			System.out.println("Desktop unlocked ...");
			
		}
		
	}
	
	/*public void run () {
		System.out.println("Child name thread.... ");
		if (LockdownBrowserWrapper.Process_Block()) {
			
			flag = false;
			System.out.println("inner block of process block");
		} else {
			
			flag = true;
			LockdownBrowserWrapper.CtrlAltDel_Enable_Disable(false);
			LockdownBrowserWrapper.Hot_Keys_Enable_Disable(false);
		}
		
		
		
	}*/

}
