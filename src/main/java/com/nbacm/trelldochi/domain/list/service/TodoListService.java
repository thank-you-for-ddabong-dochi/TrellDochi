package com.nbacm.trelldochi.domain.list.service;


import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.list.dto.MoveListRequestDto;
import com.nbacm.trelldochi.domain.list.dto.TodoListRequestDto;
import com.nbacm.trelldochi.domain.list.dto.TodoListResponseDto;
import com.nbacm.trelldochi.domain.list.entity.TodoList;

import java.util.List;

public interface TodoListService {

    TodoListResponseDto createTodoList(Long boardId, TodoListRequestDto todoListRequestDto, CustomUserDetails userDetails);
    void moveTodoList(MoveListRequestDto moveListRequestDto);
    TodoListResponseDto updateTodoList(Long boardId, Long listId, TodoListRequestDto todoListRequestDto, CustomUserDetails userDetails);
    TodoListResponseDto deleteTodoList(Long boardId, Long listId, CustomUserDetails userDetails);
}
