package com.springboot.bookstore.repository;

import com.springboot.bookstore.database.DBConnectionManager;
import com.springboot.bookstore.model.User;
import com.springboot.bookstore.utils.logger.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class UserRepository {
    private final Logger logger = Logger.getInstance();

    public Optional<User> findByEmail(String email) {
        String query = "SELECT * FROM users AS u WHERE u.email = ?";

        try (var connection = DBConnectionManager.getInstance().getConnection()) {
            var preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var user = User.builder()
                            .id(resultSet.getString("id"))
                            .email(resultSet.getString("email"))
                            .password(resultSet.getString("password"))
                            .firstName(resultSet.getString("first_name"))
                            .lastName(resultSet.getString("last_name"));
                    return Optional.of(user);
                }
                return Optional.empty();
            } catch (Exception e) {
                this.logger.fatal("Cannot execute query: " + query);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        } catch (Exception e) {
            this.logger.fatal("Database connection could not be established. Error: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public boolean save(User user) {
        String query = "INSERT INTO users (email, password, first_name, last_name) VALUES (?, ?, ?, ?)";

        try (var connection = DBConnectionManager.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                this.logger.error("User insertion failed: no rows affected.");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User insertion failed");
            }

            this.logger.log("User saved with email: " + user.getEmail());
            return true;
        } catch (Exception e) {
            this.logger.fatal("Failed to save user. Error: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
