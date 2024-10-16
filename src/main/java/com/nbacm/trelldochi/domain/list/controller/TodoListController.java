package com.nbacm.trelldochi.domain.list.controller;

import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.list.dto.MoveListRequestDto;
import com.nbacm.trelldochi.domain.list.dto.TodoListRequestDto;
import com.nbacm.trelldochi.domain.list.dto.TodoListResponseDto;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import com.nbacm.trelldochi.domain.list.service.TodoListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class TodoListController {

    private final TodoListService todoListService;

    @PostMapping("boards/{boardId}")
    public ResponseEntity<?> createTodoList(@PathVariable("boardId") Long boardId,
                                         @RequestBody TodoListRequestDto todoListRequestDto,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        TodoListResponseDto todoListResponseDto = todoListService.createTodoList(boardId, todoListRequestDto, userDetails);

        return ResponseEntity.ok(ApiResponse.success("리스트 생성 성공", todoListResponseDto));
    }

    @GetMapping("boards/{boardId}/todoList/{todoListId}")
    public ResponseEntity<?> getTodoList(@PathVariable("boardId") Long boardId,
                                      @PathVariable("todoListId") Long todoListId,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {

        TodoListResponseDto todoListResponseDto = todoListService.getTodoList(boardId, todoListId, userDetails);

        return ResponseEntity.ok(ApiResponse.success("보드 조회 성공", todoListResponseDto));
    }

    @PutMapping("boards/{boardId}/todoList/{todoListId}")
    public ResponseEntity<?> updateTodoList(@PathVariable("boardId") Long boardId,
                                        @PathVariable("todoListId") Long todoListId,
                                        @RequestBody TodoListRequestDto todoListRequestDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {

        TodoListResponseDto todoListResponseDto = todoListService.updateTodoList(boardId, todoListId, todoListRequestDto, userDetails);

        return ResponseEntity.ok(ApiResponse.success("리스트 수정 성공", todoListResponseDto));
    }

    @PatchMapping("boards/{boardId}")
    public ResponseEntity<?> moveTodoList(@PathVariable("boardId") Long boardId,
                                      @RequestBody MoveListRequestDto moveListRequestDto) {

        todoListService.moveTodoList(moveListRequestDto);

        return ResponseEntity.ok(ApiResponse.success("리스트 순서 변경 성공", ""));
    }

    @PatchMapping("boards/{boardId}/todoList/{todoListId}")
    public ResponseEntity<?> deleteTodoList(@PathVariable("boardId") Long boardId,
                                        @PathVariable("todoListId") Long todoListId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {

        TodoListResponseDto todoListResponseDto = todoListService.deleteTodoList(boardId, todoListId, userDetails);

        return ResponseEntity.ok(ApiResponse.success("리스트 삭제 성공", todoListResponseDto));
    }
}
