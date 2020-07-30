package com.kolesnikov.gamble.manager.session;

import java.io.*;
import java.net.Socket;

public class SimpleClientSession implements ClientSession {
    private final Socket userSocket;
    private final SessionExecutor sessionExecutor;

    public SimpleClientSession(Socket userSocket, SessionExecutor sessionExecutor) {
        this.userSocket = userSocket;
        this.sessionExecutor = sessionExecutor;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(userSocket.getOutputStream()))) {
            sessionExecutor.execute(reader, writer);
        } catch (
                IOException e) {
            System.out.println(e.getMessage());
        }
    }
}