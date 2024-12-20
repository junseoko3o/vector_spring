package com.milvus.vector_spring.auth;

import com.milvus.vector_spring.auth.dto.UserLoginRequestDto;
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

    public void login(UserLoginRequestDto userLoginRequestDto) {
        User user = userDetailService.loadUserByUsername(userLoginRequestDto.getEmail());
        user.updateLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
