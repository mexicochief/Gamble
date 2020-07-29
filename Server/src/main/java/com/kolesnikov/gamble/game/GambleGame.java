package com.kolesnikov.gamble.game;

import com.kolesnikov.gamble.dto.GameResult;
import com.kolesnikov.gamble.model.MessageDto;
import com.kolesnikov.gamble.model.type.MessageType;

public interface GambleGame {

    GameResult play(MessageDto messageDto);

    boolean hasSameType(MessageType messageType);
}
