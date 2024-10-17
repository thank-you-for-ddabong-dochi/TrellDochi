package com.nbacm.trelldochi.domain.board;

import com.nbacm.trelldochi.domain.attachment.service.AwsS3Service;
import com.nbacm.trelldochi.domain.board.dto.BoardRequestDto;
import com.nbacm.trelldochi.domain.board.dto.BoardResponseDto;
import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.board.repository.BoardQueryDslRepositoryImpl;
import com.nbacm.trelldochi.domain.board.repository.BoardRepository;
import com.nbacm.trelldochi.domain.board.service.BoardServiceImpl;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.common.exception.NotFoundException;
import com.nbacm.trelldochi.domain.notifications.service.NotificationService;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.entity.UserRole;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.entity.MemberRole;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceMemberRepository;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private WorkSpaceRepository workSpaceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkSpaceMemberRepository workSpaceMemberRepository;

    @Mock
    private BoardQueryDslRepositoryImpl boardQueryDslRepositoryImpl;

    @Mock
    private AwsS3Service awsS3Service;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BoardServiceImpl boardService;

    private CustomUserDetails userDetails;
    private User user;
    private WorkSpace workSpace;
    private Board board;
    private WorkSpaceMember workSpaceMember;

    @BeforeEach
    public void setUp() throws Exception {
        user = new User("test@example.com", "1234", "nick", UserRole.ADMIN);

        userDetails = new CustomUserDetails(user);

        workSpace = new WorkSpace();

        board = new Board("Test Board", "#FFFFFF", null, workSpace);

        workSpaceMember = new WorkSpaceMember(user, workSpace, MemberRole.ADMIN);

        // 리플렉션을 이용해 ID 값을 설정
        setIdViaReflection(user, 1L);
        setIdViaReflection(workSpace, 1L);
        setIdViaReflection(board, 1L);
        setIdViaReflection(workSpaceMember, 1L);
    }

    // 보드 생성 성공
    @Test
    public void testCreateBoard_Success() {

        // Given
        MultipartFile image = mock(MultipartFile.class);
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user);
        when(workSpaceMemberRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong()))
                .thenReturn(Optional.of(workSpaceMember));
        when(workSpaceRepository.findById(anyLong())).thenReturn(Optional.of(workSpace));
        when(boardRepository.save(any(Board.class))).thenReturn(board);
        when(awsS3Service.upload(any(MultipartFile.class))).thenReturn("upload_image_url");

        // When
        BoardRequestDto boardRequestDto = new BoardRequestDto("Test Board", "white", "", null);
        BoardResponseDto boardResponseDto = boardService.createBoard(1L, boardRequestDto, image, userDetails);

        // Then
        assertNotNull(boardResponseDto);
        assertEquals("Test Board", boardResponseDto.getTitle());
        verify(notificationService, times(1)).sendRealTimeNotification(anyString(), anyString());
    }

    // 보드 생성 실패 - 인증된 사용자가 아님
    @Test
    public void testCreateBoard_Unauthorized() {

        // Given
        WorkSpaceMember readOnlyMember = new WorkSpaceMember(user, workSpace, MemberRole.READONLY);
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user);
        when(workSpaceMemberRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong()))
                .thenReturn(Optional.of(readOnlyMember));

        // When
        BoardRequestDto boardRequestDto = new BoardRequestDto("Test Board", "white", "", null);
        assertThrows(ResponseStatusException.class, () -> {
            boardService.createBoard(1L, boardRequestDto, null, userDetails);
        });
    }

    // 보드 전체 조회 성공
    @Test
    public void testGetAllBoard_Success() {

        // Given
        when(workSpaceRepository.findById(anyLong())).thenReturn(Optional.of(workSpace));
        when(boardRepository.findByWorkSpace(any(WorkSpace.class))).thenReturn(List.of(board));

        // When
        List<BoardResponseDto> boards = boardService.getAllBoard(1L);

        // Then
        assertNotNull(boards);
        assertEquals(1, boards.size());
        assertEquals("Test Board", boards.get(0).getTitle());

    }

    // 보드 전체 조회 실패 - 보드를 찾지 못함
    @Test
    public void testGetBoard_NotFound() {

        // Given
        when(boardRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When Then
        assertThrows(NotFoundException.class, () -> {
            boardService.getBoard(1L, 1L);
        });
    }

    // 보드 수정 성공
    @Test
    public void testUpdateBoard_Success() {

        // Given
        MultipartFile image = mock(MultipartFile.class);
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user);
        when(workSpaceMemberRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong()))
                .thenReturn(Optional.of(workSpaceMember));
        when(workSpaceRepository.findById(anyLong())).thenReturn(Optional.of(workSpace));
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(board));

        // when
        BoardRequestDto boardRequestDto = new BoardRequestDto("Test Board", "white", "", null);
        BoardResponseDto boardResponseDto = boardService.updateBoard(1L, 1L, boardRequestDto, image, userDetails);

        // then
        assertNotNull(boardResponseDto);
        assertEquals("Test Board", boardResponseDto.getTitle());
        verify(notificationService, times(1)).sendRealTimeNotification(anyString(), anyString());
    }

    // 보드 삭제 성공
    @Test
    public void testDeleteBoard_Success() {

        // Given
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user);
        when(workSpaceMemberRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong()))
                .thenReturn(Optional.of(workSpaceMember));
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(board));

        // when
        BoardResponseDto boardResponseDto = boardService.deleteBoard(1L, 1L, userDetails);

        // Then
        assertNotNull(boardResponseDto);
        verify(boardQueryDslRepositoryImpl, times(1)).deleteRelations(anyLong());
    }









    // 리플렉션을 사용해 private 필드에 ID를 설정하는 메서드
    private void setIdViaReflection(Object target, Long idValue) throws Exception {
        Field idField = target.getClass().getDeclaredField("id");  // "id" 필드 가져오기
        idField.setAccessible(true);  // private 필드 접근 가능하도록 설정
        idField.set(target, idValue);  // 필드에 값 설정
    }



}
