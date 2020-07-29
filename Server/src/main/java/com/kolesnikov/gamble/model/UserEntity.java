package com.kolesnikov.gamble.model;

public class UserEntity {
    private final Long id;
    private final String name;
    private final int balance;

    public UserEntity(Long id, String name, int balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }
}
