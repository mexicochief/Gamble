package com.kolesnikov.gamble.dto;

import com.kolesnikov.gamble.game.Outcome;

public class GameResult {
    private final long bet;
    private final Outcome outcome;
    private final long changeOfBalance;

    public GameResult(long bet, Outcome outcome, long changeOfBalance) {
        this.bet = bet;
        this.outcome = outcome;
        this.changeOfBalance = changeOfBalance;
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
