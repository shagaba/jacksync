package com.shagaba.jacksonpatch.exception;

public class InvalidPatchValueTestException extends JacksonPatchException {

	private static final long serialVersionUID = 7653512556880571885L;

	public InvalidPatchValueTestException() {
        super();
    }

    public InvalidPatchValueTestException(String message) {
        super(message);
    }

    public InvalidPatchValueTestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPatchValueTestException(Throwable cause) {
        super(cause);
    }

}
