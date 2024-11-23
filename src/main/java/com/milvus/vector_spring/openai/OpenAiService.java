package com.milvus.vector_spring.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milvus.vector_spring.config.WebClientConfig;
import com.milvus.vector_spring.openai.dto.EmbedRequestDto;
import com.milvus.vector_spring.openai.dto.EmbedResponseDto;
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

    @Value("${open.ai.key}")
    private String openAiKey;

    private final WebClientConfig webClientConfig;

    private WebClient connect(String url) {
        return webClientConfig.webClientBuilder()
                .baseUrl(url)
                .defaultHeader("Authorization", "Bearer " + openAiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String chat(ChatRequestDto chatRequestDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(chatRequestDto)
        );

        try {
            String res = connect(openAiKey).post()
                    .uri(openAiUrl)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode rootNode = objectMapper.readTree(res);
            JsonNode contentNode = rootNode.path("choices").get(0).path("message").path("content");

            return contentNode.asText();

        } catch (Exception e) {
            throw new RuntimeException("Failed to send request", e);
        }
    }

    public EmbedResponseDto embedding(EmbedRequestDto embedRequestDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> requestBody = Map.of(
                "model", "text-embedding-3-large",
                "dimension", 3092,
                "input", embedRequestDto.getEmbedText()
        );

        try {
            String res = connect(embedUrl).post()
                    .uri(embedUrl)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(res);
            JsonNode rootNode = objectMapper.readTree(res);
            JsonNode embedding = rootNode.path("data").get(0).path("embedding");
            JsonNode usage = rootNode.path("usage");
            return new EmbedResponseDto(embedding, usage);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("", e);
        }

    }
}
