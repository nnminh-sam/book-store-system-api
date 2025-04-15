package com.springboot.bookstore.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.springboot.bookstore.dto.CreateUserDto;
import com.springboot.bookstore.model.User;
import com.springboot.bookstore.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public User findByEmail(String email) {
        var optUser = this.userRepository.findByEmail(email);
        if (optUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return optUser.get();
    }

    public boolean create(CreateUserDto createUserDto) {
        var email = createUserDto.getEmail();
        var otpUser = this.userRepository.findByEmail(email);
        if (otpUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email has been taken");
        }

        var user = new User();
        user.setEmail(email);
        user.setFirstName(createUserDto.getFirstName());
        user.setLastName(createUserDto.getLastName());
        user.setPassword(BCrypt.withDefaults().hashToString(12, createUserDto.getPassword().toCharArray()));

        return this.userRepository.save(user);
    }
}
