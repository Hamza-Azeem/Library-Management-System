package com.example.Library.Management.System.controller;

import com.example.Library.Management.System.dto.BookDto;
import com.example.Library.Management.System.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @GetMapping
    public ResponseEntity<?> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookDtoById(id));
    }
    @PostMapping
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDto bookDto) {
        bookService.saveBook(bookDto);
        return new ResponseEntity<>("Book added successfully", HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.notFound().build();
    }

}
