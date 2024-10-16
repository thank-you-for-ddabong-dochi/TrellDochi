package com.nbacm.trelldochi.domain.card.repository;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.entity.CardManager;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.nbacm.trelldochi.domain.card.entity.QCard.card;
import static com.nbacm.trelldochi.domain.card.entity.QCardManager.cardManager;
import static com.nbacm.trelldochi.domain.comment.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CardDslRepositoryImpl implements CardDslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Card> findCardAndCommentsById(Long cardId) {

        return Optional.ofNullable(jpaQueryFactory.selectFrom(card)
                .leftJoin(card.commentList, comment).fetchJoin()
                .leftJoin(card.managerList, cardManager)
                .where(card.id.eq(cardId))
                .fetchOne()
        );
    }

    @Override
    public Optional<CardManager> findUserInUserList(Long cardId, Long userId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(cardManager)
                .where(cardManager.card.id.eq(cardId), cardManager.user.id.eq(userId))
                .fetchOne()
        );
    }
}
