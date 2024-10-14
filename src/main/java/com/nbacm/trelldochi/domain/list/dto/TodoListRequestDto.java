package com.nbacm.trelldochi.domain.list.dto;

import com.nbacm.trelldochi.domain.card.entity.Card;
import lombok.Getter;

import java.util.List;

@Getter
public class TodoListRequestDto {

    private String title;
    private Long listOrder;
    private List<Card> cardList;
    private Long boardId;

    public TodoListRequestDto(String title, Long listOrder, List<Card> cardList, Long boardId) {
        this.title = title;
        this.listOrder = listOrder;
        this.cardList = cardList;
        this.boardId = boardId;
    }
}
