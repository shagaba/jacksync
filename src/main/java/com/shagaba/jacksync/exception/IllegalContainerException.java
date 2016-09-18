package com.shagaba.jacksync.exception;

public class IllegalContainerException extends JacksonPatchException {

	private static final long serialVersionUID = 7653512556880571885L;

	public IllegalContainerException() {
        super();
    }

    public IllegalContainerException(String message) {
        super(message);
    }

    public IllegalContainerException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalContainerException(Throwable cause) {
        super(cause);
    }

}
