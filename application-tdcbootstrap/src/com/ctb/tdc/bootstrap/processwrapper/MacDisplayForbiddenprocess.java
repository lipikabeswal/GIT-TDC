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


public class MacDisplayForbiddenprocess{

	public static void  showMessageBox (final String processName) {

		try {
                                SwingUtilities.invokeAndWait(new Runnable() {
                                        public void run() {
                                                
                                                JFrame frame = new JFrame("ForbiddenProcess");
                                                frame.setLayout(new BorderLayout());


                                                final JLabel label = new JLabel("          Forbidden process "+processName+" is detected");
                                                JPanel p = new JPanel(new BorderLayout());
                                                p.add(label, BorderLayout.CENTER);

                                                label.setPreferredSize(new Dimension(310, 80));

                                                frame.add(label, BorderLayout.CENTER);

                                                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                                frame.pack();
                                                frame.setVisible(true);
                                        }
                                });
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        } catch (InvocationTargetException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }



	}

	public  static  String getProcessName (int value) {

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
