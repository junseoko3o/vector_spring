package com.milvus.vector_spring.project;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.common.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectCryptoServiceImpl implements ProjectCryptoService {

    private final EncryptionService encryptionService;

    @Override
    public String decryptOpenAiKey(Project project) {
        if (project.getOpenAiKey() == null || project.getOpenAiKey().isEmpty()) {
            throw new CustomException(ErrorStatus.REQUIRE_OPEN_AI_INFO);
        }
        return encryptionService.decryptData(project.getOpenAiKey());
    }
}