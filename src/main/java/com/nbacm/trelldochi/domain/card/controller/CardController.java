package com.nbacm.trelldochi.domain.card.controller;

import com.nbacm.trelldochi.domain.card.dto.*;
import com.nbacm.trelldochi.domain.card.service.CardService;
import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.common.dto.AuthUser;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workspace/")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    // "/todoList/{todoList_id}/cards"
    @PostMapping("/{workspaceId}/bord/todo/{todoListId}/cards")
    public ResponseEntity<ApiResponse<CardResponseDto>> createCard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("todoListId") Long todoListId,
            @RequestBody CardRequestDto cardRequestDto) {
        CardResponseDto cardResponseDto = cardService.createCard(userDetails, workspaceId, todoListId,cardRequestDto);
        return ResponseEntity.ok(ApiResponse.success("카드 생성 성공", cardResponseDto));
    }

    @GetMapping("/bord/todo/cards/{cardId}")
    public ResponseEntity<ApiResponse<CardOneResponseDto>> getCard(@PathVariable("cardId") Long cardId) {
        CardOneResponseDto cardOneResponseDto = cardService.getCard(cardId);
        return ResponseEntity.ok(ApiResponse.success("카드 조회 성공", cardOneResponseDto));
    }

    @PutMapping("/{workspaceId}/bord/todo/cards/{cardId}")
    public ResponseEntity<ApiResponse<CardResponseDto>> putCard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("cardId") Long cardId,
            @RequestBody CardPatchRequestDto cardPatchRequestDto) {
        CardResponseDto cardResponseDto = cardService.putCard(userDetails, workspaceId, cardId, cardPatchRequestDto);
        return ResponseEntity.ok(ApiResponse.success("카드 수정 성공", cardResponseDto));
    }

    @PatchMapping("/{workspaceId}/bord/todo/cards/{cardId}")
    public ResponseEntity<ApiResponse<Long>> deleteCard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("cardId") Long cardId) {
        cardService.deleteCard(userDetails, workspaceId, cardId);
        return ResponseEntity.ok(ApiResponse.success("카드 삭제 성공", cardId));
    }

    @PatchMapping("/bord/todo/cards/{cardId}")
    public ResponseEntity<ApiResponse<CardOneResponseDto>> addManager(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("cardId") Long cardId,
            @RequestBody CardManagerRequestDto cardManagerRequestDto
            ) {
        CardOneResponseDto cardOneResponseDto = cardService.addManager(userDetails, cardId, cardManagerRequestDto);
        return ResponseEntity.ok(ApiResponse.success("멤버를 추가했습니다.", cardOneResponseDto));
    }

}
