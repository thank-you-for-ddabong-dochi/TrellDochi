package com.nbacm.trelldochi.domain.workspace.repository;

import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nbacm.trelldochi.domain.board.entity.QBoard.board;
import static com.nbacm.trelldochi.domain.workspace.entity.QWorkSpace.workSpace;
import static com.nbacm.trelldochi.domain.workspace.entity.QWorkSpaceMember.workSpaceMember;

@Repository
@RequiredArgsConstructor
public class WorkSpaceQueryDslRepositoryImpl implements WorkSpaceQueryDslRepository {

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
                workSpaces.stream().map(ws ->
                new WorkSpaceResponseDto(
                        ws.getId(),
                        ws.getName(),
                        ws.getDescription(),
                        ws.getBoards()
                )).toList(),
                pageable,
                count);
    }
}
