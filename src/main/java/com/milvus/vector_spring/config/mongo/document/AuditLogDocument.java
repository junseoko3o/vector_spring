package com.milvus.vector_spring.config.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "audit_logs")
public class AuditLogDocument {

    @Id
    private String id;

    private String username;
    private String action;
    private String method;
    private String parameters;
    private LocalDateTime timestamp;
}