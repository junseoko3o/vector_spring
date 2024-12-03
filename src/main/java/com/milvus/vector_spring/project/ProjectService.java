package com.milvus.vector_spring.project;

import com.milvus.vector_spring.common.EncryptionService;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.milvus.MilvusService;
import com.milvus.vector_spring.project.dto.ProjectContentsResponseDto;
import com.milvus.vector_spring.project.dto.ProjectCreateRequestDto;
import com.milvus.vector_spring.project.dto.ProjectResponseDto;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final MilvusService milvusService;
    private final EncryptionService encryptionService;

    public List<Project> findAllProject() {
        return projectRepository.findAll();
    }

    public Project findOneProject(Long id) {
        return projectRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorStatus._NOT_FOUND_PROJECT)
        );
    }

    public Project findOneProjectByKey(String key) {
        return projectRepository.findByProjectKey(key).orElseThrow(
                () -> new CustomException(ErrorStatus._NOT_FOUND_PROJECT)
        );
    }

    public ProjectContentsResponseDto findOneProjectWithContents(String key) {
        projectRepository.findByProjectKey(key);
        Project project = projectRepository.findOneProjectWithContents(key);
        return new ProjectContentsResponseDto(project);
    }

    public Project createProject(ProjectCreateRequestDto projectCreateRequestDto) {
        User user = userService.findOneUser(projectCreateRequestDto.getCreateUserId());
        Project project = Project.builder()
                .name(projectCreateRequestDto.getName())
                .key(String.valueOf(UUID.randomUUID()))
                .openAiKey(encryptionService.encryptData(projectCreateRequestDto.getOpenAiKey()))
                .createdBy(user)
                .build();
        milvusService.createSchema(project.getId());
        return projectRepository.save(project);
    }
}
