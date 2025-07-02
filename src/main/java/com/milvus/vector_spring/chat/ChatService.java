package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.dto.ChatRequestDto;
import com.milvus.vector_spring.chat.dto.ChatResponseDto;
import com.milvus.vector_spring.chat.dto.VectorSearchRankDto;
import com.milvus.vector_spring.chat.dto.VectorSearchResponseDto;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.content.Content;
import com.milvus.vector_spring.content.ContentQueryService;
import com.milvus.vector_spring.milvus.VectorSearchService;
import com.milvus.vector_spring.openai.dto.OpenAiChatResponseDto;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.ProjectCommandService;
import com.milvus.vector_spring.project.ProjectCryptoService;
import com.milvus.vector_spring.project.ProjectQueryService;
import com.milvus.vector_spring.user.UserQueryService;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.completions.CompletionUsage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatOptionService chatOptionService;
    @Value("${open.ai.key}")
    private String openAiKey;

    private final UserQueryService userQueryService;
    private final ProjectQueryService projectService;
    private final ProjectCommandService projectCommandService;
    private final ProjectCryptoService projectCryptoService;

    private final ContentQueryService contentQueryService;

    private final VectorSearchService vectorSearchService;
    private final ChatCompletionService chatCompletionService;
    private final ChatResponseService chatResponseService;

    public ChatResponseDto chat(ChatRequestDto chatRequestDto) {
        LocalDateTime inputDateTime = LocalDateTime.now();

        try {
            userQueryService.findOneUser(chatRequestDto.getUserId());

            Project project = projectService.findOneProjectByKey(chatRequestDto.getProjectKey());
            if (project.getOpenAiKey().isEmpty() && project.getChatModel().isEmpty()) {
                throw new CustomException(ErrorStatus.REQUIRE_OPEN_AI_INFO);
            }

            String secretKey = projectCryptoService.decryptOpenAiKey(project);

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
                    .flatMap(contentQueryService::findOneContentByContentId)
                    .orElse(null);

            LocalDateTime outputDateTime = LocalDateTime.now();

            long totalToken = embeddingResponse.usage().totalTokens() +
                    answer.usage().stream()
                            .mapToLong(CompletionUsage::totalTokens)
                            .sum();

            projectCommandService.plusTotalToken(project, totalToken);

            ChatResponseDto chatResponseDto = chatResponseService.buildResponse(
                    chatRequestDto,
                    finalAnswer,
                    inputDateTime,
                    outputDateTime,
                    content,
                    rankList,
                    searchResponse.getSearch()
            );

            chatResponseService.saveResponse(
                    chatRequestDto,
                    finalAnswer,
                    inputDateTime,
                    outputDateTime,
                    content,
                    rankList
            );

            return chatResponseDto;

        } catch (Exception e) {
            throw new CustomException(ErrorStatus.OPEN_AI_ERROR);
        }
    }

    public OpenAiChatResponseDto testChat(String question) {
        return chatOptionService.onlyOpenAiAnswer(openAiKey, question, "");
    }
}
