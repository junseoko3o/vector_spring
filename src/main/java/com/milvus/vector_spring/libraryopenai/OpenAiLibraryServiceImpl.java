package com.milvus.vector_spring.libraryopenai;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.libraryopenai.dto.OpenAiChatLibraryRequestDto;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.EmbeddingCreateParams;
import com.openai.models.embeddings.EmbeddingModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAiLibraryServiceImpl implements OpenAiLibraryService {

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
            case "GPT_4_1" -> ChatModel.GPT_4_1;
            default -> throw new CustomException(ErrorStatus.UNKNOWING_MODEL);
        };
    }


    @Override
    public ChatCompletion chat(OpenAiChatLibraryRequestDto openAiChatLibraryRequestDto) {
        try {
            ChatModel model = findModel(openAiChatLibraryRequestDto.getModel());
            ChatCompletionCreateParams.Builder builder = ChatCompletionCreateParams.builder()
                    .addUserMessage(openAiChatLibraryRequestDto.getUserMessages())
                    .model(model);

            if (openAiChatLibraryRequestDto.getSystemMesasges() != null &&
                    !openAiChatLibraryRequestDto.getSystemMesasges().isBlank()) {
                builder.addSystemMessage(openAiChatLibraryRequestDto.getSystemMesasges());
            }

            ChatCompletionCreateParams params = builder.build();

            return connectOpenAI(openAiChatLibraryRequestDto.getOpenAiKey())
                    .chat()
                    .completions()
                    .create(params);
        } catch (Exception e) {
            System.err.println("OpenAI chat 호출 중 오류 발생: " + e.getMessage());
            throw new CustomException(ErrorStatus.OPEN_AI_ERROR);
        }
    }

    @Override
    public CreateEmbeddingResponse embedding(String openAiKey, String input, long dimension) {
        try {
            EmbeddingCreateParams params = EmbeddingCreateParams.builder()
                    .model(EmbeddingModel.TEXT_EMBEDDING_3_LARGE) // 고정값
                    .dimensions(dimension)
                    .input(input)
                    .build();
            return connectOpenAI(openAiKey).embeddings().create(params);
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.OPENAI_EMBEDDING_ERROR);
        }
    }
}
