package com.example.Library.Management.System.service.impl;

import com.example.Library.Management.System.entity.Role;
import com.example.Library.Management.System.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    private RoleServiceImpl underTest;
    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        underTest = new RoleServiceImpl(roleRepository);
    }

    @Test
    void createRole() {
        // Arrange
        Role role = new Role("TEST");
        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);
        // Act
        underTest.createRole(role);
        // Assert
        verify(roleRepository).save(captor.capture());
        assertThat(captor.getValue().getAuthority()).isEqualTo(role.getAuthority());
    }

    @Test
    void getRoleByAuthority() {
        // Arrange

        // Act
        underTest.getRoleByAuthority("TEST");
        // Assert
        verify(roleRepository).findByAuthority("TEST");
    }
}