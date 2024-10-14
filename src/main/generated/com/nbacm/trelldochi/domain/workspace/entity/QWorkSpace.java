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

    public static final QWorkSpace workSpace = new QWorkSpace("workSpace");

    public final ListPath<com.nbacm.trelldochi.domain.board.entity.Board, com.nbacm.trelldochi.domain.board.entity.QBoard> boards = this.<com.nbacm.trelldochi.domain.board.entity.Board, com.nbacm.trelldochi.domain.board.entity.QBoard>createList("boards", com.nbacm.trelldochi.domain.board.entity.Board.class, com.nbacm.trelldochi.domain.board.entity.QBoard.class, PathInits.DIRECT2);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<WorkSpaceMember, QWorkSpaceMember> members = this.<WorkSpaceMember, QWorkSpaceMember>createList("members", WorkSpaceMember.class, QWorkSpaceMember.class, PathInits.DIRECT2);

    public QWorkSpace(String variable) {
        super(WorkSpace.class, forVariable(variable));
    }

    public QWorkSpace(Path<? extends WorkSpace> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWorkSpace(PathMetadata metadata) {
        super(WorkSpace.class, metadata);
    }

}

