package com.milvus.vector_spring.auth;

import com.milvus.vector_spring.auth.dto.UserLoginRequestDto;
import com.milvus.vector_spring.auth.dto.UserLoginResponseDto;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserDetailService;
import com.milvus.vector_spring.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailService userDetailService;
    private final UserRepository userRepository;

    public User login(UserLoginRequestDto userLoginRequestDto) {
        User user = userDetailService.loadUserByUsername(userLoginRequestDto.getEmail());
        User loginUser = User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .loginAt(LocalDateTime.now())
                .build();
        return userRepository.save(loginUser);
    }
}
