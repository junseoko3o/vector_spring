package com.milvus.vector_spring.content;

import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
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

    @Transactional
    public Content createContent(long userId, ContentCreateRequestDto contentCreateRequestDto) {
        User user = userService.findOneUser(userId);
        Content content = Content.builder()
                .title(contentCreateRequestDto.getTitle())
                .answer(contentCreateRequestDto.getAnswer())
                .user(user)
                .build();
        return contentRepository.save(content);
    }
}
