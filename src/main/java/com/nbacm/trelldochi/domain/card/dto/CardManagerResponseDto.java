package com.nbacm.trelldochi.domain.card.dto;

import com.nbacm.trelldochi.domain.card.entity.CardManager;
import lombok.Getter;

@Getter
public class CardManagerResponseDto {

    private Long userId;
    private String nickName;

    public CardManagerResponseDto(CardManager cardManager) {
        userId = cardManager.getUser().getId();
        nickName = cardManager.getUser().getNickname();
    }
}
