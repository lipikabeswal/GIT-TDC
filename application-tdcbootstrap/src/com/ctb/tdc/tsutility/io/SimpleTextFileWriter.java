package com.ctb.tdc.tsutility.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class SimpleTextFileWriter {

	private FileWriter fw;
	
	/**
	 * Constructor
	 * @param directory - Parent directory to create the file.
	 * @param filename - Name of the file to create.
	 * @throws IOException is thrown when the given directory and filename can not
	 * 		   be used to create the file.
	 */
	public SimpleTextFileWriter(String directory, String filename) throws IOException {
		this.fw = new FileWriter( new File(directory, filename) );
	}
	
	/**
	 * Writes the given message to the file created in the constructor.
	 * @param message - The message to write to the file created by the constructor.
	 * @throws IOException
	 */
	public void write(String message) throws IOException {
		this.fw.write(message);
	}

	/**
	 * Writes the given message to the file created in the constructor adding system
	 * specific new line.
	 * @param message - The message to write to the file created by the constructor.
	 * @throws IOException
	 */
	public void writeln(String message) throws IOException {
		String noHtmlMessage = message.replaceAll("<.*?>", "");
		write(noHtmlMessage);
		write( System.getProperty("line.separator") );
	}

	/**
	 * Writes the system specific new line.
	 * @throws IOException
	 */
	public void writeln() throws IOException {
		write( System.getProperty("line.separator") );
	}

	/**
	 * Closes the file.
	 * @throws IOException 
	 */
	public void close() throws IOException {
		this.fw.close();
	}

	/**
	 * Writes simple text file decoratative line.
	 * @throws IOException 
	 */
	public void writeDecorativeLine() throws IOException {
		for(int i=0; i < 80; i++) {
			this.fw.write("-");
		}
		writeln();
	}

	/**
	 * @throws IOException 
	 * 
	 */
	public void writeDateTimestamp() throws IOException {
		Calendar now = Calendar.getInstance();
		String message = String.format("%1$tc", now);
		writeln( message );
	}
}
