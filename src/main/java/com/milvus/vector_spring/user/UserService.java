package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public User findOneUser(Long id) {
         return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_USER));
    }

    private void duplicateEmailCheck(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new CustomException(ErrorStatus.DUPLICATE_USER_EMAIL);
        });
    }

    public UserProjectsResponseDto fineOneUserWithProjects(Long id) {
        userRepository.findById(id);
        User user = userRepository.fineOneUserWithProjects(id);
        return UserProjectsResponseDto.of(user);
    }

    public User findOneUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_USER));
    }

    public User signUpUser(UserSignUpRequestDto userSignUpRequestDto) {
        duplicateEmailCheck(userSignUpRequestDto.getEmail());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .email(userSignUpRequestDto.getEmail())
                .username(userSignUpRequestDto.getUsername())
                .password(encoder.encode(userSignUpRequestDto.getPassword()))
                .role("ROLE_USER")
                .build();
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User user = findOneUser(id);
        duplicateEmailCheck(userUpdateRequestDto.getEmail());
        User updatedUser = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(userUpdateRequestDto.getUsername())
                .password(user.getPassword())
                .build();
        return userRepository.save(updatedUser);
    }
}
