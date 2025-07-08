package com.milvus.vector_spring.project;

import com.milvus.vector_spring.project.dto.ProjectCreateRequestDto;
import com.milvus.vector_spring.project.dto.ProjectDeleteRequestDto;
import com.milvus.vector_spring.project.dto.ProjectUpdateRequestDto;
import com.milvus.vector_spring.user.User;

public interface ProjectCommandService {
    Project createProject(ProjectCreateRequestDto dto);
    Project updateProject(String key, ProjectUpdateRequestDto dto);
    String deleteProject(ProjectDeleteRequestDto dto);
    void plusTotalToken(String key, long totalToken);
    void updateProjectMaster(Project project, User user);
}
