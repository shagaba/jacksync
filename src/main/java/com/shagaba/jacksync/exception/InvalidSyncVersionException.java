package com.shagaba.jacksync.exception;

public class InvalidSyncVersionException extends SyncException {

	private static final long serialVersionUID = 7653512556880571885L;

	public InvalidSyncVersionException() {
        super();
    }

    public InvalidSyncVersionException(String message) {
        super(message);
    }

    public InvalidSyncVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSyncVersionException(Throwable cause) {
        super(cause);
    }

}
