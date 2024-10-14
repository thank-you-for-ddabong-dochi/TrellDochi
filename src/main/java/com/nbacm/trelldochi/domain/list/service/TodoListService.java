package com.nbacm.trelldochi.domain.list.service;


import com.nbacm.trelldochi.domain.list.dto.TodoListRequestDto;
import com.nbacm.trelldochi.domain.list.dto.TodoListResponseDto;
import com.nbacm.trelldochi.domain.list.entity.TodoList;

import java.util.List;

public interface TodoListService {

    TodoListResponseDto createTodoList(Long boardId, TodoListRequestDto todoListRequestDto);
    List<TodoListResponseDto> getAllTodoList(Long boardId);
    TodoListResponseDto getTodoList(Long boardId, Long listId);
    TodoListResponseDto updateTodoList(Long boardId, Long listId, TodoListRequestDto todoListRequestDto);
    TodoListResponseDto deleteTodoList(Long boardId, Long listId);
}
