package com.nbacm.trelldochi.domain.list.dto;

import com.nbacm.trelldochi.domain.card.entity.Card;
import lombok.Getter;

import java.util.List;

@Getter
public class TodoListRequestDto {

    private String title;
    private int listOrder;
    private List<Card> cardList;
    private Long boardId;

    public TodoListRequestDto() {}

    public TodoListRequestDto(String title, int listOrder, List<Card> cardList, Long boardId) {
        this.title = title;
        this.listOrder = listOrder;
        this.cardList = cardList;
        this.boardId = boardId;
    }
}
