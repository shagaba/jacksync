package com.shagaba.jacksync.exception;

public class JacksyncDiffException extends RuntimeException {

	private static final long serialVersionUID = 7653512556880571885L;

	public JacksyncDiffException() {
        super();
    }

    public JacksyncDiffException(String message) {
        super(message);
    }

    public JacksyncDiffException(String message, Throwable cause) {
        super(message, cause);
    }

    public JacksyncDiffException(Throwable cause) {
        super(cause);
    }

}
