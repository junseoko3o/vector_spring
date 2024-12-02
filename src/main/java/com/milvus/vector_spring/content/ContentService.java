package com.milvus.vector_spring.content;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
import com.milvus.vector_spring.content.dto.ContentUpdateRequestDto;
import com.milvus.vector_spring.milvus.MilvusService;
import com.milvus.vector_spring.milvus.dto.InsertRequestDto;
import com.milvus.vector_spring.openai.OpenAiService;
import com.milvus.vector_spring.openai.dto.EmbedRequestDto;
import com.milvus.vector_spring.openai.dto.OpenAiEmbedResponseDto;
import com.milvus.vector_spring.openai.dto.OpenAiUsageResponseDto;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final UserService userService;
    private final OpenAiService openAiService;
    private final MilvusService milvusService;

    public List<Content> findAllContent() {
        return contentRepository.findAll();
    }

    public Content findOneContById(Long id) throws CustomException{
        return contentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorStatus._NOT_FOUND_CONTENT)
        );
    }

    @Transactional
    public Content createContent(long userId, ContentCreateRequestDto contentCreateRequestDto) throws CustomException {
        User user = userService.findOneUser(userId);
        Content content = buildContent(contentCreateRequestDto, user);
        OpenAiEmbedResponseDto embedResponseDto = fetchEmbedding(content.getAnswer());
        Content savedContent = contentRepository.save(content);
        insertIntoMilvus(savedContent, embedResponseDto);
        return savedContent;
    }

    @Transactional
    public Content updateContent(long id, ContentUpdateRequestDto contentUpdateRequestDto) {
        User user = userService.findOneUser(contentUpdateRequestDto.getUpdatedUserId());
        Content content = findOneContById(id);
        Content updateContent = Content.builder()
                .id(content.getId())
                .title(contentUpdateRequestDto.getTitle())
                .answer(contentUpdateRequestDto.getAnswer())
                .createdBy(content.getCreatedBy())
                .createdAt(content.getCreatedAt())
                .updatedBy(user)
                .build();

        if(!content.getAnswer().equals(contentUpdateRequestDto.getAnswer())) {
            OpenAiEmbedResponseDto embedResponseDto = fetchEmbedding(updateContent.getAnswer());
            insertIntoMilvus(updateContent, embedResponseDto);
            return contentRepository.save(updateContent);
        }

        return contentRepository.save(updateContent);
    }

    private Content buildContent(ContentCreateRequestDto contentCreateRequestDto, User user) {
        return Content.builder()
                .title(contentCreateRequestDto.getTitle())
                .answer(contentCreateRequestDto.getAnswer())
                .createdBy(user)
                .updatedBy(user)
                .build();
    }

    private OpenAiEmbedResponseDto fetchEmbedding(String answer) throws CustomException {
        EmbedRequestDto embedRequestDto = new EmbedRequestDto(answer);
        JsonNode jsonNode = openAiService.embedding(embedRequestDto);
        JsonNode embeddingNode = jsonNode.get("data").get(0).get("embedding");
        List<Float> embeddingList = parseEmbedding(embeddingNode);

        JsonNode usageNode = jsonNode.get("usage");
        OpenAiUsageResponseDto usage = parseUsage(usageNode);

        return OpenAiEmbedResponseDto.builder()
                .embedding(embeddingList)
                .usage(usage)
                .build();
    }

    private List<Float> parseEmbedding(JsonNode embeddingNode) {
        List<Float> embeddingList = new ArrayList<>();
        for (JsonNode element : embeddingNode) {
            embeddingList.add(element.floatValue());
        }
        return embeddingList;
    }

    private OpenAiUsageResponseDto parseUsage(JsonNode usageNode) throws CustomException{
        try {
            return new ObjectMapper().treeToValue(usageNode, OpenAiUsageResponseDto.class);
        } catch (Exception e) {
            throw new CustomException(ErrorStatus._OPEN_AI_ERROR);
        }
    }

    private void insertIntoMilvus(Content content, OpenAiEmbedResponseDto embedResponseDto) throws CustomException{
        try {
            InsertRequestDto insertRequestDto = InsertRequestDto.builder()
                    .id(content.getId())
                    .vector(embedResponseDto.getEmbedding())
                    .title(content.getTitle())
                    .answer(content.getAnswer())
                    .build();
            milvusService.upsertCollection(insertRequestDto.getId(), insertRequestDto);
        } catch (Exception e) {
            throw new CustomException(ErrorStatus._MILVUS_DATABASE_ERROR);
        }
    }
}
