package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.dto.ChatRequestDto;
import com.milvus.vector_spring.chat.dto.ChatResponseDto;
import com.milvus.vector_spring.openai.dto.OpenAiChatResponseDto;

public interface ChatService {
    ChatResponseDto chat(ChatRequestDto chatRequestDto);
    OpenAiChatResponseDto testChat(String question);
}
