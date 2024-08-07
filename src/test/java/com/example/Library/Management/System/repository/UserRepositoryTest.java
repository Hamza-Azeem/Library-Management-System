package com.example.Library.Management.System.repository;

import com.example.Library.Management.System.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// Comment command line runner method when testing in main function
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    void findByUsername() {
        // Arrange
        User user = new User("HAMZA", "PASSWORD");
        userRepository.save(user);
        // Act
        Optional<User> userOptional = userRepository.findByUsername("HAMZA");
        // Assert
        assertThat(userOptional).isPresent();
        assertThat(userOptional.get().getUsername()).isEqualTo("HAMZA");
        assertThat(userOptional.get().getPassword()).isEqualTo("PASSWORD");
    }
    @Test
    void findByUsernameWillReturnNull() {
        // Act
        Optional<User> userOptional = userRepository.findByUsername("HAMZA");
        // Assert
        assertThat(userOptional).isEmpty();
    }
}