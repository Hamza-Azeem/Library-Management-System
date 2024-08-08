package com.example.Library.Management.System.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http ) throws  Exception{
        http.csrf().disable();
        http.httpBasic();
        http.authorizeHttpRequests(
                auth -> {
                    auth.requestMatchers(HttpMethod.GET, "/api/books/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/books/**").hasAnyAuthority("ADMIN", "MANAGER");
                    auth.requestMatchers(HttpMethod.PUT, "/api/books/**").hasAnyAuthority("ADMIN", "MANAGER");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/books/**").hasAuthority("MANAGER");
                    auth.requestMatchers(HttpMethod.GET, "/api/patron/**").hasAnyAuthority( "PATRON", "ADMIN", "MANAGER");
                    auth.requestMatchers(HttpMethod.POST, "/api/patron/**").hasAnyAuthority("USER", "ADMIN", "MANAGER");
                    auth.requestMatchers(HttpMethod.PUT, "/api/patron/**").hasAnyAuthority("PATRON", "ADMIN", "MANAGER");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/patron/**").hasAuthority("MANAGER");
                    auth.requestMatchers(HttpMethod.POST, "/api/borrow/**").hasAuthority("PATRON");
                    auth.requestMatchers(HttpMethod.PUT, "/api/return/**").hasAuthority("PATRON");
                    auth.anyRequest().authenticated();
                }
        );
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
