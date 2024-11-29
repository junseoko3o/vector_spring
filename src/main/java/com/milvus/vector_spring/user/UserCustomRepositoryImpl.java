package com.milvus.vector_spring.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class UserCustomRepositoryImpl implements UserCustomRepository{
    private final JPAQueryFactory queryFactory;

    public UserCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
}
