package com.milvus.vector_spring.content;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentQueryServiceImpl implements ContentQueryService {

    private final ContentRepository contentRepository;

    @Override
    public List<Content> findAllContent() {
        return contentRepository.findAll();
    }

    @Override
    public Content findOneContentById(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_CONTENT));
    }

    @Override
    public Optional<Content> findOneContentByContentId(Long id) {
        return contentRepository.findById(id);
    }
}

