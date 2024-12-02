package com.milvus.vector_spring.content;

import com.milvus.vector_spring.common.BaseTimeEntity;
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
public class Content extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "answer", nullable = false)
    private String answer;

    @CreatedBy
    @ManyToOne()
    @JoinColumn(name = "created_user_id", nullable = false)
    private User createdUser;

    @LastModifiedBy
    @ManyToOne()
    @JoinColumn(name = "updated_user_id", nullable = true)
    private User updatedUser;

    @Builder
    public Content(Long id, String title, String answer, User createdUser, User updatedUser, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.answer = answer;
        this.createdUser = createdUser;
        this.updatedUser = updatedUser;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
