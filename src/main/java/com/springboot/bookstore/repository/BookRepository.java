package com.springboot.bookstore.repository;

import com.springboot.bookstore.database.DBConnectionManager;
import com.springboot.bookstore.model.Book;
import com.springboot.bookstore.utils.logger.Logger;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookRepository {
    private final Logger logger = Logger.getInstance();

    public List<Book> find() {
        List<Book> books = new ArrayList<>();
        try (Connection connection = DBConnectionManager.getInstance().getConnection()) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from books");
            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getString("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        LocalDate.parse(resultSet.getString("publish_date"))
                );
                books.add(book);
            }

            resultSet.close();
            statement.close();
            connection.close();
            this.logger.log("Retrieved book list successfully.");
            return books;
        } catch (Exception e) {
            this.logger.fatal("Database connection could not be established. Error: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public boolean save(Book book) {
        String query = "INSERT INTO books (title, author, publish_date) VALUES (?, ?, ?)";
        try (Connection connection = DBConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setObject(3, book.getPublishDate());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            this.logger.log("New book created successfully.");
            return true;
        } catch (PSQLException e) {
            String message = Objects.requireNonNull(e.getServerErrorMessage()).getMessage();
            this.logger.error(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        } catch (Exception e) {
            this.logger.fatal("Database connection could not be established. Error: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
