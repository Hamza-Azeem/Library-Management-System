package com.example.Library.Management.System.security;

import com.example.Library.Management.System.entity.User;
import com.example.Library.Management.System.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomizedUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomizedUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }
        User user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(
                username,
                user.getPassword(),
                user.getRoles()
        );
    }
}
