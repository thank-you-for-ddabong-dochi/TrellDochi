package com.nbacm.trelldochi.domain.workspace.repository;

import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nbacm.trelldochi.domain.board.entity.QBoard.board;
import static com.nbacm.trelldochi.domain.card.entity.QCard.card;
import static com.nbacm.trelldochi.domain.comment.entity.QComment.comment;
import static com.nbacm.trelldochi.domain.list.entity.QTodoList.todoList;
import static com.nbacm.trelldochi.domain.workspace.entity.QWorkSpace.workSpace;
import static com.nbacm.trelldochi.domain.workspace.entity.QWorkSpaceMember.workSpaceMember;

@Repository
@RequiredArgsConstructor
public class WorkSpaceQueryDslRepositoryImpl implements WorkSpaceQueryDslRepository {
    @Autowired
    EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<WorkSpaceResponseDto> findWorkSpacesByUserId(Long userId, Pageable pageable) {
        List<WorkSpace> workSpaces = queryFactory
                .select(workSpace)
                .from(workSpace)
                .leftJoin(workSpace.boards, board).fetchJoin()
                .leftJoin(workSpace.members, workSpaceMember)
                .on(workSpaceMember.user.id.eq(userId))
                .offset(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(workSpace.count())
                .from(workSpace)
                .leftJoin(workSpace.members, workSpaceMember)
                .on(workSpaceMember.user.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(
                workSpaces.stream()
                        .map(WorkSpaceResponseDto::new)
                        .toList(),
                pageable,
                count);
    }

    @Override
    @Transactional
    public void deleteRelationBoards(Long workspaceId) {

        queryFactory.update(comment)
                .set(comment.isDeleted, true)
                .where(
                        comment.card.id.in(
                                JPAExpressions
                                        .select(card.id)
                                        .from(card)
                                        .join(card.todolist, todoList)
                                        .join(todoList.board, board)
                                        .join(board.workSpace, workSpace)
                                        .where(workSpace.id.eq(workspaceId))
                        ))
                .execute();

        queryFactory.update(card)
                .set(card.isDeleted, true)
                .where(
                        card.todolist.id.in(
                                JPAExpressions
                                        .select(todoList.id)
                                        .from(todoList)
                                        .join(todoList.board, board)
                                        .join(board.workSpace, workSpace)
                                        .where(workSpace.id.eq(workspaceId))
                        ))
                .execute();

        queryFactory.update(todoList)
                .set(todoList.isDeleted, true)
                .where(
                        todoList.board.id.in(
                                JPAExpressions
                                        .select(board.id)
                                        .from(board)
                                        .join(board.workSpace, workSpace)
                                        .where(workSpace.id.eq(workspaceId))
                        )
                )
                .execute();

        queryFactory
                .update(board)
                .set(board.isDeleted, true)
                .where(
                        board.workSpace.id.eq(workspaceId)
                )
                .execute();


        queryFactory
                .update(workSpace)
                .set(workSpace.isDeleted, true)
                .where(workSpace.id.eq(workspaceId))
                .execute();
    }
}
