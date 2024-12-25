package com.milvus.vector_spring.chat;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class ChatCollectionInitializer {

    @Bean
    public CommandLineRunner initChatCollection(MongoTemplate mongoTemplate) {
        return args -> {
            String schema = """
            {
              "bsonType": "object",
              "required": ["sessionId", "projectKey", "input", "output", "vectorOutput", "inputDateTime", "outputDateTime", "rank"],
              "properties": {
                "sessionId": { "bsonType": "string" },
                "projectKey": { "bsonType": "string" },
                "input": { "bsonType": "string" },
                "output": { "bsonType": "string" },
                "vectorOutput": { "bsonType": "string" },
                "content": { "bsonType": "object" },
                "inputDateTime": { "bsonType": "date" },
                "outputDateTime": { "bsonType": "date" },
                "rank": {
                  "bsonType": "array",
                  "items": {
                    "bsonType": "object",
                    "properties": {
                      "answer": { "bsonType": "string" },
                      "title": { "bsonType": "string" },
                      "score": { "bsonType": "double" },
                      "id": { "bsonType": "int" }
                    }
                  }
                }
              }
            }
            """;

            mongoTemplate.getDb().runCommand(new org.bson.Document("create", "chat_response")
                    .append("validator", new org.bson.Document("$jsonSchema", org.bson.Document.parse(schema))));

            System.out.println("chat_responses collection initialized.");
        };
    }
}

