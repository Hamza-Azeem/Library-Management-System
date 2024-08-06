package com.example.Library.Management.System.mapper;

import com.example.Library.Management.System.dto.BookDto;
import com.example.Library.Management.System.entity.Book;

public class BookMapper {
    public static Book convertToBook(BookDto bookDto) {
        return Book.builder()
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .isbn(bookDto.getIsbn())
                .publicationYear(bookDto.getPublicationYear())
                .build();
    }
    public static BookDto convertToBookDto(Book book) {
        return BookDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publicationYear(book.getPublicationYear())
                .build();
    }
}
