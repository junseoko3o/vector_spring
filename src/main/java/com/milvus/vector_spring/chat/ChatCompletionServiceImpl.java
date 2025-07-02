package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.dto.VectorSearchRankDto;
import com.milvus.vector_spring.chat.dto.VectorSearchResponseDto;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.libraryopenai.OpenAiLibraryService;
import com.milvus.vector_spring.libraryopenai.dto.OpenAiChatLibraryRequestDto;
import com.openai.models.chat.completions.ChatCompletion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatCompletionServiceImpl implements ChatCompletionService {

    private final ChatOptionService chatOptionService;
    private final OpenAiLibraryService openAiLibraryService;

    @Override
    public ChatCompletion generateAnswer(String chatModel, String userText, String openAiKey, List<VectorSearchRankDto> rankList, VectorSearchResponseDto searchResponse, String prompt) {
        try {
            String systemPrompt = prompt;
            boolean hasValidRank = !rankList.isEmpty() && rankList.get(0).getScore() >= 0.5;
            if (prompt == null || prompt.isEmpty()) {
                systemPrompt = hasValidRank
                        ? chatOptionService.prompt(userText, rankList.stream().map(VectorSearchRankDto::getAnswer).toList())
                        : chatOptionService.prompt(userText, searchResponse.getAnswers());
            }
            OpenAiChatLibraryRequestDto dto = OpenAiChatLibraryRequestDto.builder()
                    .model(chatModel)
                    .openAiKey(openAiKey)
                    .userMessages(userText)
                    .systemMesasges(systemPrompt)
                    .build();
            return openAiLibraryService.chat(dto);

        } catch (Exception e) {
            throw new CustomException(ErrorStatus.OPEN_AI_ERROR);
        }
    }
}
