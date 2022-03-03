package org.salex.hmip.client;

/**
 * Exception for error in the Homematip IP Cloud client library.
 */
public class HmIPException extends RuntimeException {
    public HmIPException(String message) {
        super(message);
    }

    public HmIPException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
