package com.shagaba.jacksync.exception;

public class ChecksumMismatchException extends SyncException {

	private static final long serialVersionUID = 5794042794197771149L;

	public ChecksumMismatchException() {
        super();
    }

    public ChecksumMismatchException(String message) {
        super(message);
    }

    public ChecksumMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChecksumMismatchException(Throwable cause) {
        super(cause);
    }

}
