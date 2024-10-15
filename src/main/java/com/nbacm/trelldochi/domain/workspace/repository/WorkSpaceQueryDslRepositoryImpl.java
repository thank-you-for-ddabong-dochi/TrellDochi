package com.nbacm.trelldochi.domain.workspace.repository;

import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nbacm.trelldochi.domain.workspace.entity.QWorkSpace.workSpace;
import static com.nbacm.trelldochi.domain.workspace.entity.QWorkSpaceMember.workSpaceMember;

@Repository
@RequiredArgsConstructor
public class WorkSpaceQueryDslRepositoryImpl implements WorkSpaceQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<WorkSpaceResponseDto> findWorkSpacesByUserId(Long userId, Pageable pageable) {
        List<WorkSpaceResponseDto> workSpaces = queryFactory
                .select(
                        Projections.constructor(
                                WorkSpaceResponseDto.class,
                                workSpace.id,
                                workSpace.name,
                                workSpace.description
                        ))
                .from(workSpace)
                .leftJoin(workSpace.members, workSpaceMember)
                .on(workSpaceMember.user.id.eq(userId))
                .fetch();

        Long count = queryFactory
                .select(workSpace.count())
                .from(workSpace)
                .leftJoin(workSpace.members, workSpaceMember)
                .on(workSpaceMember.user.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(workSpaces, pageable, count);
    }
}
