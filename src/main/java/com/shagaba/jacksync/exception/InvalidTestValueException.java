package com.shagaba.jacksync.exception;

public class InvalidTestValueException extends JacksonPatchException {

	private static final long serialVersionUID = 7653512556880571885L;

	public InvalidTestValueException() {
        super();
    }

    public InvalidTestValueException(String message) {
        super(message);
    }

    public InvalidTestValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTestValueException(Throwable cause) {
        super(cause);
    }

}
