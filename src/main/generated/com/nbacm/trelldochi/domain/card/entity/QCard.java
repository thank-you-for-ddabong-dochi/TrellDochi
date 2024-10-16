package com.nbacm.trelldochi.domain.card.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCard is a Querydsl query type for Card
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCard extends EntityPathBase<Card> {

    private static final long serialVersionUID = 134447727L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCard card = new QCard("card");

    public final ListPath<com.nbacm.trelldochi.domain.attachment.entity.Attachment, com.nbacm.trelldochi.domain.attachment.entity.QAttachment> attachmentList = this.<com.nbacm.trelldochi.domain.attachment.entity.Attachment, com.nbacm.trelldochi.domain.attachment.entity.QAttachment>createList("attachmentList", com.nbacm.trelldochi.domain.attachment.entity.Attachment.class, com.nbacm.trelldochi.domain.attachment.entity.QAttachment.class, PathInits.DIRECT2);

    public final ListPath<com.nbacm.trelldochi.domain.comment.entity.Comment, com.nbacm.trelldochi.domain.comment.entity.QComment> commentList = this.<com.nbacm.trelldochi.domain.comment.entity.Comment, com.nbacm.trelldochi.domain.comment.entity.QComment>createList("commentList", com.nbacm.trelldochi.domain.comment.entity.Comment.class, com.nbacm.trelldochi.domain.comment.entity.QComment.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> deadline = createDate("deadline", java.time.LocalDate.class);

    public final StringPath explanation = createString("explanation");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final ListPath<CardManager, QCardManager> managerList = this.<CardManager, QCardManager>createList("managerList", CardManager.class, QCardManager.class, PathInits.DIRECT2);

    public final EnumPath<CardStatus> status = createEnum("status", CardStatus.class);

    public final StringPath title = createString("title");

    public final com.nbacm.trelldochi.domain.list.entity.QTodoList todolist;

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QCard(String variable) {
        this(Card.class, forVariable(variable), INITS);
    }

    public QCard(Path<? extends Card> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCard(PathMetadata metadata, PathInits inits) {
        this(Card.class, metadata, inits);
    }

    public QCard(Class<? extends Card> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.todolist = inits.isInitialized("todolist") ? new com.nbacm.trelldochi.domain.list.entity.QTodoList(forProperty("todolist"), inits.get("todolist")) : null;
    }

}

