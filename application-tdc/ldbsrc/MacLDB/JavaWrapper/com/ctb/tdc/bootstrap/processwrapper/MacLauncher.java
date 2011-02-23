 package com.ctb.tdc.bootstrap.processwrapper;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.io.*;
import java.awt.*;


public class MacLauncher {

	/**
	 * @param args
	 */
	static volatile boolean flag = false;
	static volatile String processName = "";
	public static void main(String[] args) throws Exception {
		
		
		MacLauncher.ProcessBlock processBlock = new MacLauncher().new ProcessBlock ();
		processBlock.start();
		processBlock.join();
		
		if (flag) {
			
			
			Runtime.getRuntime().exec("/Applications/LockDownBrowser.app/Contents/MacOS/LockDownBrowser");
			
		} else {
			
			
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						
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
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						//frame.pack();
						frame.setVisible(true);
						
					}
				});
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
		
	}
	


	private class ProcessBlock extends Thread {

		public void run () {

			try {
			
				LockdownBrowserWrapper.Get_Blacklist_Process_No();
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

}

