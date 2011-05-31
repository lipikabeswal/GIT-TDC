package com.ctb.tdc.tuningutility.processwrapper;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import com.ctb.tdc.bootstrap.processwrapper.exception.ProcessWrapperException;
import com.ctb.tdc.bootstrap.ui.SplashWindow;
import com.ctb.tdc.bootstrap.util.ConsoleUtils;

/**
 * Wrapper class for starting the tuningutilitybrowser process.  The WorkstationTuning Browser
 * executable is environment specific but will be installed in known locations
 * to be able to be executed successfully from Runtime.exec() method.
 * 
 * @author Giuseppe_Gennaro
 */
public class TuningUtilityBrowserWrapper extends Thread {

	static volatile String processName = "";
	
	private String tdcHome;
	private String wtbHome;
	
	private static boolean islinux = false;
	private static boolean ismac = false;
	
	private String[] wtbCommand;
	private boolean isAvailable = false;
	private boolean flag = false;
	static volatile boolean ready = false;
	
	/**
	 * Creates a new WorkstationTuning Browser process wrapper with the given tdcHome value.
	 * @param tdcHome  The location of the Test Delivery Client's home folder containing the home folder of Jetty as well as configuration files and additional java libraries. 
	 * @throws ProcessWrapperException If problems within initialization such as determing the default URL or if the port is already in use.
	 */
	public TuningUtilityBrowserWrapper(String tdcHome, boolean macOS, boolean linux, SplashWindow splashWindow, int jettyPort) {
		super();
		
		this.tdcHome = tdcHome;
		if ( macOS ) {
			TuningUtilityBrowserWrapper.ismac = true;
			
			File wtbHomeDir = new File(this.tdcHome + "/tuningutilitybrowser/mac");
			this.wtbHome = wtbHomeDir.getAbsolutePath();
			this.wtbCommand = new String[2];
			ConsoleUtils.messageOut("12 Using wtbHome = " + this.wtbHome);
            //this.wtbCommand[0] = this.wtbHome + "/WorkStationTuningUtilityBrowser.app/Contents/MacOS/WorkStationTuningUtilityBrowser";            
            this.wtbCommand[0] = "open -a " + this.wtbHome + "/WorkStationTuningUtilityBrowser.app";
            this.wtbCommand[0] = this.wtbCommand[0].replaceAll(" ", "\\ ");
            this.wtbCommand[1] = "http://127.0.0.1:" + jettyPort + "/tuningutility.html";
            
		} else if ( linux ) {
			TuningUtilityBrowserWrapper.islinux = true;
			
			File wtbHomeDir = new File(this.tdcHome + "/tuningutilitybrowser/linux");
			this.wtbHome = wtbHomeDir.getAbsolutePath();
			this.wtbCommand = new String[2];
			
            this.wtbCommand[0] = this.wtbHome + "/bin/WorkStationTuningUtilityBrowser";          
            this.wtbCommand[0] = this.wtbCommand[0].replaceAll(" ", "\\ ");
            this.wtbCommand[1] = "http://127.0.0.1:" + jettyPort + "/tuningutility.html";
		} else {
			
			File wtbHomeDir = new File(tdcHome + "/tuningutilitybrowser/pc");
			this.wtbHome = wtbHomeDir.getAbsolutePath();			
			this.wtbCommand = new String[2];
			ConsoleUtils.messageOut(" Using wtbHome = " + this.wtbHome);
			this.wtbCommand[0] = this.wtbHome + "/WorkStationTuningUtilityBrowser.exe";
			this.wtbCommand[1] = "http://127.0.0.1:" + jettyPort + "/tuningutility.html";
		}
		
	}

	/**
	 * Returns whether the WorkstationTuningBrowsesr process is currently available.  The 
	 * internal flag is set right after system execution of the executable and 
	 * set to false immediately after the process has been terminated.
	 * 
	 * @return true after the WTB has been launched, false before it has been launched or after it has been terminated.
	 */
	public boolean isAvailable() {
		return this.isAvailable ;
	}


	/**
	 * Starts the tuningutilitybrowser.  This thread will block and wait for the WTB to finish
	 * execution before this thread itself will die.
	 */
	public void run() {
		try {
			//unpackLock();
		
			//this.splashWindow.hide();
			ConsoleUtils.messageOut(" Using wtbHome = " + this.wtbHome);
			ConsoleUtils.messageOut(" wtbCommand[0] " + this.wtbCommand[0]);
			ConsoleUtils.messageOut(" wtbCommand[1] " + this.wtbCommand[1]);
			if (ismac) {
				// Run the WTB...
				ConsoleUtils.messageOut(" Using wtbHome = " + this.wtbHome);
				ConsoleUtils.messageOut(" Executing " + this.wtbCommand[0]);
					
				Process wtb = Runtime.getRuntime().exec(this.wtbCommand, null, new File(this.wtbHome) );
				this.isAvailable = true;
				wtb.waitFor();
				this.isAvailable = false;
			} else if (islinux) {
				// Run the WTB...
				ConsoleUtils.messageOut(" Using wtbHome = " + this.wtbHome);
				ConsoleUtils.messageOut(" Executing " + this.wtbCommand[0]);
	
				
					Map<String,String> env = System.getenv();
					Iterator it = env.keySet().iterator();
					String envp[] = new String[env.keySet().size()];
					int i=0;
					while(it.hasNext()) {
						String key = (String) it.next();
						if(!(key.startsWith("http") && key.endsWith("_proxy"))) {
							envp[i] = key + "=" + (String) env.get(key);
						} else {
							envp[i] = i + "=" + i;
						}
						i++;
					} 
					
					long startTime = System.currentTimeMillis();
					ConsoleUtils.messageOut("AIR app started at " + startTime);
					Process wtb = Runtime.getRuntime().exec(this.wtbCommand, envp, new File(this.wtbHome));
					Thread.sleep(10000);
					//ready = true;
					this.isAvailable = true;
					wtb.waitFor();
					this.isAvailable = false;
					ConsoleUtils.messageOut("AIR app ended at " + System.currentTimeMillis());	
				
				
			} else {
				// Run the WTB...
				ConsoleUtils.messageOut(" Using wtbHome = " + this.wtbHome);
				ConsoleUtils.messageOut(" Executing " + this.wtbCommand[0]);
					
				ConsoleUtils.messageOut("AIR app started at " + System.currentTimeMillis());
				Process wtb = Runtime.getRuntime().exec(this.wtbCommand, null, new File(this.wtbHome));
				//Process wtb = Runtime.getRuntime().exec("C:\\Program Files\\Internet Explorer\\iexplore.exe -k");
				this.isAvailable = true;
				wtb.waitFor();
				this.isAvailable = false;
				ConsoleUtils.messageOut("AIR app ended at " + System.currentTimeMillis());
								
			}	
			
		} catch (Exception e) {
			ConsoleUtils.messageErr("An error has occured within " + this.getClass().getName(), e);
		} finally {
			exit();
		}
	}
	  
	//private ArrayList lockFiles = new ArrayList();
	
/*	public void cleanupLock() {
		
		String msg;
		Iterator it = lockFiles.iterator();
		while (it.hasNext()) {

			try {
				File file = new File((String)it.next());
				boolean ret = file.delete();
				msg = "delete " + file.getName() + " = " + ret + "   ";
				ConsoleUtils.messageOut(msg);	
			} catch (Exception e) {
				msg = "failed to delete file" + e.getMessage();				
				ConsoleUtils.messageOut(msg);		
			}
			
		}	
		
		if(islinux) {
			String pcheckFile = this.tdcHome + "/processcheck";
			File file = new File(pcheckFile);
			file.delete();
			pcheckFile = this.tdcHome + "/xmodmap_modified";
			file = new File(pcheckFile);
			file.delete();
			pcheckFile = this.tdcHome + "/xmodmap_original";
			file = new File(pcheckFile);
			file.delete();
		}
	} */
	
	/*public void unpackLock() throws Exception
    {
        try
        {
        	String encFile = this.tdcHome + "/lock.enc";
        	
        	FileInputStream input = new FileInputStream( encFile );
            int size = input.available();
            byte[] src = new byte[ size ];
            input.read( src );
            input.close();
            
            byte[] outBuff = TdcConfigEncryption.decrypt(src);
            ByteArrayInputStream decrypted = new ByteArrayInputStream( outBuff );

			ZipInputStream zis = new ZipInputStream( decrypted );
			ZipEntry zipEntry;
			
			ConsoleUtils.messageOut("Zip entries...");
			while( (zipEntry = zis.getNextEntry()) != null ) {
				if( !zipEntry.isDirectory() ) {
                    String zipEntryFilePath = null;
                    String zipEntryFileName = zipEntry.getName();
                    zipEntryFilePath = tdcHome + "/" + zipEntryFileName;
                    if ( zipEntryFilePath != null ) {
						ConsoleUtils.messageOut( " - writing " + zipEntryFilePath );
						try {
							File file = new File(zipEntryFilePath);
							FileOutputStream fos = new FileOutputStream(file, false);
							int read = 0;
							while( read >= 0 && read < zipEntry.getSize() ) {
								byte[] bytes = new byte[2048];
								// fix typo
								read = zis.read(bytes);
								if( read != -1 ) {
									if(zipEntryFileName.indexOf(".sh") >= 0) {
										// strip out Ctrl-M
										byte[] cleanbytes = new byte[read];
										for(int i=0;i<read;i++){
											if(bytes[i] != 13) {
												cleanbytes[i] = bytes[i];
											} else {
												cleanbytes[i] = ' ';
											}
										}
										fos.write(cleanbytes, 0, read);
									} else {
										fos.write(bytes, 0, read);
									}
								}
							}
							fos.close();
							lockFiles.add(zipEntryFilePath);
							if(islinux || ismac) {
								Runtime.getRuntime().exec("chmod a+rwx " + zipEntryFilePath, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
								if(zipEntryFilePath.endsWith(".sh") || zipEntryFilePath.endsWith(".c")) {
									Runtime.getRuntime().exec("tr -d '\015\032' < " + zipEntryFilePath + " > " + zipEntryFilePath + ".tmp", null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
									Runtime.getRuntime().exec("mv " + zipEntryFilePath + ".tmp " + zipEntryFilePath, null, new File(this.tdcHome.replaceAll(" ", "\\ ")));
								}
							}
						} catch (FileNotFoundException e) {
							// couldn't write file as it's in use by another instance
						}
                    }
				} else {
					new File(zipEntry.getName()).mkdir();
				}
			}
			zis.close();
            ConsoleUtils.messageOut("Done with zip entries.");
        } catch ( Exception e ) {
            System.err.println("Exception : "  + e.getMessage());
            e.printStackTrace();
        }
    } */
	
/*	class ProcessBlock extends Thread {
		private String tdchome;
		public ProcessBlock(String tdchome){
			this.tdchome = tdchome;
		}
		public void run () {

			try {
				// start change for mac 10.4
				//WorkstationTuningBrowserWrapper.Get_Blacklist_Process_No();
				Process lock = Runtime.getRuntime().exec("sh macpcheck.sh", null, new File(this.tdchome.replaceAll(" ", "\\ ")));
				lock.waitFor();
				// end change for mac 10.4
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

	}*/
	
/*	private String getProcessName (int value) {

		Properties prop = new Properties ();
		String str = null;
		try {

			prop.load(new FileInputStream ("BlacklistProcess.properties"));
			str = prop.getProperty(String.valueOf(value));

		} catch (Exception e) {

			e.printStackTrace();

		}
		return str;

	} */
	
	public static synchronized void exit() {
		try {
			if(islinux) {
        		Runtime.getRuntime().exec("killall OASTDC");
        		Thread.sleep(250);
        		Runtime.getRuntime().exec("killall OASTDC");
        	} else if(ismac) {
        		Runtime.getRuntime().exec("killall -KILL WorkStationTuningUtilityBrowser");
        		Thread.sleep(250);
        		Runtime.getRuntime().exec("killall -KILL WorkStationTuningUtilityBrowser");
        	} else {
        		try {
	        		Runtime.getRuntime().exec("taskkill /IM \"WorkStationTuningUtilityBrowser.exe\"");
	        		Thread.sleep(250);
	        		Runtime.getRuntime().exec("taskkill /IM \"WorkStationTuningUtilityBrowser.exe\"");
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        		try {
        			Runtime.getRuntime().exec("tskill \"WorkStationTuningUtilityBrowser\"");
	        		Thread.sleep(250);
	        		Runtime.getRuntime().exec("tskill \"WorkStationTuningUtilityBrowser\"");
        		} catch (Exception e) {
        			e.printStackTrace();
        		}	
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

