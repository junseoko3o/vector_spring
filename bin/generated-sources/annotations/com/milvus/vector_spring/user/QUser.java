package com.milvus.vector_spring.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1402963860L;

    public static final QUser user = new QUser("user");

    public final com.milvus.vector_spring.common.QBaseEntity _super = new com.milvus.vector_spring.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<com.milvus.vector_spring.content.Content, com.milvus.vector_spring.content.QContent> createdContentUser = this.<com.milvus.vector_spring.content.Content, com.milvus.vector_spring.content.QContent>createList("createdContentUser", com.milvus.vector_spring.content.Content.class, com.milvus.vector_spring.content.QContent.class, PathInits.DIRECT2);

    public final ListPath<com.milvus.vector_spring.project.Project, com.milvus.vector_spring.project.QProject> createdProjectUser = this.<com.milvus.vector_spring.project.Project, com.milvus.vector_spring.project.QProject>createList("createdProjectUser", com.milvus.vector_spring.project.Project.class, com.milvus.vector_spring.project.QProject.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.milvus.vector_spring.invite.Invite, com.milvus.vector_spring.invite.QInvite> invites = this.<com.milvus.vector_spring.invite.Invite, com.milvus.vector_spring.invite.QInvite>createList("invites", com.milvus.vector_spring.invite.Invite.class, com.milvus.vector_spring.invite.QInvite.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> loginAt = createDateTime("loginAt", java.time.LocalDateTime.class);

    public final StringPath password = createString("password");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<com.milvus.vector_spring.content.Content, com.milvus.vector_spring.content.QContent> updatedContentUser = this.<com.milvus.vector_spring.content.Content, com.milvus.vector_spring.content.QContent>createList("updatedContentUser", com.milvus.vector_spring.content.Content.class, com.milvus.vector_spring.content.QContent.class, PathInits.DIRECT2);

    public final ListPath<com.milvus.vector_spring.project.Project, com.milvus.vector_spring.project.QProject> updatedProjectUser = this.<com.milvus.vector_spring.project.Project, com.milvus.vector_spring.project.QProject>createList("updatedProjectUser", com.milvus.vector_spring.project.Project.class, com.milvus.vector_spring.project.QProject.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

