package com.kolesnikov.gamble.service;

import com.kolesnikov.gamble.dto.BetDto;
import com.kolesnikov.gamble.model.BetEntity;
import com.kolesnikov.gamble.repository.BetHistoryDbManager;

public class BetHistoryService {
    private final BetHistoryDbManager betHistory;

    public BetHistoryService(BetHistoryDbManager betHistory) {
        this.betHistory = betHistory;
    }


    public BetEntity put(BetDto betMessageDto) {
        return betHistory.put(new BetEntity(
                null,
                betMessageDto.getBet(),
                betMessageDto.getChangeOfBalance(),
                betMessageDto.getUserId()));
    }

    public BetEntity getById(long id) {
        return betHistory.get(id);
    }
}
