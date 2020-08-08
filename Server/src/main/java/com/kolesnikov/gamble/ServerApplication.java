package com.kolesnikov.gamble;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolesnikov.gamble.game.GameResolver;
import com.kolesnikov.gamble.game.SimpleGameResolver;
import com.kolesnikov.gamble.game.TossCoinGame;
import com.kolesnikov.gamble.manager.ClientsManager;
import com.kolesnikov.gamble.manager.SimpleClientManager;
import com.kolesnikov.gamble.manager.session.Session;
import com.kolesnikov.gamble.manager.session.SimpleSession;
import com.kolesnikov.gamble.repository.BetHistoryDbManager;
import com.kolesnikov.gamble.repository.UserDbManager;
import com.kolesnikov.gamble.server.GambleServer;
import com.kolesnikov.gamble.service.BetHistoryService;
import com.kolesnikov.gamble.service.SimpleUserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApplication {
    public static void main(String[] args) {

        Properties properties = new Properties();
        ServerSocket serverSocket = null;
        try (InputStream inputStream =
                     ServerApplication.class.getClassLoader().getResourceAsStream("server.properties")) {
            properties.load(inputStream);// todo может что-то сделать
            final String port = properties.getProperty("port");
            serverSocket = new ServerSocket(Integer.parseInt(port));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        ExecutorService threadPool = Executors.newCachedThreadPool();

        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(properties.getProperty("jdbcUrl"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(properties.getProperty("maximumPoolSize")));

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        UserDbManager userHistory = new UserDbManager(dataSource);
        BetHistoryDbManager betHistory = new BetHistoryDbManager(dataSource);
        SimpleUserService simpleUserService = new SimpleUserService(userHistory);
        BetHistoryService betHistoryService = new BetHistoryService(betHistory);

        GameResolver gameResolver = new SimpleGameResolver();
        gameResolver.addGambleEvents(new TossCoinGame(0.9));

        Session sessionExecutor = new SimpleSession(
                betHistoryService,
                simpleUserService,
                new ObjectMapper(),
                gameResolver);

        ClientsManager clientsManager = new SimpleClientManager(sessionExecutor);
        GambleServer gambleServer = new GambleServer(serverSocket, threadPool, clientsManager);
        gambleServer.run();
    }
}
