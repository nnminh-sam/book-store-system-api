package com.springboot.bookstore.repository;

import com.springboot.bookstore.database.DBConnectionManager;
import com.springboot.bookstore.model.Book;
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
import java.util.Optional;

public class BookRepository {

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
            return books;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            return true;
        } catch (PSQLException e) {
            String message = Objects.requireNonNull(e.getServerErrorMessage()).getMessage();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
