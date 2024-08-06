package com.example.Library.Management.System.entity;
import jakarta.persistence.*;
import lombok.*;


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
}
