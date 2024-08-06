package com.example.Library.Management.System.service.impl;

import com.example.Library.Management.System.dto.BookDto;
import com.example.Library.Management.System.entity.Book;
import com.example.Library.Management.System.exception.InValidRequestException;
import com.example.Library.Management.System.exception.ResourceNotFoundException;
import com.example.Library.Management.System.repository.BookRepository;
import com.example.Library.Management.System.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.Library.Management.System.mapper.BookMapper.convertToBook;
import static com.example.Library.Management.System.mapper.BookMapper.convertToBookDto;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream().map(book -> convertToBookDto(book))
                .collect(Collectors.toList());
    }

    @Override
    public Book getBookById(long id) {
        return findBookById(id);
    }

    @Override
    public BookDto getBookDtoById(long id) {
        return convertToBookDto(findBookById(id));
    }

    private Book findBookById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Book with id %s not found", id)));
    }

    @Override
    public void saveBook(BookDto bookDto) {
        bookRepository.save(convertToBook(bookDto));
    }

    @Override
    public void deleteBookById(long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isEmpty()){
            throw new ResourceNotFoundException(String.format("Book with id %s not found", id));
        }
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBook(long bookId, BookDto bookDto) {
        boolean updated = false;
        Book book = findBookById(bookId);
        if(!isBookUpdated(book, bookDto)){
            throw new InValidRequestException("No updates were found!");
        }
        return convertToBookDto(bookRepository.save(book));
    }
    private boolean isBookUpdated(Book book, BookDto bookDto) {
        boolean updated = false;
        if(bookDto.getAuthor() != null && !bookDto.getAuthor().equals(book.getAuthor())){
            book.setAuthor(bookDto.getAuthor());
            updated = true;
        }
        if(bookDto.getTitle() != null && !bookDto.getTitle().equals(book.getTitle())){
            book.setTitle(bookDto.getTitle());
            updated = true;
        }
        if(bookDto.getIsbn() != null && !bookDto.getIsbn().equals(book.getIsbn())){
            book.setIsbn(bookDto.getIsbn());
            updated = true;
        }
        if(bookDto.getPublicationYear() != 0 && bookDto.getPublicationYear() != book.getPublicationYear()){
            book.setPublicationYear(bookDto.getPublicationYear());
            updated = true;
        }
        return updated;
    }
}
