package com.nbacm.trelldochi.domain.list.service;

import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.board.repository.BoardRepository;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.list.dto.TodoListRequestDto;
import com.nbacm.trelldochi.domain.list.dto.TodoListResponseDto;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import com.nbacm.trelldochi.domain.list.repository.TodoRepository;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.entity.MemberRole;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoListService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final WorkSpaceMemberRepository workSpaceMemberRepository;

    @Override
    public TodoListResponseDto createTodoList(Long boardId, TodoListRequestDto todoListRequestDto, CustomUserDetails userDetails) {

        // 현재 로그인 중인 유저가 해당 워크스페이스의 맴버인지 판단이 필요함
        Board board = boardRepository.findById(boardId).orElseThrow();

        if(!isAuthorized(board.getWorkSpace().getId(), userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        TodoList todoList = new TodoList(todoListRequestDto);

        TodoList savedTodoList = todoRepository.save(todoList);

        return new TodoListResponseDto(savedTodoList);
    }

    /*@Override
    public List<TodoListResponseDto> getAllTodoList(Long boardId) {

        List<TodoList> todoLists = todoRepository.findByBoardId(boardId);

        return todoLists.stream().map(TodoListResponseDto::new).toList();

    }

    @Override
    public TodoListResponseDto getTodoList(Long boardId, Long listId) {

        TodoList todoList = todoRepository.findById(listId).orElse(null);

        return new TodoListResponseDto(todoList);
    }*/

    @Override
    public TodoListResponseDto updateTodoList(Long boardId, Long listId, TodoListRequestDto todoListRequestDto, CustomUserDetails userDetails) {

        // 현재 로그인 중인 유저가 해당 워크스페이스의 맴버인지 판단이 필요함
        Board board = boardRepository.findById(boardId).orElseThrow();

        if(!isAuthorized(board.getWorkSpace().getId(), userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        TodoList todoList = todoRepository.findById(listId).orElse(null);

        todoList.update(todoListRequestDto);

        return new TodoListResponseDto(todoList);
    }

    @Override
    public TodoListResponseDto deleteTodoList(Long boardId, Long listId, CustomUserDetails userDetails) {

        // 현재 로그인 중인 유저가 해당 워크스페이스의 맴버인지 판단이 필요함
        Board board = boardRepository.findById(boardId).orElseThrow();

        if(!isAuthorized(board.getWorkSpace().getId(), userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        TodoList todoList = todoRepository.findById(listId).orElse(null);

        todoRepository.delete(todoList);

        return new TodoListResponseDto(todoList);
    }

    private Boolean isAuthorized(Long workspaceId, CustomUserDetails userDetails) {
        User user = userRepository.findByEmailOrElseThrow(userDetails.getEmail());

        WorkSpaceMember workSpaceMember = workSpaceMemberRepository.findByUserIdAndWorkspaceId(user.getId(), workspaceId).orElseThrow();

        if(workSpaceMember.getRole() == MemberRole.READONLY) {
            return false;
        } else {
            return true;
        }
    }
}

