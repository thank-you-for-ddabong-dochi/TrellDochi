package com.nbacm.trelldochi.domain.card.dto;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.entity.CardStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CardResponseDto {
    private Long cardId;
    private String title;
    private String explanation;
    private LocalDate deadline;
    private CardStatus cardStatus;

    public CardResponseDto(Card savedCard) {
        cardId = savedCard.getId();
        title = savedCard.getTitle();
        explanation = savedCard.getExplanation();
        deadline = savedCard.getDeadline();
        cardStatus = savedCard.getStatus();
    }
}
