package com.springboot.bookstore.dto;

import java.time.LocalDate;

public class CreateBookDto {
    private String title;

    private String author;

    private LocalDate publishDate;

    public CreateBookDto() {
    }

    public CreateBookDto(String title, String author, LocalDate publishDate) {
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
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
}
