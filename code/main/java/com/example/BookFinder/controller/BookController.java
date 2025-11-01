package com.example.BookFinder.controller;

import com.example.BookFinder.model.Book;
import com.example.BookFinder.model.User;
import com.example.BookFinder.repository.BookRepository;
import com.example.BookFinder.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookController(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public String bookDetails(@PathVariable Long id, Model model, Principal principal) {
        Book book = bookRepository.findById(id).orElseThrow();
        boolean isFavorite = false;

        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName())
              .orElseThrow(() -> new RuntimeException("User not found"));
            isFavorite = user.getFavoriteBooks().contains(book);
        }

        model.addAttribute("book", book);
        model.addAttribute("isFavorite", isFavorite);
        model.addAttribute("isAuthenticated", principal != null);
        return "bookDetails";
    }

    @PostMapping("/{id}/favorite")
    @ResponseBody
    public String toggleFavorite(@PathVariable Long id, Principal principal) {
        if (principal == null) return "not_authenticated";

        User user = userRepository.findByUsername(principal.getName())
          .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(id).orElseThrow();

        if (user.getFavoriteBooks().contains(book)) {
            user.getFavoriteBooks().remove(book);
        } else {
            user.getFavoriteBooks().add(book);
        }

        userRepository.save(user);
        return user.getFavoriteBooks().contains(book) ? "added" : "removed";
    }
}
