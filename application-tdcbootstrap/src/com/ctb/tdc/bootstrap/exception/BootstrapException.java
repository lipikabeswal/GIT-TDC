package com.ctb.tdc.bootstrap.exception;


/**
 * Typed exception class for the bootstrap application. All methods simply
 * call the super() method.  No methods were harmed in the creation of this class.
 * 
 * @author Giuseppe_Gennaro
 */
public class BootstrapException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new bootstrap exception with null as its detail message.
	 */
	public BootstrapException() {
		super();
	}

	/**
	 * Constructs a new bootstrap exception with the specified detail message.
	 * @param  message  The detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public BootstrapException(String message) {
		super(message);
	}

	/**
	 * Constructs a new bootstrap exception with the specified cause and a detail message.
	 * @param cause  The cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public BootstrapException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new bootstrap exception with the specified detail message and cause.
	 * @param message  The detail message (which is saved for later retrieval by the Throwable.getMessage() method).
	 * @param cause  The cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public BootstrapException(String message, Throwable cause) {
		super(message, cause);
	}

}
