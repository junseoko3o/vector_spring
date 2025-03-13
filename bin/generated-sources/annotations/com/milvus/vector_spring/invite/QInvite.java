package com.milvus.vector_spring.invite;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInvite is a Querydsl query type for Invite
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInvite extends EntityPathBase<Invite> {

    private static final long serialVersionUID = -1404626196L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInvite invite = new QInvite("invite");

    public final com.milvus.vector_spring.common.QBaseEntity _super = new com.milvus.vector_spring.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.milvus.vector_spring.user.QUser createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.milvus.vector_spring.project.QProject project;

    public final StringPath receivedEmail = createString("receivedEmail");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QInvite(String variable) {
        this(Invite.class, forVariable(variable), INITS);
    }

    public QInvite(Path<? extends Invite> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInvite(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInvite(PathMetadata metadata, PathInits inits) {
        this(Invite.class, metadata, inits);
    }

    public QInvite(Class<? extends Invite> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.createdBy = inits.isInitialized("createdBy") ? new com.milvus.vector_spring.user.QUser(forProperty("createdBy")) : null;
        this.project = inits.isInitialized("project") ? new com.milvus.vector_spring.project.QProject(forProperty("project"), inits.get("project")) : null;
    }

}

