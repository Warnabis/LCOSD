package com.example.BookFinder.controller;

import com.example.BookFinder.dto.BookDTO;
import com.example.BookFinder.model.Book;
import com.example.BookFinder.model.User;
import com.example.BookFinder.service.BookService;
import com.example.BookFinder.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainMenuController {

    private final BookService bookService;
    private final UserRepository userRepository;

    public MainMenuController(BookService bookService, UserRepository userRepository) {
        this.bookService = bookService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String mainMenu(@RequestParam(required = false) String query, Model model, Principal principal) {
        List<Book> books = (query != null && !query.isBlank()) ?
          bookService.searchBooks(query) :
          bookService.getAllBooks();

        if (books == null) books = List.of();

        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            model.addAttribute("username", principal.getName());

            User user = userRepository.findByUsername(principal.getName()).orElse(null);

            List<BookDTO> booksForView = books.stream()
              .map(book -> new BookDTO(book, user != null && user.getFavoriteBooks().contains(book)))
              .collect(Collectors.toList());

            model.addAttribute("books", booksForView);
        } else {
            List<BookDTO> booksForView = books.stream()
              .map(book -> new BookDTO(book, false))
              .collect(Collectors.toList());

            model.addAttribute("books", booksForView);
        }

        return "mainMenu";
    }
}
