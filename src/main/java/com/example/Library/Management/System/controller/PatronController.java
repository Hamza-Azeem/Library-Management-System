package com.example.Library.Management.System.controller;

import com.example.Library.Management.System.dto.PatronDto;
import com.example.Library.Management.System.service.PatronService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patron")
public class PatronController {
    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(patronService.getAllPatrons());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findPatronById(@PathVariable Long id) {
        return ResponseEntity.ok(patronService.getPatronDtoById(id));
    }
    @PostMapping
    public ResponseEntity<?> savePatron(@Valid @RequestBody PatronDto patronDto) {
        patronService.savePatron(patronDto);
        return new ResponseEntity<>("Patron created successfully.", HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatron(@PathVariable Long id, @Valid @RequestBody PatronDto patronDto) {
        return ResponseEntity.ok(patronService.updatePatron(id, patronDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.notFound().build();
    }

}
