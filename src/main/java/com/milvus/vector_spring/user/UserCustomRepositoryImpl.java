package com.milvus.vector_spring.user;

import com.milvus.vector_spring.content.QContent;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class UserCustomRepositoryImpl implements UserCustomRepository{
    private final JPAQueryFactory queryFactory;

    public UserCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public User findOneUserWithContents(Long userId) {
        QUser user = QUser.user;
        QContent content = QContent.content;

        User fetchedUser = queryFactory
                .selectFrom(user)
                .leftJoin(user.contents, content).fetchJoin()
                .where(user.id.eq(userId))
                .fetchOne();
        return fetchedUser;
    }
}
