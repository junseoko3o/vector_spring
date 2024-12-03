package com.milvus.vector_spring.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository  extends JpaRepository<Project, Long>, ProjectCustomRepository {
    Optional<Project> findByProjectKey(String key);
}
