package com.milvus.vector_spring.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.milvus.vector_spring.openai.dto.EmbedRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open-ai")
public class OpenAiController {

    private final OpenAiService openAiService;


    public OpenAiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/test")
    public JsonNode test(@RequestBody ChatRequestDto chatRequestDto) {
        return openAiService.chat(chatRequestDto);
    }

    @PostMapping("/embed")
    public JsonNode test(@RequestBody EmbedRequestDto embedRequestDto) {
        return openAiService.embedding(embedRequestDto);
    }
}
