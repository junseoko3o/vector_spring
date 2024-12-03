package com.milvus.vector_spring.project;

import com.milvus.vector_spring.content.QContent;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ProjectCustomRepositoryImpl implements ProjectCustomRepository {
    private final JPAQueryFactory queryFactory;

    public ProjectCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public Project findOneProjectWithContents(String projectKey) {
        QProject project = QProject.project;
        QContent content = QContent.content;
        return queryFactory
                .selectFrom(project)
                .leftJoin(project.contents, content).fetchJoin()
                .where(project.key.eq(projectKey))
                .fetchOne();
    }
}
