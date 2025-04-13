package com.springboot.bookstore.dto;

import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

public class ClientErrorResponseDto {
    private String path;

    private int status;

    private String message;

    private LocalDateTime timestamp;

    public ClientErrorResponseDto(String path, int status, String message, LocalDateTime timestamp) {
        this.path = path;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
