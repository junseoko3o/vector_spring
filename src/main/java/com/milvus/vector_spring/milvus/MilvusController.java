package com.milvus.vector_spring.milvus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
