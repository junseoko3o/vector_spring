package com.milvus.vector_spring.milvus;

import com.milvus.vector_spring.milvus.dto.InsertRequestDto;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.service.vector.response.SearchResp;

import java.io.IOException;
import java.util.List;

public interface MilvusService {
    MilvusClientV2 connect() throws IOException;
    void createSchema(Long dbKey, int dimension);

    boolean checkCollectionLoadState(Long dbKey);

    void upsertCollection(long id, InsertRequestDto insertRequestDto, Long dbKey);

    void deleteCollection(long id);

    boolean hasCollection();

    SearchResp vectorSearch(List<Float> vectorData, Long dbKey);
}
