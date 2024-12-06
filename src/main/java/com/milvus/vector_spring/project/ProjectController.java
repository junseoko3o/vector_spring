package com.milvus.vector_spring.project;

import com.milvus.vector_spring.project.dto.ProjectContentsResponseDto;
import com.milvus.vector_spring.project.dto.ProjectCreateRequestDto;
import com.milvus.vector_spring.project.dto.ProjectUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("project")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping()
    public ResponseEntity<List<Project>> findAllProjects() {
        List<Project> projects = projectService.findAllProject();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> findOneProject(@PathVariable Long id) {
        Project project = projectService.findOneProject(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/search")
    public ResponseEntity<Project> findOneProjectByKey(@RequestParam String key) {
        Project project = projectService.findOneProjectByKey(key);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/contents")
    public ResponseEntity<ProjectContentsResponseDto> findOneProjectWithContents(@RequestParam String key) {
        ProjectContentsResponseDto project = projectService.findOneProjectWithContents(key);
        return ResponseEntity.ok(project);
    }

    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@Validated @RequestBody ProjectCreateRequestDto projectCreateRequestDto) {
        Project project = projectService.createProject(projectCreateRequestDto);
        return ResponseEntity.ok(project);
    }

    @PostMapping("/update")
    public ResponseEntity<Project> updateProject(@RequestParam() String key, ProjectUpdateRequestDto projectUpdateRequestDto) {
        Project project = projectService.updateProject(key, projectUpdateRequestDto);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteProject(@RequestParam String key) {
        String deleteProject = projectService.deleteProject(key);
        return ResponseEntity.ok(deleteProject);
    }
}
