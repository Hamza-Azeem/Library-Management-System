package com.example.Library.Management.System.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Error {
    private String message;
    private HttpStatus statusCode;
    private LocalDateTime timestamp;
}
