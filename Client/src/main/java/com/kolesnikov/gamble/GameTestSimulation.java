package com.kolesnikov.gamble;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolesnikov.gamble.model.MessageDto;
import com.kolesnikov.gamble.model.ResponseMessageDto;
import com.kolesnikov.gamble.model.UserDto;
import com.kolesnikov.gamble.model.type.GameEventType;
import com.kolesnikov.gamble.model.type.MessageType;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

public class GameTestSimulation implements Runnable {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private UserSimulationInformation userSimulationInformation = new UserSimulationInformation();
    private final long callCount;
    private final long callDelay;
    private final MessageDto messageDto = new MessageDto(
            1,
            20,
            MessageType.FLIP_COIN_GAME,
            GameEventType.HEADS);
    private final UserDto userDto = new UserDto("name", 0);


    public GameTestSimulation(long callCount, long callDelay) {
        this.callCount = callCount;
        this.callDelay = callDelay;
    }


    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", 4005);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            write(writer, userDto);
            final Optional<UserDto> optionalUser = read(reader, UserDto.class);
            userSimulationInformation.setUserId(optionalUser.orElseThrow(RuntimeException::new).getId());

            for (int j = 0; j < callCount; j++) {
                userSimulationInformation.startCountdown();

                write(writer, messageDto);

                userSimulationInformation.appendCallCount();
                final Optional<ResponseMessageDto> messageOptional = read(reader, ResponseMessageDto.class);
                if (messageOptional.isPresent()) {
                    userSimulationInformation.appendAnswerCount();
                }
                userSimulationInformation.endCountDown();

                Thread.sleep(callDelay);
            }
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void write(Writer writer, Object message) throws IOException {
        writer.write(objectMapper.writeValueAsString(message));
        writer.write(System.lineSeparator());
        writer.flush();
    }

    private <T> Optional<T> read(BufferedReader reader, Class<T> tClass) throws IOException {
        String content;
        if ((content = reader.readLine()) != null) {
            return Optional.of(tClass.cast(objectMapper.readValue(content, tClass)));
        }
        return Optional.empty();
    }

    public UserSimulationInformation getUserSimulationInformation() {
        return userSimulationInformation;
    }
}