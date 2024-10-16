package com.nbacm.trelldochi.domain.workspace.repository;

import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkSpaceQueryDslRepository {
    Page<WorkSpaceResponseDto> findWorkSpacesByUserId(Long userId, Pageable pageable);

    void deleteRelationBoards(Long workspaceId);
}
