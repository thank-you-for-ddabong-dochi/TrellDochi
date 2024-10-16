package com.nbacm.trelldochi.domain.list.repository;

import com.nbacm.trelldochi.domain.card.entity.QCard;
import com.nbacm.trelldochi.domain.comment.entity.QComment;
import com.nbacm.trelldochi.domain.list.entity.QTodoList;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TodoListQueryDslRepositoryImpl implements TodoListQueryDslRepository {

    @Autowired
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    QTodoList qTodoList = QTodoList.todoList;
    QCard qCard = QCard.card;

    @Override
    @Transactional
    public void deleteRelations(Long todoListId) {
        // todoList softDelete
        queryFactory.update(qTodoList)
                .set(qTodoList.isDeleted, true)
                .where(qTodoList.id.eq(todoListId))
                .execute();

        // card softDelete
        queryFactory.update(qCard)
                .set(qCard.isDeleted, true)
                .where(qCard.todolist.id.eq(todoListId))
                .execute();
        entityManager.flush();
        entityManager.clear();
    }
}
