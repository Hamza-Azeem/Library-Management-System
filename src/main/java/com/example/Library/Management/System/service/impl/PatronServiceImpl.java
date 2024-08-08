package com.example.Library.Management.System.service.impl;

import com.example.Library.Management.System.dto.PatronDto;
import com.example.Library.Management.System.entity.Patron;
import com.example.Library.Management.System.entity.Role;
import com.example.Library.Management.System.entity.User;
import com.example.Library.Management.System.exception.InValidRequestException;
import com.example.Library.Management.System.exception.ResourceNotFoundException;
import com.example.Library.Management.System.repository.PatronRepository;
import com.example.Library.Management.System.service.PatronService;
import com.example.Library.Management.System.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.Library.Management.System.mapper.PatronMapper.conertToPatron;
import static com.example.Library.Management.System.mapper.PatronMapper.convertToPatronDto;

@Service
public class PatronServiceImpl implements PatronService {
    private final PatronRepository patronRepository;
    private final UserService userService;

    public PatronServiceImpl(PatronRepository patronRepository, UserService userService) {
        this.patronRepository = patronRepository;
        this.userService = userService;
    }

    @Override
    public List<PatronDto> getAllPatrons() {
        return patronRepository.findAll().stream().map(patron -> convertToPatronDto(patron))
                .collect(Collectors.toList());
    }

    @Override
    public Patron getPatronById(long id) {
        return findById(id);
    }

    @Override
    public PatronDto getPatronDtoById(long id) {
        return convertToPatronDto(findById(id));
    }

    private Patron findById(long id) {
        if(!isSamePatronOrAdmin(id)){
            throw new InValidRequestException(String.format("Patron id %s is not valid", id));
        }
        return patronRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Patron with id %s not found", id)));
    }

    private boolean isSamePatronOrAdmin(long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        for(Role role: user.getRoles()){
            if(role.getAuthority().equals("ADMIN") || role.getAuthority().equals("MANAGER"))
                return true;
        }
        if(user.getPatron() == null)
            return false;
        return user.getPatron().getId() == id;
    }


    @Override
    @Transactional
    public void savePatron(PatronDto patronDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        if(user.getPatron() != null){
            throw new InValidRequestException("Invalid way to update your patron.");
        }
        Patron patron = patronRepository.save(conertToPatron(patronDto));
        userService.addPatronToUser(user, patron);
    }

    @Override
    public PatronDto updatePatron(long id, PatronDto patronDto) {
        if(!isSamePatronOrAdmin(id)){
            throw new InValidRequestException(String.format("Patron id %s is not valid", id));
        }
        Patron patron = findById(id);
        if(!isPatronUpdated(patron, patronDto)){
            throw new InValidRequestException("No updates were found!");
        }
        return convertToPatronDto(patronRepository.save(patron));
    }
    private boolean isPatronUpdated(Patron patron, PatronDto patronDto) {
        boolean updated = false;
        if(patronDto.getName() != null && !patronDto.getName().equals(patron.getName())){
            patron.setName(patronDto.getName());
            updated = true;
        }
        if(patronDto.getEmail() != null && !patronDto.getEmail().equals(patron.getEmail())){
            patron.setEmail(patronDto.getEmail());
            updated = true;
        }
        if(patronDto.getAddress() != null && !patronDto.getAddress().equals(patron.getAddress())){
            patron.setAddress(patronDto.getAddress());
            updated = true;
        }
        if(patronDto.getPhoneNumber() != null && !patronDto.getPhoneNumber().equals(patron.getPhoneNumber())){
            patron.setPhoneNumber(patronDto.getPhoneNumber());
            updated = true;
        }
        return updated;
    }

    @Override
    public void deletePatron(long id) {
        Patron patron = findById(id);
        patronRepository.delete(patron);
    }

    @Override
    public void updatePatronBorrowingRecord(Patron patron) {
        patronRepository.save(patron);
    }


}
