package com.kolesnikov.gamble;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class StressTest {

    public void test(long userCount, long callCount, long callDelay) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ArrayList<Future<GameTestSimulation>> futures = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            final GameTestSimulation gameTestSimulation = new GameTestSimulation(callCount, callDelay);
            futures.add(threadPool.submit(gameTestSimulation, gameTestSimulation));
        }

        final ArrayList<UserSimulationInformation> collect = futures
                .stream()
                .map(gameTestSimulationFuture -> {
                    try {
                        return gameTestSimulationFuture.get().getUserSimulationInformation();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));

        collect.forEach(userSimulationInformation -> System.out.println(userSimulationInformation.getUserId() + "|" +
                userSimulationInformation.getCallCount() + "|" + userSimulationInformation.getUnsuccessfulRequestCount()
                + "|" + userSimulationInformation.getAverageExecutionTime()));
        threadPool.shutdown();
    }

}