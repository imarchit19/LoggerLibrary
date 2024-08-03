package com.phonepe.sink;

import java.io.IOException;

public interface LogSink {
    // Method to write log message to the sink
    void write(String message) throws IOException;
}
