package com.milvus.vector_spring.milvus;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.milvus.dto.InsertRequestDto;
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
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.request.UpsertReq;
import io.milvus.v2.service.vector.request.data.BaseVector;
import io.milvus.v2.service.vector.response.DeleteResp;
import io.milvus.v2.service.vector.response.SearchResp;
import io.milvus.v2.service.vector.response.UpsertResp;
import io.milvus.v2.service.vector.request.data.FloatVec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static io.milvus.v2.common.DataType.FloatVector;

@Service
public class MilvusService implements MilvusInterface {

    @Value("${cluster.endpoint}")
    private String clusterEndpoint;

    @Value("${collection.name}")
    private String collectionName;

    private MilvusClientV2 client;

    @Override
    public MilvusClientV2 connect() throws CustomException {
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(clusterEndpoint)
                .build();
        client = new MilvusClientV2(connectConfig);
        System.out.println("Connected to Milvus at: " + clusterEndpoint);
        return client;
    }

    public void createSchema(Long dbKey) {
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
                .dataType(FloatVector)
                .dimension(3072)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName("title")
                .dataType(DataType.VarChar)
                .maxLength(128)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName("answer")
                .dataType(DataType.VarChar)
                .maxLength(3092)
                .build());

        String collectionCustomName = String.format(collectionName, dbKey);
        List<IndexParam> indexParamList = createIndex(collectionCustomName);
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

    public boolean checkCollectionLoadState() {
        MilvusClientV2 client = connect();

        GetLoadStateReq loadStateReq = GetLoadStateReq.builder()
                .collectionName(collectionName)
                .build();

        Boolean res = client.getLoadState(loadStateReq);
        System.out.println("Collection load state: " + res);
        return res;
    }

    public UpsertResp upsertCollection(long id, InsertRequestDto insertRequestDto) {
        MilvusClientV2 client = connect();
        JsonObject dataObject = new JsonObject();
        JsonArray vectorArray = new JsonArray();
        for (Float v : insertRequestDto.getVector()) {
            vectorArray.add(v);
        }
        dataObject.addProperty("id", id);
        dataObject.add("vector", vectorArray);
        dataObject.addProperty("title", insertRequestDto.getTitle());
        dataObject.addProperty("answer", insertRequestDto.getAnswer());

        List<JsonObject> data = Arrays.asList(dataObject);
        UpsertReq upsertReq = UpsertReq.builder()
                .collectionName(collectionName)
                .data(data)
                .build();
        return client.upsert(upsertReq);
    }

    public DeleteResp deleteCollection(long id) {
        MilvusClientV2 client = connect();
        DeleteReq deleteReq = DeleteReq.builder()
                .collectionName(collectionName)
                .filter("id in [" + id + "]")
                .build();
        return client.delete(deleteReq);
    }

    public Boolean hasCollection() {
        MilvusClientV2 client = connect();
        HasCollectionReq hasCollectionReq = HasCollectionReq.builder()
                .collectionName(collectionName)
                .build();
        return client.hasCollection(hasCollectionReq);
    }

    public SearchResp vectorSearch(List<Float> vectorData) {
        MilvusClientV2 client = connect();
        List<BaseVector> baseVectors = new ArrayList<>();
        if (vectorData != null) {
            List<Float> floatList = new ArrayList<>(vectorData);
            baseVectors.add(new FloatVec(floatList));
        }
        SearchReq searchReq = SearchReq.builder()
                .collectionName(collectionName)
                .data(baseVectors)
                .topK(5)
                .searchParams(Map.of("metric_type", "COSINE", "efConstruction", 100, "M", 16))
                .build();

        return client.search(searchReq);
    }
}
