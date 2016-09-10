package com.shagaba.jacksonpatch.exception;

public class SyncException extends RuntimeException {

	private static final long serialVersionUID = 7653512556880571885L;

	public SyncException() {
        super();
    }

    public SyncException(String message) {
        super(message);
    }

    public SyncException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyncException(Throwable cause) {
        super(cause);
    }

}
