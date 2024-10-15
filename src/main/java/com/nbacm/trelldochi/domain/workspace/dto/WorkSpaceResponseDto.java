package com.nbacm.trelldochi.domain.workspace.dto;

import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import lombok.Getter;

@Getter
public class WorkSpaceResponseDto {
    Long id;
    String name;
    String description;

    public WorkSpaceResponseDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public WorkSpaceResponseDto(WorkSpace workSpace) {
        this.id = workSpace.getId();
        this.name = workSpace.getName();
        this.description = workSpace.getDescription();
    }
}
