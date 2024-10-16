package com.nbacm.trelldochi.domain.workspace.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkSpaceMember is a Querydsl query type for WorkSpaceMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkSpaceMember extends EntityPathBase<WorkSpaceMember> {

    private static final long serialVersionUID = -217044305L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkSpaceMember workSpaceMember = new QWorkSpaceMember("workSpaceMember");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<MemberRole> role = createEnum("role", MemberRole.class);

    public final com.nbacm.trelldochi.domain.user.entity.QUser user;

    public final QWorkSpace workspace;

    public QWorkSpaceMember(String variable) {
        this(WorkSpaceMember.class, forVariable(variable), INITS);
    }

    public QWorkSpaceMember(Path<? extends WorkSpaceMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkSpaceMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkSpaceMember(PathMetadata metadata, PathInits inits) {
        this(WorkSpaceMember.class, metadata, inits);
    }

    public QWorkSpaceMember(Class<? extends WorkSpaceMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.nbacm.trelldochi.domain.user.entity.QUser(forProperty("user")) : null;
        this.workspace = inits.isInitialized("workspace") ? new QWorkSpace(forProperty("workspace"), inits.get("workspace")) : null;
    }

}

