package com.kolesnikov.gamble.model;

import com.kolesnikov.gamble.model.type.MessageType;

public class ResponseMessageDto {
    private long userId;
    private long bet;
    private MessageType messageType;
    private long changeOfBalance;
    private String message;

    public ResponseMessageDto() {
    }

    public ResponseMessageDto(long userId, long bet, MessageType messageType, long changeOfBalance, String message) {
        this.userId = userId;
        this.bet = bet;
        this.messageType = messageType;
        this.changeOfBalance = changeOfBalance;
        this.message = message;
    }

    public long getUserId() {
        return userId;
    }

    public long getBet() {
        return bet;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public long getChangeOfBalance() {
        return changeOfBalance;
    }

    public String getMessage() {
        return message;
    }
}
