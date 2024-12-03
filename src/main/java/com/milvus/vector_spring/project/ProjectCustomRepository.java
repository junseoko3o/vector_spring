package com.milvus.vector_spring.project;


public interface ProjectCustomRepository {
    Project findOneProjectWithContents(String projectKey);
}
