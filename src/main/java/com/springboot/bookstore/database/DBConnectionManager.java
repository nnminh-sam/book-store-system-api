package com.springboot.bookstore.database;

import com.springboot.bookstore.utils.ConfigurationManager;
import com.springboot.bookstore.utils.logger.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
    private static DBConnectionManager instance;
    private final Connection connection;

    private DBConnectionManager() {
        var logger = Logger.getInstance();
        var configurationManager = ConfigurationManager.getInstance();

        var host = configurationManager.get("database_host");
        var port = configurationManager.get("database_port");
        var databaseName = configurationManager.get("database_name");
        var user = configurationManager.get("database_user");
        var password = configurationManager.get("database_password");

        String databaseUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, databaseName);
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(databaseUrl, user, password);
            logger.log(String.format("Database [%s] connected at %s:%s", databaseName, host, port));
        } catch (Exception e) {
            logger.fatal("Database connection could not be established. Error: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public static DBConnectionManager getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            synchronized (DBConnectionManager.class) {
                if (instance == null || instance.connection.isClosed()) {
                    instance = new DBConnectionManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
