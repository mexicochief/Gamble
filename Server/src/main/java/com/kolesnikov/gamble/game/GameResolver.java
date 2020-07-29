package com.kolesnikov.gamble.game;

import com.kolesnikov.gamble.game.GambleGame;
import com.kolesnikov.gamble.model.MessageDto;

public interface GameResolver {

    GambleGame resolve(MessageDto messageDto);

    void addGambleEvents(GambleGame gambleEvent);

}
