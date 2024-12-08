package com.milvus.vector_spring.content;

import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
import com.milvus.vector_spring.content.dto.ContentResponseDto;
import com.milvus.vector_spring.content.dto.ContentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.milvus.vector_spring.common.Const.CONTENT_ID;
import static com.milvus.vector_spring.common.Const.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {

    private final ContentService contentService;

    @GetMapping()
    public List<ContentResponseDto> findAllContent() {
        List<Content> contentList = contentService.findAllContent();
        return contentList.stream()
                .map(ContentResponseDto::of)
                .toList();
    }

    @GetMapping("/{id}")
    public ContentResponseDto findOneContentById(@RequestHeader(CONTENT_ID) long id) {
        Content content = contentService.findOneContById(id);
        return ContentResponseDto.of(content);
    }

    @PostMapping("/create")
    public ContentResponseDto createContent(
            @RequestHeader(USER_ID) long userId,
            @Validated @RequestBody ContentCreateRequestDto contentCreateRequestDto
            ) {
        Content content = contentService.createContent(userId, contentCreateRequestDto);
        return ContentResponseDto.of(content);
    }

    @PostMapping("/update")
    public ContentResponseDto updateContent(
            @RequestHeader(CONTENT_ID) long id,
            @Validated @RequestBody ContentUpdateRequestDto contentUpdateRequestDto
            ) {
        Content content = contentService.updateContent(id, contentUpdateRequestDto);
        return ContentResponseDto.of(content);
    }
}
