package com.nbacm.trelldochi.domain.card.controller;

import com.nbacm.trelldochi.domain.card.service.CardService;
import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;




}
