package com.example.Library.Management.System.service;

import com.example.Library.Management.System.dto.PatronDto;
import com.example.Library.Management.System.entity.Patron;

import java.util.List;


public interface PatronService {
    public List<PatronDto> getAllPatrons();
    public Patron getPatronById(long id);
    public PatronDto getPatronDtoById(long id);
    public void savePatron(PatronDto patron);
    public PatronDto updatePatron(long id, PatronDto patron);
    public void deletePatron(long id);
    public void updatePatronBorrowingRecord(Patron patron);

}
