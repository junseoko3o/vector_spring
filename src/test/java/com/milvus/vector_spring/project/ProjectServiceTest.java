package com.milvus.vector_spring.project;

import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.milvus.MilvusService;
import com.milvus.vector_spring.project.dto.ProjectCreateRequestDto;
import com.milvus.vector_spring.project.dto.ProjectDeleteRequestDto;
import com.milvus.vector_spring.project.dto.ProjectUpdateRequestDto;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Value("${open.ai.key}")
    private String openAiApiKey;

    @MockBean
    private MilvusService milvusService;

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
                .dimensions(3072)
                .openAiKey(openAiApiKey)
                .chatModel("gpt-4")
                .embedModel("text-embedding-3-small")
                .prompt("You are a helpful AI assistant.")
                .build();

        Project saved = projectService.createProject(dto);

        verify(milvusService).createSchema(saved.getId(), 3072);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Project");
        assertThat(saved.getCreatedBy().getId()).isEqualTo(user.getId());
        assertThat(saved.getOpenAiKey()).isNotNull();
    }

    @Test
    void updateProject_Success() {
        Project saved = projectService.createProject(ProjectCreateRequestDto.builder()
                .name("Old Project")
                .createdUserId(user.getId())
                .dimensions(3072)
                .openAiKey(openAiApiKey)
                .chatModel("gpt-4")
                .embedModel("text-embedding-3-small")
                .prompt("Initial Prompt")
                .build());

        Project updated = projectService.updateProject(saved.getKey(), ProjectUpdateRequestDto.builder()
                .name("Updated Project")
                .updatedUserId(user.getId())
                .dimensions(3072)
                .openAiKey(openAiApiKey)
                .chatModel("gpt-4o")
                .embedModel("text-embedding-3-large")
                .prompt("Updated Prompt")
                .build());

        assertThat(updated.getName()).isEqualTo("Updated Project");
        assertThat(updated.getChatModel()).isEqualTo("gpt-4o");
        assertThat(updated.getPrompt()).isEqualTo("Updated Prompt");
    }

    @Test
    void deleteProject_Success() {
        Project project = projectService.createProject(ProjectCreateRequestDto.builder()
                .name("Project to Delete")
                .createdUserId(user.getId())
                .dimensions(3072)
                .openAiKey(openAiApiKey)
                .chatModel("gpt-4")
                .embedModel("text-embedding-3-small")
                .prompt("To delete")
                .build());

        String result = projectService.deleteProject(ProjectDeleteRequestDto.builder()
                .key(project.getKey())
                .userId(user.getId())
                .build());

        verify(milvusService).deleteCollection(project.getId());
        assertThat(result).isEqualTo("Deleted Success!");
    }

    @Test
    void deleteProject_Fail_NotOwner() {
        Project project = projectService.createProject(ProjectCreateRequestDto.builder()
                .name("Project Unauthorized Delete")
                .createdUserId(user.getId())
                .dimensions(3072)
                .openAiKey(openAiApiKey)
                .chatModel("gpt-4")
                .embedModel("text-embedding-3-small")
                .prompt("Some prompt")
                .build());

        User anotherUser = User.builder()
                .email("other@example.com")
                .username("other")
                .password("password")
                .build();
        userRepository.save(anotherUser);

        assertThrows(CustomException.class, () -> {
            projectService.deleteProject(ProjectDeleteRequestDto.builder()
                    .key(project.getKey())
                    .userId(anotherUser.getId())
                    .build());
        });
    }

    @Test
    void plusTotalToken_Success() {
        Project project = projectService.createProject(ProjectCreateRequestDto.builder()
                .name("Token Project")
                .createdUserId(user.getId())
                .dimensions(3072)
                .openAiKey(openAiApiKey)
                .chatModel("gpt-4")
                .embedModel("text-embedding-3-small")
                .prompt("Prompt")
                .build());

        projectService.plusTotalToken(project.getKey(), 150L);
        Project updated = projectService.findOneProjectByKey(project.getKey());

        assertThat(updated.getTotalToken()).isEqualTo(150);
    }

    @Test
    void decryptOpenAiKey_Success() {
        Project project = projectService.createProject(ProjectCreateRequestDto.builder()
                .name("Decrypt Test")
                .createdUserId(user.getId())
                .dimensions(3072)
                .openAiKey(openAiApiKey)
                .chatModel("gpt-4")
                .embedModel("text-embedding-3-small")
                .prompt("Prompt")
                .build());

        String decryptedKey = projectService.decryptOpenAiKey(project);

        assertThat(decryptedKey).isEqualTo(openAiApiKey);

    }

    @Test
    void decryptOpenAiKey_Fail_NoKey() {
        Project project = projectService.createProject(ProjectCreateRequestDto.builder()
                .name("No Key Project")
                .createdUserId(user.getId())
                .dimensions(3072)
                .chatModel("gpt-4")
                .embedModel("text-embedding-3-small")
                .prompt("Prompt")
                .build());

        assertThrows(CustomException.class, () -> {
            projectService.decryptOpenAiKey(project);
        });
    }
}