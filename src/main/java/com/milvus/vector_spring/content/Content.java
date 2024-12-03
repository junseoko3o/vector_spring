package com.milvus.vector_spring.content;

import com.milvus.vector_spring.common.BaseEntity;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Content extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "answer", nullable = false)
    private String answer;

    @ManyToOne()
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_user_id", nullable = false)
    protected User createdBy;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "updated_user_id")
    protected User updatedBy;

    @Builder
    public Content(Long id, String title, String answer, Project projects, LocalDateTime createdAt, LocalDateTime updatedAt, User createdBy, User updatedBy) {
        this.id = id;
        this.title = title;
        this.answer = answer;
        this.project = projects;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
