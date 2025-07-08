package com.milvus.vector_spring.project;

import com.milvus.vector_spring.content.QContent;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

public class ProjectCustomRepositoryImpl implements ProjectCustomRepository {
    private final JPAQueryFactory queryFactory;

    public ProjectCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<Project> findOneProjectWithContents(String projectKey) {
        QProject project = QProject.project;
        QContent content = QContent.content;
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(project)
                        .leftJoin(project.contents, content).fetchJoin()
                        .where(project.key.eq(projectKey))
                        .fetchOne()
        );
    }
}
