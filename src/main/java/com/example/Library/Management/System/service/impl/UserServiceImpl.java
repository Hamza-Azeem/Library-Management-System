package com.example.Library.Management.System.service.impl;

import com.example.Library.Management.System.entity.Patron;
import com.example.Library.Management.System.entity.Role;
import com.example.Library.Management.System.entity.User;
import com.example.Library.Management.System.exception.DuplicateResourceException;
import com.example.Library.Management.System.exception.ResourceNotFoundException;
import com.example.Library.Management.System.models.RegistrationRequest;

import com.example.Library.Management.System.repository.UserRepository;
import com.example.Library.Management.System.service.RoleService;
import com.example.Library.Management.System.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public User findUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new ResourceNotFoundException(String.format("User: %s not found", username));
        }
        return user.get();
    }

    @Override
    public void saveUser(RegistrationRequest registrationRequest) {
        if(userRepository.existsByUsername(registrationRequest.getUsername())){
            throw new DuplicateResourceException(String.format("User: %s already exists", registrationRequest.getUsername()));
        }
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        Role role = roleService.getRoleByAuthority("USER");
        if( role == null){
            role = roleService.createRole(new Role("USER"));
        }
        user.addRole(role);
        userRepository.save(user);
    }

    @Override
    public void addPatronToUser(User user, Patron patron) {
        for(Role role : user.getRoles()){
            if(role.getAuthority().equals("ADMIN") || role.getAuthority().equals("MANAGER")){
                return;
            }
        }
        Role role = roleService.getRoleByAuthority("PATRON");
        if( role == null){
            role = roleService.createRole(new Role("PATRON"));
        }
        user.addRole(role);
        user.setPatron(patron);
        userRepository.save(user);
    }
}
