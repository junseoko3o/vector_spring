package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.UserProjectsResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.milvus.vector_spring.common.apipayload.status.ErrorStatus.NOT_FOUND_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryServiceImpl userQueryService;

    @Test
    void findAllUser_ReturnsUserList() {
        // given
        User user = new User();
        when(userRepository.findAll()).thenReturn(List.of(user));

        // when
        List<User> users = userQueryService.findAllUser();

        // then
        assertThat(users).isNotEmpty();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findOneUser_WhenUserExists_ReturnUser() {
        // given
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        User found = userQueryService.findOneUser(1L);

        // then
        assertThat(found).isEqualTo(user);
    }

    @Test
    void findOneUser_WhenUserNotFound_ThrowsException() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when + then
        CustomException exception = assertThrows(CustomException.class, () -> {
            userQueryService.findOneUser(1L);
        });
        assertThat(exception.getBaseCode()).isEqualTo(NOT_FOUND_USER);
    }

    @Test
    void fineOneUserWithProjects_ReturnsDto() {
        // given
        User user = new User();
        when(userRepository.fineOneUserWithProjects(1L)).thenReturn(user);

        // when
        UserProjectsResponseDto dto = userQueryService.fineOneUserWithProjects(1L);

        // then
        assertThat(dto).isNotNull();
        verify(userRepository, times(1)).fineOneUserWithProjects(1L);
    }

    @Test
    void findOneUserByEmail_WhenUserExists_ReturnUser() {
        // given
        User user = new User();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // when
        User found = userQueryService.findOneUserByEmail("test@example.com");

        // then
        assertThat(found).isEqualTo(user);
    }

    @Test
    void findOneUserByEmail_WhenUserNotFound_ThrowsException() {
        // given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // when + then
        CustomException exception = assertThrows(CustomException.class, () -> {
            userQueryService.findOneUserByEmail("test@example.com");
        });
        assertThat(exception.getBaseCode()).isEqualTo(NOT_FOUND_USER);
    }
}
