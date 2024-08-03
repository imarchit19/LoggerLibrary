package com.phonepe.config;

import com.phonepe.constants.LoggerLibraryConstants;
import com.phonepe.core.LogLevel;
import com.phonepe.core.Logger;
import com.phonepe.core.SinkType;
import com.phonepe.sink.ConsoleLogSink;
import com.phonepe.sink.FileLogSink;
import com.phonepe.sink.LogSink;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;

public class LoggerConfig {

    private Properties config;

    public LoggerConfig(String configFilePath) throws IOException {
        config = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFilePath)) {
            if (input == null) {
                throw new IOException("Configuration file not found: " + configFilePath);
            }
            config.load(input);
        }
    }

    public Logger createLogger() throws IOException {
        // Extract configuration details or set defaults if not provided
        String timeFormat = config.getProperty(LoggerLibraryConstants.TIMESTAMP_FORMAT, "dd-MM-yyyy HH:mm:ss");
        LogLevel logLevel = LogLevel.valueOf(config.getProperty(LoggerLibraryConstants.LOG_LEVEL, LogLevel.INFO.toString()));
        String fileLocation = config.getProperty(LoggerLibraryConstants.FILE_LOCATION);
        long maxSize = Long.parseLong(config.getProperty(LoggerLibraryConstants.MAX_SIZE, "1048576"));
        SinkType sinkType = SinkType.valueOf(config.getProperty(LoggerLibraryConstants.SINK_TYPE, SinkType.CONSOLE.toString()));
        // Get host name or instance ID
        String hostName = InetAddress.getLocalHost().getHostName();

        // Create Logger instance with basic configuration
        Logger logger = new Logger(timeFormat, logLevel, sinkType, hostName);

        switch (sinkType) {
            case FILE: {
                // Create and add file sink if fileLocation is provided
                if (fileLocation != null) {
                    LogSink fileSink = new FileLogSink(fileLocation, maxSize);
                    logger.addSink(logLevel, fileSink);
                }
                break;
            }
            case CONSOLE: {
                // Create and add console sink
                LogSink consoleSink = new ConsoleLogSink();
                logger.addSink(logLevel, consoleSink);
                break;
            }
        }

        return logger;
    }
}
