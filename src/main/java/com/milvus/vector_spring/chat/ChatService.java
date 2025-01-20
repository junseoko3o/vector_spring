package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.dto.ChatRequestDto;
import com.milvus.vector_spring.chat.dto.ChatResponseDto;
import com.milvus.vector_spring.chat.dto.VectorSearchRankDto;
import com.milvus.vector_spring.chat.dto.VectorSearchResponseDto;
import com.milvus.vector_spring.common.service.EncryptionService;
import com.milvus.vector_spring.content.Content;
import com.milvus.vector_spring.content.ContentService;
import com.milvus.vector_spring.content.dto.ContentResponseDto;
import com.milvus.vector_spring.openai.OpenAiService;
import com.milvus.vector_spring.openai.dto.EmbedRequestDto;
import com.milvus.vector_spring.openai.dto.OpenAiChatResponseDto;
import com.milvus.vector_spring.openai.dto.OpenAiEmbedResponseDto;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.ProjectService;
import com.milvus.vector_spring.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserService userService;
    private final ProjectService projectService;
    private final ContentService contentService;
    private final OpenAiService openAiService;
    private final ChatOptionService chatOptionService;
    private final EncryptionService encryptionService;
    private final MongoTemplate mongoTemplate;

    public ChatResponseDto chat(ChatRequestDto chatRequestDto) {
        LocalDateTime inputDateTime = LocalDateTime.now();

        validateUser(chatRequestDto.getUserId());
        Project project = getProject(chatRequestDto.getProjectKey());
        String secretKey = encryptionService.decryptData(project.getOpenAiKey());

        OpenAiEmbedResponseDto embedResponse = getEmbedding(secretKey, chatRequestDto.getText());
        VectorSearchResponseDto searchResponse = performVectorSearch(embedResponse);
        List<VectorSearchRankDto> rankList = mapSearchResultsToRankList(searchResponse);
        String prompt = project.getPrompt();
        String finalAnswer = generateFinalAnswer(project, chatRequestDto.getText(), secretKey, rankList, searchResponse, prompt);

        Content content = contentService.findOneContentById(searchResponse.getFirstSearchId());
        LocalDateTime outputDateTime = LocalDateTime.now();

        ChatResponseDto chatResponseDto = buildChatResponse(project, chatRequestDto, finalAnswer, inputDateTime, outputDateTime, searchResponse, content);

        mongoTemplate.save(chatRequestDto, "chat_response");
        return chatResponseDto;
    }

    private void validateUser(Long userId) {
        userService.findOneUser(userId);
    }

    private Project getProject(String projectKey) {
        return projectService.findOneProjectByKey(projectKey);
    }

    private OpenAiEmbedResponseDto getEmbedding(String secretKey, String text) {
        EmbedRequestDto embedRequest = EmbedRequestDto.builder()
                .embedText(text)
                .build();
        return openAiService.embedding(secretKey, embedRequest);
    }

    private VectorSearchResponseDto performVectorSearch(OpenAiEmbedResponseDto embedResponse) {
        return chatOptionService.vectorSearchResult(embedResponse.getData().get(0).getEmbedding());
    }

    private List<VectorSearchRankDto> mapSearchResultsToRankList(VectorSearchResponseDto searchResponse) {
        return searchResponse.getSearch().getSearchResults().stream()
                .flatMap(List::stream)
                .map(result -> {
                    Map<String, Object> entity = result.getEntity();
                    return VectorSearchRankDto.builder()
                            .answer((String) entity.get("answer"))
                            .title((String) entity.get("title"))
                            .score(result.getScore())
                            .id((Long) result.getId())
                            .build();
                })
                .toList();
    }

    private String generateFinalAnswer(Project project, String text, String secretKey, List<VectorSearchRankDto> rankList, VectorSearchResponseDto searchResponse, String prompt) {
        if (!rankList.isEmpty() && rankList.get(0).getScore() >= 0.5) {
            if (prompt.isEmpty()) {
                prompt = chatOptionService.prompt(text, searchResponse.getAnswers());
            }
            OpenAiChatResponseDto chatResponse = chatOptionService.openAiChatResponse(
                    secretKey,
                    prompt,
                    project.getBasicModel()
            );
            return chatResponse.getChoices().get(0).getMessage().getContent();
        }
        OpenAiChatResponseDto fallbackResponse = chatOptionService.onlyOpenAiAnswer(secretKey, text, project.getBasicModel());
        return fallbackResponse.getChoices().get(0).getMessage().getContent();
    }

    private ChatResponseDto buildChatResponse(
            Project project, ChatRequestDto chatRequestDto, String finalAnswer,
            LocalDateTime inputDateTime, LocalDateTime outputDateTime,
            VectorSearchResponseDto searchResponse, Content content) {
        chatRequestDto.setSessionId("DEV");
        return ChatResponseDto.chatResponseDto(
                project.getKey(),
                chatRequestDto.getSessionId(),
                chatRequestDto.getText(),
                finalAnswer,
                inputDateTime,
                outputDateTime,
                searchResponse.getSearch(),
                ContentResponseDto.contentResponseDto(content)
        );
    }
}