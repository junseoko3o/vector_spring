package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import com.milvus.vector_spring.user.dto.UserUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.milvus.vector_spring.common.apipayload.status.ErrorStatus.DUPLICATE_USER_EMAIL;
import static com.milvus.vector_spring.common.apipayload.status.ErrorStatus.NOT_FOUND_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserCommandServiceTest {

    @Autowired
    private UserCommandServiceImpl userCommandService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void signUpUser_WhenEmailNotDuplicate_SavesUser() {
        UserSignUpRequestDto dto = UserSignUpRequestDto.builder()
                .email("new@example.com")
                .username("username")
                .password("password")
                .build();

        User savedUser = userCommandService.signUpUser(dto);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(dto.getEmail());
        assertThat(savedUser.getUsername()).isEqualTo(dto.getUsername());
        assertThat(passwordEncoder.matches("password", savedUser.getPassword())).isTrue();
    }

    @Test
    void signUpUser_WhenEmailDuplicate_ThrowsException() {
        User user = User.builder()
                .email("duplicate@example.com")
                .password("encoded")
                .username("dup")
                .build();
        userRepository.save(user);

        UserSignUpRequestDto dto = UserSignUpRequestDto.builder()
                .email("duplicate@example.com")
                .build();

        CustomException exception = assertThrows(CustomException.class, () -> {
            userCommandService.signUpUser(dto);
        });

        assertThat(exception.getBaseCode()).isEqualTo(DUPLICATE_USER_EMAIL);
    }

    @Test
    void updateUser_WhenUserExistsAndEmailNotChanged_UpdatesUser() {
        User user = User.builder()
                .email("user@example.com")
                .password("pass")
                .username("before")
                .build();
        userRepository.save(user);

        UserUpdateRequestDto dto = UserUpdateRequestDto.builder()
                .email("user@example.com")
                .username("after")
                .build();

        User updated = userCommandService.updateUser(user.getId(), dto);

        assertThat(updated.getUsername()).isEqualTo("after");
    }

    @Test
    void updateUser_WhenUserNotFound_ThrowsException() {
        UserUpdateRequestDto dto = UserUpdateRequestDto.builder()
                .email("notfound@example.com")
                .build();

        CustomException exception = assertThrows(CustomException.class, () -> {
            userCommandService.updateUser(999L, dto);
        });

        assertThat(exception.getBaseCode()).isEqualTo(NOT_FOUND_USER);
    }

    @Test
    void updateUser_WhenEmailChangedAndDuplicate_ThrowsException() {
        User existing = userRepository.save(User.builder()
                .email("existing@example.com")
                .username("user1")
                .password("pass")
                .build());

        userRepository.save(User.builder()
                .email("new@example.com")
                .username("user2")
                .password("pass")
                .build());

        UserUpdateRequestDto dto = UserUpdateRequestDto.builder()
                .email("new@example.com")
                .build();

        CustomException exception = assertThrows(CustomException.class, () -> {
            userCommandService.updateUser(existing.getId(), dto);
        });

        assertThat(exception.getBaseCode()).isEqualTo(DUPLICATE_USER_EMAIL);
    }
}
