package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.content.ContentService;
import com.milvus.vector_spring.milvus.MilvusService;
import com.milvus.vector_spring.openai.OpenAiService;
import com.milvus.vector_spring.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private ProjectService projectService;
    private ContentService contentService;
    private MilvusService milvusService;
    private OpenAiService openAiService;
}
