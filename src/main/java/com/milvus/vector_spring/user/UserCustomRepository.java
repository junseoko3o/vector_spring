package com.milvus.vector_spring.user;

public interface UserCustomRepository {
    User findOneUserWithProjects(Long userId);
}
