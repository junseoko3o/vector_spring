package com.milvus.vector_spring.project;

import com.milvus.vector_spring.common.BaseEntity;
import com.milvus.vector_spring.content.Content;
import com.milvus.vector_spring.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "project")
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "open_ai_key", nullable = true)
    private String openAiKey;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> contents= new ArrayList<>();

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_user_id", nullable = false)
    protected User createdBy;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "updated_user_id")
    protected User updatedBy;

    @Builder
    public Project(Long id, String name, String key, String openAiKey, LocalDateTime createdAt, LocalDateTime updatedAt, User createdBy, User updatedBy) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.openAiKey = openAiKey;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}
