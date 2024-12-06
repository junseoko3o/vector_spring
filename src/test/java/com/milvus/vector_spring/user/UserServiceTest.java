package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import com.milvus.vector_spring.user.dto.UserUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserSignUpRequestDto signUpRequestDto;
    private UserUpdateRequestDto updateRequestDto;
    private User user;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        signUpRequestDto = new UserSignUpRequestDto("testuser","test@example.com", "password123!");
        updateRequestDto = new UserUpdateRequestDto("updatedUser", "test@example.com");
        user = new User(1L, "test@example.com", "testuser", "password123", null);
    }

    @Test
    @DisplayName("유저 전체 찾기")
    void findAllUser() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.findAllUser();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("test@example.com", users.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("유저 회원 가입")
    void signUpUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.signUpUser(signUpRequestDto);

        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("testuser", createdUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("유저 정보 수정")
    void updateUser() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(1L, updateRequestDto);

        assertNotNull(updatedUser);
        assertEquals("updatedUser", updatedUser.getUsername());
        assertEquals("test@example.com", updatedUser.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("유저 수정 시 없는 유저 수정 시도")
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> userService.updateUser(1L, updateRequestDto));
        assertEquals(ErrorStatus._NOT_FOUND_USER, exception.getBaseCode());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("유저 회원가입 시 이미 존재하는 이메일로 가입 시도")
    void signUpUser_ShouldThrowException_WhenEmailIsDuplicate() {
        when(userRepository.findByEmail(signUpRequestDto.getEmail())).thenReturn(java.util.Optional.of(user));

        CustomException exception = assertThrows(CustomException.class, () -> userService.signUpUser(signUpRequestDto));
        assertEquals(ErrorStatus._DUPLICATE_USER_EMAIL, exception.getBaseCode());
        verify(userRepository, times(1)).findByEmail(signUpRequestDto.getEmail());
    }
}