package com.milvus.vector_spring.user;

import com.milvus.vector_spring.project.QProject;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository{
    private final JPAQueryFactory queryFactory;

    public UserCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public User findOneUserWithProjects(Long userId) {
        QUser user = QUser.user;
        QProject project = QProject.project;
        return queryFactory
                .selectFrom(user)
                .leftJoin(user.createdProjectUser, project).fetchJoin()
                .where(user.id.eq(userId))
                .fetchOne();
    }
}
