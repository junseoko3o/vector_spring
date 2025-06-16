package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.dto.ChatRequestDto;
import com.milvus.vector_spring.chat.dto.ChatResponseDto;
import com.milvus.vector_spring.chat.dto.VectorSearchRankDto;
import com.milvus.vector_spring.chat.dto.VectorSearchResponseDto;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.common.service.EncryptionService;
import com.milvus.vector_spring.config.mongo.document.ChatResponseDocument;
import com.milvus.vector_spring.content.Content;
import com.milvus.vector_spring.content.ContentService;
import com.milvus.vector_spring.libraryopenai.OpenAiLibraryService;
import com.milvus.vector_spring.libraryopenai.dto.OpenAiChatLibraryRequestDto;
import com.milvus.vector_spring.openai.dto.OpenAiChatResponseDto;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.ProjectService;
import com.milvus.vector_spring.user.UserService;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.completions.CompletionUsage;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        userService.findOneUser(chatRequestDto.getUserId());
        Project project = projectService.findOneProjectByKey(chatRequestDto.getProjectKey());
        if (project.getOpenAiKey().isEmpty() && project.getChatModel().isEmpty()) {
            throw new CustomException(ErrorStatus.REQUIRE_OPEN_AI_INFO);
        }
        String secretKey = encryptionService.decryptData(project.getOpenAiKey());

        CreateEmbeddingResponse embedResponse = openAiLibraryService.embedding(secretKey, chatRequestDto.getText(), project.getDimensions());
        VectorSearchResponseDto searchResponse = performVectorSearch(embedResponse);
        List<VectorSearchRankDto> rankList = mapSearchResultsToRankList(searchResponse);
        String prompt = project.getPrompt();
        ChatCompletion answer = generateFinalAnswer(project.getChatModel(), chatRequestDto.getText(), secretKey, rankList, searchResponse, prompt);
        String finalAnswer = answer.choices().get(0).message().content().orElse("");
        Content content = Optional.ofNullable(searchResponse.getFirstSearchId())
                .flatMap(contentService::findOneContentByContentId)
                .orElse(null);
        LocalDateTime outputDateTime = LocalDateTime.now();
        long totalToken = embedResponse.usage().totalTokens() +
                answer.usage().stream()
                        .mapToLong(CompletionUsage::totalTokens)
                        .sum();
        projectService.plusTotalToken(project, totalToken);
        ChatResponseDto chatResponseDto = buildChatResponse(project, chatRequestDto, finalAnswer, inputDateTime, outputDateTime, searchResponse, content);
        saveChatResponse(chatRequestDto, finalAnswer, inputDateTime, outputDateTime, content, rankList);
        return chatResponseDto;
    }

    private VectorSearchResponseDto performVectorSearch(CreateEmbeddingResponse embedResponse) {
        List<Float> floatList = embedResponse.data().get(0).embedding();
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

    private ChatCompletion generateFinalAnswer(
            String chatModel, String text, String secretKey,
            List<VectorSearchRankDto> rankList, VectorSearchResponseDto searchResponse, String prompt) {

        try {
            String systemPrompt = prompt;
            boolean hasValidRank = !rankList.isEmpty() && rankList.get(0).getScore() >= 0.5;
            if (prompt == null || prompt.isEmpty()) {
                systemPrompt = hasValidRank
                        ? chatOptionService.prompt(text, rankList.stream().map(VectorSearchRankDto::getAnswer).toList())
                        : chatOptionService.prompt(text, searchResponse.getAnswers());
            }
            OpenAiChatLibraryRequestDto dto = OpenAiChatLibraryRequestDto.builder()
                    .model(chatModel)
                    .openAiKey(secretKey)
                    .userMessages(text)
                    .systemMesasges(systemPrompt)
                    .build();
            return openAiLibraryService.chat(dto);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new CustomException(ErrorStatus.OPEN_AI_ERROR);
        }
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
                content
        );
    }

    private void saveChatResponse(
            ChatRequestDto chatRequestDto,
            String finalAnswer,
            LocalDateTime inputDateTime,
            LocalDateTime outputDateTime,
            Content content,
            List<VectorSearchRankDto> rankList
    ) {
        ChatResponseDocument.SimpleContentDto simpleContent = Optional.ofNullable(content)
                .map(c -> new ChatResponseDocument.SimpleContentDto(
                        c.getId(),
                        c.getKey(),
                        c.getTitle(),
                        c.getAnswer()
                ))
                .orElse(new ChatResponseDocument.SimpleContentDto(null, null, null, null));
        ChatResponseDocument doc = new ChatResponseDocument(
                chatRequestDto.getSessionId(),
                chatRequestDto.getText(),
                finalAnswer,
                inputDateTime,
                outputDateTime,
                simpleContent,
                rankList
        );


        mongoTemplate.save(doc);
    }

    public OpenAiChatResponseDto testChat(String question) {
        return chatOptionService.onlyOpenAiAnswer("",question,"");
    }
}