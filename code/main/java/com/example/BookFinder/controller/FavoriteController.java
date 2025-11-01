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
@RequestMapping("/favorites")
public class FavoriteController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public FavoriteController(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @PostMapping("/toggle/{bookId}")
    public String toggleFavorite(@PathVariable Long bookId, Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Book book = bookRepository.findById(bookId).orElseThrow();

        if (user.getFavoriteBooks().contains(book)) {
            user.getFavoriteBooks().remove(book);
        } else {
            user.getFavoriteBooks().add(book);
        }

        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping
    public String favoriteBooks(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("favorites", user.getFavoriteBooks());
        return "favorites";
    }
}