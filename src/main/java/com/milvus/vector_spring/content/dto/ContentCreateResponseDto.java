package com.milvus.vector_spring.content.dto;

import com.milvus.vector_spring.content.Content;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.dto.UserResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ContentCreateResponseDto {
    private final Long id;
    private final String title;
    private final String answer;
    private final Long userId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ContentCreateResponseDto of (Content content) {
        return new ContentCreateResponseDto(
                content.getId(),
                content.getTitle(),
                content.getAnswer(),
                content.getUser().getId(),
                content.getCreatedAt(),
                content.getUpdatedAt()
        );
    }
}
