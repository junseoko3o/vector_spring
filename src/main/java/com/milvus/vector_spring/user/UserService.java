package com.milvus.vector_spring.user;

import com.milvus.vector_spring.user.dto.UserProjectsResponseDto;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import com.milvus.vector_spring.user.dto.UserUpdateRequestDto;

import java.util.List;

public interface UserService {
    List<User> findAllUser();
    User findOneUser(Long id);
    UserProjectsResponseDto fineOneUserWithProjects(Long id);
    User findOneUserByEmail(String email);
    User signUpUser(UserSignUpRequestDto userSignUpRequestDto);
    User updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto);
}
