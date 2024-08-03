package com.phonepe;

import com.phonepe.config.LoggerConfig;
import com.phonepe.constants.LoggerLibraryConstants;
import com.phonepe.core.LogLevel;
import com.phonepe.core.Logger;

import java.io.IOException;

public class LoggerLibrary {
    public static void main(String[] args) throws IOException {
        // Load logger configuration from properties file
        LoggerConfig loggerConfig = new LoggerConfig("logger-config.properties");
        Logger logger = loggerConfig.createLogger();

        // Log messages with different levels and namespaces
        System.out.println("Logging messages...");
        logger.log("This is an info message", LogLevel.INFO, LoggerLibraryConstants.NAMESPACE);
        logger.log("This is a warning", LogLevel.WARN, LoggerLibraryConstants.NAMESPACE);
        logger.log("This is an error", LogLevel.ERROR, LoggerLibraryConstants.NAMESPACE);
        System.out.println("Finished logging messages.");
    }
}