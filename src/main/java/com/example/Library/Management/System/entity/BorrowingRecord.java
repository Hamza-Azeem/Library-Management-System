package com.example.Library.Management.System.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


// ● Borrowing Record: Tracks the association between books and patrons,
//including borrowing and return dates.
@Entity
@Table(name = "borrowing_record")
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
public class BorrowingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Patron patron;
    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowingRecord(Patron patron, Book book, LocalDate borrowDate) {
        this.patron = patron;
        this.book = book;
        this.borrowDate = borrowDate;
    }
}
