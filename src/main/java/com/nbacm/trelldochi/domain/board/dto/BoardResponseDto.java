package com.nbacm.trelldochi.domain.board.dto;

import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardResponseDto {

    private String title;
    private String contents;
    private List<TodoList> todoLists;
    private Long workspaceId;

    public BoardResponseDto() {
    }

    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.todoLists = board.getTodoLists();
        this.workspaceId = board.getWorkSpace().getId();

    }
}
