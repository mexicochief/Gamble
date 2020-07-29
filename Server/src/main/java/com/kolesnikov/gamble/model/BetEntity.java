package com.kolesnikov.gamble.model;

public class BetEntity {
    private final Long id;
    private final long bet;
    private final long changeOfBalance;
    private final long userId;


    public BetEntity(Long id, long bet, long changeOfBalance, long userId) {
        this.id = id;
        this.bet = bet;
        this.changeOfBalance = changeOfBalance;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public long getBet() {
        return bet;
    }

    public long getChangeOfBalance() {
        return changeOfBalance;
    }

    public long getUserId() {
        return userId;
    }
}
