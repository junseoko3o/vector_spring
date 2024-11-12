package com.milvus.vector_spring.openai;

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
    public String test(@RequestBody ChatRequestDto chatRequestDto) {
        return openAiService.chat(chatRequestDto);
    }
}
