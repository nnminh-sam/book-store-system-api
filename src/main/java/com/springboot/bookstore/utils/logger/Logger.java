package com.springboot.bookstore.utils.logger;

import java.time.LocalDateTime;

public class Logger {
    private static volatile Logger instance;

    private Logger() {}

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public void log(String message) {
        LocalDateTime timestamp = LocalDateTime.now();
        System.out.println("[" + timestamp + "] " +  LogLevel.INFO + ": "  + message);
    }

    public void error(String message) {
        LocalDateTime timestamp = LocalDateTime.now();
        System.out.println("[" + timestamp + "] " +  LogLevel.ERROR + ": "  + message);
    }

    public void warn(String message) {
        LocalDateTime timestamp = LocalDateTime.now();
        System.out.println("[" + timestamp + "] " +  LogLevel.WARN + ": "  + message);
    }

    public void debug(String message) {
        LocalDateTime timestamp = LocalDateTime.now();
        System.out.println("[" + timestamp + "] " +  LogLevel.DEBUG + ": "  + message);
    }

    public void fatal(String message) {
        LocalDateTime timestamp = LocalDateTime.now();
        System.out.println("[" + timestamp + "] " +  LogLevel.FATAL + ": "  + message);
    }
}
