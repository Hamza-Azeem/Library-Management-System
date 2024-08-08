package com.example.Library.Management.System.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    @NotBlank(message = "Title field is required")
    private String title;
    @NotBlank(message = "Author field is required")
    private String author;
    @NotBlank(message = "isbn field is required")
    private String isbn;
    @Min(value = 1000, message = "publication year can't be less than 1000")
    @Max(value = 2026, message = "publication year can't be more than 2026")
    @NotNull(message = "Publication year field is required")
    private int publicationYear;
}
