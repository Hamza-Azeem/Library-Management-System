package com.example.Library.Management.System.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PatronDto {
    @NotBlank(message = "Name field is required")
    private String name;
    @NotBlank(message = "Phone number field is required")
    private String phoneNumber;
    @NotBlank(message = "Address field is required")
    private String address;
    @NotNull(message = "Email field is required")
    @Email(message = "Invalid email format")
    private String email;
}
