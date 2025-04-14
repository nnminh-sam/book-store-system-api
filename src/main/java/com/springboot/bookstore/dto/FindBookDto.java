package com.springboot.bookstore.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

public class FindBookDto extends BaseRequestDto {
    private String id;

    private String title;

    private String author;

    private LocalDate publishDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public void validateSortBy() {
        final List<String> ALLOWED_SORT_FIELDS = List.of("title", "author", "publishDate");

        if (this.sortBy != null && !ALLOWED_SORT_FIELDS.contains(this.sortBy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sortBy field: " + this.sortBy);
        }
    }
}
