package com.nbacm.trelldochi.domain.workspace.service;

import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceRequestDto;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;

public interface WorkSpaceAdminService {
    public WorkSpaceResponseDto createWorkSpace(String email, WorkSpaceRequestDto requestDto);
}
