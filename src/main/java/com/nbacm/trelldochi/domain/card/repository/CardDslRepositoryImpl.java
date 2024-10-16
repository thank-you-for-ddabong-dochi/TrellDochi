package com.nbacm.trelldochi.domain.card.repository;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.entity.CardManager;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
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

    @Override
    public Page<Card> searchCards(String title, String explanation, LocalDate deadline, String managerName, Long boardId, Pageable pageable) {
        // 먼저 Card와 Comment를 Fetch Join
        List<Card> results = jpaQueryFactory.select(card)
                .from(card)
                .leftJoin(card.commentList).fetchJoin()
                .leftJoin(card.todolist).fetchJoin()
                .leftJoin(card.todolist.board).fetchJoin()
                .where(
                        containsTitle(title),
                        containsExplanation(explanation),
                        eqDeadline(deadline),
                        containsManagerName(managerName),
                        eqBoardId(boardId),
                        card.isDeleted.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 이후 AttachmentList를 별도의 쿼리로 로드
        results.forEach(card -> Hibernate.initialize(card.getAttachmentList()));

        long total = jpaQueryFactory.selectFrom(card)
                .where(
                        containsTitle(title),
                        containsExplanation(explanation),
                        eqDeadline(deadline),
                        containsManagerName(managerName),
                        eqBoardId(boardId),
                        card.isDeleted.eq(false)
                )
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression containsTitle(String title) {
        return title != null ? card.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression containsExplanation(String explanation) {
        return explanation != null ? card.explanation.containsIgnoreCase(explanation) : null;
    }

    private BooleanExpression eqDeadline(LocalDate deadline) {
        return deadline != null ? card.deadline.eq(deadline) : null;
    }

    private BooleanExpression containsManagerName(String managerName) {
        return managerName != null ? card.managerList.any().user.nickname.containsIgnoreCase(managerName) : null;
    }

    private BooleanExpression eqBoardId(Long boardId) {
        return boardId != null ? card.todolist.board.id.eq(boardId) : null;
    }

}
