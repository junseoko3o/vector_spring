package com.milvus.vector_spring.chat.cache;

import com.milvus.vector_spring.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectCacheDto {
    private Long id;
    private String key;
    private String name;
    private String openAiKey;
    private String chatModel;
    private long dimensions;
    private String prompt;
    private String embedModel;

    public static ProjectCacheDto from(Project project) {
        return ProjectCacheDto.builder()
                .id(project.getId())
                .key(project.getKey())
                .name(project.getName())
                .openAiKey(project.getOpenAiKey())
                .chatModel(project.getChatModel())
                .dimensions(project.getDimensions())
                .prompt(project.getPrompt())
                .embedModel(project.getEmbedModel())
                .build();
    }

    public Project toProject() {
        return Project.builder()
                .id(this.id)
                .key(this.key)
                .name(this.name)
                .openAiKey(this.openAiKey)
                .chatModel(this.chatModel)
                .dimensions(this.dimensions)
                .prompt(this.prompt)
                .embedModel(this.embedModel)
                .build();
    }
}

