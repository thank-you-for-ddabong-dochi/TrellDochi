package com.nbacm.trelldochi.domain.user.entity;

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

    private static final long serialVersionUID = -928898203L;

    public static final QUser user = new QUser("user");

    public final ListPath<com.nbacm.trelldochi.domain.card.entity.CardManager, com.nbacm.trelldochi.domain.card.entity.QCardManager> cardManagers = this.<com.nbacm.trelldochi.domain.card.entity.CardManager, com.nbacm.trelldochi.domain.card.entity.QCardManager>createList("cardManagers", com.nbacm.trelldochi.domain.card.entity.CardManager.class, com.nbacm.trelldochi.domain.card.entity.QCardManager.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDelete = createBoolean("isDelete");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final EnumPath<UserRole> userRole = createEnum("userRole", UserRole.class);

    public final ListPath<com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember, com.nbacm.trelldochi.domain.workspace.entity.QWorkSpaceMember> workSpaceMembers = this.<com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember, com.nbacm.trelldochi.domain.workspace.entity.QWorkSpaceMember>createList("workSpaceMembers", com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember.class, com.nbacm.trelldochi.domain.workspace.entity.QWorkSpaceMember.class, PathInits.DIRECT2);

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

