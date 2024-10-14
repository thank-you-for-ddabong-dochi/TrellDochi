package com.nbacm.trelldochi.domain.list.dto;

import lombok.Getter;

@Getter
public class MoveListRequestDto {

    private Long todoListId;
    private int targetOrder;

    public MoveListRequestDto(Long todoListId, int targetOrder) {
        this.todoListId = todoListId;
        this.targetOrder = targetOrder;
    }
}
