package com.milvus.vector_spring.content;

import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
import com.milvus.vector_spring.milvus.MilvusService;
import com.milvus.vector_spring.openai.OpenAiService;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final UserService userService;
    private final OpenAiService openAiService;
    private final MilvusService milvusService;

    @Transactional
    public Content createContent(long userId, ContentCreateRequestDto contentCreateRequestDto) {
        User user = userService.findOneUser(userId);
        Content content = Content.builder()
                .title(contentCreateRequestDto.getTitle())
                .answer(contentCreateRequestDto.getAnswer())
                .user(user)
                .build();

        String answer = contentCreateRequestDto.getAnswer();
//        milvusService.upsertCollection(content1.getId(), embedResponseDto, contentCreateRequestDto);
        return content;
    }
}
