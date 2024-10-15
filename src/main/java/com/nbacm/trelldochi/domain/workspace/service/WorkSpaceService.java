package com.nbacm.trelldochi.domain.workspace.service;

import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;
import org.springframework.data.domain.Page;

public interface WorkSpaceService {
    Page<WorkSpaceResponseDto> getWorkSpaces(String email, int page, int size);

    WorkSpaceResponseDto getWorkSpace(String email, Long userId);

}
