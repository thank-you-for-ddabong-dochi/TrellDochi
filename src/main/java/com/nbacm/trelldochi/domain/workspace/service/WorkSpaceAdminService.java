package com.nbacm.trelldochi.domain.workspace.service;

import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceRequestDto;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;

public interface WorkSpaceAdminService {
    WorkSpaceResponseDto createWorkSpace(String email, WorkSpaceRequestDto requestDto);

    WorkSpaceResponseDto updateWorkSpace(String email, WorkSpaceRequestDto requestDto, Long workspaceId);

    void deleteWorkSpace(String email, Long workspaceId);

    Object changeMemberRole(String email, Long workspaceId, Long memberId, String role);

    void deleteMember(String email, Long workspaceId, Long memberId);
}
