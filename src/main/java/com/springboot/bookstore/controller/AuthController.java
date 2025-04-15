package com.springboot.bookstore.controller;

import com.springboot.bookstore.dto.AuthenticationResponseDto;
import com.springboot.bookstore.dto.CreateUserDto;
import com.springboot.bookstore.dto.LoginDto;
import com.springboot.bookstore.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthenticationResponseDto login(@RequestBody LoginDto loginDto) {
        return this.authService.login(loginDto);
    }

    @PostMapping("/sign-up")
    public AuthenticationResponseDto signUp(@RequestBody CreateUserDto createUserDto) {
        return this.authService.signUp(createUserDto);
    }
}
