package ru.yandex.practicum;

import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class GameLogger {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    .withZone(ZoneOffset.UTC);

    private final PrintWriter writer;

    public GameLogger(PrintWriter writer) {
        this.writer = writer;
        info("=== START NEW SESSION ===");
        info("Working dir: " + System.getProperty("user.dir"));
        info("Java version: " + System.getProperty("java.version"));
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void warning(String message) {
        log("WARNING", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    private void log(String level, String message) {
        String timestamp = FORMATTER.format(Instant.now());
        writer.println(timestamp + " " + level + " " + message);
        writer.flush();
    }
}