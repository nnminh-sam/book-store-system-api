package com.springboot.bookstore.repository;

import com.springboot.bookstore.database.DBConnectionManager;
import com.springboot.bookstore.dto.FindBookDto;
import com.springboot.bookstore.factory.RequestParamValidatorFactory;
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
import java.util.Optional;

public class BookRepository {
    private final Logger logger = Logger.getInstance();

    public List<Book> find(Optional<FindBookDto> findBookDtoOpt) {
        List<Book> books = new ArrayList<>();
        String baseQuery = "SELECT * FROM books";
        StringBuilder queryBuilder = new StringBuilder(baseQuery);
        List<String> conditions = new ArrayList<>();

        if (findBookDtoOpt.isPresent()) {
            FindBookDto dto = findBookDtoOpt.get();
            RequestParamValidatorFactory.validateDto(dto);

            String orderBy = dto.getOrderBy();
            String id = dto.getId();
            String title = dto.getTitle();
            String author = dto.getAuthor();
            LocalDate publishDate = dto.getPublishDate();

            if (id != null && !id.isEmpty()) {
                conditions.add("id = '" + id + "'");
            }

            if (title != null && !title.isEmpty()) {
                conditions.add("LOWER(title) LIKE LOWER('%" + title + "%')");
            }

            if (author != null && !author.isEmpty()) {
                conditions.add("LOWER(author) LIKE LOWER('%" + author + "%')");
            }

            if (publishDate != null) {
                conditions.add("publish_date = '" + publishDate + "'");
            }

            if (!conditions.isEmpty()) {
                queryBuilder.append(" WHERE ").append(String.join(" AND ", conditions));
            }

            if (dto.getSortBy() != null && !dto.getSortBy().isEmpty()) {
                String order = (orderBy != null && orderBy.equalsIgnoreCase("desc")) ? "DESC" : "ASC";
                queryBuilder.append(" ORDER BY ").append(dto.getSortBy()).append(" ").append(order);
            }

            if (dto.getSize() > 0) {
                int page = Math.max(dto.getPage(), 1);
                queryBuilder.append(" LIMIT ").append(dto.getSize());
                queryBuilder.append(" OFFSET ").append((page - 1) * dto.getSize());
            }
        }

        this.logger.log("(Execute Query) " + queryBuilder);

        try (Connection connection = DBConnectionManager.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(queryBuilder.toString())) {

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getString("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        LocalDate.parse(resultSet.getString("publish_date"))
                );
                books.add(book);
            }

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
