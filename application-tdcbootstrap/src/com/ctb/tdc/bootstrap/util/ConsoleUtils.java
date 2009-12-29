package com.ctb.tdc.bootstrap.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class to provide formatted console output to either STDOUT or STDERR.
 * 
 * @author Giuseppe_Gennaro
 *
 */
public class ConsoleUtils {

	/**
	 * Date and time format used by this utility class currenlty in the form of "yyyy-MM-dd HH:mm:ss". 
	 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"; 
	
	
	/**
	 * Writes to System.out the specified message.
	 * @param msg  The message to be written.
	 */
	public static void messageOut(String msg) {
		
		System.out.println(getCurrentTime() + " " + msg);
	}

	/**
	 * Writes to System.err the specified message.
	 * @param msg  The message to be written.
	 */
	public static void messageErr(String msg) {

		System.err.println(getCurrentTime() + " " + msg);
	}
	
	/**
	 * Writes to System.err the specified message along with exeucting an exception printStackTrace.
	 * @param msg  The message to be written.
	 * @param e  The exception have it its stack trace written.
	 */
	public static void messageErr(String msg, Exception e) {
		messageErr(msg);
		e.printStackTrace(System.err);
	}

	
	/**
	 * Obtain the current time in a String format specified by the DATE_TIME_FORMAT variable.
	 */
	private static String getCurrentTime() {
		SimpleDateFormat sdf;

		sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance();
		sdf.applyPattern(DATE_TIME_FORMAT);

	    return sdf.format(new Date());
	}
}
