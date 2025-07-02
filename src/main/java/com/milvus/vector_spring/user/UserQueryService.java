package com.milvus.vector_spring.user;

import com.milvus.vector_spring.user.dto.UserProjectsResponseDto;

import java.util.List;

public interface UserQueryService {
    List<User> findAllUser();
    User findOneUser(Long id);
    UserProjectsResponseDto fineOneUserWithProjects(Long id);
    User findOneUserByEmail(String email);
}
