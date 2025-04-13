# Book store system

> This system is for practice design pattern purpose only!

## Singleton Pattern

These are three ways to apply Singleton pattern in Book store system.

### Managing database connection

Database connection manager will manage application connections to the Postgres DBMS. Apply
singleton pattern into this will ensure that there is only one DB Connection Manager through
out the application.

File: [`DBConnectionManager.java`](./src/main/java/com/springboot/bookstore/database/DBConnectionManager.java)

```java
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
```

Singleton pattern is applied to create one instance of the `DBConnectionManager`.

- Private constructor ensures that the class cannot be constructed with database connection initialization.
- Generating an instance of the `DBConnectionManager` class can be archived by the `getInstance()` function. If the
`instance` variable is not null or the database connection is lost, a new instance is created with new database connection.
- Database connection can be retrieved by the `getConnection()` function. Due to the `static getInstance()` function, 
application can get the database connection of the current instance easily. 
