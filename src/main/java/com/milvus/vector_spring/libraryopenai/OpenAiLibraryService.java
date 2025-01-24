package com.milvus.vector_spring.libraryopenai;

import com.milvus.vector_spring.libraryopenai.dto.OpenAiChatLibraryRequestDto;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.JsonValue;
import com.openai.models.*;
import org.springframework.stereotype.Service;

@Service
public class OpenAiLibraryService {


    private OpenAIClient connectOpenAI(String openAiKey) {
        return OpenAIOkHttpClient.builder()
                .apiKey(openAiKey)
                .build();
    }

    private ChatModel findModel(String model) {
        return switch (model) {
            case "GPT_4O_LATEST" -> ChatModel.CHATGPT_4O_LATEST;
            case "GPT_4O_MINI" -> ChatModel.GPT_4O_MINI;
            case "GPT_4O" -> ChatModel.GPT_4O;
            default -> throw new IllegalArgumentException("Unknown model: " + model);
        };
    }

    private EmbeddingModel findEmbedModel(String model) {
        return switch (model) {
            case "GPT_EMBED_LARGE" -> EmbeddingModel.TEXT_EMBEDDING_3_LARGE;
            case "GPT_EMBED_SMALL" -> EmbeddingModel.TEXT_EMBEDDING_3_SMALL;
            default -> throw new IllegalArgumentException("Unknown model: " + model);
        };
    }

    public ChatCompletion chat(OpenAiChatLibraryRequestDto openAiChatLibraryRequestDto)  {
            String openAiKey = "";
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .addMessage(ChatCompletionUserMessageParam.builder()
                            .role(JsonValue.from(openAiChatLibraryRequestDto.getRole()))
                            .content(openAiChatLibraryRequestDto.getContent())
                            .build())
                    .model(findModel(openAiChatLibraryRequestDto.getModel()))
                    .build();
            System.out.println(params);

            return connectOpenAI(openAiKey).chat().completions().create(params);
        }
    }
