package com.kolesnikov.gamble.exception.repository;

public class GambleDbException extends RuntimeException {
    public GambleDbException(String message) {
        super(message);
    }

    public GambleDbException(String message, Throwable cause) {
        super(message, cause);
    }
}
