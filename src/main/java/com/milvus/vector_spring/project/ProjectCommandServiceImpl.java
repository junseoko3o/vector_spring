package com.milvus.vector_spring.project;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.common.service.EncryptionService;
import com.milvus.vector_spring.milvus.MilvusService;
import com.milvus.vector_spring.project.dto.ProjectCreateRequestDto;
import com.milvus.vector_spring.project.dto.ProjectDeleteRequestDto;
import com.milvus.vector_spring.project.dto.ProjectUpdateRequestDto;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectCommandServiceImpl implements ProjectCommandService {

    private final ProjectRepository projectRepository;
    private final UserQueryService userQueryService;
    private final MilvusService milvusService;
    private final EncryptionService encryptionService;

    @Override
    @Transactional
    public Project createProject(ProjectCreateRequestDto dto) {
        User user = userQueryService.findOneUser(dto.getCreatedUserId());
        Project project = Project.builder()
                .name(dto.getName())
                .key(String.valueOf(UUID.randomUUID()))
                .openAiKey(dto.getOpenAiKey() != null ? encryptionService.encryptData(dto.getOpenAiKey()) : null)
                .embedModel(dto.getEmbedModel() != null ? dto.getEmbedModel() : null)
                .chatModel(dto.getChatModel() != null ? dto.getChatModel() : null)
                .dimensions(dto.getDimensions() != 0 ? dto.getDimensions() : 0)
                .prompt(dto.getPrompt() != null ? dto.getPrompt() : null)
                .totalToken(0)
                .createdBy(user)
                .updatedBy(user)
                .build();
        Project savedProject = projectRepository.save(project);
        milvusService.createSchema(savedProject.getId(), (int) dto.getDimensions());
        return savedProject;
    }

    @Override
    @Transactional
    public Project updateProject(String key, ProjectUpdateRequestDto dto) {
        User user = userQueryService.findOneUser(dto.getUpdatedUserId());
        Project project = projectRepository.findProjectByKey(key)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_PROJECT));
        String secretKey = resolveOpenAiKey(project, dto);
        Project updateProject = Project.builder()
                .id(project.getId())
                .key(project.getKey())
                .name(dto.getName())
                .openAiKey(secretKey)
                .chatModel(dto.getChatModel())
                .embedModel(dto.getEmbedModel())
                .dimensions(dto.getDimensions())
                .prompt(dto.getPrompt())
                .createdBy(project.getCreatedBy())
                .createdAt(project.getCreatedAt())
                .updatedBy(user)
                .build();
        return projectRepository.save(updateProject);
    }

    @Override
    @Transactional
    public String deleteProject(ProjectDeleteRequestDto dto) {
        Project project = projectRepository.findProjectByKey(dto.getKey())
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_PROJECT));
        if (!Objects.equals(project.getCreatedBy().getId(), dto.getUserId())) {
            throw new CustomException(ErrorStatus.NOT_PROJECT_MASTER_USER);
        }
        milvusService.deleteCollection(project.getId());
        projectRepository.delete(project);
        return "Deleted Success!";
    }

    @Override
    @Transactional
    public void plusTotalToken(String key, long totalToken) {
        Optional<Project> project = projectRepository.findProjectByKey(key);
        if (project.isPresent()) {
            Project p = project.get();
            long currentTotal = p.getTotalToken();
            p.updateTotalToken(currentTotal + totalToken);
            projectRepository.save(p);
        }
    }
    @Override
    public void updateProjectMaster(Project project, User user) {
        project.updateByUser(user, user);
        projectRepository.save(project);
    }

    private String resolveOpenAiKey(Project project, ProjectUpdateRequestDto dto) {
        String projectKey = project.getOpenAiKey();
        String dtoKey = dto.getOpenAiKey();

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
}

