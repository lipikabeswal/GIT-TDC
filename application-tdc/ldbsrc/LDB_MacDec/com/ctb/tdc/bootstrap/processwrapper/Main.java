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
		
		Main.ProcessBlock processBlock = new Main().new ProcessBlock ();
		Main.HotkeysDisable hotKeysDisable = new Main().new HotkeysDisable ();
		
		//Main.KillRuntimeProcess killRuntimeProcess = new Main ().new KillRuntimeProcess ();
		Main.HotKeysEnable hotKeysEnable = new Main().new HotKeysEnable ();
		
		processBlock.start();
		processBlock.join();
		
		if (!flag) {
			
			LockdownBrowserWrapper.Open_Close_Interface(false);
			Thread.sleep(1000);
			hotKeysDisable.start();
			hotKeysDisable.join();
			LockdownBrowserWrapper.Open_Close_Interface(true);
			Thread.sleep(1 * 15 * 1000);
			LockdownBrowserWrapper.Open_Close_Interface(false);
			Thread.sleep(1000);
			hotKeysEnable.start();
			hotKeysEnable.join();
			LockdownBrowserWrapper.Open_Close_Interface(true);
			
		} else {
		
			MacDisplayForbiddenprocess.showMessageBox(processName);

		}

	

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
