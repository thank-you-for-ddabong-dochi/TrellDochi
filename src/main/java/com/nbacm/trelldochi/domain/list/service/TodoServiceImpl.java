package com.nbacm.trelldochi.domain.list.service;

import com.nbacm.trelldochi.domain.list.dto.TodoListRequestDto;
import com.nbacm.trelldochi.domain.list.dto.TodoListResponseDto;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import com.nbacm.trelldochi.domain.list.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoListService {

    private final TodoRepository todoRepository;

    @Override
    public TodoListResponseDto createTodoList(Long boardId, TodoListRequestDto todoListRequestDto) {

        TodoList todoList = new TodoList(todoListRequestDto);

        TodoList savedTodoList = todoRepository.save(todoList);

        return new TodoListResponseDto(savedTodoList);
    }

    @Override
    public List<TodoListResponseDto> getAllTodoList(Long boardId) {

        List<TodoList> todoLists = todoRepository.findByBoardId(boardId);

        return todoLists.stream().map(TodoListResponseDto::new).toList();

    }

    @Override
    public TodoListResponseDto getTodoList(Long boardId, Long listId) {

        TodoList todoList = todoRepository.findById(listId).orElse(null);

        return new TodoListResponseDto(todoList);
    }

    @Override
    public TodoListResponseDto updateTodoList(Long boardId, Long listId, TodoListRequestDto todoListRequestDto) {

        TodoList todoList = todoRepository.findById(listId).orElse(null);

        todoList.update(todoListRequestDto);

        return new TodoListResponseDto(todoList);
    }

    @Override
    public TodoListResponseDto deleteTodoList(Long boardId, Long listId) {

        TodoList todoList = todoRepository.findById(listId).orElse(null);

        todoRepository.delete(todoList);

        return new TodoListResponseDto(todoList);
    }
}
