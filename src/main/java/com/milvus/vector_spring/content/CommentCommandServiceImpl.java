package com.milvus.vector_spring.content;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.common.service.EncryptionService;
import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
import com.milvus.vector_spring.content.dto.ContentUpdateRequestDto;
import com.milvus.vector_spring.libraryopenai.OpenAiLibraryService;
import com.milvus.vector_spring.milvus.MilvusService;
import com.milvus.vector_spring.milvus.dto.InsertRequestDto;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.ProjectQueryService;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserQueryService;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements ContentCommandService {

    private final UserQueryService userQueryService;
    private final ProjectQueryService projectQueryService;
    private final EncryptionService encryptionService;
    private final ContentRepository contentRepository;
    private final OpenAiLibraryService openAiLibraryService;
    private final MilvusService milvusService;

    @Override
    public Content createContent(long userId, ContentCreateRequestDto dto) {
        User user = userQueryService.findOneUser(userId);
        Project project = projectQueryService.findOneProjectByKey(dto.getProjectKey());

        if (project.getOpenAiKey().isEmpty()) {
            throw new CustomException(ErrorStatus.REQUIRE_OPEN_AI_INFO);
        }
        if (project.getEmbedModel().isEmpty() || project.getDimensions() == 0) {
            throw new CustomException(ErrorStatus.REQUIRE_OPEN_AI_INFO);
        }

        String key = encryptionService.decryptData(project.getOpenAiKey());
        Content content = Content.builder()
                .key(UUID.randomUUID().toString())
                .title(dto.getTitle())
                .answer(dto.getAnswer())
                .project(project)
                .createdBy(user)
                .updatedBy(user)
                .build();

        CreateEmbeddingResponse embedResponseDto = openAiLibraryService.embedding(key, content.getAnswer(), project.getDimensions());
        Content savedContent = contentRepository.save(content);
        insertIntoMilvus(savedContent, embedResponseDto, project.getId());

        return savedContent;
    }

    @Override
    public Content updateContent(long id, ContentUpdateRequestDto dto) {
        User user = userQueryService.findOneUser(dto.getUpdatedUserId());
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_CONTENT));
        Project project = projectQueryService.findOneProject(content.getProject().getId());

        Content updateContent = Content.builder()
                .id(content.getId())
                .key(content.getKey())
                .title(dto.getTitle())
                .answer(dto.getAnswer())
                .project(project)
                .createdBy(content.getCreatedBy())
                .createdAt(content.getCreatedAt())
                .updatedBy(user)
                .build();

        String key = encryptionService.decryptData(project.getOpenAiKey());

        if (!content.getAnswer().equals(dto.getAnswer())) {
            CreateEmbeddingResponse embedResponseDto = openAiLibraryService.embedding(key, updateContent.getAnswer(), project.getDimensions());
            insertIntoMilvus(updateContent, embedResponseDto, project.getId());
        }
        return contentRepository.save(updateContent);
    }

    private void insertIntoMilvus(Content content, CreateEmbeddingResponse embedResponseDto, Long dbKey) {
        List<Float> floatList = embedResponseDto.data().get(0).embedding();
        try {
            milvusService.upsertCollection(content.getId(),
                    InsertRequestDto.builder()
                            .id(content.getId())
                            .vector(floatList)
                            .title(content.getTitle())
                            .answer(content.getAnswer())
                            .build(),
                    dbKey);
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.MILVUS_DATABASE_ERROR);
        }
    }
}