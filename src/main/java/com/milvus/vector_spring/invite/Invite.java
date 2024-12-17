package com.milvus.vector_spring.invite;

import com.milvus.vector_spring.common.BaseEntity;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "invite")
public class Invite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "received_id")
    private Long receivedId;

    @ManyToOne
    @JoinColumn(name = "invited_id", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Builder
    public Invite(Long id, Long receivedId, User createdBy, Project project) {
        this.id = id;
        this.receivedId = receivedId;
        this.createdBy = createdBy;
        this.project = project;
    }
}
