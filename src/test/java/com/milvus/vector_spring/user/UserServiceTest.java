package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.UserProjectsResponseDto;
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
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void findAllUser_ReturnsUserList() {
        User user = User.builder()
                .email("user1@example.com")
                .username("user1")
                .password("pass")
                .build();
        userRepository.save(user);

        var users = userService.findAllUser();

        assertThat(users).isNotEmpty();
        assertThat(users.get(1).getEmail()).isEqualTo("user1@example.com");
        assertThat(users.get(0).getEmail()).isEqualTo("admin@admin.com");
    }

    @Test
    void findOneUser_WhenUserExists_ReturnUser() {
        User user = User.builder()
                .email("user2@example.com")
                .username("user2")
                .password("pass")
                .build();
        userRepository.save(user);

        User found = userService.findOneUser(user.getId());

        assertThat(found).isEqualTo(user);
    }

    @Test
    void findOneUser_WhenUserNotFound_ThrowsException() {
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.findOneUser(999L);
        });

        assertThat(exception.getBaseCode()).isEqualTo(NOT_FOUND_USER);
    }

    @Test
    void findOneUserByEmail_WhenUserExists_ReturnUser() {
        User user = User.builder()
                .email("email3@example.com")
                .username("user3")
                .password("pass")
                .build();
        userRepository.save(user);

        User found = userService.findOneUserByEmail("email3@example.com");

        assertThat(found).isEqualTo(user);
    }

    @Test
    void findOneUserByEmail_WhenUserNotFound_ThrowsException() {
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.findOneUserByEmail("none@example.com");
        });

        assertThat(exception.getBaseCode()).isEqualTo(NOT_FOUND_USER);
    }

    @Test
    void findOneUserWithProjects_ReturnsDto() {
        User user = User.builder()
                .email("proj@example.com")
                .username("withproject")
                .password("pass")
                .build();
        userRepository.save(user);

        UserProjectsResponseDto dto = userService.findOneUserWithProjects(user.getId());

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(user.getId());
    }

    @Test
    void signUpUser_WhenEmailNotDuplicate_SavesUser() {
        UserSignUpRequestDto dto = UserSignUpRequestDto.builder()
                .email("new@example.com")
                .username("username")
                .password("password")
                .build();

        User savedUser = userService.signUpUser(dto);

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
            userService.signUpUser(dto);
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

        User updated = userService.updateUser(user.getId(), dto);

        assertThat(updated.getUsername()).isEqualTo("after");
    }

    @Test
    void updateUser_WhenUserNotFound_ThrowsException() {
        UserUpdateRequestDto dto = UserUpdateRequestDto.builder()
                .email("notfound@example.com")
                .build();

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.updateUser(999L, dto);
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
            userService.updateUser(existing.getId(), dto);
        });

        assertThat(exception.getBaseCode()).isEqualTo(DUPLICATE_USER_EMAIL);
    }
}
