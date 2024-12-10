package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.dto.ChatRequestDto;
import com.milvus.vector_spring.chat.dto.ChatResponseDto;
import com.milvus.vector_spring.chat.dto.VectorSearchResponseDto;
import com.milvus.vector_spring.common.EncryptionService;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserService userService;
    private final ProjectService projectService;
    private final ContentService contentService;
    private final OpenAiService openAiService;
    private final ChatOptionService chatOptionService;
    private final EncryptionService encryptionService;

    public ChatResponseDto chat(ChatRequestDto chatRequestDto) {
        LocalDateTime inputDateTime = LocalDateTime.now();
        userService.findOneUser(chatRequestDto.getUserId());

        Project project = projectService.findOneProjectByKey(chatRequestDto.getProjectKey());
        String secretKey = encryptionService.decryptData(project.getOpenAiKey());

        EmbedRequestDto embedRequestDto = EmbedRequestDto.builder()
                .embedText(chatRequestDto.getText())
                .build();
        OpenAiEmbedResponseDto openAiEmbedResponseDto = openAiService.embedding(secretKey, embedRequestDto);

        VectorSearchResponseDto vectorSearchResponseDto = chatOptionService.vectorSearchResult(openAiEmbedResponseDto.getData().get(0).getEmbedding());

        OpenAiChatResponseDto openAiChatResponseDto = chatOptionService.openAiChatResponse(
                secretKey,
                chatOptionService.prompt(chatRequestDto.getText(), vectorSearchResponseDto.getAnswers())
        );

        Content content = contentService.findOneContentById(vectorSearchResponseDto.getFirstSearchId());
        LocalDateTime outputDateTime = LocalDateTime.now();
        return ChatResponseDto.chatResponseDto(
                project.getKey(),
                "DEV",
                chatRequestDto.getText(),
                openAiChatResponseDto.getChoices().get(0).getMessage().getContent(),
                inputDateTime,
                outputDateTime,
                vectorSearchResponseDto.getSearch(),
                ContentResponseDto.contentResponseDto(content)
        );
    }
}
