package com.nbacm.trelldochi.domain.board.dto;

import com.nbacm.trelldochi.domain.list.entity.TodoList;
import lombok.Getter;

@Getter
public class ListSummaryDto {
    private Long id;
    private String title;

    public ListSummaryDto() {}
    public ListSummaryDto(TodoList todoList) {
        this.id = todoList.getId();
        this.title = todoList.getTitle();
    }
}
