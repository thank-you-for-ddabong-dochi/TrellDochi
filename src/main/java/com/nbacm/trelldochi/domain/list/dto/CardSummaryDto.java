package com.nbacm.trelldochi.domain.list.dto;

import com.nbacm.trelldochi.domain.card.entity.Card;
import lombok.Getter;

@Getter
public class CardSummaryDto {

    private String title;
    private int commentCount;

    public CardSummaryDto() {}

    public CardSummaryDto(Card card) {
        this.title = card.getTitle();
        this.commentCount = card.getCommentList().size();
    }
}
