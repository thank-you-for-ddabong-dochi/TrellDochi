package com.nbacm.trelldochi.domain.todoList.service;

import com.nbacm.trelldochi.domain.board.dto.BoardResponseDto;
import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.board.repository.BoardRepository;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.list.dto.MoveListRequestDto;
import com.nbacm.trelldochi.domain.list.dto.TodoListRequestDto;
import com.nbacm.trelldochi.domain.list.dto.TodoListResponseDto;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import com.nbacm.trelldochi.domain.list.repository.TodoListQueryDslRepositoryImpl;
import com.nbacm.trelldochi.domain.list.repository.TodoRepository;
import com.nbacm.trelldochi.domain.list.service.TodoListService;
import com.nbacm.trelldochi.domain.list.service.TodoServiceImpl;
import com.nbacm.trelldochi.domain.notifications.service.NotificationService;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.entity.UserRole;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.entity.MemberRole;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TodoListServiceTest {

    @Mock
    TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private WorkSpaceMemberRepository workSpaceMemberRepository;

    @Mock
    private TodoListQueryDslRepositoryImpl todoListQueryDslRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TodoServiceImpl todoService;

    private CustomUserDetails userDetails;
    private User user;
    private WorkSpace workSpace;
    private Board board;
    private TodoList todoList;
    private WorkSpaceMember workSpaceMember;

    @BeforeEach
    public void setUp() throws Exception {
        user = new User("test@example.com", "1234", "nick", UserRole.ADMIN);
        userDetails = new CustomUserDetails(user);
        workSpace = new WorkSpace();
        board = new Board();
        todoList = new TodoList("Sample Todo", 1, board);
        workSpaceMember = new WorkSpaceMember(user, workSpace, null);

        // 리플렉션을 이용해 ID 설정
        setIdViaReflection(user, 1L);
        setIdViaReflection(workSpace, 1L);
        setIdViaReflection(board, 1L);
        setIdViaReflection(todoList, 1L);
        setIdViaReflection(workSpaceMember, 1L);
        setWorkSpaceViaReflection(board, workSpace);
    }

    @Test
    public void testCreateTodoList_Success() {
        // Given
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(board));
        when(todoRepository.findMaxListOrderByBoardId(anyLong())).thenReturn(1);
        when(todoRepository.save(any(TodoList.class))).thenReturn(todoList);
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user);
        when(workSpaceMemberRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).thenReturn(Optional.of(workSpaceMember));

        // When
        TodoListRequestDto requestDto = new TodoListRequestDto("New Todo List");
        TodoListResponseDto responseDto = todoService.createTodoList(1L, requestDto, userDetails);

        // Then
        assertNotNull(responseDto);
        assertEquals("Sample Todo", responseDto.getTitle());
        verify(notificationService, times(1)).sendRealTimeNotification(anyString(), anyString());
    }

    // TodoList 생성 실패 - 인증되지 않은 사용자
    @Test
    public void testCreateTodoList_Unauthorized() {
        // Given
        WorkSpaceMember readOnlyMember = new WorkSpaceMember(user, workSpace, MemberRole.READONLY);
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(board));
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user);
        when(workSpaceMemberRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).thenReturn(Optional.of(readOnlyMember));

        // When Then
        TodoListRequestDto requestDto = new TodoListRequestDto("New Todo List");
        assertThrows(ResponseStatusException.class, () -> {
            todoService.createTodoList(1L, requestDto, userDetails);
        });
    }


    // TodoList 이동 성공
    @Test
    public void testMoveTodoList_Success() {
        // Given
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(todoList));

        MoveListRequestDto moveRequestDto = new MoveListRequestDto(1L, 3);

        // When
        todoService.moveTodoList(moveRequestDto);

        // Then
        verify(todoRepository, times(1)).decrementOrderBetween(anyInt(), anyInt());
        verify(todoRepository, times(1)).save(any(TodoList.class));
        verify(notificationService, times(1)).sendRealTimeNotification(anyString(), anyString());
    }

    // TodoList 조회 성공
    @Test
    public void testGetTodoList_Success() {
        // Given
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(todoList));

        // When
        TodoListResponseDto responseDto = todoService.getTodoList(1L, 1L, userDetails);

        // Then
        assertNotNull(responseDto);
        assertEquals("Sample Todo", responseDto.getTitle());
    }

    // TodoList 삭제 성공
//    @Test
//    public void testDeleteTodoList_Success() throws Exception {
//        // Given
//
//        setIsDeletedViaReflection(todoList, true);
//        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(board));
//        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(todoList));
//        when(workSpaceMemberRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong()))
//                .thenReturn(Optional.of(workSpaceMember));
//
//        // When
//        TodoListResponseDto todoListResponseDto = todoService.deleteTodoList(1L, 1L, userDetails);
//
//        // Then
//        assertNotNull(todoListResponseDto);
//        verify(todoListQueryDslRepository, times(1)).deleteRelations(anyLong());
//    }






    private void setIdViaReflection(Object target, Long idValue) throws Exception {
        Field idField = target.getClass().getDeclaredField("id");  // "id" 필드 가져오기
        idField.setAccessible(true);  // private 필드 접근 가능하도록 설정
        idField.set(target, idValue);  // 필드에 값 설정
    }

    private void setWorkSpaceViaReflection(Object target, WorkSpace workSpace) throws Exception {
        Field isDeletedField = target.getClass().getDeclaredField("workSpace");
        isDeletedField.setAccessible(true);
        isDeletedField.set(target, workSpace);
    }

    private void setIsDeletedViaReflection(Object target, Boolean isDeleted) throws Exception {
        Field isDeletedField = target.getClass().getDeclaredField("isDeleted");
        isDeletedField.setAccessible(true);
        isDeletedField.set(target, isDeleted);
    }
}
