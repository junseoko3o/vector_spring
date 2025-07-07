package com.milvus.vector_spring.project;


import java.util.Optional;

public interface ProjectCustomRepository {
    Optional<Project> findOneProjectWithContents(String projectKey);
}
