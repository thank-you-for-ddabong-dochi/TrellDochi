package com.nbacm.trelldochi.domain.list.dto;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;

@Getter
public class TodoListResponseDto {

    private String title;
    private int listOrder;
    private List<CardSummaryDto> cardList;
    private Long boardId;

    public TodoListResponseDto(TodoList todoList) {
        this.title = todoList.getTitle();
        this.listOrder = todoList.getListOrder();
        this.boardId = todoList.getBoard().getId();
    }

    public void addCardList(List<CardSummaryDto> cardList) {
        this.cardList = cardList;
    }
}
