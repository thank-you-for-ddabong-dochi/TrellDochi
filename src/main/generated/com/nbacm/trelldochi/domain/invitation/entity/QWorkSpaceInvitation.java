package com.nbacm.trelldochi.domain.invitation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWorkSpaceInvitation is a Querydsl query type for WorkSpaceInvitation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkSpaceInvitation extends EntityPathBase<WorkSpaceInvitation> {

    private static final long serialVersionUID = -1614719994L;

    public static final QWorkSpaceInvitation workSpaceInvitation = new QWorkSpaceInvitation("workSpaceInvitation");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> receivedUserId = createNumber("receivedUserId", Long.class);

    public final NumberPath<Long> requestUserId = createNumber("requestUserId", Long.class);

    public final EnumPath<com.nbacm.trelldochi.domain.invitation.enums.INVITATION_STATUS> status = createEnum("status", com.nbacm.trelldochi.domain.invitation.enums.INVITATION_STATUS.class);

    public final NumberPath<Long> workspaceId = createNumber("workspaceId", Long.class);

    public QWorkSpaceInvitation(String variable) {
        super(WorkSpaceInvitation.class, forVariable(variable));
    }

    public QWorkSpaceInvitation(Path<? extends WorkSpaceInvitation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWorkSpaceInvitation(PathMetadata metadata) {
        super(WorkSpaceInvitation.class, metadata);
    }

}

