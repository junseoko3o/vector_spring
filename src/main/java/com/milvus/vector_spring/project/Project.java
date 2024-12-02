package com.milvus.vector_spring.project;

import com.milvus.vector_spring.common.BaseTimeEntity;
import com.milvus.vector_spring.content.Content;
import com.milvus.vector_spring.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "project")
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> contents= new ArrayList<>();

    @CreatedBy
    @ManyToOne()
    @JoinColumn(name = "created_user_id", nullable = false)
    private User createdProjectUser;

    @LastModifiedBy
    @ManyToOne()
    @JoinColumn(name = "updated_user_id", nullable = true)
    private User updatedProjectUser;
}
