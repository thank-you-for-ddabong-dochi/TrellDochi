package com.nbacm.trelldochi.domain.workspace.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkSpace is a Querydsl query type for WorkSpace
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkSpace extends EntityPathBase<WorkSpace> {

    private static final long serialVersionUID = -475525195L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkSpace workSpace = new QWorkSpace("workSpace");

    public final ListPath<com.nbacm.trelldochi.domain.board.entity.Board, com.nbacm.trelldochi.domain.board.entity.QBoard> boards = this.<com.nbacm.trelldochi.domain.board.entity.Board, com.nbacm.trelldochi.domain.board.entity.QBoard>createList("boards", com.nbacm.trelldochi.domain.board.entity.Board.class, com.nbacm.trelldochi.domain.board.entity.QBoard.class, PathInits.DIRECT2);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final ListPath<WorkSpaceMember, QWorkSpaceMember> members = this.<WorkSpaceMember, QWorkSpaceMember>createList("members", WorkSpaceMember.class, QWorkSpaceMember.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final com.nbacm.trelldochi.domain.user.entity.QUser owner;

    public QWorkSpace(String variable) {
        this(WorkSpace.class, forVariable(variable), INITS);
    }

    public QWorkSpace(Path<? extends WorkSpace> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkSpace(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkSpace(PathMetadata metadata, PathInits inits) {
        this(WorkSpace.class, metadata, inits);
    }

    public QWorkSpace(Class<? extends WorkSpace> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new com.nbacm.trelldochi.domain.user.entity.QUser(forProperty("owner")) : null;
    }

}

