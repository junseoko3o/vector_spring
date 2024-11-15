package com.milvus.vector_spring.user;

import com.milvus.vector_spring.exception.CustomException;
import com.milvus.vector_spring.exception.ErrorCode;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> findOneUser(Long id) {
        return userRepository.findByUserId(id);
    }

    private void duplicateEmailCheck(String email) {
        Optional<User> user = userRepository.findByUserEmail(email);
        if (user.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_EMAIL);
        }
    }

    public User signUpUser(UserSignUpRequestDto userSignUpRequestDto) {
        duplicateEmailCheck(userSignUpRequestDto.getEmail());
        User user = User.builder()
                .email(userSignUpRequestDto.getEmail())
                .userName(userSignUpRequestDto.getUserName())
                .password(userSignUpRequestDto.getPassword())
                .build();
        return userRepository.save(user);
    }
}
