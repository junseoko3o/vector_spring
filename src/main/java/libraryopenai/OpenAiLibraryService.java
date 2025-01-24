package libraryopenai;

import com.milvus.vector_spring.openai.dto.OpenAiChatRequestDto;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.JsonValue;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatCompletionUserMessageParam;
import libraryopenai.dto.OpenAiChatLibraryRequestDto;
import org.springframework.stereotype.Service;

@Service
public class OpenAiLibraryService {


    private OpenAIClient connectOpenAI(String openAiKey) {
        return OpenAIOkHttpClient.builder()
                .apiKey(openAiKey)
                .build();
    }

    public ChatCompletion chat(String openAiKey, OpenAiChatLibraryRequestDto openAiChatLibraryRequestDto) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addMessage(ChatCompletionUserMessageParam.builder()
                        .role(JsonValue.from(openAiChatLibraryRequestDto.getRole()))
                        .content(openAiChatLibraryRequestDto.getContent())
                        .build())
                .model(openAiChatLibraryRequestDto.getModel())
                .build();

        return connectOpenAI(openAiKey).chat().completions().create(params);
    }
}
