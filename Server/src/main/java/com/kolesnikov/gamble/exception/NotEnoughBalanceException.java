package com.kolesnikov.gamble.exception;

public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException(int balance) {
        super("Not enough balance, balance is " + balance);
    }
}
