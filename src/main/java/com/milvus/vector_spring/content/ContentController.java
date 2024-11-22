package com.milvus.vector_spring.content;

import com.milvus.vector_spring.common.apipayload.ApiResponse;
import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
import com.milvus.vector_spring.content.dto.ContentCreateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {

    private final ContentService contentService;

    @PostMapping("/create")
    public ApiResponse<ContentCreateResponseDto> createContent(@Validated @RequestBody ContentCreateRequestDto contentCreateRequestDto) {
        Content content = contentService.createContent(contentCreateRequestDto);
        ContentCreateResponseDto response = ContentCreateResponseDto.of(content);
        return ApiResponse.ok(response);
    }
}
