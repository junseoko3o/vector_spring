package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import com.milvus.vector_spring.user.dto.UserUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
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
    void findAllUser() {
        // Given
        when(userRepository.findAll()).thenReturn(List.of(user));

        // When
        List<User> users = userService.findAllUser();
        
        // Then
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("test@example.com", users.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void signUpUser() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User createdUser = userService.signUpUser(signUpRequestDto);

        // Then
        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("testuser", createdUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User updatedUser = userService.updateUser(1L, updateRequestDto);

        // Then
        assertNotNull(updatedUser);
        assertEquals("updatedUser", updatedUser.getUsername());
        assertEquals("test@example.com", updatedUser.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> userService.updateUser(1L, updateRequestDto));
        assertEquals(ErrorStatus._NOT_FOUND_USER, exception.getBaseCode());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void signUpUser_ShouldThrowException_WhenEmailIsDuplicate() {
        // Given
        when(userRepository.findByEmail(signUpRequestDto.getEmail())).thenReturn(java.util.Optional.of(user));

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> userService.signUpUser(signUpRequestDto));
        assertEquals(ErrorStatus._DUPLICATE_USER_EMAIL, exception.getBaseCode());
        verify(userRepository, times(1)).findByEmail(signUpRequestDto.getEmail());
    }
}