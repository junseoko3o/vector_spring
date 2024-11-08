package com.milvus.vector_spring.milvus;

import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.DataType;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.AddFieldReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.GetLoadStateReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class MilvusService {

    @Value("${cluster.endpoint}")
    private String clusterEndpoint;

    @Value("${collection.name}")
    private String collectionName;

    private MilvusClientV2 client;

    public void connect() {
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(clusterEndpoint)
                .build();

        client = new MilvusClientV2(connectConfig);

        System.out.println("Connected to Milvus at: " + clusterEndpoint);
    }

    public void createSchema() {
        connect();
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

        List<IndexParam> indexParamList = createIndex();

        CreateCollectionReq createCollectionReq = CreateCollectionReq.builder()
                .collectionName(collectionName)
                .collectionSchema(schema)
                .indexParams(indexParamList)
                .build();

        client.createCollection(createCollectionReq);


        GetLoadStateReq getLoadStateReq = loadCollection(collectionName);
        client.getLoadState(getLoadStateReq);
    }

    public List<IndexParam> createIndex() {
        IndexParam indexParamForVectorField = IndexParam.builder()
                .fieldName("vector")
                .indexType(IndexParam.IndexType.HNSW)
                .metricType(IndexParam.MetricType.COSINE)
                .extraParams(Map.of("efConstruction", 100, "M", 16))
                .build();

        List<IndexParam> indexParamList = new ArrayList<>();
        indexParamList.add(indexParamForVectorField);
        return indexParamList;
    }

    public GetLoadStateReq loadCollection(String collectionName) {
        return GetLoadStateReq.builder()
                .collectionName(collectionName)
                .build();
    }

    public void checkCollectionLoadState() {
        GetLoadStateReq loadStateReq = GetLoadStateReq.builder()
                .collectionName(collectionName)
                .build();

        Boolean res = client.getLoadState(loadStateReq);

        System.out.println("Collection load state: " + res);
    }
}
