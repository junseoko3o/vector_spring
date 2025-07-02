package com.milvus.vector_spring.openai;

import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.openai.dto.*;

public interface OpenAiService {

    OpenAiChatResponseDto chat(String openAiKey, OpenAiChatRequestDto openAiChatRequestDto) throws CustomException;

    OpenAiEmbedResponseDto embedding(String openAiKey, EmbedRequestDto embedRequestDto) throws CustomException;

    OpenAiZodResponseDto zod(String openAiKey, OpenAiChatRequestDto openAiChatRequestDto) throws CustomException;
}
