package com.nbacm.trelldochi.domain.board.repository;

import com.nbacm.trelldochi.domain.board.entity.QBoard;
import com.nbacm.trelldochi.domain.card.entity.QCard;
import com.nbacm.trelldochi.domain.comment.entity.QComment;
import com.nbacm.trelldochi.domain.list.entity.QTodoList;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BoardQueryDslRepositoryImpl implements BoardQueryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    QBoard qBoard = QBoard.board;
    QTodoList qTodoList = QTodoList.todoList;
    QCard qCard = QCard.card;
    QComment qComment = QComment.comment;


    @Override
    @Transactional
    public void deleteRelations(Long boardId) {
        // board softDelete
        queryFactory.update(qBoard)
                .set(qBoard.isDeleted, true)
                .where(qBoard.id.eq(boardId))
                .execute();

        // todoList softDelete
        queryFactory.update(qTodoList)
                .set(qTodoList.isDeleted, true)
                .where(qTodoList.board.id.eq(boardId))
                .execute();

        // card softDelete
        queryFactory.update(qCard)
                .set(qCard.isDeleted, true)  // qCard 테이블의 isDeleted 필드 업데이트
                .where(qCard.todolist.id.in(
                        JPAExpressions.select(qTodoList.id)
                                .from(qTodoList)
                                .join(qTodoList.board, qBoard)
                                .where(qBoard.id.eq(boardId))
                ))  // boardId가 일치하는 조건
                .execute();

        // comment softDelete
        queryFactory.update(qComment)
                .set(qComment.isDeleted, true)
                .where(qComment.card.id.in(
                        JPAExpressions.select(qCard.id)
                                .from(qCard)
                                .join(qCard.todolist, qTodoList)
                                .join(qTodoList.board, qBoard)
                                .where(qBoard.id.eq(boardId))
                ))
                .execute();
    }
}
