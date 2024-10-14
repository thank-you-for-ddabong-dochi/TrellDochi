package com.nbacm.trelldochi.domain.board.controller;

import com.nbacm.trelldochi.domain.board.dto.BoardRequestDto;
import com.nbacm.trelldochi.domain.board.dto.BoardResponseDto;
import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.board.service.BoardService;
import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 보드 생성
    @PostMapping("/workspace/{workspaceId}")
    public ResponseEntity<?> createBoard(@PathVariable Long workspaceId,
                                         @RequestBody BoardRequestDto boardRequestDto) {

        BoardResponseDto boardResponseDto = boardService.createBoard(workspaceId, boardRequestDto);

        return ResponseEntity.ok(ApiResponse.success("보드 생성 성공",boardResponseDto));
    }

    // 현재 워크스페이스의 보드 전체 조회
    @GetMapping("/workspace/{workspaceId}/boards")
    public ResponseEntity<?> getAllBoard(@PathVariable Long workspaceId) {

        List<BoardResponseDto> boardResponseDtoList = boardService.getAllBoard(workspaceId);

        return ResponseEntity.ok(ApiResponse.success("보드 전체 조회 성공", boardResponseDtoList));
    }

    @GetMapping("/workspace/{workspaceId}/boards/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long workspaceId, @PathVariable Long boardId) {

        BoardResponseDto boardResponseDto = boardService.getBoard(workspaceId, boardId);

        return ResponseEntity.ok(ApiResponse.success("보드 단건 조회 성공", boardResponseDto));
    }

    @PutMapping("/workspace/{workspaceId}/boards/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long workspaceId,
                                         @PathVariable Long boardId,
                                         @RequestBody BoardRequestDto boardRequestDto) {

        BoardResponseDto boardResponseDto = boardService.updateBoard(workspaceId, boardId, boardRequestDto);

        return ResponseEntity.ok(ApiResponse.success("보드 수정 성공", boardResponseDto));
    }

    @DeleteMapping("/workspace/{workspaceId}/boards/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long workspaceId, @PathVariable Long boardId) {

        BoardResponseDto boardResponseDto = boardService.deleteBoard(workspaceId, boardId);

        return ResponseEntity.ok(ApiResponse.success("보드 삭제 성공", boardResponseDto));
    }
}
