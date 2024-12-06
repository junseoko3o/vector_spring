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

    private final ContentService contentService;

    @GetMapping()
    public ResponseEntity<List<Content>> findAllContent() {
        List<Content> contentList = contentService.findAllContent();
        return ResponseEntity.ok(contentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Content> findOneContentById(@RequestHeader(CONTENT_ID) long id) {
        Content content = contentService.findOneContById(id);
        return ResponseEntity.ok(content);
    }

    @PostMapping("/create")
    public ResponseEntity<ContentResponseDto> createContent(
            @RequestHeader(USER_ID) long userId,
            @Validated @RequestBody ContentCreateRequestDto contentCreateRequestDto
            ) {
        Content content = contentService.createContent(userId, contentCreateRequestDto);
        ContentResponseDto response = ContentResponseDto.of(content);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<ContentResponseDto> updateContent(
            @RequestHeader(CONTENT_ID) long id,
            @Validated @RequestBody ContentUpdateRequestDto contentUpdateRequestDto
            ) {
        Content content = contentService.updateContent(id, contentUpdateRequestDto);
        ContentResponseDto response = ContentResponseDto.of(content);
        return ResponseEntity.ok(response);
    }
}
