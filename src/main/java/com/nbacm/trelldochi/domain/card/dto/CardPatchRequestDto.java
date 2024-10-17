package com.nbacm.trelldochi.domain.card.dto;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.entity.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CardPatchRequestDto {

    private String title;
    private String explanation;
    private LocalDate deadline;
    private CardStatus cardStatus;
}
