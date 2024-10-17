package com.nbacm.trelldochi.domain.list.dto;

import com.nbacm.trelldochi.domain.card.entity.Card;
import lombok.Getter;

import java.util.List;

@Getter
public class TodoListRequestDto {

    private String title;
    private int listOrder;

    public TodoListRequestDto() {}

    public TodoListRequestDto(String title, int listOrder) {
        this.title = title;
        this.listOrder = listOrder;
    }
}
