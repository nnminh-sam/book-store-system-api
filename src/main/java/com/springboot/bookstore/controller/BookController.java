package com.springboot.bookstore.controller;

import com.springboot.bookstore.dto.CreateBookDto;
import com.springboot.bookstore.model.Book;
import com.springboot.bookstore.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService = new BookService();

    @GetMapping
    public List<Book> find() {
        return this.bookService.find();
    }

    @PostMapping
    public String create(@RequestBody CreateBookDto createBookDto) {
        return this.bookService.create(createBookDto);
    }
}
