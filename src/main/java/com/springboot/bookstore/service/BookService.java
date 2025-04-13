package com.springboot.bookstore.service;

import com.springboot.bookstore.dto.CreateBookDto;
import com.springboot.bookstore.model.Book;
import com.springboot.bookstore.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository = new BookRepository();

    public List<Book> find() {
        return this.bookRepository.find();
    }

    public String create(CreateBookDto createBookDto) {
        var result = this.bookRepository.save(new Book(createBookDto.getTitle(), createBookDto.getAuthor(), createBookDto.getPublishDate()));
        if (!result) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot create book");
        }
        return "Book created";
    }
}
