package com.example.BookFinder.service;

import com.example.BookFinder.model.Book;
import com.example.BookFinder.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books != null ? books : List.of();
    }

    public List<Book> searchBooks(String query) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(query);
        return books != null ? books : List.of();
    }
}
