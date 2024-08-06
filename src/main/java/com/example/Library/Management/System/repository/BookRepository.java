package com.example.Library.Management.System.repository;

import com.example.Library.Management.System.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
