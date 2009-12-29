package com.ctb.tdc.bootstrap.disablers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ctb.tdc.bootstrap.util.ConsoleUtils;

/**
 * Class to disable screen savers from execution.  Currently only implented
 * for Mac OS X support.  To summarize, the screen saver is set to an idle time
 * of 0 which is the equivalent of "never".
 * 
 * @author Giuseppe_Gennaro
 *
 */
public class MacScreenSaverDisabler {

	private boolean isMac = false;
	private String plist = null;
	private String idleTime = null;
	
	
	
	//--------------------------------------------------------------------------
	
	public MacScreenSaverDisabler() {
        String os = System.getProperty("os.name");
        if (os == null) 
            os = "";
        isMac = ( os.toLowerCase().indexOf("mac") != -1 );
        
        if( isMac ) {
        	obtainPlist();
        }
        
	}
	
	
	//--------------------------------------------------------------------------

	/**
	 * Obtain the plist attribute for enabling and disabling the screen saver.
	 * This information is found within 
	 * ~/Library/Preferences/ByHost/com.apple.screensaver.MACADDRESS.plist
	 * file.
	 */
	private void obtainPlist() {
		File plistFolder = new File(System.getProperty("user.home") + "/Library/Preferences/ByHost");
		if( !plistFolder.canRead() ) {
			ConsoleUtils.messageErr("Can not read! " + plistFolder.getAbsolutePath() );
		} else if ( !plistFolder.isDirectory() ) {
			ConsoleUtils.messageErr("Not a directory! " + plistFolder.getAbsolutePath() );
		} else  {
			File files[] = plistFolder.listFiles(new ScreenSaverPlistFilter());
			if( files.length > 1 ) {
				ConsoleUtils.messageErr("Found more than one plist file! " + plistFolder.getAbsolutePath() );
			} else if( files.length == 0 ) {
				ConsoleUtils.messageErr("Found no plist file! " + plistFolder.getAbsolutePath() );
			} else {
				String plistFilename = files[0].getName();
				this.plist = plistFilename.substring(0, plistFilename.length() - ".plist".length() );
			}
		}
	}
	
	
	/**
	 * Obtain the current settings to be restored when disable is deactivated.
	 * By using the plist information found by obtainPlist(), the mac os x 
	 * system command "defaults" can be used to obtain the idle time.
	 */
	private void obtainCurrentSettings() {
		ConsoleUtils.messageOut("obtainCurrentSettings()");
		
		if( plist != null ) {
			String[] command = new String[4];
			command[0] = "defaults";
			command[1] = "read";
			command[2] = "ByHost/" + plist;
			command[3] = "idleTime";
			
			/*
			for(int i=0; i < command.length; i++ ){
				System.out.print(command[i] + " ");
			}
			System.out.println("");
			*/
			
			try {
				Process process = Runtime.getRuntime().exec(command);
				BufferedReader stdout = new BufferedReader(new InputStreamReader( process.getInputStream() ));
				idleTime = stdout.readLine();
				stdout.close();
				ConsoleUtils.messageOut(" idleTime currently at " + idleTime);
				
			} catch (IOException e) {
				ConsoleUtils.messageErr("Error in obtaining current settings!", e);
			}
		}
	}
	
	/**
	 * Disable the screen saver by changing its settings to never run.
	 */
	private void disableScreenSaver() {
		ConsoleUtils.messageOut("disableScreenSaver()");

		if( plist != null ) {
			String[] command = new String[6];
			command[0] = "defaults";
			command[1] = "write";
			command[2] = "ByHost/" + plist;
			command[3] = "idleTime";
			command[4] = "-int";
			command[5] = "0";

			/*
			for(int i=0; i < command.length; i++ ){
				System.out.print(command[i] + " ");
			}
			System.out.println("");
			*/
			
			try {
				Process process = Runtime.getRuntime().exec(command);
				BufferedReader stdout = new BufferedReader(new InputStreamReader( process.getInputStream() ));
				String line = null;
				while( (line = stdout.readLine()) != null ) {
					 ConsoleUtils.messageOut(line);
				}
				stdout.close();
				ConsoleUtils.messageOut(" idleTime set to 0");
				
			} catch (IOException e) {
				ConsoleUtils.messageErr("Error in disabling screen saver!", e);
			}
		}
	}
	
	/**
	 * Restore original settings to the screen saver.
	 */
	private void restoreCurrentSettings() {
		ConsoleUtils.messageOut("restoreCurrentSettings()");
	}
	
	/**
	 * Re-enable the disabled screen saver.
	 */
	private void enableScreenSaver() {
		ConsoleUtils.messageOut("enableScreenSaver()");

		if( plist != null ) {
			String[] command = new String[6];
			command[0] = "defaults";
			command[1] = "write";
			command[2] = "ByHost/" + plist;
			command[3] = "idleTime";
			command[4] = "-int";
			command[5] = idleTime;

			/*
			for(int i=0; i < command.length; i++ ){
				System.out.print(command[i] + " ");
			}
			System.out.println("");
			*/
			
			try {
				Process process = Runtime.getRuntime().exec(command);
				BufferedReader stdout = new BufferedReader(new InputStreamReader( process.getInputStream() ));
				String line = null;
				while( (line = stdout.readLine()) != null ) {
					 ConsoleUtils.messageOut(line);
				}
				stdout.close();
				ConsoleUtils.messageOut(" idleTime set to " + idleTime);
				
			} catch (IOException e) {
				ConsoleUtils.messageErr("Error in enabling screen saver!", e);
			}
		}
	}
	
	
	//--------------------------------------------------------------------------
	
	/**
	 * Activates the disabler by storing temporarily current settings and then
	 * disable the process.
	 */
	public void activate() {
		
		if( isMac ) {
			obtainCurrentSettings();
			disableScreenSaver();
		}
	}

	/**
	 * Deactivates the disabler by restoring original settings and enabling the
	 * once-disabled process.
	 */
	public void deactivate() {

		if( isMac ) {
			restoreCurrentSettings();
			enableScreenSaver();
		}
	}


	
	//--------------------------------------------------------------------------
	
	private class ScreenSaverPlistFilter implements FilenameFilter {

		private Pattern p = Pattern.compile("com\\.apple\\.screensaver\\.\\w*?\\.plist");

		public boolean accept(File dir, String name) {

			Matcher m = p.matcher(name);
			if( m.matches() ) {
				return true;
			}
			return false;
		}

	}


}
