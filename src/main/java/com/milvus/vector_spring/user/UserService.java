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

    public void findOneUser(Long id) {
        Optional<User> = userRepository.findByUserId(id);
        if (!userRepository.existsById(id)) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
    }

    private void duplicateEmailCheck(String email) {
        Optional<User> user = userRepository.findByUserEmail(email);
        if (user.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_EMAIL);
        }
    }

    public User signUpUser(UserSignUpRequestDto userSignUpRequestDto) throws CustomException {
        duplicateEmailCheck(userSignUpRequestDto.getEmail());
        User user = User.builder()
                .email(userSignUpRequestDto.getEmail())
                .userName(userSignUpRequestDto.getUserName())
                .password(userSignUpRequestDto.getPassword())
                .build();
        return userRepository.save(user);
    }

//    public Optional<User> updateUser(Long id, UserSignUpRequestDto userSignUpRequestDto) throws CustomException {
////        User user = findOneUser(id);
////        user.setUserName(userSignUpRequestDto.getUserName());
////        user.setEmail(userSignUpRequestDto.getEmail());
////        userRepository.save(user);
////        return user;
//    }
}
