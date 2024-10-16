package com.nbacm.trelldochi.domain.list.service;

import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.board.repository.BoardRepository;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.common.exception.NotFoundException;
import com.nbacm.trelldochi.domain.list.dto.CardSummaryDto;
import com.nbacm.trelldochi.domain.list.dto.MoveListRequestDto;
import com.nbacm.trelldochi.domain.list.dto.TodoListRequestDto;
import com.nbacm.trelldochi.domain.list.dto.TodoListResponseDto;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import com.nbacm.trelldochi.domain.list.repository.TodoListQueryDslRepositoryImpl;
import com.nbacm.trelldochi.domain.list.repository.TodoRepository;
import com.nbacm.trelldochi.domain.notifications.service.NotificationService;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.entity.MemberRole;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoListService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final WorkSpaceMemberRepository workSpaceMemberRepository;
    private final TodoListQueryDslRepositoryImpl todoListQueryDslRepositoryImpl;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public TodoListResponseDto createTodoList(Long boardId, TodoListRequestDto todoListRequestDto, CustomUserDetails userDetails) {

        // 현재 로그인 중인 유저가 해당 워크스페이스의 맴버인지 판단이 필요함
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("Board not found"));

        // 해당 보드 내의 가장 높은 ListOrder 조회
        int maxListOrder = todoRepository.findMaxListOrderByBoardId(boardId);

        if(!isAuthorized(board.getWorkSpace().getId(), userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 해당 리스트가 보드에 속해있는 것이 맞는지에 대해 판단이 요구됨

        TodoList todoList = new TodoList(todoListRequestDto.getTitle(), maxListOrder + 1, board);

        TodoList savedTodoList = todoRepository.save(todoList);

        notificationService.sendRealTimeNotification("리스트 생성",todoList.getTitle().toString()+"리스트가 생성되었어요!");

        return new TodoListResponseDto(savedTodoList);
    }



    @Override
    @Transactional
    public void moveTodoList(MoveListRequestDto moveListRequestDto) {

        TodoList todoList = todoRepository.findById(moveListRequestDto.getTodoListId()).orElseThrow(() -> new NotFoundException("TodoList not found"));
        int currentOrder = todoList.getListOrder();
        int targetOrder = moveListRequestDto.getTargetOrder();

        // 별도의 메서드로 분리!!!
        // 리스트를 뒤쪽으로 이동시킬 때
        if(currentOrder < targetOrder) {
            todoRepository.decrementOrderBetween(currentOrder+1, targetOrder);
        }
        // 리스트를 앞쪽으로 이동시킬 때
        else if(currentOrder > targetOrder) {
            todoRepository.incrementOrderBetween(targetOrder, currentOrder-1);
        }

        todoList.move(targetOrder);
        todoRepository.save(todoList);
        notificationService.sendRealTimeNotification("리스트 이동",todoList.getTitle().toString()+"리스트가 이동되었어요!");
    }

    @Override
    public TodoListResponseDto getTodoList(Long boardId, Long todoListId, CustomUserDetails userDetails) {

        TodoList todoList = todoRepository.findById(todoListId).orElseThrow(() -> new NotFoundException("TodoList not found"));

        if(todoList.getIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        TodoListResponseDto todoListResponseDto = new TodoListResponseDto(todoList);

        return todoListResponseDto;
    }


    @Override
    @Transactional
    public TodoListResponseDto updateTodoList(Long boardId, Long listId, TodoListRequestDto todoListRequestDto, CustomUserDetails userDetails) {

        // 현재 로그인 중인 유저가 해당 워크스페이스의 맴버인지 판단이 필요함
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("Board not found"));

        if(!isAuthorized(board.getWorkSpace().getId(), userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        TodoList todoList = todoRepository.findById(listId).orElse(null);

        todoList.update(todoListRequestDto);
        notificationService.sendRealTimeNotification("리스트 업데이트",todoList.getTitle().toString()+"리스트가 수정 되었습니다.");
        return new TodoListResponseDto(todoList);
    }

    @Override
    @Transactional
    public TodoListResponseDto deleteTodoList(Long boardId, Long listId, CustomUserDetails userDetails) {

        // 현재 로그인 중인 유저가 해당 워크스페이스의 맴버인지 판단이 필요함
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("Board not found"));

        if(!isAuthorized(board.getWorkSpace().getId(), userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        TodoList todoList = todoRepository.findById(listId).orElseThrow(() -> new NotFoundException("TodoList not found"));

        if(!todoList.getIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        todoListQueryDslRepositoryImpl.deleteRelations(todoList.getId());

        return new TodoListResponseDto(todoList);
    }


    private Boolean isAuthorized(Long workspaceId, CustomUserDetails userDetails) {
        User user = userRepository.findByEmailOrElseThrow(userDetails.getEmail());

        WorkSpaceMember workSpaceMember = workSpaceMemberRepository.findByUserIdAndWorkspaceId(user.getId(), workspaceId).orElseThrow(() -> new NotFoundException("Work Space Member not found"));

        if(workSpaceMember.getRole() == MemberRole.READONLY) {
            return false;
        } else {
            return true;
        }
    }
}

