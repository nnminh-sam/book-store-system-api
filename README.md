# Book store system

> This system is for practice design pattern purpose only!

---

## Table of Contents

- [Singleton Pattern](#singleton-pattern)
  - [Managing database connection](#managing-database-connection)
  - [System logging](#system-logging)
  - [Configuration manager](#configuration-manager)

---

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

### Configuration manager

Applied singleton pattern into `ConfigurationManager` class to manage application configuration.

`ConfigurationManager` class: [`ConfigurationManager`](./src/main/java/com/springboot/bookstore/utils/ConfigurationManager.java)

```java
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
```

Applies singleton pattern into `ConfigurationManager` class:
- Ensure mismatch and duplicated configuration.
- Easy global access through out the application.
- Make application configuration upgradable in the future.
- Cacheable since there aren't recreate or reload through out the application.

## Factory Pattern

These are three ways to apply Factory pattern into Book store system.

### Request param DTO validator factory

The abstract `BaseRequestDto` class has an abstract method `validateSortBy()` method which validates the `orderBy`
fields of child classes that implement the `BaseRequestDto` class.  

[`BaseRequestDto`](./src/main/java/com/springboot/bookstore/dto/BaseRequestDto.java) class:

```java
package com.springboot.bookstore.dto;

public abstract class BaseRequestDto {
    protected int page;

    protected int size;

    protected String orderBy;

    protected String sortBy;

    public abstract void validateSortBy();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
```

Then use the `RequestParamValidatorFactory` class to validate the `BaseRequestDto` class. Due to polymorphism any
classes that implements the `BaseRequestDto` class can be validated.

[`RequestParamValidatorFactory`](./src/main/java/com/springboot/bookstore/factory/RequestParamValidatorFactory.java) class:

```java
package com.springboot.bookstore.factory;

import com.springboot.bookstore.dto.BaseRequestDto;

public class RequestParamValidatorFactory {
    public static void validateDto(BaseRequestDto dto) {
        dto.validateSortBy();
    }
}
```

- Usage of the DTOs don't need to know the validation logic, juse use the validator factory - Factory delegates logics.
- Easy to add new request types without modifying the factory - Open/close principle.
- DTOs are responsible for validating their own fields.
- Avoid duplicating DTO validation.
