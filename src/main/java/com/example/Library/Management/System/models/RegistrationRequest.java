package com.example.Library.Management.System.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@Builder
public class RegistrationRequest {
    @NotBlank(message = "Username is required")
    @Length(min = 8, max = 16, message = "Username can't be less than 8 or more than 16 characters")
    private String username;
    @NotBlank(message = "Password is required")
    @Length(min = 8, max = 24, message = "Password can't be less than 8 or more than 24 characters")
    private String password;
}
