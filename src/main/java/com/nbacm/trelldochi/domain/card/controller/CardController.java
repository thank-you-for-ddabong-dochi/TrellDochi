package com.nbacm.trelldochi.domain.card.controller;

import com.nbacm.trelldochi.domain.card.dto.CardRequestDto;
import com.nbacm.trelldochi.domain.card.dto.CardResponseDto;
import com.nbacm.trelldochi.domain.card.service.CardService;
import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping()
    public ResponseEntity<CardResponseDto> createCard(@RequestBody CardRequestDto cardRequestDto) {
        CardResponseDto cardResponseDto = cardService.createCard(cardRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(cardResponseDto);
    }


}
