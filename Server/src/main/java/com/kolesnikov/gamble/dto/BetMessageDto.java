package com.kolesnikov.gamble.dto;

import com.kolesnikov.gamble.game.Outcome;

public class BetMessageDto {
    private final long userId;
    private final Outcome outcome;
    private final long bet;
    private final long changeOfBalance;

    public BetMessageDto(long id, Outcome outcome, long bet, long changeOfBalance) {
        this.userId = id;
        this.outcome = outcome;
        this.bet = bet;
        this.changeOfBalance = changeOfBalance;
    }

    public long getUserId() {
        return userId;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public long getChangeOfBalance() {
        return changeOfBalance;
    }

    public long getBet() {
        return bet;
    }
}
