# Book store system

> This system is for practice design pattern purpose only!

## Singleton Pattern

These are three ways to apply Singleton pattern in Book store system.

### Managing database connection

Database connection manager will manage application connections to the Postgres DBMS. Apply
singleton pattern into this will ensure that there is only one DB Connection Manager through
out the application.

`DBConnectionManager.java` class: [`DBConnectionManager.java`](./src/main/java/com/springboot/bookstore/database/DBConnectionManager.java)

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

- Singleton pattern is applied to create one instance of the `DBConnectionManager`.
- Private constructor ensures that the class cannot be constructed with database connection initialization.
- Generating an instance of the `DBConnectionManager` class can be archived by the `getInstance()` function. If the
`instance` variable is not null or the database connection is lost, a new instance is created with new database 
connection. Database connection can be retrieved by the `getConnection()` function. Due to the `static getInstance()` function, 
application can get the database connection of the current instance easily. 

### System logging

Applied singleton pattern to create a single instance `Logger` object for logging useful information of the request.

`Logger` class: [`Logger`](./src/main/java/com/springboot/bookstore/utils/logger/Logger.java) 

```java
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
```

- Singleton pattern is applied in `Logger` class to ensure only one instance of the `Logger` class through out the 
application. This makes logging consistent and centralized.
- Stateless logger will save memory and avoid unnecessary object creation.
- Easy global access through out the application without parsing logger through function parameter.
- Consistent log format and behavior.
- Easy for future upgrade: file logging, external logging, etc.
