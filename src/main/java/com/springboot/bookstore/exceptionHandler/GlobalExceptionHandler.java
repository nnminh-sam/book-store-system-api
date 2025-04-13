package com.springboot.bookstore.exceptionHandler;

import com.springboot.bookstore.dto.ClientErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ClientErrorResponseDto> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request) {
        LocalDateTime timestamp = LocalDateTime.now();
        String path = request.getRequestURI();
        String message = ex.getReason();
        HttpStatusCode status = ex.getStatusCode();
        return new ResponseEntity<>(new ClientErrorResponseDto(path, status.value(), message, timestamp), status);
    }
}
