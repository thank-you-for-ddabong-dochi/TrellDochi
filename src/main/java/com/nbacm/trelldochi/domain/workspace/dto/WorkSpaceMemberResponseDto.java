package com.nbacm.trelldochi.domain.workspace.dto;

import com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember;
import lombok.Getter;

@Getter
public class WorkSpaceMemberResponseDto {
    private Long id;
    private Long workSpaceId;
    private Long userId;
    private String role;

    public WorkSpaceMemberResponseDto(WorkSpaceMember workSpaceMember) {
        this.id = workSpaceMember.getId();
        this.workSpaceId = workSpaceMember.getWorkspace().getId();
        this.userId = workSpaceMember.getUser().getId();
        this.role = workSpaceMember.getRole().name();
    }
}
