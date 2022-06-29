package edu.umcs.bookystore.exceptions;

public class OutOfStockException extends Exception {

	private long bookId;

	public OutOfStockException() {
	}

	public OutOfStockException(String message, long bookId) {
		super(message);
		this.bookId = bookId;
	}

	public OutOfStockException(String message, long bookId, Throwable cause) {
		super(message, cause);
		this.bookId = bookId;
	}

	public long getBookId() {
		return bookId;
	}

}
