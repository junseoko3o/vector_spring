package com.milvus.vector_spring.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.config.WebClientConfig;
import com.milvus.vector_spring.openai.dto.EmbedRequestDto;
import com.milvus.vector_spring.openai.dto.OpenAiChatResponseDto;
import com.milvus.vector_spring.openai.dto.OpenAiEmbedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    @Value("${open.ai.url}")
    private String openAiUrl;

    @Value("${embed.url}")
    private String embedUrl;

    private final WebClientConfig webClientConfig;

    private WebClient connect(String url, String openAiKey) {
        return webClientConfig.webClientBuilder()
                .baseUrl(url)
                .defaultHeader("Authorization", "Bearer " + openAiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public OpenAiChatResponseDto chat(String openAiKey, ChatRequestDto chatRequestDto) throws CustomException{
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(chatRequestDto)
        );
        try {
            String res = connect(openAiUrl, openAiKey).post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return objectMapper.readValue(res, OpenAiChatResponseDto.class);
        } catch (Exception e) {
            throw new CustomException(ErrorStatus._OPEN_AI_ERROR);
        }
    }

    public OpenAiEmbedResponseDto embedding(String openAiKey, EmbedRequestDto embedRequestDto) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> requestBody = Map.of(
                "model", "text-embedding-3-large",
                "dimension", 3072,
                "input", embedRequestDto.getEmbedText()
        );
        try {
            String res = connect(embedUrl, openAiKey).post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return objectMapper.readValue(res, OpenAiEmbedResponseDto.class);
        } catch (Exception e) {
            throw new CustomException(ErrorStatus._OPEN_AI_ERROR);
        }
    }
}
