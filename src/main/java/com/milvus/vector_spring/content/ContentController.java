package com.milvus.vector_spring.content;

import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
import com.milvus.vector_spring.content.dto.ContentResponseDto;
import com.milvus.vector_spring.content.dto.ContentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.milvus.vector_spring.common.Const.CONTENT_ID;
import static com.milvus.vector_spring.common.Const.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {

    private final ContentQueryService contentQueryService;
    private final ContentCommandService contentCommandService;

    @GetMapping()
    public List<ContentResponseDto> findAllContent() {
        List<Content> contentList = contentQueryService.findAllContent();
        return contentList.stream()
                .map(ContentResponseDto::contentResponseDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ContentResponseDto findOneContentById(@RequestHeader(CONTENT_ID) long id) {
        Content content = contentQueryService.findOneContentById(id);
        return ContentResponseDto.contentResponseDto(content);
    }

    @PostMapping("/create")
    public ResponseEntity<ContentResponseDto> createContenrt(
            @RequestHeader(USER_ID) long userId,
            @Validated @RequestBody ContentCreateRequestDto contentCreateRequestDto
            ) {
        Content content = contentCommandService.createContent(userId, contentCreateRequestDto);
        return ResponseEntity.ok(ContentResponseDto.contentResponseDto(content));
    }

    @PostMapping("/update")
    public ResponseEntity<ContentResponseDto> updateContent(
            @RequestHeader(CONTENT_ID) long id,
            @Validated @RequestBody ContentUpdateRequestDto contentUpdateRequestDto
            ) {
        Content content = contentCommandService.updateContent(id, contentUpdateRequestDto);
        return ResponseEntity.ok(ContentResponseDto.contentResponseDto(content));
    }
}
