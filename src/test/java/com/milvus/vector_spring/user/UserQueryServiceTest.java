package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.UserProjectsResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.milvus.vector_spring.common.apipayload.status.ErrorStatus.NOT_FOUND_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserQueryServiceTest {

    @Autowired
    private UserQueryServiceImpl userQueryService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllUser_ReturnsUserList() {
        User user = User.builder()
                .email("user1@example.com")
                .username("user1")
                .password("pass")
                .build();
        userRepository.save(user);

        var users = userQueryService.findAllUser();

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

        User found = userQueryService.findOneUser(user.getId());

        assertThat(found).isEqualTo(user);
    }

    @Test
    void findOneUser_WhenUserNotFound_ThrowsException() {
        CustomException exception = assertThrows(CustomException.class, () -> {
            userQueryService.findOneUser(999L);
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

        User found = userQueryService.findOneUserByEmail("email3@example.com");

        assertThat(found).isEqualTo(user);
    }

    @Test
    void findOneUserByEmail_WhenUserNotFound_ThrowsException() {
        CustomException exception = assertThrows(CustomException.class, () -> {
            userQueryService.findOneUserByEmail("none@example.com");
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

        UserProjectsResponseDto dto = userQueryService.findOneUserWithProjects(user.getId());

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(user.getId());
    }
}
