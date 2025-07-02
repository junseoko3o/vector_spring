package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private void duplicateEmailCheck(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new CustomException(ErrorStatus.DUPLICATE_USER_EMAIL);
        });
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User findOneUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_USER));
    }

    @Override
    public UserProjectsResponseDto fineOneUserWithProjects(Long id) {
        User user = userRepository.fineOneUserWithProjects(id);
        return UserProjectsResponseDto.of(user);
    }

    @Override
    public User findOneUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_USER));
    }

    @Override
    public User signUpUser(UserSignUpRequestDto userSignUpRequestDto) {
        duplicateEmailCheck(userSignUpRequestDto.getEmail());
        User user = User.builder()
                .email(userSignUpRequestDto.getEmail())
                .username(userSignUpRequestDto.getUsername())
                .password(passwordEncoder.encode(userSignUpRequestDto.getPassword()))
                .role("ROLE_USER")
                .build();
        return userRepository.save(user);
    }

    @Override
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
