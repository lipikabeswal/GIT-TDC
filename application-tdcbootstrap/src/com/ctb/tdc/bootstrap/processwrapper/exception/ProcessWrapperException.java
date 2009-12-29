/**
 * 
 */
package com.ctb.tdc.bootstrap.processwrapper.exception;

/**
 * @author Giuseppe_Gennaro
 *
 */
public class ProcessWrapperException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new process wrapper exception with null as its detail message.
	 */
	public ProcessWrapperException() {
		super();
	}

	/**
	 * Constructs a new process wrapper exception with the specified detail message.
	 * @param  message  The detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public ProcessWrapperException(String message) {
		super(message);
	}

	/**
	 * Constructs a new process wrapper exception with the specified cause and a detail message.
	 * @param cause  The cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public ProcessWrapperException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new process wrapper exception with the specified detail message and cause.
	 * @param message  The detail message (which is saved for later retrieval by the Throwable.getMessage() method).
	 * @param cause  The cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public ProcessWrapperException(String message, Throwable cause) {
		super(message, cause);
	}

}
