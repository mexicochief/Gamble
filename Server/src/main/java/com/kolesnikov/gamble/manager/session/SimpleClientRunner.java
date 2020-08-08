package com.kolesnikov.gamble.manager.session;

import java.io.*;
import java.net.Socket;

public class SimpleClientRunner implements ClientRunner {
    private final Socket userSocket;
    private final Session session;

    public SimpleClientRunner(Socket userSocket, Session sessionExecutor) {
        this.userSocket = userSocket;
        this.session = sessionExecutor;
    }

    @Override
    public void run() {
        try (userSocket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(userSocket.getOutputStream()))) {
            session.start(reader, writer);
        } catch (IOException e) {
            System.out.println("cause: " + e.getCause() + ", message: " + e.getMessage());
        }
    }
}