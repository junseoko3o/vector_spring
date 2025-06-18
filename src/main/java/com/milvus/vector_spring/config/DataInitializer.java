package com.milvus.vector_spring.config;

import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class DataInitializer {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Optional<User> existEmail = userRepository.findByEmail(adminEmail);
            if (existEmail.isEmpty()) {
                User admin = User.builder()
                        .id(1L)
                        .email(adminEmail)
                        .username("ADMIN")
                        .password(passwordEncoder.encode(adminPassword))
                        .role("ROLE_ADMIN")
                        .build();
                userRepository.save(admin);
                System.out.println("✅ Admin account created successfully.");
            } else {
                System.out.println("ℹ️ Admin account already exists.");
            }
        };
    }
}