package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.UserProjectsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

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
}