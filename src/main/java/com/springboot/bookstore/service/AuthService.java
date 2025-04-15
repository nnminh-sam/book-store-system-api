package com.springboot.bookstore.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.springboot.bookstore.dto.AuthenticationResponseDto;
import com.springboot.bookstore.dto.CreateUserDto;
import com.springboot.bookstore.dto.LoginDto;
import com.springboot.bookstore.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class AuthService {
    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    private AuthenticationResponseDto generateTokens(String email) {
        final var HOUR_IN_MILLIS = 60 * 60 * 1000;
        final var DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS;

        var response = new AuthenticationResponseDto();
        response.setAccessToken(JwtUtil.generateToken(email, new Date(System.currentTimeMillis() + HOUR_IN_MILLIS)));
        response.setRefreshToken(JwtUtil.generateToken(email, new Date(System.currentTimeMillis() + DAY_IN_MILLIS)));

        return response;
    }

    public AuthenticationResponseDto login(LoginDto loginDto) {
        final var email = loginDto.getEmail();
        var user = this.userService.findByEmail(email);

        final var password = loginDto.getPassword().toCharArray();
        final var hashedPassword = user.getPassword().toCharArray();

        var result = BCrypt.verifyer().verify(password, hashedPassword);
        if (result.verified) {
            var response = this.generateTokens(email);
            response.setEmail(email);
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            return response;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email or password");
    }

    public AuthenticationResponseDto signUp(CreateUserDto createUserDto) {
        var email = createUserDto.getEmail();
        this.userService.create(createUserDto);
        var response = this.generateTokens(email);
        response.setEmail(email);
        response.setFirstName(createUserDto.getFirstName());
        response.setLastName(createUserDto.getLastName());
        return response;
    }
}
