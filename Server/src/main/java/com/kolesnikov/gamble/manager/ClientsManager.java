package com.kolesnikov.gamble.manager;

import com.kolesnikov.gamble.manager.session.ClientSession;

import java.net.Socket;

public interface ClientsManager {
    ClientSession createSession(Socket socket);
}
