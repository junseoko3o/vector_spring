package com.milvus.vector_spring.milvus;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.milvus.vector_spring.content.dto.ContentCreateRequestDto;
import com.milvus.vector_spring.openai.dto.EmbedResponseDto;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.DataType;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.AddFieldReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.GetLoadStateReq;
import io.milvus.v2.service.collection.request.HasCollectionReq;
import io.milvus.v2.service.index.request.CreateIndexReq;
import io.milvus.v2.service.vector.request.DeleteReq;
import io.milvus.v2.service.vector.request.UpsertReq;
import io.milvus.v2.service.vector.response.DeleteResp;
import io.milvus.v2.service.vector.response.UpsertResp;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class MilvusService implements MilvusInterface {

    @Value("${cluster.endpoint}")
    private String clusterEndpoint;

    @Value("${collection.name}")
    private String collectionName;

    private MilvusClientV2 client;

    @Override
    public MilvusClientV2 connect() throws IOException {
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(clusterEndpoint)
                .build();

        client = new MilvusClientV2(connectConfig);
        System.out.println("Connected to Milvus at: " + clusterEndpoint);
        return client;
    }

    public void createSchema(Integer dbKey) throws IOException {
        MilvusClientV2 client = connect();

        CreateCollectionReq.CollectionSchema schema = client.createSchema();
        schema.addField(AddFieldReq.builder()
                .fieldName("id")
                .dataType(DataType.Int64)
                .isPrimaryKey(true)
                .autoID(false)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName("vector")
                .dataType(DataType.FloatVector)
                .dimension(3092)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName("name")
                .dataType(DataType.VarChar)
                .maxLength(128)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName("answer")
                .dataType(DataType.VarChar)
                .maxLength(2048)
                .build());

        String collectionCustomName = String.format(collectionName, dbKey);
        List<IndexParam> indexParamList = createIndex(collectionName);
        CreateCollectionReq createCollectionReq = CreateCollectionReq.builder()
                .collectionName(collectionCustomName)
                .collectionSchema(schema)
                .indexParams(indexParamList)
                .build();
        client.createCollection(createCollectionReq);

        GetLoadStateReq getLoadStateReq = loadCollection(collectionCustomName);
        client.getLoadState(getLoadStateReq);
    }

    private List<IndexParam> createIndex(String collectionName) {
        IndexParam indexParamForVectorField = IndexParam.builder()
                .fieldName("vector")
                .indexType(IndexParam.IndexType.HNSW)
                .metricType(IndexParam.MetricType.COSINE)
                .extraParams(Map.of("efConstruction", 100, "M", 16))
                .build();

        List<IndexParam> indexParamList = new ArrayList<>();
        indexParamList.add(indexParamForVectorField);

        CreateIndexReq.builder()
                .collectionName(collectionName)
                .indexParams(indexParamList)
                .build();
        return indexParamList;
    }

    private GetLoadStateReq loadCollection(String collectionName) {
        return GetLoadStateReq.builder()
                .collectionName(collectionName)
                .build();
    }

    public boolean checkCollectionLoadState() throws IOException {
        MilvusClientV2 client = connect();

        GetLoadStateReq loadStateReq = GetLoadStateReq.builder()
                .collectionName(collectionName)
                .build();

        Boolean res = client.getLoadState(loadStateReq);
        System.out.println("Collection load state: " + res);
        return res;
    }

    public UpsertResp upsertCollection(long id, EmbedResponseDto embedResponseDto, ContentCreateRequestDto contentCreateRequestDto) throws IOException {
        MilvusClientV2 client = connect();
        JsonArray embeddingArray = new JsonArray();
        embedResponseDto.getEmbedding().forEach(embeddingArray::add);

        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("id", id);
        jsonData.add("vector", embeddingArray);
        jsonData.addProperty("title", contentCreateRequestDto.getTitle());
        jsonData.addProperty("answer", contentCreateRequestDto.getAnswer());

        List<JsonObject> data = Arrays.asList(jsonData);
        UpsertReq upsertReq = UpsertReq.builder()
                .collectionName(collectionName)
                .data(data)
                .build();
        return client.upsert(upsertReq);
    }

    public DeleteResp deleteCollection(long id) throws IOException {
        MilvusClientV2 client = connect();
        DeleteReq deleteReq = DeleteReq.builder()
                .collectionName(collectionName)
                .filter("id in [" + id + "]")
                .build();
        return client.delete(deleteReq);
    }

    public Boolean hasCollection() throws IOException {
        MilvusClientV2 client = connect();
        HasCollectionReq hasCollectionReq = HasCollectionReq.builder()
                .collectionName(collectionName)
                .build();
        return client.hasCollection(hasCollectionReq);
    }
}
