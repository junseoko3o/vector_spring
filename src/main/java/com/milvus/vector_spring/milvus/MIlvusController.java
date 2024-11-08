package com.milvus.vector_spring.milvus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/milvus")
public class MIlvusController {

    private final MilvusService milvusService;

    @Autowired()
    public MIlvusController(MilvusService milvusService) {
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
}
