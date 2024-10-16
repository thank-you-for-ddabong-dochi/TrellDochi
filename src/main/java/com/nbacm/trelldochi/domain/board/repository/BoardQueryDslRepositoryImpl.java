package com.nbacm.trelldochi.domain.board.repository;

import com.nbacm.trelldochi.domain.board.entity.QBoard;
import com.nbacm.trelldochi.domain.list.entity.QTodoList;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BoardQueryDslRepositoryImpl implements BoardQueryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    QBoard qBoard = QBoard.board;
    QTodoList qTodoList = QTodoList.todoList;


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
    }
}
