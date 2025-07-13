package com.milvus.vector_spring.auth;

import com.milvus.vector_spring.auth.dto.UserLoginRequestDto;
import com.milvus.vector_spring.auth.dto.UserLoginResponseDto;
import com.milvus.vector_spring.config.jwt.JwtTokenProvider;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("login@example.com")
                .username("login")
                .password(passwordEncoder.encode("password"))
                .build();
        userRepository.save(user);
    }

    @Test
    void login_WithCorrectCredentials_ReturnsAccessToken() {
        UserLoginRequestDto dto = UserLoginRequestDto.builder()
                .email("login@example.com")
                .password("password")
                .build();

        UserLoginResponseDto response = authService.login(dto);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("login@example.com");
        assertThat(response.getAccessToken()).isNotEmpty();
    }
}
