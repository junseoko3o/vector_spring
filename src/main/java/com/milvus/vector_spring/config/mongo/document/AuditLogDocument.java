package com.milvus.vector_spring.config.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "audit_logs")
public class AuditLogDocument {
    public enum ActionType {
        CREATE, UPDATE, DELETE
    }

    @Id
    private String id;

    private String sessionId;
    private String email;
    private ActionType action;
    private String method;
    private String parameters;
    private LocalDateTime timestamp;
}