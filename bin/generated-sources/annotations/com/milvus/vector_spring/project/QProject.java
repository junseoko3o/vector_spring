package com.milvus.vector_spring.project;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = -268528926L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProject project = new QProject("project");

    public final com.milvus.vector_spring.common.QBaseEntity _super = new com.milvus.vector_spring.common.QBaseEntity(this);

    public final StringPath basicModel = createString("basicModel");

    public final ListPath<com.milvus.vector_spring.content.Content, com.milvus.vector_spring.content.QContent> contents = this.<com.milvus.vector_spring.content.Content, com.milvus.vector_spring.content.QContent>createList("contents", com.milvus.vector_spring.content.Content.class, com.milvus.vector_spring.content.QContent.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.milvus.vector_spring.user.QUser createdBy;

    public final NumberPath<Long> dimensions = createNumber("dimensions", Long.class);

    public final StringPath embedModel = createString("embedModel");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.milvus.vector_spring.invite.Invite, com.milvus.vector_spring.invite.QInvite> invites = this.<com.milvus.vector_spring.invite.Invite, com.milvus.vector_spring.invite.QInvite>createList("invites", com.milvus.vector_spring.invite.Invite.class, com.milvus.vector_spring.invite.QInvite.class, PathInits.DIRECT2);

    public final StringPath key = createString("key");

    public final StringPath name = createString("name");

    public final StringPath openAiKey = createString("openAiKey");

    public final StringPath prompt = createString("prompt");

    public final NumberPath<Long> totalToken = createNumber("totalToken", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.milvus.vector_spring.user.QUser updatedBy;

    public QProject(String variable) {
        this(Project.class, forVariable(variable), INITS);
    }

    public QProject(Path<? extends Project> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProject(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProject(PathMetadata metadata, PathInits inits) {
        this(Project.class, metadata, inits);
    }

    public QProject(Class<? extends Project> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.createdBy = inits.isInitialized("createdBy") ? new com.milvus.vector_spring.user.QUser(forProperty("createdBy")) : null;
        this.updatedBy = inits.isInitialized("updatedBy") ? new com.milvus.vector_spring.user.QUser(forProperty("updatedBy")) : null;
    }

}

