package com.example.Library.Management.System.repository;

import com.example.Library.Management.System.entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, Long> {
}
