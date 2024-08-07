package com.example.Library.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PatronDto {
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
}
