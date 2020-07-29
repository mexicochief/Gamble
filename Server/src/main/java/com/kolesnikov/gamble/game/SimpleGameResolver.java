package com.kolesnikov.gamble.game;

import com.kolesnikov.gamble.model.MessageDto;

import java.util.ArrayList;
import java.util.Optional;

public class SimpleGameResolver implements GameResolver {
    private ArrayList<GambleGame> gambleEvents = new ArrayList<>();

    @Override
    public GambleGame resolve(MessageDto messageDto) {
        final Optional<GambleGame> first = gambleEvents.stream()
                .filter(gambleEvent -> gambleEvent.hasSameType(messageDto.getMessageType()))
                .findFirst();

        return first.orElseThrow(() -> new IllegalStateException("no such event")); // todo может красивее сделать
    }

    public void addGambleEvents(GambleGame gambleEvent) {
        gambleEvents.add(gambleEvent);
    }
}
