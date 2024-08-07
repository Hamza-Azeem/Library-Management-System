package com.example.Library.Management.System.repository;

import com.example.Library.Management.System.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
// Comment command line runner method when testing in main function
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByAuthority() {
        // Arrange
        Role role = new Role("TEST");
        roleRepository.save(role);
        // Act
        Optional<Role> actual = roleRepository.findByAuthority("TEST");
        // Assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getAuthority()).isEqualTo("TEST");
    }
    @Test
    void findByAuthorityNull() {
        Optional<Role> actual = roleRepository.findByAuthority("TEST");
        assertThat(actual).isEmpty();
    }
}