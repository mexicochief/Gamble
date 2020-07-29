package com.kolesnikov.gamble.server;


import com.kolesnikov.gamble.manager.ClientsManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class GambleServer {
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool;
    private final ClientsManager clientsManager;

    public GambleServer(ServerSocket serverSocket,
                        ExecutorService threadPool,
                        ClientsManager clientsManager) {
        this.serverSocket = serverSocket;
        this.threadPool = threadPool;
        this.clientsManager = clientsManager;
    }

    public void run() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                threadPool.submit(clientsManager.createSession(socket));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
