package com.milvus.vector_spring.project;

import com.milvus.vector_spring.common.apipayload.ApiResponse;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.dto.ProjectCreateRequestDto;
import com.milvus.vector_spring.project.dto.ProjectResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping()
    public ApiResponse<List<Project>> findAllProjects() {
        List<Project> projects = projectService.findAllProject();
        return ApiResponse.ok(projects);
    }

    @GetMapping("/${id}")
    public ApiResponse<Project> findOneProject(@PathVariable Long id) {
        Project project = projectService.findOneProject(id);
        return ApiResponse.ok(project);
    }

    @GetMapping()
    public ApiResponse<Project> findOneProjectByKey(@RequestParam String key) {
        Project project = projectService.findOneProjectByKey(key);
        return ApiResponse.ok(project);
    }

    @PostMapping("/create")
    public ApiResponse<Project> createProject(@Validated @RequestBody ProjectCreateRequestDto projectCreateRequestDto) {
        Project project = projectService.createProject(projectCreateRequestDto);
        return ApiResponse.ok(project);
    }
}
