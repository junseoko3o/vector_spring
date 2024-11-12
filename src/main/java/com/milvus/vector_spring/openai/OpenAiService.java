package com.milvus.vector_spring.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.milvus.vector_spring.config.ObjectMapperConfig;
import com.milvus.vector_spring.config.WebClientConfig;
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

    @Value("${open.ai.key}")
    private String openAiKey;

    private final WebClientConfig webClientConfig;
    private final ObjectMapperConfig objectMapperConfig;

    private WebClient connect() {
        return webClientConfig.webClientBuilder()
                .baseUrl(openAiUrl)
                .defaultHeader("Authorization", "Bearer " + openAiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String chat(ChatRequestDto chatRequestDto) {
        System.out.println(chatRequestDto);
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(chatRequestDto)
        );

        try {
            String res = connect().post()
                    .uri(openAiUrl)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode rootNode = objectMapperConfig.objectMapper().readTree(res);
            JsonNode contentNode = rootNode.path("choices").get(0).path("message").path("content");

            return contentNode.asText();

        } catch (Exception e) {
            throw new RuntimeException("Failed to send request", e);
        }
    }
}
