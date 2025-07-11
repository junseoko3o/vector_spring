package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.cache.ProjectCacheService;
import com.milvus.vector_spring.chat.dto.ChatRequestDto;
import com.milvus.vector_spring.chat.dto.ChatResponseDto;
import com.milvus.vector_spring.chat.dto.VectorSearchRankDto;
import com.milvus.vector_spring.chat.dto.VectorSearchResponseDto;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.config.mongo.document.ChatResponseDocument;
import com.milvus.vector_spring.content.Content;
import com.milvus.vector_spring.content.ContentService;
import com.milvus.vector_spring.milvus.VectorSearchService;
import com.milvus.vector_spring.openai.dto.OpenAiChatResponseDto;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.ProjectService;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.completions.CompletionUsage;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    @Value("${open.ai.key}")
    private String openAiKey;

    private final ChatOptionService chatOptionService;
    private final ProjectService projectService;
    private final ProjectCacheService projectCacheService;
    private final ContentService contentService;

    private final VectorSearchService vectorSearchService;
    private final ChatCompletionService chatCompletionService;
    private final MongoTemplate mongoTemplate;

    public ChatResponseDto chat(ChatRequestDto chatRequestDto) {
        LocalDateTime inputDateTime = LocalDateTime.now();

        try {
            Project project = projectCacheService.getProject(chatRequestDto.getProjectKey());
            if (project.getOpenAiKey().isEmpty() && project.getChatModel().isEmpty()) {
                throw new CustomException(ErrorStatus.REQUIRE_OPEN_AI_INFO);
            }
            String secretKey = projectService.decryptOpenAiKey(project);
            var embeddingResponse = vectorSearchService.createEmbedding(secretKey, chatRequestDto.getText(), project.getDimensions());
            VectorSearchResponseDto searchResponse = vectorSearchService.searchVector(embeddingResponse, project.getId());
            List<VectorSearchRankDto> rankList = vectorSearchService.convertToRankList(searchResponse);

            ChatCompletion answer = chatCompletionService.generateAnswer(
                    project.getChatModel(),
                    chatRequestDto.getText(),
                    secretKey,
                    rankList,
                    searchResponse,
                    project.getPrompt()
            );
            String finalAnswer = answer.choices().get(0).message().content().orElse("");

            Content content = Optional.ofNullable(searchResponse.getFirstSearchId())
                    .flatMap(contentService::findOneContentByContentId)
                    .orElse(null);

            LocalDateTime outputDateTime = LocalDateTime.now();

            long totalToken = embeddingResponse.usage().totalTokens() +
                    answer.usage().stream()
                            .mapToLong(CompletionUsage::totalTokens)
                            .sum();

            projectService.plusTotalToken(project.getKey(), totalToken);

            ChatResponseDto chatResponseDto = buildResponse(
                    chatRequestDto,
                    finalAnswer,
                    inputDateTime,
                    outputDateTime,
                    content,
                    rankList,
                    searchResponse.getSearch()
            );

            saveResponse(
                    chatRequestDto,
                    finalAnswer,
                    inputDateTime,
                    outputDateTime,
                    content,
                    rankList
            );

            return chatResponseDto;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public OpenAiChatResponseDto testChat(String question) {
        return chatOptionService.onlyOpenAiAnswer(openAiKey, question, "");
    }

    private ChatResponseDto buildResponse(ChatRequestDto requestDto, String finalAnswer, LocalDateTime inputTime, LocalDateTime outputTime, Content content, List<VectorSearchRankDto> rankList, SearchResp searchResults) {
        requestDto.setSessionId("DEV");
        return ChatResponseDto.chatResponseDto(
                requestDto.getProjectKey(),
                requestDto.getSessionId(),
                requestDto.getText(),
                finalAnswer,
                inputTime,
                outputTime,
                searchResults,
                content
        );
    }

    private void saveResponse(ChatRequestDto requestDto, String finalAnswer, LocalDateTime inputTime, LocalDateTime outputTime, Content content, List<VectorSearchRankDto> rankList) {
        ChatResponseDocument.SimpleContentDto simpleContent = Optional.ofNullable(content)
                .map(c -> new ChatResponseDocument.SimpleContentDto(
                        c.getId(),
                        c.getKey(),
                        c.getTitle(),
                        c.getAnswer()
                ))
                .orElse(new ChatResponseDocument.SimpleContentDto(null, null, null, null));
        ChatResponseDocument doc = new ChatResponseDocument(
                requestDto.getSessionId(),
                requestDto.getText(),
                finalAnswer,
                inputTime,
                outputTime,
                simpleContent,
                rankList
        );
        mongoTemplate.save(doc);
    }
}
