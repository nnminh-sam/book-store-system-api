package com.springboot.bookstore.database;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
    private static DBConnectionManager instance;
    private final Connection connection;

    private final String URL = "jdbc:postgresql://localhost:5432/bookstore";
    private final String USER = "postgres";
    private final String PASSWORD = "postgres";

    // * Private constructor limiting the creation of the database connection manager
    private DBConnectionManager() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(this.URL, this.USER, this.PASSWORD);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // * DBConnectionManager instance getter function
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

    // * Database connection getter function
    public Connection getConnection() {
        return this.connection;
    }
}
