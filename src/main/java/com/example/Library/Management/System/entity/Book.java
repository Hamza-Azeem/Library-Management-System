package com.example.Library.Management.System.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "book")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// ● Book: Includes attributes like ID, title, author, publication year, ISBN, etc.
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String isbn;
    @Column(name = "publication_year")
    private int publicationYear;
    @OneToMany(mappedBy = "book")
    private List<BorrowingRecord> borrowingRecords;


    public Book(Long id, String title, String author, String isbn, int publicationYear) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
    }

    public void addBorrowingRecord(BorrowingRecord borrowingRecord) {
        if (this.borrowingRecords == null) {
            borrowingRecords = new ArrayList<>();
        }
        borrowingRecords.add(borrowingRecord);
    }
}
