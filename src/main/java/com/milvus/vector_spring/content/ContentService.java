package com.milvus.vector_spring.content;

import com.milvus.vector_spring.common.EncryptionService;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
import com.milvus.vector_spring.content.dto.ContentUpdateRequestDto;
import com.milvus.vector_spring.milvus.MilvusService;
import com.milvus.vector_spring.milvus.dto.InsertRequestDto;
import com.milvus.vector_spring.openai.OpenAiService;
import com.milvus.vector_spring.openai.dto.EmbedRequestDto;
import com.milvus.vector_spring.openai.dto.OpenAiEmbedResponseDto;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.ProjectService;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final UserService userService;
    private final ProjectService projectService;
    private final OpenAiService openAiService;
    private final MilvusService milvusService;
    private final EncryptionService encryptionService;

    public List<Content> findAllContent() {
        return contentRepository.findAll();
    }

    public Content findOneContById(Long id) throws CustomException{
        return contentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorStatus._NOT_FOUND_CONTENT)
        );
    }

    public Content createContent(long userId, ContentCreateRequestDto contentCreateRequestDto) throws CustomException {
        User user = userService.findOneUser(userId);
        Project project = projectService.findOneProjectByKey(contentCreateRequestDto.getProjectKey());
        if (project.getOpenAiKey().isEmpty()) {
            throw new CustomException(ErrorStatus._OPEN_AI_ERROR);
        }
        String key = encryptionService.decryptData(project.getOpenAiKey());
        Content content = Content.builder()
                .title(contentCreateRequestDto.getTitle())
                .answer(contentCreateRequestDto.getAnswer())
                .project(project)
                .createdBy(user)
                .updatedBy(user)
                .build();;
        OpenAiEmbedResponseDto embedResponseDto = fetchEmbedding(key, content.getAnswer());
        Content savedContent = contentRepository.save(content);
        insertIntoMilvus(savedContent, embedResponseDto);
        return savedContent;
    }

    public Content updateContent(long id, ContentUpdateRequestDto contentUpdateRequestDto) {
        User user = userService.findOneUser(contentUpdateRequestDto.getUpdatedUserId());
        Content content = findOneContById(id);
        Project project = projectService.findOneProject(content.getProject().getId());
        Content updateContent = Content.builder()
                .id(content.getId())
                .title(contentUpdateRequestDto.getTitle())
                .answer(contentUpdateRequestDto.getAnswer())
                .project(project)
                .createdBy(content.getCreatedBy())
                .createdAt(content.getCreatedAt())
                .updatedBy(user)
                .build();
        String key = encryptionService.decryptData(project.getOpenAiKey());
        if(!content.getAnswer().equals(contentUpdateRequestDto.getAnswer())) {
            OpenAiEmbedResponseDto embedResponseDto = fetchEmbedding(key, updateContent.getAnswer());
            insertIntoMilvus(updateContent, embedResponseDto);
            return contentRepository.save(updateContent);
        }

        return contentRepository.save(updateContent);
    }

    private OpenAiEmbedResponseDto fetchEmbedding(String openAiKey, String answer) throws CustomException {
        EmbedRequestDto embedRequestDto = new EmbedRequestDto(answer);
        return openAiService.embedding(openAiKey, embedRequestDto);
    }

    private void insertIntoMilvus(Content content, OpenAiEmbedResponseDto embedResponseDto) throws CustomException{
        try {
            InsertRequestDto insertRequestDto = InsertRequestDto.builder()
                    .id(content.getId())
                    .vector(embedResponseDto.getData().get(0).getEmbedding())
                    .title(content.getTitle())
                    .answer(content.getAnswer())
                    .build();
            milvusService.upsertCollection(insertRequestDto.getId(), insertRequestDto);
        } catch (Exception e) {
            throw new CustomException(ErrorStatus._MILVUS_DATABASE_ERROR);
        }
    }
}
