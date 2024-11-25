package com.milvus.vector_spring.milvus;

import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
import com.milvus.vector_spring.openai.dto.EmbedResponseDto;
import io.milvus.v2.service.vector.request.UpsertReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/milvus")
public class MilvusController {

    private final MilvusService milvusService;

    public MilvusController(MilvusService milvusService) {
        this.milvusService = milvusService;
    }

    @GetMapping()
    public String connect() {
        try {
            milvusService.connect();
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }

    @GetMapping("/create")
    public String create() throws IOException {
        milvusService.createSchema(1);
        return "Success";
    }

    @GetMapping("/check")
    public boolean checkMilvus() throws IOException {
        return milvusService.checkCollectionLoadState();
    }

    @PostMapping("/upsert")
    public UpsertReq upsertCollection(
            @RequestParam long id,
            @RequestBody EmbedResponseDto embedResponseDto,
            @RequestBody ContentCreateRequestDto contentCreateRequestDto) {

        return milvusService.upsertCollection(id, embedResponseDto, contentCreateRequestDto);
    }

}
