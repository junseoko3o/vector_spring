package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.dto.VectorSearchResponseDto;
import com.milvus.vector_spring.milvus.MilvusService;
import com.milvus.vector_spring.openai.OpenAiService;
import com.milvus.vector_spring.openai.dto.OpenAiChatRequestDto;
import com.milvus.vector_spring.openai.dto.OpenAiChatResponseDto;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatOptionService {
    private final OpenAiService openAiService;
    private final MilvusService milvusService;

    public String prompt(String text, List<String> data) {
        return "You are a chatbot designed to understand user questions accurately and provide helpful responses.\n" +
                "Please follow the steps below in order, but do not include step 1 in your response.\\n\n" +
                "1. Analyze the user utterance \"" + text + "\"to determine the user's question intent, but do not include this intent in your response.\n" +
                "2. If there is relevant information in , use it to provide an answer that directly addresses the user's question intent.\n" +
                "3. If there is no relevant information in , inform the user that there is no related information and ask them to try another question.\n" +
                "4. You must answer in korean and only user's answer\n" +
                data;
    }

    public OpenAiChatResponseDto openAiChatResponse(String openAiKey, String prompt) {
        List<OpenAiChatRequestDto.OpenAiMessageDto> messages = List.of(
                new OpenAiChatRequestDto.OpenAiMessageDto("system", prompt)
        );
        OpenAiChatRequestDto requestDto = new OpenAiChatRequestDto("gpt-4o", messages);
        return openAiService.chat(openAiKey, requestDto);
    }

    public OpenAiChatResponseDto onlyOpenAiAnswer(String openAiKey, String question) {
        List<OpenAiChatRequestDto.OpenAiMessageDto> messages = List.of(
                new OpenAiChatRequestDto.OpenAiMessageDto("user", question)
        );
        OpenAiChatRequestDto requestDto = new OpenAiChatRequestDto("gpt-4o", messages);
        return openAiService.chat(openAiKey, requestDto);
    }

    public VectorSearchResponseDto vectorSearchResult(List<Float> vector) {
        SearchResp search = milvusService.vectorSearch(vector);
        Long id = (Long) search.getSearchResults().stream()
                .findFirst()
                .flatMap(results -> results.stream().findFirst())
                .map(SearchResp.SearchResult::getId)
                .orElse(null);

        List<String> answers = search.getSearchResults().stream()
                .flatMap(Collection::stream)
                .map(result -> (String) result.getEntity().get("answer"))
                .collect(Collectors.toList());

        return new VectorSearchResponseDto(search, id, answers);
    }
}
