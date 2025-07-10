package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import com.milvus.vector_spring.user.dto.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private void duplicateEmailCheck(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new CustomException(ErrorStatus.DUPLICATE_USER_EMAIL);
        });
    }

    @Override
    @Transactional
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
    @Transactional
    public User updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_USER));

        if (!user.getEmail().equals(userUpdateRequestDto.getEmail())) {
            duplicateEmailCheck(userUpdateRequestDto.getEmail());
        }

        User updatedUser = User.builder()
                .id(user.getId())
                .email(userUpdateRequestDto.getEmail())
                .username(userUpdateRequestDto.getUsername())
                .password(user.getPassword())
                .build();
        return userRepository.save(updatedUser);
    }
}
