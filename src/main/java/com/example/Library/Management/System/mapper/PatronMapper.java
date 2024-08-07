package com.example.Library.Management.System.mapper;

import com.example.Library.Management.System.dto.PatronDto;
import com.example.Library.Management.System.entity.Patron;

public class PatronMapper {
    public static Patron conertToPatron(PatronDto patronDto){
        return Patron.builder()
                .name(patronDto.getName())
                .email(patronDto.getEmail())
                .address(patronDto.getAddress())
                .phoneNumber(patronDto.getPhoneNumber())
                .build();
    }
    public static PatronDto convertToPatronDto(Patron patron){
        return PatronDto.builder()
                .name(patron.getName())
                .email(patron.getEmail())
                .address(patron.getAddress())
                .phoneNumber(patron.getPhoneNumber())
                .build();
    }
}
