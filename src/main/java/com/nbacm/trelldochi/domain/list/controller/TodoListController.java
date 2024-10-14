package com.nbacm.trelldochi.domain.list.controller;

import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.list.dto.TodoListRequestDto;
import com.nbacm.trelldochi.domain.list.dto.TodoListResponseDto;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import com.nbacm.trelldochi.domain.list.service.TodoListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class TodoListController {

    private final TodoListService todoListService;

    @PostMapping("boards/{boardId}")
    public ResponseEntity<?> createBoard(@PathVariable Long boardId,
                                         @RequestBody TodoListRequestDto todoListRequestDto) {

        TodoListResponseDto todoListResponseDto = todoListService.createTodoList(boardId, todoListRequestDto);

        return ResponseEntity.ok(ApiResponse.success("리스트 생성 성공", todoListResponseDto));
    }

    @PutMapping("boards/{boardId}/todoList/{todoListId}")
    public ResponseEntity<?> updateList(@PathVariable Long boardId,
                                        @PathVariable Long todoListId,
                                        @RequestBody TodoListRequestDto todoListRequestDto) {

        TodoListResponseDto todoListResponseDto = todoListService.updateTodoList(boardId, todoListId, todoListRequestDto);

        return ResponseEntity.ok(ApiResponse.success("리스트 수정 성공", todoListResponseDto));
    }

    @DeleteMapping("boards/{boardId}/todoList/{todoListId}")
    public ResponseEntity<?> deleteList(@PathVariable Long boardId, @PathVariable Long todoListId) {

        TodoListResponseDto todoListResponseDto = todoListService.deleteTodoList(boardId, todoListId);

        return ResponseEntity.ok(ApiResponse.success("리스트 삭제 성공", todoListResponseDto));
    }
}
