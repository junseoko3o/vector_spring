package com.milvus.vector_spring.content;

import com.milvus.vector_spring.common.service.EncryptionService;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
import com.milvus.vector_spring.content.dto.ContentUpdateRequestDto;
import com.milvus.vector_spring.libraryopenai.OpenAiLibraryService;
import com.milvus.vector_spring.milvus.MilvusService;
import com.milvus.vector_spring.milvus.dto.InsertRequestDto;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.ProjectService;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserService;
import com.openai.models.CreateEmbeddingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final UserService userService;
    private final ProjectService projectService;
    private final OpenAiLibraryService openAiLibraryService;
    private final MilvusService milvusService;
    private final EncryptionService encryptionService;

    public List<Content> findAllContent() {
        return contentRepository.findAll();
    }

    public Content findOneContentById(Long id) throws CustomException{
        return contentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorStatus.NOT_FOUND_CONTENT)
        );
    }
    public Content createContent(long userId, ContentCreateRequestDto contentCreateRequestDto) throws CustomException {
        User user = userService.findOneUser(userId);
        Project project = projectService.findOneProjectByKey(contentCreateRequestDto.getProjectKey());
        if (project.getOpenAiKey().isEmpty()) {
            throw new CustomException(ErrorStatus.REQUIRE_OPEN_AI_INFO);
        }
        if (project.getEmbedModel().isEmpty() || project.getDimensions() == 0) {
            throw new CustomException(ErrorStatus.REQUIRE_OPEN_AI_INFO);
        }
        String key = encryptionService.decryptData(project.getOpenAiKey());
        Content content = Content.builder()
                .key(UUID.randomUUID().toString())
                .title(contentCreateRequestDto.getTitle())
                .answer(contentCreateRequestDto.getAnswer())
                .project(project)
                .createdBy(user)
                .updatedBy(user)
                .build();;
        CreateEmbeddingResponse embedResponseDto = fetchEmbedding(key, content.getAnswer(), project.getDimensions());
        Content savedContent = contentRepository.save(content);
        insertIntoMilvus(savedContent, embedResponseDto);
        return savedContent;
    }

    public Content updateContent(long id, ContentUpdateRequestDto contentUpdateRequestDto) {
        User user = userService.findOneUser(contentUpdateRequestDto.getUpdatedUserId());
        Content content = findOneContentById(id);
        Project project = projectService.findOneProject(content.getProject().getId());
        Content updateContent = Content.builder()
                .id(content.getId())
                .key(content.getKey())
                .title(contentUpdateRequestDto.getTitle())
                .answer(contentUpdateRequestDto.getAnswer())
                .project(project)
                .createdBy(content.getCreatedBy())
                .createdAt(content.getCreatedAt())
                .updatedBy(user)
                .build();
        String key = encryptionService.decryptData(project.getOpenAiKey());
        if(!content.getAnswer().equals(contentUpdateRequestDto.getAnswer())) {
            CreateEmbeddingResponse embedResponseDto = fetchEmbedding(key, updateContent.getAnswer(), project.getDimensions());
            insertIntoMilvus(updateContent, embedResponseDto);
            return contentRepository.save(updateContent);
        }

        return contentRepository.save(updateContent);
    }

    private CreateEmbeddingResponse fetchEmbedding(String openAiKey, String answer, long dimensions) throws CustomException {
        return openAiLibraryService.embedding(openAiKey, answer, dimensions);
    }

    private void insertIntoMilvus(Content content, CreateEmbeddingResponse embedResponseDto) throws CustomException{
        List<Double> doubleList = embedResponseDto.data().get(0).embedding();
        List<Float> vector = new ArrayList<>();
        for (Double value : doubleList) {
            vector.add(value.floatValue());
        }
        try {
            InsertRequestDto insertRequestDto = InsertRequestDto.builder()
                    .id(content.getId())
                    .vector(vector)
                    .title(content.getTitle())
                    .answer(content.getAnswer())
                    .build();
            milvusService.upsertCollection(insertRequestDto.getId(), insertRequestDto);
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.MILVUS_DATABASE_ERROR);
        }
    }
}
