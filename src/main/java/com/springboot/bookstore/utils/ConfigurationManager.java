package com.springboot.bookstore.utils;

import com.springboot.bookstore.utils.logger.Logger;

import java.io.InputStream;
import java.util.Properties;

public class ConfigurationManager {
    private static volatile ConfigurationManager instance;
    private final Properties properties = new Properties();

    private ConfigurationManager() {
        var logger = Logger.getInstance();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                logger.error("application.properties not found in classpath.");
            }
        } catch (Exception e) {
            logger.fatal("Could not load application.properties: " + e.getMessage());
        }
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double getDouble(String key, double defaultValue) {
        try {
            return Double.parseDouble(properties.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
