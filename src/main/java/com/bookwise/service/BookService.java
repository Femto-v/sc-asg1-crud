package com.bookwise.service;

import com.bookwise.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book addBook(Book book);
    List<Book> getAllBooks();
    Optional<Book> getBookById(Long id);
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);
    List<Book> searchByTitle(String title);
}