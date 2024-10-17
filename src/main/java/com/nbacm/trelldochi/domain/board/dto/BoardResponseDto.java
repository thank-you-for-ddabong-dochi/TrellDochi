package com.nbacm.trelldochi.domain.board.dto;

import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

@Getter
public class BoardResponseDto {

    private Long boardId;
    private String title;
    private String backgroundColor;
    private String backgroundImageUrl;
    private List<ListSummaryDto> todoLists;
    private Long workspaceId;

    public BoardResponseDto() {
    }

    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.backgroundColor = board.getBackgroundColor();
        this.backgroundImageUrl = board.getBackgroundImageUrl();
        if(board.getTodoLists() != null) {
            this.todoLists = board.getTodoLists().stream().sorted(Comparator.comparing(TodoList::getListOrder)).map(ListSummaryDto::new).toList();
        }
        this.workspaceId = board.getWorkSpace().getId();

    }
}
