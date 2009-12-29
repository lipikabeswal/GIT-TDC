package com.ctb.tdc.bootstrap.processwrapper.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Thread that will monitor the STDOUT output from a system process.
 * 
 * @author Giuseppe_Gennaro
 *
 */
public class ProcessStdOutMonitor implements Runnable {

	private Process process;
	private PrintStream printStream;
	
	/**
	 * Constructor.
	 * @param process  The process whose stdout will be monitored.
	 * @param out  The output stream to pipe out the process' stdout.
	 */
	public ProcessStdOutMonitor(Process process, PrintStream out) {
		this.process = process;
		this.printStream = out;
	}

	/**
	 * Opens a printstream as specified in the constructor to write the output
	 * from the wrapped process' stdout.  The thread will remain alive for as
	 * long as the wrapped process keeps its stdout open.
	 */
	public void run() {
	
		 BufferedReader out = new BufferedReader(new InputStreamReader( this.process.getInputStream() ));
		 String line;

		 try {
			 this.printStream.println(this.getClass().getName() + ": started.");
			 line = out.readLine();
			 while( line != null ) {
				 this.printStream.println("stdout: " + line);
				 line = out.readLine();
			 }
			 out.close();
			 this.printStream.println(this.getClass().getName() + ": completed.");
		 } catch( IOException e ) {
			 e.printStackTrace();
		 }
 
	}
	
	
}
