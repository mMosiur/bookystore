package edu.umcs.bookystore.exceptions;

public class UsernameAlreadyExistsException extends Exception {

	public UsernameAlreadyExistsException() {
	}

	public UsernameAlreadyExistsException(String message) {
		super(message);
	}

	public UsernameAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public UsernameAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
