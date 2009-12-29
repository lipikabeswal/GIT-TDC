package com.ctb.tdc.bootstrap.util;

import java.net.ServerSocket;

/**
 * Utility class for anything related to server sockets.
 * 
 * @author Giuseppe Gennaro
 */
public class ServerSocketUtils {

	public static boolean isPortInUse(int port) {
		boolean results;
		
		// Check if the port is already in use, throwing exception if it is.
		ServerSocket ss;
		try {
			ss = new ServerSocket(port, 1);
			results = false;
			ss.close();
		} catch( Exception e ) {
			results = true;
		}
		
		return results;
	}
}
