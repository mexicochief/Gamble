package com.kolesnikov.gamble.manager;

import com.kolesnikov.gamble.manager.session.ClientRunner;

import java.net.Socket;

public interface ClientsManager {
    ClientRunner createSession(Socket socket);
}
