package org.salex.hmip.client;

public class HmIPException extends RuntimeException {
    public HmIPException(String message) {
        super(message);
    }

    public HmIPException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
