package com.kolesnikov.gamble;

public class ClientApplication {
    public static void main(String[] args)  {
        StressTest stressTest = new StressTest();
        stressTest.test(200, 200, 0);
    }
}