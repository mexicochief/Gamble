package com.kolesnikov.gamble;

public class UserSimulationInformation {
    private long userId = 0;
    private long callCount = 0;
    private long answerCount = 0;
    private long startTime = 0;
    private long overallExecutionTime = 0;

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void startCountdown() {
        startTime = System.nanoTime();
    }

    public void endCountDown() {
        overallExecutionTime += System.nanoTime() - startTime;
    }

    public void appendCallCount() {
        callCount++;
    }

    public void appendAnswerCount() {
        answerCount++;
    }

    public long getAverageExecutionTime() {
        return callCount == 0 ? 0 : overallExecutionTime / callCount;
    }

    public long getCallCount() {
        return callCount;
    }

    public long getUnsuccessfulRequestCount() {
        return callCount - answerCount;
    }

    public long getUserId() {
        return userId;
    }
}