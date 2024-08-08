package com.example.Library.Management.System.controller;

import com.example.Library.Management.System.service.BorrowingRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BorrowingRecordController {
    private final BorrowingRecordService borrowingRecordService;

    public BorrowingRecordController(BorrowingRecordService borrowingRecordService) {
        this.borrowingRecordService = borrowingRecordService;
    }
    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<?> allowPatronToBorrow(@PathVariable Long bookId, @PathVariable Long patronId){
        borrowingRecordService.borrowBook(bookId, patronId);
        return ResponseEntity.ok().body("Book borrowed successfully.");
    }
    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<?> recordReturnOfBook(@PathVariable Long bookId, @PathVariable Long patronId){
        borrowingRecordService.returnBook(bookId, patronId);
        return ResponseEntity.ok().body("Book returned successfully.");
    }
}
