package com.milvus.vector_spring.invite;

import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invite, Long> {
    Optional<Invite> findByProjectAndReceivedId(Project project, Long receivedId);
    Optional<List<Invite>> findByCreatedBy(User createdBy);
    Optional<List<Invite>> findByCreatedByAndProject(User createdBy, Project project);
}
