package com.nbacm.trelldochi.domain.board.controller;

import com.nbacm.trelldochi.domain.board.dto.BoardRequestDto;
import com.nbacm.trelldochi.domain.board.dto.BoardResponseDto;
import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.board.service.BoardService;
import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 보드 생성
    @PostMapping("/workspace/{workspaceId}")
    public ResponseEntity<?> createBoard(@PathVariable("workspaceId") Long workspaceId,
                                         @RequestBody BoardRequestDto boardRequestDto,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        BoardResponseDto boardResponseDto = boardService.createBoard(workspaceId, boardRequestDto, userDetails);

        return ResponseEntity.ok(ApiResponse.success("보드 생성 성공",boardResponseDto));
    }

    // 현재 워크스페이스의 보드 전체 조회
    @GetMapping("/workspace/{workspaceId}/boards")
    public ResponseEntity<?> getAllBoard(@PathVariable("workspaceId") Long workspaceId) {

        List<BoardResponseDto> boardResponseDtoList = boardService.getAllBoard(workspaceId);

        return ResponseEntity.ok(ApiResponse.success("보드 전체 조회 성공", boardResponseDtoList));
    }

    @GetMapping("/workspace/{workspaceId}/boards/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable("workspaceId") Long workspaceId, @PathVariable("boardId") Long boardId) {

        BoardResponseDto boardResponseDto = boardService.getBoard(workspaceId, boardId);

        return ResponseEntity.ok(ApiResponse.success("보드 단건 조회 성공", boardResponseDto));
    }

    @PutMapping("/workspace/{workspaceId}/boards/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable("workspaceId") Long workspaceId,
                                         @PathVariable("boardId") Long boardId,
                                         @RequestBody BoardRequestDto boardRequestDto,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        BoardResponseDto boardResponseDto = boardService.updateBoard(workspaceId, boardId, boardRequestDto, userDetails);

        return ResponseEntity.ok(ApiResponse.success("보드 수정 성공", boardResponseDto));
    }

    @DeleteMapping("/workspace/{workspaceId}/boards/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable("workspaceId") Long workspaceId,
                                         @PathVariable("boardId") Long boardId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        BoardResponseDto boardResponseDto = boardService.deleteBoard(workspaceId, boardId, userDetails);

        return ResponseEntity.ok(ApiResponse.success("보드 삭제 성공", boardResponseDto));
    }
}
