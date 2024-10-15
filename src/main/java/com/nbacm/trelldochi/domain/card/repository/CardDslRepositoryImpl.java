package com.nbacm.trelldochi.domain.card.repository;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.nbacm.trelldochi.domain.card.entity.QCard.card;
import static com.nbacm.trelldochi.domain.comment.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CardDslRepositoryImpl implements CardDslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Card> findCardAndCommentsById(Long cardId) {

        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(card)
                .leftJoin(card.commentList, comment).fetchJoin()
                .where(card.id.eq(cardId))
                .fetchOne()
        );
    }
}
