package com.milvus.vector_spring.project;

import java.util.List;

public interface ProjectQueryService {
    List<Project> findAllProject();
    Project findOneProject(Long id);
    Project findOneProjectByKey(String key);
    Project findOneProjectWithContents(String key);
}
