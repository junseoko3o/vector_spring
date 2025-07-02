package com.milvus.vector_spring.user;

import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import com.milvus.vector_spring.user.dto.UserUpdateRequestDto;

public interface UserCommandService {
    User signUpUser(UserSignUpRequestDto userSignUpRequestDto);
    User updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto);
}