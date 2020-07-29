package com.kolesnikov.gamble.model;

import com.kolesnikov.gamble.model.type.GameEventType;
import com.kolesnikov.gamble.model.type.MessageType;

public class MessageDto {
    private long userId;
    private long betValue;
    private MessageType messageType;
    private GameEventType eventType;

    public MessageDto() {
    }

    public MessageDto(long userId, int betValue, MessageType messageType, GameEventType eventType) {
        this.userId = userId;
        this.betValue = betValue;
        this.messageType = messageType;
        this.eventType = eventType;
    }

    public long getBetValue() {
        return betValue;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public GameEventType getEventType() {
        return eventType;
    }

    public long getUserId() {
        return userId;
    }
}
