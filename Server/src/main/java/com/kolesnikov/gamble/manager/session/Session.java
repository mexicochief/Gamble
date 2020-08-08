package com.kolesnikov.gamble.manager.session;

import java.io.*;

public interface Session {
    void start(BufferedReader bufferedReader, BufferedWriter bufferedWriter) throws IOException;
}
