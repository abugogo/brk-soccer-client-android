package com.soccer.lib;

public class SoccerException extends Exception {
	private static final long serialVersionUID = -7968989291293437270L;

    public SoccerException() {
        super();
    }

    public SoccerException(String message) {
        super(message);
    }

    public SoccerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SoccerException(Throwable cause) {
        super(cause);
    }

    protected SoccerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause);
    }
}
