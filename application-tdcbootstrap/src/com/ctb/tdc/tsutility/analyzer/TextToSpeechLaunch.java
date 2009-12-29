package com.ctb.tdc.tsutility.analyzer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import javax.swing.JOptionPane;

import javazoom.jl.player.Player;

import com.ctb.tdc.tsutility.tts.TTSUtil;
import com.ctb.tdc.tsutility.tts.TTSUtil.MP3;

import java.util.Arrays;

public class TextToSpeechLaunch {

   static final String[] browsers = { "firefox", "opera", "konqueror", "epiphany",
      "seamonkey", "galeon", "kazehakase", "mozilla", "netscape" };

   public static Process openURL(String url) {
	   
      String osName = System.getProperty("os.name");
      
      try {
         if (osName.startsWith("Mac OS")) {
            Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] {String.class});
            openURL.invoke(null, new Object[] {url});
         }
         else if (osName.startsWith("Windows"))
        	 return Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
         else { //assume Unix or Linux
            boolean found = false;
            for (String browser : browsers)
               if (!found) {
                  found = Runtime.getRuntime().exec(
                     new String[] {"which", browser}).waitFor() == 0;
                  if (found)
                     Runtime.getRuntime().exec(new String[] {browser, url});
                  }
            if (!found)
               throw new Exception(Arrays.toString(browsers));
            }
      }
      catch (Exception e) {
         JOptionPane.showMessageDialog(null, "Error attempting to launch web browser\n" + e.toString());
      }
	
      return null;
   }
   
	public static String speak(String text) {
		
		String resultStr = "";
		
		try {
			
			if ( (text != null) && (text.length() > 0)) {				
				
				MP3 mp3 = TTSUtil.speak(text);
				resultStr = mp3.getResultString();

				InputStream is = mp3.getStream();
			
	            BufferedInputStream bis = new BufferedInputStream(is);
	            
	            // run in new thread to play in background
	            final Player player = new Player(bis);				
	            new Thread() {
	                public void run() {
	                    try { player.play(); }
	                    catch (Exception e) { System.out.println(e); }
	                }
	            }.start();
	         
			}
			
		} catch (Exception ex) {		
			ex.getMessage();
			System.out.println("Error: " + ex.getMessage());											
			resultStr = "Failed to speak input text";
		}
	
		return resultStr;
	}

}
