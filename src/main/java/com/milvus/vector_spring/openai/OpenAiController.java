package com.milvus.vector_spring.openai;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open-ai")
public class OpenAiController {

    private final OpenAiService openAiService;


    public OpenAiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }
}
