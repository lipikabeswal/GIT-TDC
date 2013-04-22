package com.ctb.tdc.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * Every TDC application will have single instance of class
 * <code>ReLoginUtility</code> that allows the auto relogin 
 * activity when memory of TDC client is greater than {@link #ADOBE_AIR_MAX_MEM_THRESHOLD_VALUE}
 * The current runtime can be obtained from the <code>getReLoginUtility</code> method. 
 * 
 * @author Soumen Choudhury
 * @version 1.0
 */
public final class ReLoginUtility {
	
	private static Logger logger = Logger.getLogger(ReLoginUtility.class);
	/**
	 *	Name of the operating System.
	 */
	private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
	/**
	 * Maximum threshold percentage of physical memory when TDC client
	 * start auto relogin.
	 */
	private static final int MACHINE_MAX_MEM_THRESHOLD_PERCENTAGE = 30;
	/**
	 * Maximum threshold value (in MB) of physical memory when TDC client
	 * start auto relogin.
	 */
	private static final int ADOBE_AIR_MAX_MEM_THRESHOLD_VALUE = 700;
	
	private static ReLoginUtility reLoginUtility = null;
	private static String loginId = null;
	private static String password = null;
	private static String accessCode = null;
	private static long totalPhysicalMemory = 0L;
	private static int procMemThresholdValue = 0;
	
	/**
	 * Don't let anyone to instantiate this class from outside.
	 * When the class is instantiated at that time it will see the
	 * total physical memory of the system and check whether it gerater
	 * than the TDC client threshold <code>700 MB</code> or not. Memory
	 * percentage or <code>700 MB</code> whichever is lesser will be set as TDC client 
	 * threshold value for current session.
	 * 
	 * @see #MACHINE_MAX_MEM_THRESHOLD_PERCENTAGE
	 * @see #ADOBE_AIR_MAX_MEM_THRESHOLD_VALUE
	 * 
	 */
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
	
	/**
	 * This method format the details retrieved from system command
	 * and calculate the total physical memory value.
	 * 
	 * @param output of the system command
	 */
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
	/**
	 * This method create the instance of class <code>ReLoginUtility</code>,
	 * if instance is not already created, otherwise just return the instance.
	 * 
	 * @return  the <code>ReLoginUtility</code> object associated with the current
     *          application.
	 */
	public static ReLoginUtility getReLoginUtility() {
		if(reLoginUtility == null) {
			synchronized (ReLoginUtility.class) {
				reLoginUtility = new ReLoginUtility();
			}
		}
		return reLoginUtility;
	}
	/**
	 * This method get the current memory consumption from system command. If memory 
	 * exceed then set the login id, password and access code for future login.
	 *  
	 * @return  <code><true/></code> if memory exceed maximum limit
	 * or return <code><false/></code>
	 * 
	 * @see #procMemThresholdValue
	 * 
	 */
	public static String isMaxMemory(String strUserName, String strPassword, String strAccessCode) {
		try {
			Process p = null;
			if (OS_NAME.indexOf("win") >= 0) {
				String cmd = "tasklist /fo list /fi \"IMAGENAME eq LockdownBrowser.exe\"";
				p = Runtime.getRuntime().exec(cmd);
			} else {
				String cmd [] = new String [] { "/bin/sh", "-c", "ps aux | grep -i LockdownBrowser | grep -v grep" };
				p = Runtime.getRuntime().exec(cmd);
			}
			if(processMemoryLoadPercentageValue(p) > procMemThresholdValue) {
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
	/**
	 * This method process the memory details retrieved from the system
	 * command.
	 *   
	 * @param system command process to get the memory details
	 * @return current memory consumption of <code>LockdownBrowser</code> application
	 * 
	 */
	private static double processMemoryLoadPercentageValue(Process p) {
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

	/**
	 * This method return the current login id
	 * 
	 * @return login id
	 */
	public static String getLoginId() {
		return loginId;
	}
	/**
	 * This method set the current login id
	 * 
	 * @param login id
	 */
	public static void setLoginId(String loginId) {
		ReLoginUtility.loginId = loginId;
	}
	/**
	 * This method return the current password
	 * 
	 * @return password
	 */
	public static String getPassword() {
		return password;
	}
	/**
	 * This method set the current password
	 * 
	 * @param password
	 */
	public static void setPassword(String password) {
		ReLoginUtility.password = password;
	}
	/**
	 * This method return the current access code
	 * 
	 * @return access code
	 */
	public static String getAccessCode() {
		return accessCode;
	}
	/**
	 * This method set the current access code
	 * 
	 * @param access code
	 */
	public static void setAccessCode(String accessCode) {
		ReLoginUtility.accessCode = accessCode;
	}
	/**
	 * This method return total physical memory of the system
	 * 
	 * @return total physical memory
	 */
	public static long getTotalPhysicalMemory() {
		return totalPhysicalMemory;
	}
}