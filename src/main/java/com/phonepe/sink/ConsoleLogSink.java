package com.phonepe.sink;

import java.io.IOException;

public class ConsoleLogSink implements LogSink {

    @Override
    public void write(String message) throws IOException {
        // Write the log message to the console
        System.out.println(message);
    }
}
