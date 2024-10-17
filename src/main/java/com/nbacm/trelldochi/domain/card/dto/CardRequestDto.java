package com.nbacm.trelldochi.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CardRequestDto {
    private String title;
    private String explanation;
    private LocalDate deadline;
    private String status;
}
