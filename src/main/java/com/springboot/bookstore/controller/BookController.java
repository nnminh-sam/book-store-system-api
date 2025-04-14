package com.springboot.bookstore.controller;

import com.springboot.bookstore.dto.CreateBookDto;
import com.springboot.bookstore.dto.FindBookDto;
import com.springboot.bookstore.model.Book;
import com.springboot.bookstore.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService = new BookService();

    @GetMapping
    public List<Book> find(@ModelAttribute Optional<FindBookDto> findBookDtoOpt) {
        return this.bookService.find(findBookDtoOpt);
    }

    @PostMapping
    public String create(@RequestBody CreateBookDto createBookDto) {
        return this.bookService.create(createBookDto);
    }
}
