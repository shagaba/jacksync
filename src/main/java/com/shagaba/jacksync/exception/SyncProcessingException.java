package com.shagaba.jacksync.exception;

public class SyncProcessingException extends SyncException {

	private static final long serialVersionUID = 7653512556880571885L;

	public SyncProcessingException() {
        super();
    }

    public SyncProcessingException(String message) {
        super(message);
    }

    public SyncProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyncProcessingException(Throwable cause) {
        super(cause);
    }

}
