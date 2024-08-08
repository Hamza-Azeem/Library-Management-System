package com.example.Library.Management.System.service.impl;

import com.example.Library.Management.System.dto.BookDto;
import com.example.Library.Management.System.entity.Book;
import com.example.Library.Management.System.exception.DuplicateResourceException;
import com.example.Library.Management.System.exception.ResourceNotFoundException;
import com.example.Library.Management.System.repository.BookRepository;
import com.example.Library.Management.System.service.BookService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    @Cacheable(value = "books", key = "'all'")
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream().map(book -> convertToBookDto(book))
                .collect(Collectors.toList());
    }

    @Override
    public Book getBookById(long id) {
        return findBookById(id);
    }

    @Override
    @Cacheable(value = "bookId", key = "#id")
    public BookDto getBookDtoById(long id) {
        return convertToBookDto(findBookById(id));
    }

    private Book findBookById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Book with id %s not found", id)));
    }

    @Override
    @CacheEvict(value = "books", allEntries = true)
    public void saveBook(BookDto bookDto) {
        if(bookRepository.existsByIsbn(bookDto.getIsbn())){
            throw new DuplicateResourceException(String.format("Book with isbn %s already exists", bookDto.getIsbn()));
        }
        bookRepository.save(convertToBook(bookDto));
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "books", allEntries = true),
                    @CacheEvict(value = "bookId", key = "#id")
            }
    )
    public void deleteBookById(long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isEmpty()){
            throw new ResourceNotFoundException(String.format("Book with id %s not found", id));
        }
        bookRepository.deleteById(id);
    }

    @Override
    @CacheEvict(value = "books", allEntries = true)
    @CachePut(value = "bookId", key = "#bookId")
    public BookDto updateBook(long bookId, BookDto bookDto) {
        if(bookRepository.existsByIsbn(bookDto.getIsbn())){
            throw new DuplicateResourceException(String.format("Book with isbn %s already exists", bookDto.getIsbn()));
        }
        Book book = findBookById(bookId);
        book.setAuthor(bookDto.getAuthor());
        book.setTitle(bookDto.getTitle());
        book.setIsbn(bookDto.getIsbn());
        book.setPublicationYear(bookDto.getPublicationYear());
        return convertToBookDto(bookRepository.save(book));
    }
    @Override
    @CacheEvict(value = "bookId", key = "#book.id")
    public void updateBookBorrowingRecord(Book book) {
        bookRepository.save(book);
    }
}
