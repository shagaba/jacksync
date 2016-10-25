package com.shagaba.jacksync.exception;

public class ChecksumException extends RuntimeException {

	private static final long serialVersionUID = 1867928513812977072L;

	public ChecksumException() {
        super();
    }

    public ChecksumException(String message) {
        super(message);
    }

    public ChecksumException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChecksumException(Throwable cause) {
        super(cause);
    }

}
