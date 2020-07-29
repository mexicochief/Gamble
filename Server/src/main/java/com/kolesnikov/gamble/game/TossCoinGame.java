package com.kolesnikov.gamble.game;

import com.kolesnikov.gamble.dto.GameResult;
import com.kolesnikov.gamble.model.type.GameEventType;
import com.kolesnikov.gamble.model.MessageDto;
import com.kolesnikov.gamble.model.type.MessageType;

import java.util.Random;

public class TossCoinGame implements GambleGame {
    private final MessageType messageType = MessageType.FLIP_COIN_GAME;
    private final double coefficient;

    public TossCoinGame(double coefficient) {
        this.coefficient = coefficient;
    }

    @Override
    public GameResult play(MessageDto messageDto) {
        final Outcome outcome = computeGameResult(messageDto.getEventType());
        final long changeOfBalance = computeChangeOfBalance(outcome, messageDto.getBetValue());
        return new GameResult(messageDto.getBetValue(), outcome, changeOfBalance);
    }

    @Override
    public boolean hasSameType(MessageType messageType) {
        return this.messageType == messageType;
    }

    private Outcome computeGameResult(GameEventType gameEventType) {
        if (tossCoin() == gameEventType) {
            return Outcome.WIN;
        }
        return Outcome.LOSE;
    }

    private GameEventType tossCoin() {
        final Random random = new Random();
        if (random.nextBoolean()) {
            return GameEventType.HEADS;
        } else {
            return GameEventType.TAILS;
        }
    }

    private long computeChangeOfBalance(Outcome outcome, long betValue) {// todo может тут нормально округлять?
        if (outcome == Outcome.WIN) {
            return (long) (betValue * coefficient);
        }
        return -betValue;
    }
}
