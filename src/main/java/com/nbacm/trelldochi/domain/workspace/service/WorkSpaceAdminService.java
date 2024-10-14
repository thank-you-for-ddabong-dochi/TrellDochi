package com.nbacm.trelldochi.domain.workspace.service;

import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceRequestDto;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;

public interface WorkSpaceAdminService {
    WorkSpaceResponseDto createWorkSpace(String email, WorkSpaceRequestDto requestDto);

    WorkSpaceResponseDto updateWorkSpace(String email, WorkSpaceRequestDto requestDto, Long workspaceId);
}
