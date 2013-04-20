package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public final class ReLoginUtility {
	
	private static ReLoginUtility reLoginUtility = null;
	private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
	private static final double PROCESS_MEM_PERCENTAGE = 30;
	private static String loginId = null;
	private static String password = null;
	private static String accessCode = null;
	private static long totalPhysicalMemory = 0L;
	
	private ReLoginUtility() { 
		if(OS_NAME.indexOf("win") >= 0) {
			BufferedReader input = null;
			String line = null;
			try {
				Process p = Runtime.getRuntime().exec(new String [] {"systeminfo"});
				input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					if(line != null && line.trim().length() > 0) {
					    String details [] = line.split(":");
					    if(details[0].indexOf("Total Physical Memory") != -1) {
						    if(details[1].indexOf("GB") != -1) {
						    	totalPhysicalMemory = Long.valueOf(details[1].replace(",", "").replace("MB", "").trim()) * 1024;
						    }else if(details[1].indexOf("MB") != -1) {
						    	totalPhysicalMemory = Long.valueOf(details[1].replace(",", "").replace("MB", "").trim());
						    } else if(details[1].indexOf("KB") != -1) {
						    	totalPhysicalMemory = Long.valueOf(details[1].replace(",", "").replace("MB", "").trim()) / 1024;
						    }
						    break;
					    }
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(input != null)
						input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static ReLoginUtility getReLoginUtility() {
		if(reLoginUtility == null) {
			synchronized (ReLoginUtility.class) {
				reLoginUtility = new ReLoginUtility();
			}
		}
		return reLoginUtility;
	}
	
	public static String isMaxMemory(String strUserName, String strPassword, String strAccessCode) 
	throws ParserConfigurationException, SAXException, IOException {
		try {
			Process p = null;
			if (OS_NAME.indexOf("win") >= 0) {
				String cmd = "tasklist /fo list /fi \"IMAGENAME eq LockdownBrowser.exe\"";
				p = Runtime.getRuntime().exec(cmd);
			} else {
				String cmd [] = new String [] { "/bin/sh", "-c", "ps aux | grep -i LockdownBrowser | grep -v grep" };
				p = Runtime.getRuntime().exec(cmd);
			}
			if(getMemoryLoadPercentage(p) > PROCESS_MEM_PERCENTAGE) {
				setLoginId(strUserName);
				setPassword(strPassword);
				setAccessCode(strAccessCode);
	    		return "<true/>";
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "<false/>";
	}
	
	private static double getMemoryLoadPercentage(Process p) {
		if(p == null)
			return 0;
		
		BufferedReader input = null;
		String line = null;
		double percentConsumption = 0;
		try {
			input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			if(OS_NAME.indexOf("win") >= 0) {
				while ((line = input.readLine()) != null) {
					if(line != null && line.trim().length() > 0) {
					    String details [] = line.split(":");
					    if(details[0].indexOf("Mem") != -1) {
					    	long ldbMemory = Integer.valueOf(details[1].replace(",", "").replace("K", "").trim());
					    	ldbMemory = ldbMemory / 1024;
					    	percentConsumption = (double) ldbMemory / totalPhysicalMemory;
					    	percentConsumption = percentConsumption * 100;
					    }
					}
				}
			} else {
				while ((line = input.readLine()) != null) {
					if(line != null && line.trim().length() > 0) {
						String details [] = line.split(" ");
						int count = 0;
						for(String d : details) {
							if(d.trim().length() > 0) {
								if(++count == 4) {
									percentConsumption = Double.valueOf(d);
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return percentConsumption;
	}

	public static String getLoginId() {
		return loginId;
	}

	public static void setLoginId(String loginId) {
		ReLoginUtility.loginId = loginId;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		ReLoginUtility.password = password;
	}

	public static String getAccessCode() {
		return accessCode;
	}

	public static void setAccessCode(String accessCode) {
		ReLoginUtility.accessCode = accessCode;
	}

	public static long getTotalPhysicalMemory() {
		return totalPhysicalMemory;
	}
}