package com.kolesnikov.gamble.service;

import com.kolesnikov.gamble.model.UserDto;
import com.kolesnikov.gamble.model.UserEntity;
import com.kolesnikov.gamble.repository.UserDbManager;

import java.util.Optional;

public class SimpleUserService {
    private final UserDbManager userHistory;

    public SimpleUserService(UserDbManager userHistory) {
        this.userHistory = userHistory;
    }


    public Optional<UserEntity> put(UserDto userDto) {
        return userHistory.put(new UserEntity(null, userDto.getName(), 100)); // todo сделать что-то с балансом
    }

    public Optional<UserEntity> getById(long id) {
        return userHistory.get(id);
    }
}
