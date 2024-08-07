package com.example.Library.Management.System;

import com.example.Library.Management.System.entity.Role;
import com.example.Library.Management.System.entity.User;
import com.example.Library.Management.System.repository.RoleRepository;
import com.example.Library.Management.System.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LibraryManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementSystemApplication.class, args);
	}

	// Comment it while testing
	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository,
											   RoleRepository roleRepository,
											   PasswordEncoder passwordEncoder) {
		return args -> {
			if(roleRepository.findByAuthority("ADMIN").isEmpty()) {
				Role role = new Role("ADMIN");
				roleRepository.save(role);
			}
			if(userRepository.findByUsername("admin").isEmpty()) {
				User user = new User();
				user.setUsername("admin");
				user.setPassword(passwordEncoder.encode("admin"));
				user.addRole(roleRepository.findByAuthority("ADMIN").get());
				userRepository.save(user);
			}
		};
	}

}
