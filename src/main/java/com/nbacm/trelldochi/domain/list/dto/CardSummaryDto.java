package com.nbacm.trelldochi.domain.list.dto;

import lombok.Getter;

@Getter
public class CardSummaryDto {

    private String title;
    private int commentCount;

    public CardSummaryDto(String title, int commentCount) {
        this.title = title;
        this.commentCount = commentCount;
    }
}
