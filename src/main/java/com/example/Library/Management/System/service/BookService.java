package com.example.Library.Management.System.service;

import com.example.Library.Management.System.dto.BookDto;
import com.example.Library.Management.System.entity.Book;

import java.util.List;

public interface BookService {
    public List<BookDto> getAllBooks();
    public Book getBookById(long id);
    public BookDto getBookDtoById(long id);
    public void saveBook(BookDto bookDto);
    public void deleteBookById(long id);
    public BookDto updateBook(long bookId, BookDto bookDto);
    public void updateBookBorrowingRecord(Book book);
}
