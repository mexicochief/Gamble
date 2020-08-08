package com.kolesnikov.gamble.exception.service;

public class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message + " not found");
    }
}
