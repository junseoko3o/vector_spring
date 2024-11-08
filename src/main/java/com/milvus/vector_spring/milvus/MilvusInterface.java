package com.milvus.vector_spring.milvus;

import io.milvus.v2.client.MilvusClientV2;

import java.io.IOException;

public interface MilvusInterface {
    MilvusClientV2 connect() throws IOException;
}
