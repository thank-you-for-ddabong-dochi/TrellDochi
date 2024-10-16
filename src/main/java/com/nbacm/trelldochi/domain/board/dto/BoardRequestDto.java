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

    public BoardRequestDto() {
    }

    public BoardRequestDto(String title, String contents, List<TodoList> todoLists) {
        this.title = title;
        this.contents = contents;
        this.todoLists = todoLists;
    }
}
