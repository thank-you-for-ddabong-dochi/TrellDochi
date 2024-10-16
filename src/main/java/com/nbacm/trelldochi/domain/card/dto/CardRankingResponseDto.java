package com.nbacm.trelldochi.domain.card.dto;

import com.nbacm.trelldochi.domain.card.entity.Card;
import lombok.Getter;

@Getter
public class CardRankingResponseDto {

    private Long cardId;
    private String title;
    private int rank;
    private int viewCount;

    public CardRankingResponseDto(Long id, String title, int i, int viewCount) {
        this.cardId = id;
        this.title = title;
        this.rank = i;
        this.viewCount = viewCount;
    }
}
