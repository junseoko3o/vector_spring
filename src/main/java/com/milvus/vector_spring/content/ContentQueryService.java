package com.milvus.vector_spring.content;

import java.util.List;
import java.util.Optional;

public interface ContentQueryService {
    List<Content> findAllContent();
    Content findOneContentById(Long id);
    Optional<Content> findOneContentByContentId(Long id);
}
