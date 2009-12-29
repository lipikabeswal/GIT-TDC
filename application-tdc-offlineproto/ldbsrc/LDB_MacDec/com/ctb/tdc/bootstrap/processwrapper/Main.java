package com.ctb.tdc.bootstrap.processwrapper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;



import java.io.IOException;

public class Main extends Thread {

	/**
	 * @param args
	 */
	static volatile boolean flag = false;
	static String processName = null;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		/*Main m = new Main();

		Thread t = new Thread (m);
		t.start();
		Thread.sleep(1000);
		//"C:\\Program Files\\CTB\\Online Assessment\\lockdownbrowser\\pc\\LockdownBrowser.exe"
		if (flag) {


			System.out.println("Main Thread Started..........");
			//LockdownBrowserWrapper.Process_Desktop("window","C:\\Program Files\\Internet Explorer\\iexplore.exe");
			Runtime.getRuntime().exec("C:\\Program Files\\Internet Explorer\\iexplore.exe");
			System.out.println("Thread Name before sleep:"+Thread.currentThread().getName());
			Thread.sleep(1 * 60 * 1000);
			System.out.println("Thread Name after sleep:"+Thread.currentThread().getName());
			System.out.println("Thread...Name..");
			//LockdownBrowserWrapper.CtrlAltDel_Enable_Disable(true);
			LockdownBrowserWrapper.TaskSwitching_Enable_Disable(true);
			System.out.println("WinLock-C.exe process killed");
			System.exit(0);

		}*/
		
		Main.ProcessBlock processBlock = new Main().new ProcessBlock ();
		Main.HotkeysDisable hotKeysDisable = new Main().new HotkeysDisable ();
		//Main.KillRuntimeProcess killRuntimeProcess = new Main ().new KillRuntimeProcess ();
		Main.HotKeysEnable hotKeysEnable = new Main().new HotKeysEnable ();
		
		processBlock.start();
		processBlock.join();
		
		if (!flag) {
			
			hotKeysDisable.start();
			//killRuntimeProcess.start();
			//LockdownBrowserWrapper.Hot_Keys_Enable_Disable(false);
			Thread.sleep(1 * 60 * 1000);
			hotKeysEnable.start();
			//LockdownBrowserWrapper.Hot_Keys_Enable_Disable(true);
			
		} else {
		
			MacDisplayForbiddenprocess.showMessageBox(processName);

		}

		//System.exit(0);

	}
	private class HotKeysEnable extends Thread {
		public void run () {
            try {
            	
            	System.out.println("Thread Name after sleep:"+Thread.currentThread().getName());
    			System.out.println("Thread...Name..");
    			
    			
    			
    			
            		 LockdownBrowserWrapper.Hot_Keys_Enable_Disable(true);
            } catch (Exception e)  {

				e.printStackTrace();
 
			}
			

		}

	}


	private class ProcessBlock extends Thread {

		public void run() {

			System.out.println("Child name thread.... ");
			int value = LockdownBrowserWrapper.Get_Blacklist_Process_No();
			if (value == 0) {

				flag = false;
				System.out.println("inner block of process block");
			} else {

				flag = true;				
				 processName =MacDisplayForbiddenprocess.getProcessName (value);
				//showWindow (processName);
				

			}

		}

	}


	private class HotkeysDisable extends Thread {

		public void run () {

			try {

				flag = true;
				System.out.println("Main Thread Started..........");
				
				
				
				
				 LockdownBrowserWrapper.Hot_Keys_Enable_Disable(false);

			} catch (Exception e)  {

				e.printStackTrace();

			}

		}

	}


	

}
