package com.example.Library.Management.System.service.impl;

import com.example.Library.Management.System.entity.Book;
import com.example.Library.Management.System.entity.BorrowingRecord;
import com.example.Library.Management.System.entity.Patron;
import com.example.Library.Management.System.exception.InValidRequestException;
import com.example.Library.Management.System.repository.BorrowingRecordRepository;
import com.example.Library.Management.System.service.BookService;
import com.example.Library.Management.System.service.BorrowingRecordService;
import com.example.Library.Management.System.service.PatronService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingRecordServiceImpl implements BorrowingRecordService {
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final PatronService patronService;
    private final BookService bookService;

    public BorrowingRecordServiceImpl(BorrowingRecordRepository borrowingRecordRepository, PatronService patronService, BookService bookService) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.patronService = patronService;
        this.bookService = bookService;
    }

    @Override
    @Transactional
    public void borrowBook(Long bookId, Long patronId) {
        Book book = bookService.getBookById(bookId);
        Patron patron = patronService.getPatronById(patronId);
        List<BorrowingRecord> borrowingRecords = borrowingRecordRepository.findAll();
        // check if the book was borrowed by someone.
        for (BorrowingRecord borrowingRecord : borrowingRecords) {
            if (borrowingRecord.getBook().getId() == bookId && borrowingRecord.getReturnDate() == null) {
                throw new InValidRequestException("Book is already borrowed");
            }
        }
        BorrowingRecord borrowingRecord = new BorrowingRecord(
                patron,
                book,
                LocalDate.now()
        );
        borrowingRecordRepository.save(borrowingRecord);
        patron.addBorrowingRecord(borrowingRecord);
        book.addBorrowingRecord(borrowingRecord);
        patronService.updatePatronBorrowingRecord(patron);
        bookService.updateBookBorrowingRecord(book);
    }

    @Override
    public void returnBook(Long bookId, Long patronId) {
        patronService.getPatronById(patronId);
        BorrowingRecord borrowingRecord = borrowingRecordRepository.findAll()
                .stream().filter(br -> br.getBook().getId() == bookId && br.getReturnDate() == null)
                .findFirst().orElse(null);
        if (borrowingRecord == null || borrowingRecord.getPatron().getId() != patronId) {
            throw new InValidRequestException("Book is not borrowed");
        }
        borrowingRecord.setReturnDate(LocalDate.now());
        borrowingRecordRepository.save(borrowingRecord);
    }
}
