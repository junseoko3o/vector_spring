package libraryopenai.dto;

import com.openai.models.ChatCompletionRole;
import com.openai.models.ChatModel;
import lombok.Getter;

@Getter
public class OpenAiChatLibraryRequestDto {
    private String content;
    private ChatCompletionRole role;
    private ChatModel model;
}
