package com.springboot.bookstore.database;

import com.springboot.bookstore.utils.logger.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
    private final Logger logger = Logger.getInstance();

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
            this.logger.log("Database connection established.");
        } catch (Exception e) {
            this.logger.fatal("Database connection could not be established. Error: " + e.getMessage());
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
