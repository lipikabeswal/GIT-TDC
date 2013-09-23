package com.ctb.tdc.tsutility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import com.ctb.tdc.bootstrap.util.ConsoleUtils;
import com.ctb.tdc.tsutility.ui.BandwidthMainWindow;
import com.ctb.tdc.tsutility.ui.MainWindow;
import com.ctb.tdc.tsutility.ui.SpeechMainWindow;

/**
 * The class containing the main method used in launching the troubleshooting utility.
 */
public class Main {
	
	
	
	private static String[] proxyHost = null;
	private static String[] proxyUsername = null;
	
			 
    private static void createAndShowGUI() {
    	
    	MainWindow networkWindow = new MainWindow();
    	BandwidthMainWindow bandwidthWindow = new BandwidthMainWindow();
    	SpeechMainWindow speechWindow = new SpeechMainWindow();
    	
    	networkWindow.setFrames(networkWindow, bandwidthWindow, speechWindow);
    	bandwidthWindow.setFrames(networkWindow, bandwidthWindow, speechWindow);
    	speechWindow.setFrames(networkWindow, bandwidthWindow, speechWindow);
    	try{
	    	if(proxyHost != null ){
	    		networkWindow.setProxyHost(proxyHost[0]);
	    		networkWindow.setProxyPort(proxyHost[1]);
	    	}
	    	if(proxyUsername != null ){
	    		networkWindow.setProxyUsername(proxyUsername[0]);
	    		networkWindow.setProxyPassword(proxyUsername[1]);
	    	}
    	} 	catch (Exception e)
        	{
    			networkWindow.setProxyHost(null);
    			networkWindow.setProxyPort(null);
    			networkWindow.setProxyUsername(null);
	    		networkWindow.setProxyPassword(null);
        	}
    	
    	networkWindow.switchFrame(networkWindow);
    	//networkWindow.switchFrame(bandwidthWindow);
    	//networkWindow.switchFrame(speechWindow);
    }
    
    

    public static void main(String[] args) {
    	String proxyurl = null;
    	String[] tempArray = new String[2];
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
    	try{
	    	ArrayList config = loadFile("config");
	    	if(config != null ){
	    		proxyurl = (String) config.get(3);
	    	}
	    	//ConsoleUtils.messageOut("proxyurl from config> "+proxyurl);
			for(int i=0;i<args.length;i++) {
				String arg = args[i];
				if(arg != null && !arg.trim().equals("")) {
					if(arg.startsWith("/proxy")) {
						proxyurl = arg.substring(7, arg.length());
					} 
				}
			}
			//ConsoleUtils.messageOut("proxyurl after cmd line > "+proxyurl);
		
			if(proxyurl != null && !proxyurl.trim().equals("") ){
				if(proxyurl.indexOf('@') != -1){
					tempArray = proxyurl.split("@");
				}else{
					tempArray[0] = proxyurl;
				}
				if(tempArray[0].indexOf(':') != -1){
					proxyHost = tempArray[0].split(":");
				}
				if(tempArray[1] != null){				
					if(tempArray[1].indexOf(':') != -1){
						proxyUsername = tempArray[1].split(":");
					}
				}
			}
		}	catch (Exception e)
	        {
				proxyHost = null;
				proxyUsername = null;
	        }
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }	
    
	public static ArrayList loadFile(String fileName)
    {
        if ((fileName == null) || (fileName == ""))
            throw new IllegalArgumentException();
        
        String line;
        ArrayList file = new ArrayList();

        try
        {    
        	File root;
        	root = new File(".");
        	String pathOfTheCurrentClass = root.getPath(); 
        	BufferedReader in = new BufferedReader(new FileReader(pathOfTheCurrentClass + "/../../"+fileName));

            if (!in.ready())
                throw new IOException();

            while ((line = in.readLine()) != null) 
                file.add(line);

            in.close();
        }
        catch (IOException e)
        {
        	return null;
        }
        if(file.size()<4){
        	return null;
        }else{
        	return file;
        }
     }
	
	

}
