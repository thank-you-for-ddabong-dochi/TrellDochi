package com.nbacm.trelldochi.domain.list.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTodoList is a Querydsl query type for TodoList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTodoList extends EntityPathBase<TodoList> {

    private static final long serialVersionUID = -1859611791L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTodoList todoList = new QTodoList("todoList");

    public final com.nbacm.trelldochi.domain.board.entity.QBoard board;

    public final ListPath<com.nbacm.trelldochi.domain.card.entity.Card, com.nbacm.trelldochi.domain.card.entity.QCard> cardList = this.<com.nbacm.trelldochi.domain.card.entity.Card, com.nbacm.trelldochi.domain.card.entity.QCard>createList("cardList", com.nbacm.trelldochi.domain.card.entity.Card.class, com.nbacm.trelldochi.domain.card.entity.QCard.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Integer> listOrder = createNumber("listOrder", Integer.class);

    public final StringPath title = createString("title");

    public QTodoList(String variable) {
        this(TodoList.class, forVariable(variable), INITS);
    }

    public QTodoList(Path<? extends TodoList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTodoList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTodoList(PathMetadata metadata, PathInits inits) {
        this(TodoList.class, metadata, inits);
    }

    public QTodoList(Class<? extends TodoList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.nbacm.trelldochi.domain.board.entity.QBoard(forProperty("board"), inits.get("board")) : null;
    }

}

