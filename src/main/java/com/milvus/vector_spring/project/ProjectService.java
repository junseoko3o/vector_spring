package com.milvus.vector_spring.project;

import com.milvus.vector_spring.common.service.EncryptionService;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.milvus.MilvusService;
import com.milvus.vector_spring.project.dto.ProjectCreateRequestDto;
import com.milvus.vector_spring.project.dto.ProjectUpdateRequestDto;
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
        return projectRepository.findProjectByKey(key).orElseThrow(
                () -> new CustomException(ErrorStatus._NOT_FOUND_PROJECT)
        );
    }

    public Project findOneProjectWithContents(String key) {
        projectRepository.findProjectByKey(key);
        return projectRepository.findOneProjectWithContents(key);
    }

    public Project createProject(ProjectCreateRequestDto projectCreateRequestDto) {
        User user = userService.findOneUser(projectCreateRequestDto.getCreatedUserId());
        Project project = Project.builder()
                .name(projectCreateRequestDto.getName())
                .key(String.valueOf(UUID.randomUUID()))
                .openAiKey(projectCreateRequestDto.getOpenAiKey() != null ? encryptionService.encryptData(projectCreateRequestDto.getOpenAiKey()) : null)
                .embedModel(projectCreateRequestDto.getEmbedModel() != null ? projectCreateRequestDto.getEmbedModel() : null)
                .basicModel(projectCreateRequestDto.getBasicModel() != null ? projectCreateRequestDto.getBasicModel() : null)
                .dimensions(projectCreateRequestDto.getDimensions() != 0 ? projectCreateRequestDto.getDimensions() : 0)
                .totalToken(0)
                .createdBy(user)
                .updatedBy(user)
                .build();
        Project savedProject = projectRepository.save(project);
        milvusService.createSchema(savedProject.getId(), (int) projectCreateRequestDto.getDimensions());
        return savedProject;
    }

    public Project updateProject(String key, ProjectUpdateRequestDto projectUpdateRequestDto) {
        User user = userService.findOneUser(projectUpdateRequestDto.getUpdatedUserId());
        Project project = findOneProjectByKey(key);
        String secretKey = resolveOpenAiKey(project, projectUpdateRequestDto);
        Project updateProject = Project.builder()
                .id(project.getId())
                .key(project.getKey())
                .name(projectUpdateRequestDto.getName())
                .openAiKey(secretKey)
                .basicModel(projectUpdateRequestDto.getBasicModel())
                .embedModel(projectUpdateRequestDto.getEmbedModel())
                .dimensions(projectUpdateRequestDto.getDimensions())
                .prompt(projectUpdateRequestDto.getPrompt())
                .createdBy(project.getCreatedBy())
                .createdAt(project.getCreatedAt())
                .updatedBy(user)
                .build();
        return projectRepository.save(updateProject);
    }

    public String deleteProject(String key) {
        Project project = findOneProjectByKey(key);
        milvusService.deleteCollection(project.getId());
        projectRepository.delete(project);
        return "Deleted Success!";
    }

    public void plusTotalToken(Project project, long totalToken) {
        long currentTotal = project.getTotalToken();
        project.updateTotalToken(currentTotal + totalToken);
        projectRepository.save(project);
    }

    private String resolveOpenAiKey(Project project, ProjectUpdateRequestDto projectUpdateRequestDto) {
        String projectKey = project.getOpenAiKey();
        String dtoKey = projectUpdateRequestDto.getOpenAiKey();

        boolean isProjectKeyEmpty = projectKey == null || projectKey.isEmpty();
        boolean isDtoKeyEmpty = dtoKey == null || dtoKey.isEmpty();

        if (isProjectKeyEmpty && isDtoKeyEmpty) {
            return null;
        }
        if (isProjectKeyEmpty) {
            return encryptionService.encryptData(dtoKey);
        }
        if (!isDtoKeyEmpty) {
            String decryptedProjectKey = encryptionService.decryptData(projectKey);
            if (decryptedProjectKey.equals(dtoKey)) {
                return projectKey;
            }
            return encryptionService.encryptData(dtoKey);
        }
        return projectKey;
    }

    public void updateProjectMaster(Project project, User user) {
        project.updateByUser(user, user);
        projectRepository.save(project);
    }
}
