package com.nbacm.trelldochi.domain.card.controller;

import com.nbacm.trelldochi.domain.card.dto.CardOneResponseDto;
import com.nbacm.trelldochi.domain.card.dto.CardPatchRequestDto;
import com.nbacm.trelldochi.domain.card.dto.CardRequestDto;
import com.nbacm.trelldochi.domain.card.dto.CardResponseDto;
import com.nbacm.trelldochi.domain.card.service.CardService;
import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    // "/todoList/{todoList_id}/cards"
    @PostMapping("/cards")
    public ResponseEntity<ApiResponse<CardResponseDto>> createCard(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody CardRequestDto cardRequestDto) {
        CardResponseDto cardResponseDto = cardService.createCard(authUser, cardRequestDto);
        return ResponseEntity.ok(ApiResponse.success("카드 생성 성공", cardResponseDto));
    }

    @GetMapping("/cards/{cardId}")
    public ResponseEntity<ApiResponse<CardOneResponseDto>> getCard(@PathVariable("cardId") Long cardId) {
        CardOneResponseDto cardOneResponseDto = cardService.getCard(cardId);
        return ResponseEntity.ok(ApiResponse.success("카드 조회 성공", cardOneResponseDto));
    }

    @PutMapping("/cards/{cardId}")
    public ResponseEntity<ApiResponse<CardResponseDto>> putCard(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("cardId") Long cardId,
            @RequestBody CardPatchRequestDto cardPatchRequestDto) {
        CardResponseDto cardResponseDto = cardService.putCard(authUser, cardId, cardPatchRequestDto);
        return ResponseEntity.ok(ApiResponse.success("카드 수정 성공", cardResponseDto));
    }

    @PatchMapping("/cards/{cardId}")
    public ResponseEntity<ApiResponse<Long>> deleteCard(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("cardId") Long cardId) {
        cardService.deleteCard(authUser, cardId);
        return ResponseEntity.ok(ApiResponse.success("카드 삭제 성공", cardId));
    }

}
