package com.milvus.vector_spring.project;

import com.milvus.vector_spring.project.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("project")
public class ProjectController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;

    @GetMapping()
    public List<ProjectResponseDto> findAllProjects() {
        List<Project> projects = projectQueryService.findAllProject();
        return projects.stream()
                .map(ProjectResponseDto::projectResponseDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ProjectResponseDto findOneProject(@PathVariable Long id) {
        Project project = projectQueryService.findOneProject(id);
        return ProjectResponseDto.projectResponseDto(project);
    }

    @GetMapping("/search")
    public ProjectResponseDto findOneProjectByKey(@RequestParam String key) {
        Project project = projectQueryService.findOneProjectByKey(key);
        return ProjectResponseDto.projectResponseDto(project);
    }

    @GetMapping("/contents")
    public ProjectContentsResponseDto findOneProjectWithContents(@RequestParam String key) {
        Project project = projectQueryService.findOneProjectWithContents(key);
        return ProjectContentsResponseDto.projectContentsResponseDto(project);
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectResponseDto> createProject(@Validated @RequestBody ProjectCreateRequestDto projectCreateRequestDto) {
        Project project = projectCommandService.createProject(projectCreateRequestDto);
        return ResponseEntity.ok(ProjectResponseDto.projectResponseDto(project));
    }

    @PostMapping("/update")
    public ResponseEntity<ProjectResponseDto> updateProject(@RequestParam("key") String key, @Validated @RequestBody ProjectUpdateRequestDto projectUpdateRequestDto) {
        Project project = projectCommandService.updateProject(key, projectUpdateRequestDto);
        return ResponseEntity.ok(ProjectResponseDto.projectResponseDto(project));
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteProject(@RequestBody ProjectDeleteRequestDto projectDeleteRequestDto) {
        String deleteProject = projectCommandService.deleteProject(projectDeleteRequestDto);
        return ResponseEntity.ok(deleteProject);
    }
}
