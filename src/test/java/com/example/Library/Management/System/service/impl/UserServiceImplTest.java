package com.example.Library.Management.System.service.impl;

import com.example.Library.Management.System.entity.Role;
import com.example.Library.Management.System.entity.User;
import com.example.Library.Management.System.exception.ResourceNotFoundException;
import com.example.Library.Management.System.models.RegistrationRequest;
import com.example.Library.Management.System.repository.UserRepository;
import com.example.Library.Management.System.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private UserServiceImpl underTest;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleService roleService;
    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepository, passwordEncoder, roleService);
    }
    @Test
    void findUserByUsername() {
        // Arrange
        when(userRepository.findByUsername("TEST")).thenReturn(Optional.of(new User()));
        // Act
        underTest.findUserByUsername("TEST");
        // Assert
        verify(userRepository).findByUsername("TEST");
    }
   @Test
    void findUserByUsernameWillThrowExceptionWhenUsernameNotFound() {
        // Arrange
       String username = "TEST";
       when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        // Act
        // Assert
        assertThatThrownBy(() -> underTest.findUserByUsername(username))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("User: %s not found", username));
    }

    @Test
    void saveUser() {
        // Arrange
        String username = "HAMZA";
        String password = "PASSWORD";
        RegistrationRequest registrationRequest = new RegistrationRequest(
                username,
                password);
        Role role = new Role(1L, "USER");
        when(roleService.getRoleByAuthority("USER")).thenReturn(role);
        when(passwordEncoder.encode(password)).thenReturn(password + "encoded");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        // Act
        underTest.saveUser(registrationRequest);
        // Assert
        verify(userRepository).save(captor.capture());
        User actual = captor.getValue();
        assertThat(actual.getUsername()).isEqualTo(username);
        assertThat(actual.getPassword()).isEqualTo(password+"encoded");
        assertThat(actual.getPatron()).isEqualTo(null);
        assertThat(actual.getRoles().size()).isEqualTo(1);
    }
}