package com.milvus.vector_spring.user;

import com.milvus.vector_spring.exception.CustomException;
import com.milvus.vector_spring.exception.ErrorCode;
import com.milvus.vector_spring.user.dto.UserUpdateRequestDto;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public User findOneUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
        return user.get();
    }

    private void duplicateEmailCheck(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_EMAIL);
        }
    }

    public User signUpUser(UserSignUpRequestDto userSignUpRequestDto) throws CustomException {
        duplicateEmailCheck(userSignUpRequestDto.getEmail());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .email(userSignUpRequestDto.getEmail())
                .userName(userSignUpRequestDto.getUserName())
                .password(encoder.encode(userSignUpRequestDto.getPassword()))
                .build();
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto) throws CustomException {
        User user = findOneUser(id);
        duplicateEmailCheck(userUpdateRequestDto.getEmail());
        User updatedUser = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(userUpdateRequestDto.getUserName())
                .build();
        userRepository.save(user);
        return updatedUser;
    }
}
