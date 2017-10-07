package com.shagaba.jacksync.exception;

public class PatchProcessingException extends SyncException {

	private static final long serialVersionUID = 7653512556880571885L;

	public PatchProcessingException() {
        super();
    }

    public PatchProcessingException(String message) {
        super(message);
    }

    public PatchProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PatchProcessingException(Throwable cause) {
        super(cause);
    }

}
