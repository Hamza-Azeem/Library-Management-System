package com.example.Library.Management.System.service.impl;

import com.example.Library.Management.System.dto.PatronDto;
import com.example.Library.Management.System.entity.Patron;
import com.example.Library.Management.System.entity.Role;
import com.example.Library.Management.System.entity.User;
import com.example.Library.Management.System.exception.DuplicateResourceException;
import com.example.Library.Management.System.exception.InValidRequestException;
import com.example.Library.Management.System.repository.PatronRepository;
import com.example.Library.Management.System.service.UserService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PatronServiceImplTest {
    private PatronServiceImpl underTest;
    @Mock
    private PatronRepository patronRepository;
    @Mock
    private UserService userService;
    @Mock
    private Authentication authentication;
    @Mock
    SecurityContext securityContext;
    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        underTest = new PatronServiceImpl(patronRepository, userService);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void getAllPatrons() {
        // Arrange

        // Act
        underTest.getAllPatrons();
        // Assert
        verify(patronRepository).findAll();
    }

    @Test
    void getPatronById() {
        // Arrange
        Role role = new Role("PATRON");
        String username = "HAMZA";
        String password = "PASSWORD";
        User user = new User(1L, username, password);
        user.addRole(role);
        long id = faker.number().randomDigit();
        String name = faker.name().fullName();
        String phoneNumber = faker.phoneNumber().cellPhone();
        String address = faker.address().streetAddressNumber();
        String email = faker.internet().emailAddress();
        Patron patron = new Patron(
                id,
                name,
                phoneNumber,
                address,
                email,
                user
        );
        user.setPatron(patron);
        when(authentication.getName()).thenReturn(username);
        when(userService.findUserByUsername(username)).thenReturn(user);
        when(patronRepository.findById(id)).thenReturn(Optional.of(patron));
        // Act
        Patron actual = underTest.getPatronById(id);
        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getAddress()).isEqualTo(address);
    }
    @Test
    void getPatronByIdAccessedByAdmin() {
        // Arrange
        Role userRole = new Role("USER");
        Role adminRole = new Role("ADMIN");
        String username = "HAMZA";
        String password = "PASSWORD";
        User admin = new User(1L, username, password);
        admin.addRole(adminRole);
        User user = new User(2L, "user", "password");
        user.addRole(userRole);
        long id = faker.number().randomDigit();
        String name = faker.name().fullName();
        String phoneNumber = faker.phoneNumber().cellPhone();
        String address = faker.address().streetAddressNumber();
        String email = faker.internet().emailAddress();
        Patron patron = new Patron(
                id,
                name,
                phoneNumber,
                address,
                email,
                user
        );
        when(authentication.getName()).thenReturn(username);
        when(userService.findUserByUsername(username)).thenReturn(admin);
        when(patronRepository.findById(id)).thenReturn(Optional.of(patron));
        // Act
        Patron actual = underTest.getPatronById(id);
        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getAddress()).isEqualTo(address);
    }
    @Test
    void getPatronByIdWillThrowExceptionWhenAccessedByDifferentUser() {
        // Arrange
        Role userRole = new Role("USER");
        String username = "HAMZA";
        String password = "PASSWORD";
        User diffUser = new User(1L, username, password);
        diffUser.addRole(userRole);
        User user = new User(2L, "user", "password");
        user.addRole(userRole);
        long id = faker.number().randomDigit();
        String name = faker.name().fullName();
        String phoneNumber = faker.phoneNumber().cellPhone();
        String address = faker.address().streetAddressNumber();
        String email = faker.internet().emailAddress();
        Patron patron = new Patron(
                id,
                name,
                phoneNumber,
                address,
                email,
                user
        );
        user.setPatron(patron);
        when(authentication.getName()).thenReturn(username);
        when(userService.findUserByUsername(username)).thenReturn(diffUser);
        when(patronRepository.findById(id)).thenReturn(Optional.of(patron));
        // Act
        // Assert
        assertThatThrownBy(()-> underTest.getPatronDtoById(id))
                .isInstanceOf(InValidRequestException.class)
                .hasMessage(String.format("Patron id %s is not valid", id));
    }

 @Test
    void getPatronByIdWillThrowExceptionWhenAccessedByDifferentPatron() {
        // Arrange
        Role userRole = new Role("USER");
        String username = "HAMZA";
        String password = "PASSWORD";
        User diffUser = new User(1L, username, password);
        diffUser.addRole(userRole);
        User user = new User(2L, "user", "password");
        user.addRole(userRole);
        long id = faker.number().randomDigit();
        String name = faker.name().fullName();
        String phoneNumber = faker.phoneNumber().cellPhone();
        String address = faker.address().streetAddressNumber();
        String email = faker.internet().emailAddress();
        Patron patron = new Patron(
                id,
                name,
                phoneNumber,
                address,
                email,
                user
        );
        Patron diffPatron = new Patron(
                id+1,
                name,
                phoneNumber,
                address,
                email,
                diffUser
        );
        user.setPatron(patron);
        diffUser.setPatron(diffPatron);
        when(authentication.getName()).thenReturn(username);
        when(userService.findUserByUsername(username)).thenReturn(diffUser);
        when(patronRepository.findById(id)).thenReturn(Optional.of(patron));
        // Act
        // Assert
        assertThatThrownBy(()-> underTest.getPatronDtoById(id))
                .isInstanceOf(InValidRequestException.class)
                .hasMessage(String.format("Patron id %s is not valid", id));
    }


    @Test
    void savePatronWillSavePatron() {
        // Arrange
        String username = "HAMZA";
        String password = "PASSWORD";
        User user = new User(1L, username, password);
        String email = faker.internet().emailAddress();
        when(authentication.getName()).thenReturn(username);
        when(userService.findUserByUsername(username)).thenReturn(user);
        when(patronRepository.existsByEmail(email)).thenReturn(false);
        ArgumentCaptor<Patron> captor = ArgumentCaptor.forClass(Patron.class);
        // Act
        underTest.savePatron(PatronDto.builder().build());
        // Assert
        verify(patronRepository).save(captor.capture());
        assertThat(captor.getValue()).isNotNull();
    }
    @Test
    void savePatronWillThrowExceptionIfPatronAlreadyExists() {
        // Arrange
        String username = "HAMZA";
        String password = "PASSWORD";
        User user = new User(1L, username, password);
        long id = faker.number().randomDigit();
        String name = faker.name().fullName();
        String phoneNumber = faker.phoneNumber().cellPhone();
        String address = faker.address().streetAddressNumber();
        String email = faker.internet().emailAddress();
        Patron patron = new Patron(
                id,
                name,
                phoneNumber,
                address,
                email,
                user
        );
        PatronDto patronDto = new PatronDto(
                name,
                phoneNumber,
                address,
                email
        );
        user.setPatron(patron);
        when(authentication.getName()).thenReturn(username);
        when(userService.findUserByUsername(username)).thenReturn(user);
        when(patronRepository.existsByEmail(email)).thenReturn(false);
        // Act
        // Assert
        assertThatThrownBy(()-> underTest.savePatron(patronDto))
                .isInstanceOf(InValidRequestException.class)
                .hasMessage("Invalid way to update your patron.");
    }
    @Test
    void savePatronWillThrowExceptionIfPatronEmailAlreadyExists() {
        // Arrange
        String username = "HAMZA";
        String password = "PASSWORD";
        User user = new User(1L, username, password);
        long id = faker.number().randomDigit();
        String name = faker.name().fullName();
        String phoneNumber = faker.phoneNumber().cellPhone();
        String address = faker.address().streetAddressNumber();
        String email = faker.internet().emailAddress();
        PatronDto patronDto = new PatronDto(
                name,
                phoneNumber,
                address,
                email
        );
        when(authentication.getName()).thenReturn(username);
        when(userService.findUserByUsername(username)).thenReturn(user);
        when(patronRepository.existsByEmail(email)).thenReturn(true);
        // Act
        // Assert
        assertThatThrownBy(()-> underTest.savePatron(patronDto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage(String.format("Patron already exists with email %s", patronDto.getEmail()));
    }

    @Test
    void updatePatron() {
        // Arrange
        Role role = new Role("PATRON");
        String username = "HAMZA";
        String password = "PASSWORD";
        User user = new User(1L, username, password);
        user.addRole(role);

        long id = faker.number().randomDigit();
        String name = faker.name().fullName();
        String phoneNumber = faker.phoneNumber().cellPhone();
        String address = faker.address().streetAddressNumber();
        String email = faker.internet().emailAddress();
        Patron patron = new Patron(id, name, phoneNumber, address, email, user);
        user.setPatron(patron);

        PatronDto patronDto = new PatronDto(
                name + "updated",
                phoneNumber + "updated",
                address + "updated",
                email + "updated"
        );

        when(authentication.getName()).thenReturn(username);
        when(userService.findUserByUsername(username)).thenReturn(user);
        when(patronRepository.findById(id)).thenReturn(Optional.of(patron));
        when(patronRepository.save(patron)).thenReturn(patron);
        // Act
        PatronDto actual = underTest.updatePatron(id, patronDto);
        // Assert
        assertThat(actual.getName()).isEqualTo(patronDto.getName());
        assertThat(actual.getPhoneNumber()).isEqualTo(patronDto.getPhoneNumber());
        assertThat(actual.getAddress()).isEqualTo(patronDto.getAddress());
        assertThat(actual.getEmail()).isEqualTo(patronDto.getEmail());
    }
    @Test
    void updatePatronWillThrowExceptionIfPatronEmailAlreadyExists() {
        // Arrange
        Role role = new Role("PATRON");
        String username = "HAMZA";
        String password = "PASSWORD";
        User user = new User(1L, username, password);
        user.addRole(role);
        long id = faker.number().randomDigit();
        String name = faker.name().fullName();
        String phoneNumber = faker.phoneNumber().cellPhone();
        String address = faker.address().streetAddressNumber();
        String email = faker.internet().emailAddress();
        Patron patron = new Patron(id, name, phoneNumber, address, email, user);
        PatronDto patronDto = new PatronDto(
                name + "updated",
                phoneNumber + "updated",
                address + "updated",
                email + "updated"
        );
        when(authentication.getName()).thenReturn(username);
        when(userService.findUserByUsername(username)).thenReturn(user);
        when(patronRepository.findById(id)).thenReturn(Optional.of(patron));
        when(patronRepository.save(patron)).thenReturn(patron);
        when(patronRepository.existsByEmail(patronDto.getEmail())).thenReturn(true);
        // Act
        // Assert
        assertThatThrownBy(()-> underTest.savePatron(patronDto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage(String.format("Patron already exists with email %s", patronDto.getEmail()));
    }

    @Test
    void deletePatron() {
        // Arrange
        Role role = new Role("PATRON");
        String username = "HAMZA";
        String password = "PASSWORD";
        User user = new User(1L, username, password);
        user.addRole(role);
        long id = faker.number().randomDigit();
        String name = faker.name().fullName();
        String phoneNumber = faker.phoneNumber().cellPhone();
        String address = faker.address().streetAddressNumber();
        String email = faker.internet().emailAddress();
        Patron patron = new Patron(
                id,
                name,
                phoneNumber,
                address,
                email,
                user
        );
        user.setPatron(patron);
        when(authentication.getName()).thenReturn(username);
        when(userService.findUserByUsername(username)).thenReturn(user);
        when(patronRepository.findById(id)).thenReturn(Optional.of(patron));
        // Act
        underTest.deletePatron(id);
        // Assert
        verify(patronRepository).delete(patron);
    }
    @Test
    public void updatePatronBorrowingRecord(){
        Patron patron = Patron.builder().build();
        underTest.updatePatronBorrowingRecord(patron);
        verify(patronRepository).save(patron);
    }
}