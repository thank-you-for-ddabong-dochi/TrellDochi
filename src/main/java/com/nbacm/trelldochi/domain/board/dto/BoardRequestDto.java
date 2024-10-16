package com.nbacm.trelldochi.domain.board.dto;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardRequestDto {

    private String title;
    private String backgroundColor;
    private String backgroundImageUrl;
    private List<TodoList> todoLists;

    public BoardRequestDto() {
    }

    public BoardRequestDto(String title, String backgroundColor, String backgroundImageUrl, List<TodoList> todoLists) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.backgroundImageUrl = backgroundImageUrl;
        this.todoLists = todoLists;
    }

    public void addImageUrl(String imageUrl) {
        this.backgroundImageUrl = imageUrl;
    }
}
