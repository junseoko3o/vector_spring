package com.milvus.vector_spring.user;

public interface UserCustomRepository {
    User findOneUserWithContents(Long userId);
}
