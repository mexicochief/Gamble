package com.kolesnikov.gamble.manager.session;

import java.io.*;

public interface SessionExecutor {
    void execute(BufferedReader bufferedReader, BufferedWriter bufferedWriter) throws IOException;
}
