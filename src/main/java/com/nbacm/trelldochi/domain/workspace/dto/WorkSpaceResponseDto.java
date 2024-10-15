package com.nbacm.trelldochi.domain.workspace.dto;

import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import lombok.Getter;

import java.util.List;

@Getter
public class WorkSpaceResponseDto {
    Long id;
    String name;
    String description;
    List<WorkSpaceBoardResponseDto> boards;

    public WorkSpaceResponseDto(Long id, String name, String description, List<Board> boards) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.boards = boards.stream().map(board ->
                new WorkSpaceBoardResponseDto(
                        board.getId(),
                        board.getTitle())).toList();
    }

    public WorkSpaceResponseDto(WorkSpace workSpace) {
        this.id = workSpace.getId();
        this.name = workSpace.getName();
        this.description = workSpace.getDescription();
    }
}
