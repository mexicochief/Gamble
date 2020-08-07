package com.kolesnikov.gamble.exception;

import java.io.IOException;
import java.util.function.Supplier;

public class GambleReadException extends IOException {
    public GambleReadException() {
        super("Message from client is null");
    }
}
