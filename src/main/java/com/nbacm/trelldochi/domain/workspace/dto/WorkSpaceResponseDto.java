package com.nbacm.trelldochi.domain.workspace.dto;

import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import lombok.Getter;

import java.util.List;

@Getter
public class WorkSpaceResponseDto {
    Long id;
    String name;
    String description;
    List<WorkSpaceBoardResponseDto> boards;

    public WorkSpaceResponseDto(WorkSpace workSpace) {
        this.id = workSpace.getId();
        this.name = workSpace.getName();
        this.description = workSpace.getDescription();
        this.boards = workSpace.getBoards().stream().map(board ->
                new WorkSpaceBoardResponseDto(
                        board.getId(),
                        board.getTitle())).toList();
    }
}
