package com.kolesnikov.gamble.manager;

import com.kolesnikov.gamble.manager.session.ClientRunner;
import com.kolesnikov.gamble.manager.session.Session;
import com.kolesnikov.gamble.manager.session.SimpleClientRunner;

import java.net.Socket;

public class SimpleClientManager implements ClientsManager {
    private final Session sessionExecutor;


    public SimpleClientManager(Session sessionExecutor) {
        this.sessionExecutor = sessionExecutor;
    }

    public ClientRunner createSession(Socket socket) {

        return new SimpleClientRunner(socket, sessionExecutor);
    }
}
