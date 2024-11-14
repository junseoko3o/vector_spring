package com.milvus.vector_spring.user;

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
}
