package com.milvus.vector_spring.milvus;

import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
import io.milvus.v2.service.vector.response.UpsertResp;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/milvus")
public class MilvusController {

    private final MilvusService milvusService;

    public MilvusController(MilvusService milvusService) {
        this.milvusService = milvusService;
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
}
