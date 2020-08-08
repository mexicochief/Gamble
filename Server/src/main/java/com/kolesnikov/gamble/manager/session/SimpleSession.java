package com.kolesnikov.gamble.manager.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolesnikov.gamble.dto.BetDto;
import com.kolesnikov.gamble.dto.GameResult;
import com.kolesnikov.gamble.exception.GambleReadException;
import com.kolesnikov.gamble.exception.NotEnoughBalanceException;
import com.kolesnikov.gamble.exception.service.ServiceException;
import com.kolesnikov.gamble.game.GambleGame;
import com.kolesnikov.gamble.game.GameResolver;
import com.kolesnikov.gamble.model.*;
import com.kolesnikov.gamble.service.BetHistoryService;
import com.kolesnikov.gamble.service.SimpleUserService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Optional;

public class SimpleSession implements Session {
    private final BetHistoryService betHistoryService;
    private final ObjectMapper objectMapper;
    private final GameResolver gameResolver;
    private final SimpleUserService simpleUserService;

    public SimpleSession(BetHistoryService betHistoryService,
                         SimpleUserService simpleUserService,
                         ObjectMapper objectMapper,
                         GameResolver gameResolver) {
        this.betHistoryService = betHistoryService;
        this.objectMapper = objectMapper;
        this.gameResolver = gameResolver;
        this.simpleUserService = simpleUserService;
    }

    @Override
    public void start(BufferedReader reader, BufferedWriter writer) { // todo сделать красивее
        try {
            final Optional<UserDto> userOptional = read(reader, UserDto.class);
            final UserDto userDto = userOptional.orElseThrow(GambleReadException::new);
            final UserEntity user = simpleUserService
                    .put(userDto)
                    .orElseThrow(() -> new ServiceException("User"));
            write(writer, new UserDto(user.getName(), user.getId()));

            while (Thread.currentThread().isAlive()) {
                final Optional<MessageDto> messageOptional = read(reader, MessageDto.class);
                final MessageDto messageDto = messageOptional.orElseThrow(GambleReadException::new);

                int balance = simpleUserService
                        .getById(user.getId())
                        .orElseThrow(() -> new ServiceException("User"))
                        .getBalance();
                if (balance <= 0) {
                    throw new NotEnoughBalanceException("Balance is 0, game end");
                }
                if (balance < messageDto.getBetValue()) {
                    write(writer, new ResponseMessageDto(
                            user.getId(),
                            messageDto.getBetValue(),
                            messageDto.getMessageType(),
                            0,
                            balance,
                            "not enough balance"));
                } else {
                    final GambleGame gambleEvent = gameResolver.resolve(messageDto);
                    final GameResult betResult = gambleEvent.play(messageDto);

                    final BetDto betDto = new BetDto(
                            user.getId(),
                            betResult.getOutcome(),
                            betResult.getBet(),
                            betResult.getChangeOfBalance());

                    final BetEntity betEntity = betHistoryService
                            .put(betDto)
                            .orElseThrow(() -> new ServiceException("Bet"));

                    balance = simpleUserService
                            .getById(user.getId())
                            .orElseThrow(() -> new ServiceException("User"))
                            .getBalance();

                    final ResponseMessageDto responseMessageDto = new ResponseMessageDto(
                            user.getId(),
                            betEntity.getBet(),
                            messageDto.getMessageType(),
                            betEntity.getChangeOfBalance(),
                            balance,
                            betResult.getOutcome().toString());
                    write(writer, responseMessageDto);
                }
            }
        } catch (NotEnoughBalanceException | IOException | ServiceException e) {
            System.out.println("cause: " + e.getCause() + ", message: " + e.getMessage());
        }
    }

    private void write(BufferedWriter writer, Object response) throws IOException {
        writer.write(objectMapper.writeValueAsString(response));
        writer.newLine();
        writer.flush();
    }

    private <T> Optional<T> read(BufferedReader reader, Class<T> tClass) throws IOException {
        final String content = reader.readLine();
        return content != null ? Optional.of(tClass.cast(objectMapper.readValue(content, tClass))) : Optional.empty();
    }
}
