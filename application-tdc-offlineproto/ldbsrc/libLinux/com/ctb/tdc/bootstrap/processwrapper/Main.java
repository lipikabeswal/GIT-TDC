package com.ctb.tdc.bootstrap.processwrapper;

import java.io.IOException;

public class Main extends Thread {

	/**
	 * @param args
	 */
	static volatile boolean flag = false;
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
		Main.KillRuntimeProcess killRuntimeProcess = new Main ().new KillRuntimeProcess ();
		Main.HotKeysEnable hotKeysEnable = new Main().new HotKeysEnable ();
		
		processBlock.start();
		processBlock.join();
		
		if (flag) {
			
			hotKeysDisable.start();
			killRuntimeProcess.start();
			Thread.sleep(1 * 60 * 1000);
			hotKeysEnable.start();
			
		}

		System.exit(0);

	}
	private class HotKeysEnable extends Thread {

		public void run () {

			System.out.println("Thread Name after sleep:"+Thread.currentThread().getName());
			System.out.println("Thread...Name..");
			//LockdownBrowserWrapper.CtrlAltDel_Enable_Disable(true);
			LockdownBrowserWrapper.Hot_Keys_Enable_Disable(true);

			System.out.println("WinLock-C.exe process killed");

		}

	}


	private class ProcessBlock extends Thread {

		public void run () {

			System.out.println("Child name thread.... ");
			if (LockdownBrowserWrapper.Process_Check()) {

				flag = false;
				System.out.println("inner block of process block");
			} else {
                               
                                System.out.println("Else block of process block.......");
                		flag = true;

			}

		}

	}

	private class HotkeysDisable extends Thread {

		public void run () {

			try {

				//flag = true;
				System.out.println("Main Thread Started..........");
				//LockdownBrowserWrapper.Process_Desktop("window","C:\\Program Files\\Internet Explorer\\iexplore.exe");
				//Runtime.getRuntime().exec("C:\\Program Files\\Internet Explorer\\iexplore.exe");
				//LockdownBrowserWrapper.CtrlAltDel_Enable_Disable(false);
				LockdownBrowserWrapper.Hot_Keys_Enable_Disable(false);
			} catch (Exception e)  {

				e.printStackTrace();

			}

		}

	}

	/**
	 * 
	 * @author Sandip Sadhu
	 * This method is responsible to run a thread in background and will be killed any runtime process
	 *
	 */

	private class KillRuntimeProcess extends Thread {

		public void run () {

			while (!interrupted()) {

				try {
					//System.out.println("Inside Kill Task Mgr....");
					//Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\taskmgr.exe");
					LockdownBrowserWrapper.kill_printscreen_snapshot();
	

				} catch (Exception e) {

					e.printStackTrace();

				}

			}

		}

	}

}

