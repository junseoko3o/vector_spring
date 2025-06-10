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
//       implementation 'com.kjetland:mbknor-jackson-jsonschema_2.13:1.0.39'
//        return args -> {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(objectMapper);
//            com.fasterxml.jackson.databind.JsonNode jsonSchema = schemaGen.generateJsonSchema(ChatResponseDocument.class);
//
//            Document validator = new Document("$jsonSchema", Document.parse(jsonSchema.toString()));
//
//            try {
//                mongoTemplate.getDb().runCommand(new Document("create", "chat_response")
//                        .append("validator", validator));
//                System.out.println("chat_response collection initialized with validation.");
//            } catch (Exception e) {
//                System.out.println("Collection may already exist or error occurred: " + e.getMessage());
//            }
//        };
//        }
    }

