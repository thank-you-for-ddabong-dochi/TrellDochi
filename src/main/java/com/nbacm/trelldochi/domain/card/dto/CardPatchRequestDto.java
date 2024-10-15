package com.nbacm.trelldochi.domain.card.dto;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.entity.CardStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CardPatchRequestDto {

    private String title;
    private String explanation;
    private LocalDate deadline;
    private CardStatus cardStatus;
}
