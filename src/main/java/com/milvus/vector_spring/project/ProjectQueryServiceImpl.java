package com.milvus.vector_spring.project;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectQueryServiceImpl implements ProjectQueryService {
    private final ProjectRepository projectRepository;

    @Override
    public List<Project> findAllProject() {
        return projectRepository.findAll();
    }

    @Override
    public Project findOneProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_PROJECT));
    }

    @Override
    public Project findOneProjectByKey(String key) {
        return projectRepository.findProjectByKey(key)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_PROJECT));
    }

    @Override
    public Project findOneProjectWithContents(String key) {
        projectRepository.findProjectByKey(key);
        return projectRepository.findOneProjectWithContents(key);
    }
}
