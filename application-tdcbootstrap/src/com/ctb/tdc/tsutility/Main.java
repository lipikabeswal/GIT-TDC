package com.ctb.tdc.tsutility;

import com.ctb.tdc.tsutility.ui.BandwidthMainWindow;
import com.ctb.tdc.tsutility.ui.MainWindow;
import com.ctb.tdc.tsutility.ui.SpeechMainWindow;

/**
 * The class containing the main method used in launching the troubleshooting utility.
 */
public class Main {
			 
    private static void createAndShowGUI() {
    	
    	MainWindow networkWindow = new MainWindow();
    	BandwidthMainWindow bandwidthWindow = new BandwidthMainWindow();
    	SpeechMainWindow speechWindow = new SpeechMainWindow();
    	
    	networkWindow.setFrames(networkWindow, bandwidthWindow, speechWindow);
    	bandwidthWindow.setFrames(networkWindow, bandwidthWindow, speechWindow);
    	speechWindow.setFrames(networkWindow, bandwidthWindow, speechWindow);
    	
    	networkWindow.switchFrame(networkWindow);
    	//networkWindow.switchFrame(bandwidthWindow);
    	//networkWindow.switchFrame(speechWindow);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }	
	
	

}
