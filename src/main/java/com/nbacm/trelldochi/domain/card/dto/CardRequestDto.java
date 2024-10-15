package com.nbacm.trelldochi.domain.card.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CardRequestDto {
    private String title;
    private String explanation;
    private LocalDate deadline;
    private String status;
}
