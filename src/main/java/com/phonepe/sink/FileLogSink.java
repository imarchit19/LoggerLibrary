package com.phonepe.sink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

public class FileLogSink implements LogSink {
    private String filePath;
    private long maxSize;
    private BufferedWriter writer;

    public FileLogSink(String filePath, long maxSize) throws IOException {
        this.filePath = filePath;
        this.maxSize = maxSize;

        // Ensure the directory exists
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs(); // Create the directory if it doesn't exist
            System.out.println("Directory created: " + parentDir.getAbsolutePath());
        }

        // Initialize the BufferedWriter for the log file
        this.writer = new BufferedWriter(new FileWriter(filePath, true));
        System.out.println("FileLogSink initialized. Log file path: " + filePath);
    }

    @Override
    public void write(String message) throws IOException {
        System.out.println("Writing message to log file: " + message);
        writer.write(message);
        writer.newLine();
        writer.flush();
        checkRotate(); // Check if log rotation is needed
    }

    private void checkRotate() throws IOException {
        System.out.println("Checking if rotation is needed. Current file size: " + Files.size(Paths.get(filePath)));
        if (Files.size(Paths.get(filePath)) > maxSize) {
            System.out.println("Rotating log file...");
            writer.close(); // Close the current log file
            rotateLogs(); // Rotate the logs
            writer = new BufferedWriter(new FileWriter(filePath, true)); // Reinitialize the writer for the new log file
            System.out.println("Log file rotated.");
        }
    }

    private void rotateLogs() throws IOException {
        System.out.println("Rotating and compressing log files...");
        // Move and rename the old log files, compressing the oldest log file
        for (int i = 4; i > 0; i--) {
            String oldFile = filePath + "." + i + ".gz";
            String newFile = filePath + "." + (i + 1) + ".gz";
            Path path = Paths.get(oldFile);
            if (Files.exists(path)) {
                Files.move(path, Paths.get(newFile));
                System.out.println("Moved: " + oldFile + " to " + newFile);
            }
        }
        try (GZIPOutputStream out = new GZIPOutputStream(Files.newOutputStream(Paths.get(filePath + ".1.gz")))) {
            Files.copy(Paths.get(filePath), out); // Compress the current log file
            System.out.println("Compressed current log file to: " + filePath + ".1.gz");
        }
        Files.delete(Paths.get(filePath)); // Delete the original log file
        System.out.println("Deleted original log file: " + filePath);
    }
}
