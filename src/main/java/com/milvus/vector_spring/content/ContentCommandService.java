package com.milvus.vector_spring.content;

import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
import com.milvus.vector_spring.content.dto.ContentUpdateRequestDto;

public interface ContentCommandService {
    Content createContent(long userId, ContentCreateRequestDto dto);
    Content updateContent(long id, ContentUpdateRequestDto dto);
}
