package com.shagaba.jacksync.exception;

public class MergeProcessingException extends SyncException {

	private static final long serialVersionUID = 7653512556880571885L;

	public MergeProcessingException() {
        super();
    }

    public MergeProcessingException(String message) {
        super(message);
    }

    public MergeProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MergeProcessingException(Throwable cause) {
        super(cause);
    }

}
