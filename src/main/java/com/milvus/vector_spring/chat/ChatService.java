package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.dto.ChatRequestDto;
import com.milvus.vector_spring.chat.dto.ChatResponseDto;
import com.milvus.vector_spring.chat.dto.VectorSearchRankDto;
import com.milvus.vector_spring.chat.dto.VectorSearchResponseDto;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.common.service.EncryptionService;
import com.milvus.vector_spring.content.Content;
import com.milvus.vector_spring.content.ContentService;
import com.milvus.vector_spring.content.dto.ContentResponseDto;
import com.milvus.vector_spring.libraryopenai.OpenAiLibraryService;
import com.milvus.vector_spring.libraryopenai.dto.OpenAiChatLibraryRequestDto;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.ProjectService;
import com.milvus.vector_spring.user.UserService;
import com.openai.models.ChatCompletion;
import com.openai.models.CompletionUsage;
import com.openai.models.CreateEmbeddingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserService userService;
    private final ProjectService projectService;
    private final ContentService contentService;
    private final ChatOptionService chatOptionService;
    private final EncryptionService encryptionService;
    private final MongoTemplate mongoTemplate;
    private final OpenAiLibraryService openAiLibraryService;

    public ChatResponseDto chat(ChatRequestDto chatRequestDto) {
        LocalDateTime inputDateTime = LocalDateTime.now();

        validateUser(chatRequestDto.getUserId());
        Project project = getProject(chatRequestDto.getProjectKey());
        if (project.getOpenAiKey().isEmpty() && project.getChatModel().isEmpty()) {
            throw new CustomException(ErrorStatus.REQUIRE_OPEN_AI_INFO);
        }
        String secretKey = encryptionService.decryptData(project.getOpenAiKey());

        CreateEmbeddingResponse embedResponse = getEmbedding(secretKey, chatRequestDto.getText(), project.getDimensions());
        VectorSearchResponseDto searchResponse = performVectorSearch(embedResponse);
        List<VectorSearchRankDto> rankList = mapSearchResultsToRankList(searchResponse);
        String prompt = project.getPrompt();
        ChatCompletion answer = generateFinalAnswer(project, chatRequestDto.getText(), secretKey, rankList, searchResponse, prompt);
        String finalAnswer = answer.choices().get(0).message().content().orElse("");

        Content content = contentService.findOneContentById(searchResponse.getFirstSearchId());
        LocalDateTime outputDateTime = LocalDateTime.now();
        long totalToken = embedResponse.usage().totalTokens() +
                answer.usage().stream()
                        .mapToLong(CompletionUsage::totalTokens)
                        .sum();
        projectService.plusTotalToken(project, totalToken);
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

    private CreateEmbeddingResponse getEmbedding(String secretKey, String text, long dimension) {
        return openAiLibraryService.embedding(secretKey, text, dimension);
    }

    private VectorSearchResponseDto performVectorSearch(CreateEmbeddingResponse embedResponse) {
        List<Float> floatList = embedResponse.data().get(0).embedding().stream()
                .map(Double::floatValue)
                .collect(Collectors.toList());
        return chatOptionService.vectorSearchResult(floatList);
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

    private ChatCompletion generateFinalAnswer(Project project, String text, String secretKey, List<VectorSearchRankDto> rankList, VectorSearchResponseDto searchResponse, String prompt) {
        var messages = new ArrayList<OpenAiChatLibraryRequestDto.OpenAiLibaryMessageDto>();
        messages.add(new OpenAiChatLibraryRequestDto.OpenAiLibaryMessageDto("user", text));
        OpenAiChatLibraryRequestDto dto = OpenAiChatLibraryRequestDto.builder()
                .model(project.getChatModel())
                .openAiKey(secretKey)
                .messages(messages)
                .build();
        if (!rankList.isEmpty() && rankList.get(0).getScore() >= 0.5) {
            if (prompt.isEmpty()) {
                prompt = chatOptionService.prompt(text, searchResponse.getAnswers());
            }
            messages.add(new OpenAiChatLibraryRequestDto.OpenAiLibaryMessageDto("system", prompt));
            return openAiLibraryService.chat(dto);
        }
        return openAiLibraryService.chat(dto);
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