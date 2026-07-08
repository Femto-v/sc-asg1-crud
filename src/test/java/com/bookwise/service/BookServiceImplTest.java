package com.bookwise.service;

import com.bookwise.model.Book;
import com.bookwise.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = new Book();
        sampleBook.setId(1L);
        sampleBook.setTitle("Clean Code");
        sampleBook.setAuthor("Robert C. Martin");
        sampleBook.setIsbn("978-0132350884");
        sampleBook.setAvailable(true);
    }

    @Test
    @DisplayName("addBook should save and return the book")
    void addBookShouldSaveAndReturnBook() {
        when(bookRepository.save(sampleBook)).thenReturn(sampleBook);

        Book savedBook = bookService.addBook(sampleBook);

        assertNotNull(savedBook);
        assertEquals("Clean Code", savedBook.getTitle());
        assertEquals("Robert C. Martin", savedBook.getAuthor());
        verify(bookRepository).save(sampleBook);
    }

    @Test
    @DisplayName("getAllBooks should return all books from repository")
    void getAllBooksShouldReturnAllBooks() {
        List<Book> books = List.of(sampleBook, new Book());
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertSame(books, result);
        verify(bookRepository).findAll();
    }

    @Test
    @DisplayName("getBookById should return a book when present")
    void getBookByIdShouldReturnBookWhenPresent() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        Optional<Book> result = bookService.getBookById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(bookRepository).findById(1L);
    }

    @Test
    @DisplayName("updateBook should modify existing book fields and save changes")
    void updateBookShouldModifyExistingBookAndSave() {
        Book updatedDetails = new Book();
        updatedDetails.setTitle("Effective Java");
        updatedDetails.setAuthor("Joshua Bloch");
        updatedDetails.setIsbn("978-0134685991");
        updatedDetails.setAvailable(false);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book updatedBook = bookService.updateBook(1L, updatedDetails);

        assertEquals("Effective Java", updatedBook.getTitle());
        assertEquals("Joshua Bloch", updatedBook.getAuthor());
        assertEquals("978-0134685991", updatedBook.getIsbn());
        assertFalse(updatedBook.isAvailable());
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("deleteBook should delegate deletion to repository")
    void deleteBookShouldDelegateDeletionToRepository() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    @DisplayName("searchByTitle should return books matching the title")
    void searchByTitleShouldReturnMatchingBooks() {
        List<Book> matchingBooks = List.of(sampleBook);
        when(bookRepository.findByTitleContainingIgnoreCase("clean")).thenReturn(matchingBooks);

        List<Book> result = bookService.searchByTitle("clean");

        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
        verify(bookRepository).findByTitleContainingIgnoreCase("clean");
    }
}
