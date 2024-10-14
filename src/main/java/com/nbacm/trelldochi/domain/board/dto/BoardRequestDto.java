package com.nbacm.trelldochi.domain.board.dto;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardRequestDto {

    private String title;
    private String contents;
    private List<TodoList> todoLists;
    private Long workSpaceId;

    public BoardRequestDto(String title, String contents, List<TodoList> todoLists, Long workSpaceId) {
        this.title = title;
        this.contents = contents;
        this.todoLists = todoLists;
        this.workSpaceId = workSpaceId;
    }
}
