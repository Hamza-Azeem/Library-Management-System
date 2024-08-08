package com.example.Library.Management.System.service.impl;

import com.example.Library.Management.System.entity.Book;
import com.example.Library.Management.System.entity.BorrowingRecord;
import com.example.Library.Management.System.entity.Patron;
import com.example.Library.Management.System.exception.InValidRequestException;
import com.example.Library.Management.System.repository.BorrowingRecordRepository;
import com.example.Library.Management.System.service.BookService;
import com.example.Library.Management.System.service.PatronService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BorrowingRecordServiceImplTest {
    private BorrowingRecordServiceImpl underTest;
    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;
    @Mock
    private PatronService patronService;
    @Mock
    private BookService bookService;
    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        underTest = new BorrowingRecordServiceImpl(borrowingRecordRepository, patronService, bookService);
    }

    @Test
    void borrowBookWillCreateNewRecord() {
        // Arrange
        long bookId = faker.number().randomDigit();
        long patronId = faker.number().randomDigit();
        Patron patron = Patron.builder()
                .id(patronId)
                .build();
        Book book =  Book.builder()
                .id(bookId)
                .build();
        LocalDate borrowDate = LocalDate.now();
        when(borrowingRecordRepository.findAll()).thenReturn(List.of());
        when(bookService.getBookById(bookId)).thenReturn(book);
        when(patronService.getPatronById(patronId)).thenReturn(patron);
        doNothing().when(patronService).updatePatronBorrowingRecord(patron);
        doNothing().when(bookService).updateBookBorrowingRecord(book);
        ArgumentCaptor<BorrowingRecord> captor = ArgumentCaptor.forClass(BorrowingRecord.class);
        // Act
        underTest.borrowBook(bookId, patronId);
        // Assert
        verify(borrowingRecordRepository).save(captor.capture());
        BorrowingRecord actual = captor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getPatron().getId()).isEqualTo(patronId);
        assertThat(actual.getBook().getId()).isEqualTo(bookId);
        assertThat(actual.getBorrowDate()).isEqualTo(borrowDate);
        assertThat(actual.getReturnDate()).isNull();
    }
    @Test
    void borrowBookWillThrowExceptionWhenBookIsBorrowed() {
        // Arrange
        long bookId = faker.number().randomDigit();
        long patronId = faker.number().randomDigit();
        Patron patron = Patron.builder()
                .id(patronId)
                .build();
        Book book =  Book.builder()
                .id(bookId)
                .build();
        LocalDate borrowDate = LocalDate.now();
        BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                .id(bookId)
                .borrowDate(borrowDate)
                .patron(patron)
                .book(book)
                .build();
        when(borrowingRecordRepository.findAll()).thenReturn(List.of(borrowingRecord));
        when(bookService.getBookById(bookId)).thenReturn(book);
        when(patronService.getPatronById(patronId)).thenReturn(patron);
        // Act
        // Assert
        assertThatThrownBy(() -> underTest.borrowBook(bookId, patronId))
                .isInstanceOf(InValidRequestException.class)
                .hasMessage("Book is already borrowed");
    }

    @Test
    void returnBookWillReturnTheBook() {
        // Arrange
        long bookId = faker.number().randomDigit();
        long patronId = faker.number().randomDigit();
        Patron patron = Patron.builder()
                .id(patronId)
                .build();
        Book book =  Book.builder()
                .id(bookId)
                .build();
        LocalDate borrowDate = LocalDate.now();
        BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                .id(bookId)
                .borrowDate(borrowDate)
                .patron(patron)
                .book(book)
                .build();
        when(borrowingRecordRepository.findAll()).thenReturn(List.of(borrowingRecord));
        when(patronService.getPatronById(patronId)).thenReturn(patron);
        ArgumentCaptor<BorrowingRecord> captor = ArgumentCaptor.forClass(BorrowingRecord.class);
        // Act
        underTest.returnBook(bookId, patronId);
        // Assert
        verify(borrowingRecordRepository).save(captor.capture());
        BorrowingRecord actual = captor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getPatron().getId()).isEqualTo(patronId);
        assertThat(actual.getBook().getId()).isEqualTo(bookId);
        assertThat(actual.getBorrowDate()).isEqualTo(borrowDate);
        assertThat(actual.getReturnDate()).isNotNull();
    }
    @Test
    void returnBookWillThrowExceptionWhenBookIsNotBorrowed() {
        // Arrange
        long bookId = faker.number().randomDigit();
        long patronId = faker.number().randomDigit();
        Patron patron = Patron.builder()
                .id(patronId)
                .build();
        Book book =  Book.builder()
                .id(bookId)
                .build();
        LocalDate borrowDate = LocalDate.now();
        BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                .id(bookId)
                .borrowDate(borrowDate)
                .returnDate(borrowDate)
                .patron(patron)
                .book(book)
                .build();
        when(borrowingRecordRepository.findAll()).thenReturn(List.of(borrowingRecord));
        when(patronService.getPatronById(patronId)).thenReturn(patron);
        // Act
        // Assert
        assertThatThrownBy(() -> underTest.returnBook(bookId, patronId))
                .isInstanceOf(InValidRequestException.class)
                .hasMessage("Book is not borrowed");
    }
    @Test
    void returnBookWillThrowExceptionWhenBookIsNotBorrowedByThisPatron() {
        // Arrange
        long bookId = faker.number().randomDigit();
        long patronId = faker.number().randomDigit();
        Patron diffPatron = Patron.builder()
                .id(patronId)
                .build();
        Patron patron = Patron.builder()
                .id(patronId + 1)
                .build();
        Book book =  Book.builder()
                .id(bookId)
                .build();
        LocalDate borrowDate = LocalDate.now();
        BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                .id(bookId)
                .borrowDate(borrowDate)
                .returnDate(borrowDate)
                .patron(patron)
                .book(book)
                .build();
        when(borrowingRecordRepository.findAll()).thenReturn(List.of(borrowingRecord));
        when(patronService.getPatronById(patronId)).thenReturn(diffPatron);
        // Act
        // Assert
        assertThatThrownBy(() -> underTest.returnBook(bookId, patronId))
                .isInstanceOf(InValidRequestException.class)
                .hasMessage("Book is not borrowed");
    }
}