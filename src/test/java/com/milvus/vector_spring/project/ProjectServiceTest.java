package com.milvus.vector_spring.project;

import com.milvus.vector_spring.project.dto.ProjectCreateRequestDto;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserRepository;
import com.milvus.vector_spring.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@example.com")
                .username("test")
                .password("password")
                .build();
        userRepository.save(user);
    }

    @Test
    void createProject_Success() {
        ProjectCreateRequestDto dto = ProjectCreateRequestDto.builder()
                .name("Test Project")
                .createdUserId(user.getId())
                .dimensions(1536)
                .openAiKey(openAiApiKey)
                .chatModel("gpt-4")
                .embedModel("text-embedding-3-small")
                .prompt("You are a helpful AI assistant.")
                .build();

        Project saved = projectService.createProject(dto);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Project");
        assertThat(saved.getCreatedBy().getId()).isEqualTo(user.getId());
        assertThat(saved.getOpenAiKey()).isNotNull();
    }
}