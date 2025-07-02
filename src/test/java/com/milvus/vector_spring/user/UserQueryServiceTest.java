package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.UserProjectsResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.milvus.vector_spring.common.apipayload.status.ErrorStatus.NOT_FOUND_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserQueryServiceImpl userQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userQueryService = new UserQueryServiceImpl(userRepository);
    }

    @Test
    void findAllUser_ReturnsUserList() {
        User user = new User();
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userQueryService.findAllUser();

        assertThat(users).isNotEmpty();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findOneUser_WhenUserExists_ReturnUser() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User found = userQueryService.findOneUser(1L);

        assertThat(found).isEqualTo(user);
    }

    @Test
    void findOneUser_WhenUserNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            userQueryService.findOneUser(1L);
        });
        assertThat(exception.getBaseCode()).isEqualTo(NOT_FOUND_USER);
    }

    @Test
    void fineOneUserWithProjects_ReturnsDto() {
        User user = new User();
        when(userRepository.fineOneUserWithProjects(1L)).thenReturn(user);

        UserProjectsResponseDto dto = userQueryService.fineOneUserWithProjects(1L);

        assertThat(dto).isNotNull();
        verify(userRepository, times(1)).fineOneUserWithProjects(1L);
    }

    @Test
    void findOneUserByEmail_WhenUserExists_ReturnUser() {
        User user = new User();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User found = userQueryService.findOneUserByEmail("test@example.com");

        assertThat(found).isEqualTo(user);
    }

    @Test
    void findOneUserByEmail_WhenUserNotFound_ThrowsException() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            userQueryService.findOneUserByEmail("test@example.com");
        });
        assertThat(exception.getBaseCode()).isEqualTo(NOT_FOUND_USER);
    }
}
