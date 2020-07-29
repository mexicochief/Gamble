package com.kolesnikov.gamble.manager;

import com.kolesnikov.gamble.manager.session.ClientSession;
import com.kolesnikov.gamble.manager.session.SessionExecutor;
import com.kolesnikov.gamble.manager.session.SimpleClientSession;

import java.net.Socket;

public class SimpleClientManager implements ClientsManager {
    private final SessionExecutor sessionExecutor;


    public SimpleClientManager(SessionExecutor sessionExecutor) {
        this.sessionExecutor = sessionExecutor;
    }

    public ClientSession createSession(Socket socket) {

        return new SimpleClientSession(socket, sessionExecutor);
    }
}
