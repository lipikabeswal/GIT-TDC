package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public final class ReLoginUtility {
	
	private static Logger logger = Logger.getLogger(ReLoginUtility.class);
	private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
	private static final int MACHINE_MAX_MEM_THRESHOLD_PERCENTAGE = 30;
	private static final int ADOBE_AIR_MAX_MEM_THRESHOLD_VALUE = 700;
	
	private static ReLoginUtility reLoginUtility = null;
	private static String loginId = null;
	private static String password = null;
	private static String accessCode = null;
	private static long totalPhysicalMemory = 0L;
	private static int procMemThresholdValue = 0;
	
	private ReLoginUtility() { 
		BufferedReader input = null;
		String line = null;
		Process p = null;
		try {
			if(OS_NAME.indexOf("win") >= 0) {
				p = Runtime.getRuntime().exec(new String [] { "systeminfo" });
				input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					if(line != null && line.trim().length() > 0) {
					    String details [] = line.split(":");
					    if(details[0].indexOf("Total Physical Memory") != -1) {
					    	calculateTotalMemory(details[1].toUpperCase());
						    break;
					    }
					}
				}
			} else if(OS_NAME.indexOf("mac") >= 0) { 
				p = Runtime.getRuntime().exec(new String [] { "/bin/sh", "-c", "sysctl hw.physmem" });
				input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					if(line != null && line.trim().length() > 0) {
						String details [] = line.split(":");
						calculateTotalMemory(details[1].toUpperCase());
					}
				}
			} else { 
				p = Runtime.getRuntime().exec(new String [] { "/bin/sh", "-c", "cat /proc/meminfo | grep MemTotal" });
				input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					if(line != null && line.trim().length() > 0) {
						String details [] = line.split(":");
						calculateTotalMemory(details[1].toUpperCase());
					}
				}
			}
			procMemThresholdValue = (int) (totalPhysicalMemory * MACHINE_MAX_MEM_THRESHOLD_PERCENTAGE) / 100;
			if(procMemThresholdValue > ADOBE_AIR_MAX_MEM_THRESHOLD_VALUE) {
				procMemThresholdValue = ADOBE_AIR_MAX_MEM_THRESHOLD_VALUE;
			}
			logger.info("ReLoginUtility : procMemThresholdValue value :" + procMemThresholdValue);
			logger.info("ReLoginUtility initiated successfully");
		} catch (Exception e) {
			logger.error("ReLoginUtility : error occured :", e);
		} finally {
			try {
				if(input != null)
					input.close();
			} catch (IOException e) {
				logger.error("ReLoginUtility : error occured :", e);
			}
		}
	}
	
	private static void calculateTotalMemory(String details) {
		if(details.indexOf("GB") != -1) {
		    totalPhysicalMemory = Long.valueOf(details.replaceAll(",", "").replaceAll("GB", "").trim()) * 1024;
	    } else if(details.indexOf("MB") != -1) {
	    	totalPhysicalMemory = Long.valueOf(details.replaceAll(",", "").replaceAll("MB", "").trim());
	    } else if(details.indexOf("KB") != -1) {
	    	totalPhysicalMemory = Long.valueOf(details.replaceAll(",", "").replaceAll("KB", "").trim()) / 1024;
	    } else {
	    	totalPhysicalMemory = Long.valueOf(details.replaceAll(",", "").trim()) / 1024 / 1024;
	    }
		logger.info("ReLoginUtility : totalPhysicalMemory value :" + totalPhysicalMemory);
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
			if(getMemoryLoadPercentage(p) > procMemThresholdValue) {
				logger.info("ReLoginUtility : Memory exceeded auto login will proceed");
				setLoginId(strUserName);
				setPassword(strPassword);
				setAccessCode(strAccessCode);
	    		return "<true/>";
	    	}
		} catch (Exception e) {
			logger.error("ReLoginUtility : error occured :" + e);
		}
		return "<false/>";
	}
	
	private static double getMemoryLoadPercentage(Process p) {
		if(p == null)
			return 0;
		
		BufferedReader input = null;
		String line = null;
		double memoryConsumption = 0;
		try {
			input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			if(OS_NAME.indexOf("win") >= 0) {
				while ((line = input.readLine()) != null) {
					if(line != null && line.trim().length() > 0) {
					    String details [] = line.split(":");
					    if(details[0].indexOf("Mem") != -1) {
					    	long ldbMemory = Integer.valueOf(details[1].replace(",", "").replace("K", "").trim());
					    	memoryConsumption = ldbMemory / 1024;
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
									memoryConsumption = (totalPhysicalMemory / 100 ) * Double.valueOf(d) ;
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error("ReLoginUtility : error occured :", e);
		} finally {
			try {
				if(input != null)
					input.close();
			} catch (IOException e) {
				logger.error("ReLoginUtility : error occured :" + e);
				e.printStackTrace();
			}
		}
		logger.info("ReLoginUtility : memoryConsumption value :" + memoryConsumption);
		return memoryConsumption;
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