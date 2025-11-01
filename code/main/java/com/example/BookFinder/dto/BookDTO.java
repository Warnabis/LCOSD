package com.example.BookFinder.dto;

import com.example.BookFinder.model.Book;

public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String genre;
    private boolean favorite;

    public BookDTO(Book book, boolean favorite) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.genre = book.getGenre();
        this.favorite = favorite;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
}
