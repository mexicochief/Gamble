package com.kolesnikov.gamble.manager.session;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolesnikov.gamble.dto.BetMessageDto;
import com.kolesnikov.gamble.dto.GameResult;
import com.kolesnikov.gamble.exception.GambleReadException;
import com.kolesnikov.gamble.exception.NotEnoughBalanceException;
import com.kolesnikov.gamble.game.GambleGame;
import com.kolesnikov.gamble.game.GameResolver;
import com.kolesnikov.gamble.model.*;
import com.kolesnikov.gamble.service.BetHistoryService;
import com.kolesnikov.gamble.service.SimpleUserService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Optional;

public class SimpleSessionExecutor implements SessionExecutor {
    private final BetHistoryService betHistoryService;
    private final ObjectMapper objectMapper;
    private final GameResolver gameResolver;
    private final SimpleUserService simpleUserService;

    public SimpleSessionExecutor(BetHistoryService betHistoryService,
                                 SimpleUserService simpleUserService,
                                 ObjectMapper objectMapper,
                                 GameResolver gameResolver) {
        this.betHistoryService = betHistoryService;
        this.objectMapper = objectMapper;
        this.gameResolver = gameResolver;
        this.simpleUserService = simpleUserService;
    }

    @Override
    public void execute(BufferedReader reader, BufferedWriter writer) throws IOException {
        final Optional<UserDto> userOptional = read(reader, UserDto.class);
        final UserDto userDto = userOptional.orElseThrow(GambleReadException::new);
        final UserEntity user = simpleUserService.put(userDto);
        boolean hasGameContinuing = true;
        write(writer, new UserDto(user.getName(), user.getId()));

        while (hasGameContinuing) {
            final Optional<MessageDto> messageOptional = read(reader, MessageDto.class);
            final MessageDto messageDto = messageOptional.orElseThrow(GambleReadException::new);
            try {
                final int balance = simpleUserService.getById(user.getId()).getBalance();
                if (balance <= 0) {
                    hasGameContinuing = false;
                    throw new NotEnoughBalanceException("Balance is 0, game end");
                }
                if (balance < messageDto.getBetValue()) {
                    throw new NotEnoughBalanceException(balance);
                }
                final GambleGame gambleEvent = gameResolver.resolve(messageDto);
                final GameResult betResult = gambleEvent.play(messageDto);
                final BetMessageDto betMessageDto = new BetMessageDto(
                        user.getId(),
                        betResult.getOutcome(),
                        betResult.getBet(),
                        betResult.getChangeOfBalance());

                final BetEntity betEntity = betHistoryService.put(betMessageDto);

                final ResponseMessageDto responseMessage = new ResponseMessageDto(
                        user.getId(),
                        betEntity.getBet(),
                        messageDto.getMessageType(),
                        betEntity.getChangeOfBalance(),
                        simpleUserService.getById(user.getId()).getBalance(), // todo лучше сделать красивее
                        betResult.getOutcome().toString());
                write(writer, responseMessage);
            } catch (JsonMappingException | NotEnoughBalanceException e) {
                write(writer, new ResponseMessageDto(
                        user.getId(),
                        messageDto.getBetValue(),
                        messageDto.getMessageType(),
                        0,
                        simpleUserService.getById(user.getId()).getBalance(),
                        e.getMessage()));
            } catch (GambleReadException e) {
                e.getMessage();
                hasGameContinuing = false;
            }
        }
    }

    private void write(BufferedWriter writer, Object response) throws IOException {
        writer.write(objectMapper.writeValueAsString(response));
        writer.write(System.lineSeparator());
        writer.flush();
    }

    private <T> Optional<T> read(BufferedReader reader, Class<T> tClass) throws IOException {
        final String content = reader.readLine();
        return content != null ? Optional.of(tClass.cast(objectMapper.readValue(content, tClass))) : Optional.empty();
    }
}
