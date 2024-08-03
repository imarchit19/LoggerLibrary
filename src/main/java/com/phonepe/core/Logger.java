package com.phonepe.core;

import com.phonepe.sink.LogSink;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Logger {
    private Map<LogLevel, LogSink> sinks = new HashMap<>();
    private String timeFormat;
    private LogLevel logLevel;

    private SinkType sinkType;
    private String hostName;

    public Logger(String timeFormat, LogLevel logLevel, SinkType sinkType, String hostName) {
        this.timeFormat = timeFormat;
        this.logLevel = logLevel;
        this.sinkType = sinkType;
        this.hostName = hostName;
    }

    // Add a sink for a specific log level
    public void addSink(LogLevel level, LogSink sink) {
        sinks.put(level, sink);
    }

    // Log a message with specified level and namespace
    public void log(String message, LogLevel level, String namespace) {
        if (level.ordinal() >= logLevel.ordinal()) {
            // Generate a timestamp and a tracking ID
            String timestamp = new SimpleDateFormat(timeFormat).format(new Date());
            String trackingId = UUID.randomUUID().toString();

            // Construct the complete log message
            String logMessage = String.format("%s [%s] %s - %s - %s - %s", level, timestamp, hostName, trackingId, namespace, message);

            // Get the appropriate sink and write the log message
            LogSink sink = sinks.get(level);
            if (sink != null) {
                try {
                    sink.write(logMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
