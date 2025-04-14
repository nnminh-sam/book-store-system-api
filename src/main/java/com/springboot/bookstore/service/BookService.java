package com.springboot.bookstore.service;

import com.springboot.bookstore.dto.CreateBookDto;
import com.springboot.bookstore.dto.FindBookDto;
import com.springboot.bookstore.model.Book;
import com.springboot.bookstore.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository = new BookRepository();

    public List<Book> find(Optional<FindBookDto> findBookDtoOpt) {
        return this.bookRepository.find(findBookDtoOpt);
    }

    public String create(CreateBookDto createBookDto) {
        var result = this.bookRepository.save(new Book(createBookDto.getTitle(), createBookDto.getAuthor(), createBookDto.getPublishDate()));
        if (!result) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot create book");
        }
        return "Book created";
    }
}
