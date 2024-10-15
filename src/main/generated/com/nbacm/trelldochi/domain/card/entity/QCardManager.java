package com.nbacm.trelldochi.domain.card.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCardManager is a Querydsl query type for CardManager
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCardManager extends EntityPathBase<CardManager> {

    private static final long serialVersionUID = 1997167166L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCardManager cardManager = new QCardManager("cardManager");

    public final QCard card;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.nbacm.trelldochi.domain.user.entity.QUser user;

    public QCardManager(String variable) {
        this(CardManager.class, forVariable(variable), INITS);
    }

    public QCardManager(Path<? extends CardManager> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCardManager(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCardManager(PathMetadata metadata, PathInits inits) {
        this(CardManager.class, metadata, inits);
    }

    public QCardManager(Class<? extends CardManager> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.card = inits.isInitialized("card") ? new QCard(forProperty("card"), inits.get("card")) : null;
        this.user = inits.isInitialized("user") ? new com.nbacm.trelldochi.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

