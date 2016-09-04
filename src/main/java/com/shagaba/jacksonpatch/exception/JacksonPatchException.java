package com.shagaba.jacksonpatch.exception;

public class JacksonPatchException extends RuntimeException {

	private static final long serialVersionUID = 7653512556880571885L;

	public JacksonPatchException() {
        super();
    }

    public JacksonPatchException(String message) {
        super(message);
    }

    public JacksonPatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public JacksonPatchException(Throwable cause) {
        super(cause);
    }

}
