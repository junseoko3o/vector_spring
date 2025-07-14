package com.milvus.vector_spring.project;

import com.milvus.vector_spring.config.JpaAuditingConfig;
import com.milvus.vector_spring.config.QueryDslConfig;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({QueryDslConfig.class, JpaAuditingConfig.class})
@ActiveProfiles("test")
class ProjectRepositoryTest {

        @Autowired
        private ProjectRepository projectRepository;

        @Autowired
        private UserRepository userRepository;


    @Test
    @DisplayName("프로젝트 키로 프로젝트를 조회할 수 있다")
    void find_by_project_key_success() {
        // given
        User user = User.builder()
                .email("tester@example.com")
                .username("테스터")
                .password("securePassword123")
                .build();
        userRepository.save(user);

        Project project = Project.builder()
                .name("AI Research")
                .key("projectKey123")
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Project savedProject = projectRepository.save(project);

        // when
        Optional<Project> found = projectRepository.findProjectByKey(savedProject.getKey());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(found.get().getName());
        assertThat(found.get().getKey()).isEqualTo(found.get().getKey());
        assertThat(found.get().getCreatedBy().getEmail()).isEqualTo(user.getEmail());
    }
}