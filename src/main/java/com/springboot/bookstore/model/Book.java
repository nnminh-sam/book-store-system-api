package com.springboot.bookstore.model;

import java.time.LocalDate;
import java.util.Date;

public class Book {
    private String id;

    private String title;

    private String author;

    private LocalDate publishDate;

    public Book() {}

    public Book(String title, String author, LocalDate publishDate) {
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
    }

    public Book(String id, String title, String author, LocalDate publishDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
    }

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
}
