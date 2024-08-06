package com.example.Library.Management.System.service.impl;

import com.example.Library.Management.System.dto.BookDto;
import com.example.Library.Management.System.entity.Book;
import com.example.Library.Management.System.exception.InValidRequestException;
import com.example.Library.Management.System.exception.ResourceNotFoundException;
import com.example.Library.Management.System.repository.BookRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    private BookServiceImpl underTest;
    @Mock
    private BookRepository bookRepository;
    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        underTest = new BookServiceImpl(bookRepository);
    }

    @Test
    void getAllBooks() {
        // Act
        underTest.getAllBooks();
        // Assert
        verify(bookRepository).findAll();
    }

    @Test
    void getBookByIdWillReturnBook() {
        // Arrange
        long id = faker.number().randomDigit();
        String title = faker.book().title();
        String author = faker.book().author();
        String isbn = faker.code().isbn10();
        int publicationYear = faker.number().numberBetween(1000, 2024);
        Book book = new Book(
                id,
                title,
                author,
                isbn,
                publicationYear
        );
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        // Act
        Book actual = underTest.getBookById(id);
        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getAuthor()).isEqualTo(author);
        assertThat(actual.getTitle()).isEqualTo(title);
        assertThat(actual.getIsbn()).isEqualTo(isbn);
        assertThat(actual.getPublicationYear()).isEqualTo(publicationYear);
    }
    @Test
    void getBookByIdWillThrowExceptionWhenBookNotFound() {
        // Arrange
        long id = faker.number().randomDigit();
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        // Assert
        assertThatThrownBy(()->underTest.getBookById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Book with id %s not found", id));
    }

    @Test
    void getBookDtoById() {
        // Arrange
        long id = faker.number().randomDigit();
        String title = faker.book().title();
        String author = faker.book().author();
        String isbn = faker.code().isbn10();
        int publicationYear = faker.number().numberBetween(1000, 2024);
        Book book = new Book(
                id,
                title,
                author,
                isbn,
                publicationYear
        );
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        // Act
        BookDto actual = underTest.getBookDtoById(id);
        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getAuthor()).isEqualTo(author);
        assertThat(actual.getTitle()).isEqualTo(title);
        assertThat(actual.getIsbn()).isEqualTo(isbn);
        assertThat(actual.getPublicationYear()).isEqualTo(publicationYear);
    }
    @Test
    void getBookDtoByIdWillThrowExceptionWhenBookDtoNotFound() {
        // Arrange
        long id = faker.number().randomDigit();
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        // Assert
        assertThatThrownBy(()->underTest.getBookDtoById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Book with id %s not found", id));
    }

    @Test
    void saveBook() {
        // Arrange
        String title = faker.book().title();
        String author = faker.book().author();
        String isbn = faker.code().isbn10();
        int publicationYear = faker.number().numberBetween(1000, 2024);
        BookDto bookDto = new BookDto(
                title,
                author,
                isbn,
                publicationYear
        );
        ArgumentCaptor<Book> argument = ArgumentCaptor.forClass(Book.class);
        // Act
        underTest.saveBook(bookDto);
        // Assert
        verify(bookRepository).save(argument.capture());
        Book actual = argument.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getTitle()).isEqualTo(title);
        assertThat(actual.getAuthor()).isEqualTo(author);
        assertThat(actual.getIsbn()).isEqualTo(isbn);
        assertThat(actual.getPublicationYear()).isEqualTo(publicationYear);
    }

    @Test
    void deleteBookByIdWillDeleteBook() {
        // Arrange
        long id = faker.number().randomDigit();
        when(bookRepository.findById(id)).thenReturn(Optional.of(Book.builder().build()));
        // Act
        underTest.deleteBookById(id);
        // Assert
        verify(bookRepository).deleteById(id);
    }

    @Test
    void deleteBookByIdWillThrowExceptionWhenBookNotFound() {
        // Arrange
        long id = faker.number().randomDigit();
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        // Assert
        assertThatThrownBy(()->underTest.deleteBookById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Book with id %s not found", id));
    }

    @Test
    void updateAllBookFeatures() {
        // Arrange
        long id = faker.number().randomDigit();
        String title = faker.book().title();
        String author = faker.book().author();
        String isbn = faker.code().isbn10();
        int publicationYear = faker.number().numberBetween(1000, 2024);
        BookDto bookDto = new BookDto(
                title + "updated",
                author + "updated",
                isbn + "updated",
                publicationYear + 1
        );
        Book book = new Book(
                id,
                title,
                author,
                isbn,
                publicationYear
        );
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        // Act
        BookDto actual = underTest.updateBook(id, bookDto);
        // Assert
        assertThat(actual.getTitle()).isEqualTo(title + "updated");
        assertThat(actual.getAuthor()).isEqualTo(author + "updated");
        assertThat(actual.getIsbn()).isEqualTo(isbn + "updated");
        assertThat(actual.getPublicationYear()).isEqualTo(publicationYear + 1);
    }
    @Test
    void updateOnlyBookTitle() {
        // Arrange
        long id = faker.number().randomDigit();
        String title = faker.book().title();
        String author = faker.book().author();
        String isbn = faker.code().isbn10();
        int publicationYear = faker.number().numberBetween(1000, 2024);
        BookDto bookDto = new BookDto(
                title + "updated",
                null,
                null,
                0
        );
        Book book = new Book(
                id,
                title,
                author,
                isbn,
                publicationYear
        );
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        // Act
        BookDto actual = underTest.updateBook(id, bookDto);
        // Assert
        assertThat(actual.getTitle()).isEqualTo(title+"updated");
        assertThat(actual.getAuthor()).isEqualTo(author);
        assertThat(actual.getIsbn()).isEqualTo(isbn);
        assertThat(actual.getPublicationYear()).isEqualTo(publicationYear);
    }
    @Test
    void updateOnlyBookAuthor() {
        // Arrange
        long id = faker.number().randomDigit();
        String title = faker.book().title();
        String author = faker.book().author();
        String isbn = faker.code().isbn10();
        int publicationYear = faker.number().numberBetween(1000, 2024);
        BookDto bookDto = new BookDto(
                title,
                author + "updated",
                isbn,
                publicationYear
        );
        Book book = new Book(
                id,
                title,
                author,
                isbn,
                publicationYear
        );
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        // Act
        BookDto actual = underTest.updateBook(id, bookDto);
        // Assert
        assertThat(actual.getTitle()).isEqualTo(title);
        assertThat(actual.getAuthor()).isEqualTo(author+"updated");
        assertThat(actual.getIsbn()).isEqualTo(isbn);
        assertThat(actual.getPublicationYear()).isEqualTo(publicationYear);
    }
    @Test
    void updateOnlyBookIsbn() {
        // Arrange
        long id = faker.number().randomDigit();
        String title = faker.book().title();
        String author = faker.book().author();
        String isbn = faker.code().isbn10();
        int publicationYear = faker.number().numberBetween(1000, 2024);
        BookDto bookDto = new BookDto(
                title,
                author ,
                isbn+ "updated",
                publicationYear
        );
        Book book = new Book(
                id,
                title,
                author,
                isbn,
                publicationYear
        );
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        // Act
        BookDto actual = underTest.updateBook(id, bookDto);
        // Assert
        assertThat(actual.getTitle()).isEqualTo(title);
        assertThat(actual.getAuthor()).isEqualTo(author);
        assertThat(actual.getIsbn()).isEqualTo(isbn+"updated");
        assertThat(actual.getPublicationYear()).isEqualTo(publicationYear);
    }
    @Test
    void updateOnlyBookPublicationYear() {
        // Arrange
        long id = faker.number().randomDigit();
        String title = faker.book().title();
        String author = faker.book().author();
        String isbn = faker.code().isbn10();
        int publicationYear = faker.number().numberBetween(1000, 2024);
        BookDto bookDto = new BookDto(
                title,
                author ,
                isbn,
                publicationYear + 1
        );
        Book book = new Book(
                id,
                title,
                author,
                isbn,
                publicationYear
        );
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        // Act
        BookDto actual = underTest.updateBook(id, bookDto);
        // Assert
        assertThat(actual.getTitle()).isEqualTo(title);
        assertThat(actual.getAuthor()).isEqualTo(author);
        assertThat(actual.getIsbn()).isEqualTo(isbn);
        assertThat(actual.getPublicationYear()).isEqualTo(publicationYear + 1);
    }
    @Test
    void updateBookWillThrowExceptionWhenIdNotFound() {
        // Arrange
        long id = faker.number().randomDigit();
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        // Assert
        assertThatThrownBy(()->underTest.updateBook(id, BookDto.builder().build()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Book with id %s not found", id));
    }
    @Test
    void updateBookWillThrowExceptionWhenUpdatesNotFound() {
        // Arrange
        long id = faker.number().randomDigit();
        String title = faker.book().title();
        String author = faker.book().author();
        String isbn = faker.code().isbn10();
        int publicationYear = faker.number().numberBetween(1000, 2024);
        BookDto bookDto = new BookDto(
                title,
                author ,
                isbn,
                publicationYear
        );
        Book book = new Book(
                id,
                title,
                author,
                isbn,
                publicationYear
        );
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        // Assert
        assertThatThrownBy(()->underTest.updateBook(id, BookDto.builder().build()))
                .isInstanceOf(InValidRequestException.class)
                .hasMessage("No updates were found!");
    }
}