package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import com.milvus.vector_spring.user.dto.UserUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.milvus.vector_spring.common.apipayload.status.ErrorStatus.DUPLICATE_USER_EMAIL;
import static com.milvus.vector_spring.common.apipayload.status.ErrorStatus.NOT_FOUND_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserCommandServiceImpl userCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userCommandService = new UserCommandServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void signUpUser_WhenEmailNotDuplicate_SavesUser() {
        UserSignUpRequestDto dto = UserSignUpRequestDto.builder()
                .email("new@example.com")
                .username("username")
                .password("password")
                .build();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userCommandService.signUpUser(dto);

        assertThat(savedUser.getEmail()).isEqualTo(dto.getEmail());
        assertThat(savedUser.getUsername()).isEqualTo(dto.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        verify(userRepository).findByEmail(dto.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signUpUser_WhenEmailDuplicate_ThrowsException() {
        UserSignUpRequestDto dto = UserSignUpRequestDto.builder()
                .email("duplicate@example.com")
                .build();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));

        CustomException exception = assertThrows(CustomException.class, () -> {
            userCommandService.signUpUser(dto);
        });
        assertThat(exception.getBaseCode()).isEqualTo(DUPLICATE_USER_EMAIL);
    }

    @Test
    void updateUser_WhenUserExistsAndEmailNotChanged_UpdatesUser() {
        Long id = 1L;
        UserUpdateRequestDto dto = UserUpdateRequestDto.builder()
                .email("existing@example.com")
                .username("updatedUsername")
                .build();

        User existingUser = User.builder()
                .id(id)
                .email(dto.getEmail())
                .password("oldPassword")
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userCommandService.updateUser(id, dto);

        assertThat(updatedUser.getId()).isEqualTo(id);
        assertThat(updatedUser.getEmail()).isEqualTo(dto.getEmail());
        assertThat(updatedUser.getUsername()).isEqualTo(dto.getUsername());
        assertThat(updatedUser.getPassword()).isEqualTo(existingUser.getPassword());
        verify(userRepository).findById(id);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserNotFound_ThrowsException() {
        Long id = 1L;
        UserUpdateRequestDto dto = UserUpdateRequestDto.builder().build();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            userCommandService.updateUser(id, dto);
        });
        assertThat(exception.getBaseCode()).isEqualTo(NOT_FOUND_USER);
    }

    @Test
    void updateUser_WhenEmailChangedAndDuplicate_ThrowsException() {
        Long id = 1L;
        UserUpdateRequestDto dto = UserUpdateRequestDto.builder()
                .email("newemail@example.com")
                .build();

        User existingUser = User.builder()
                .id(id)
                .email("oldemail@example.com")
                .password("oldPassword")
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));

        CustomException exception = assertThrows(CustomException.class, () -> {
            userCommandService.updateUser(id, dto);
        });
        assertThat(exception.getBaseCode()).isEqualTo(DUPLICATE_USER_EMAIL);
    }
}
