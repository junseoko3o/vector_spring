package com.milvus.vector_spring.project;

import com.milvus.vector_spring.common.apipayload.ApiResponse;
import com.milvus.vector_spring.project.dto.ProjectContentsResponseDto;
import com.milvus.vector_spring.project.dto.ProjectCreateRequestDto;
import com.milvus.vector_spring.project.dto.ProjectUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.milvus.vector_spring.common.Const.PROJECT_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("project")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping()
    public ApiResponse<List<Project>> findAllProjects() {
        List<Project> projects = projectService.findAllProject();
        return ApiResponse.ok(projects);
    }

    @GetMapping("/{id}")
    public ApiResponse<Project> findOneProject(@PathVariable Long id) {
        Project project = projectService.findOneProject(id);
        return ApiResponse.ok(project);
    }

    @GetMapping("/search")
    public ApiResponse<Project> findOneProjectByKey(@RequestParam String key) {
        Project project = projectService.findOneProjectByKey(key);
        return ApiResponse.ok(project);
    }

    @GetMapping("/contents")
    public ApiResponse<ProjectContentsResponseDto> findOneProjectWithContents(@RequestParam String key) {
        ProjectContentsResponseDto project = projectService.findOneProjectWithContents(key);
        return ApiResponse.ok(project);
    }

    @PostMapping("/create")
    public ApiResponse<Project> createProject(@Validated @RequestBody ProjectCreateRequestDto projectCreateRequestDto) {
        Project project = projectService.createProject(projectCreateRequestDto);
        return ApiResponse.ok(project);
    }

    @PostMapping("/update")
    public ApiResponse<Project> updateProject(@RequestParam() String key, ProjectUpdateRequestDto projectUpdateRequestDto) {
        Project project = projectService.updateProject(key, projectUpdateRequestDto);
        return ApiResponse.ok(project);
    }

    @DeleteMapping()
    public ApiResponse<String> deleteProject(@RequestParam String key) {
        String deleteProject = projectService.deleteProject(key);
        return ApiResponse.ok(deleteProject);
    }
}
