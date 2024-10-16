package com.nbacm.trelldochi.domain.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -1074482059L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final StringPath backgroundColor = createString("backgroundColor");

    public final StringPath backgroundImageUrl = createString("backgroundImageUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath title = createString("title");

    public final ListPath<com.nbacm.trelldochi.domain.list.entity.TodoList, com.nbacm.trelldochi.domain.list.entity.QTodoList> todoLists = this.<com.nbacm.trelldochi.domain.list.entity.TodoList, com.nbacm.trelldochi.domain.list.entity.QTodoList>createList("todoLists", com.nbacm.trelldochi.domain.list.entity.TodoList.class, com.nbacm.trelldochi.domain.list.entity.QTodoList.class, PathInits.DIRECT2);

    public final com.nbacm.trelldochi.domain.workspace.entity.QWorkSpace workSpace;

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.workSpace = inits.isInitialized("workSpace") ? new com.nbacm.trelldochi.domain.workspace.entity.QWorkSpace(forProperty("workSpace"), inits.get("workSpace")) : null;
    }

}

