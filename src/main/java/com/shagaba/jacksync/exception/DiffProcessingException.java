package com.shagaba.jacksync.exception;

public class DiffProcessingException extends SyncException {

	private static final long serialVersionUID = 7653512556880571885L;

	public DiffProcessingException() {
        super();
    }

    public DiffProcessingException(String message) {
        super(message);
    }

    public DiffProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DiffProcessingException(Throwable cause) {
        super(cause);
    }

}
