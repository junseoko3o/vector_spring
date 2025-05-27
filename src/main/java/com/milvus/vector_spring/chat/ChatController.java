package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.dto.ChatRequestDto;
import com.milvus.vector_spring.chat.dto.ChatResponseDto;
import com.milvus.vector_spring.common.annotation.RateLimit;
import com.milvus.vector_spring.openai.dto.OpenAiChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping()
    private ChatResponseDto search(@RequestBody ChatRequestDto chatRequestDto) {
        return chatService.chat(chatRequestDto);
    }

    @PostMapping("/test")
    @RateLimit
    private OpenAiChatResponseDto testSearch(@RequestBody String question) {
        return chatService.testChat(question);
    }
}
