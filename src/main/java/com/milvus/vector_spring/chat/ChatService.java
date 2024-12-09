package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.dto.ChatRequestDto;
import com.milvus.vector_spring.chat.dto.ChatResponseDto;
import com.milvus.vector_spring.common.EncryptionService;
import com.milvus.vector_spring.content.Content;
import com.milvus.vector_spring.content.ContentService;
import com.milvus.vector_spring.content.dto.ContentResponseDto;
import com.milvus.vector_spring.milvus.MilvusService;
import com.milvus.vector_spring.openai.OpenAiService;
import com.milvus.vector_spring.openai.dto.EmbedRequestDto;
import com.milvus.vector_spring.openai.dto.OpenAiEmbedResponseDto;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.ProjectService;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserService;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserService userService;
    private final ProjectService projectService;
    private final ContentService contentService;
    private final MilvusService milvusService;
    private final OpenAiService openAiService;
    private final EncryptionService encryptionService;

    public ChatResponseDto chat(ChatRequestDto chatRequestDto) {
        LocalDateTime inputDateTime = LocalDateTime.now();
        User user = userService.findOneUser(chatRequestDto.getUserId());
        Project project = projectService.findOneProjectByKey(chatRequestDto.getProjectKey());
        EmbedRequestDto embedRequestDto = EmbedRequestDto.builder()
                .embedText(chatRequestDto.getText())
                .build();
        String secretKey = encryptionService.decryptData(project.getOpenAiKey());
        OpenAiEmbedResponseDto openAiEmbedResponseDto = openAiService.embedding(secretKey, embedRequestDto);
        SearchResp search = milvusService.vectorSearch(openAiEmbedResponseDto.getData().get(0).getEmbedding());
        Long id = (Long) search.getSearchResults().stream()
                .findFirst()
                .flatMap(results -> results.stream().findFirst())
                .map(SearchResp.SearchResult::getId)
                .orElse(null);
        Content content = contentService.findOneContById(id);
        return ChatResponseDto.chatResponseDto(
                project.getKey(),
                "asdf",
                chatRequestDto.getText(),
                "output",
                inputDateTime,
                LocalDateTime.now(),
                search,
                ContentResponseDto.contentResponseDto(content)
        );
    }
}
