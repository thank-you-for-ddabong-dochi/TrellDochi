package com.nbacm.trelldochi.domain.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.nbacm.trelldochi.domain.comment.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentDslRepositoryImpl implements CommentDslRepository {

    @Autowired
    EntityManager entityManager;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void deleteAllWithCardId(Long cardId) {
        jpaQueryFactory.update(comment)
                .set(comment.isDeleted, true)
                .where(comment.card.id.eq(cardId))
                .execute();

        entityManager.flush();
        entityManager.clear();
    }
}
