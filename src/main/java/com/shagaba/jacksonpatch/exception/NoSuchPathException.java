package com.shagaba.jacksonpatch.exception;

public class NoSuchPathException extends JacksonPatchException {

	private static final long serialVersionUID = 7653512556880571885L;

	public NoSuchPathException() {
        super();
    }

    public NoSuchPathException(String message) {
        super(message);
    }

    public NoSuchPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchPathException(Throwable cause) {
        super(cause);
    }

}
