package com.example.Library.Management.System.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RegistrationRequest {
    private String username;
    private String password;
}
