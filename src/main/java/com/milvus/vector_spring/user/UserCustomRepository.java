package com.milvus.vector_spring.user;

public interface UserCustomRepository {
    User fineOneUserWithProjects(Long userId);
}
