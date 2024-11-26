package com.milvus.vector_spring.content;

import com.milvus.vector_spring.common.apipayload.ApiResponse;
import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
import com.milvus.vector_spring.content.dto.ContentCreateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.milvus.vector_spring.common.Const.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {

    private final ContentService contentService;

    @PostMapping("/create")
    public ApiResponse<ContentCreateResponseDto> createContent(
            @RequestHeader(USER_ID) long userId,
            @Validated @RequestBody ContentCreateRequestDto contentCreateRequestDto
            ) throws IOException {
        Content content = contentService.createContent(userId, contentCreateRequestDto);
        ContentCreateResponseDto response = ContentCreateResponseDto.of(content);
        return ApiResponse.ok(response);
    }
}
