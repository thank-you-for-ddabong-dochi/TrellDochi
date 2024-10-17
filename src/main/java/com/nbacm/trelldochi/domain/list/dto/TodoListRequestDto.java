package com.nbacm.trelldochi.domain.list.dto;

import com.nbacm.trelldochi.domain.card.entity.Card;
import lombok.Getter;

import java.util.List;

@Getter
public class        TodoListRequestDto {

    private String title;

    public TodoListRequestDto() {}

    public TodoListRequestDto(String title) {
        this.title = title;
    }
}
